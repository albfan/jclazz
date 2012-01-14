package ru.andrew.jclazz.decompiler.engine.blocks;

import java.util.Map.Entry;
import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.engine.ops.*;
import ru.andrew.jclazz.decompiler.*;

import java.util.*;
import ru.andrew.jclazz.core.ClazzException;
import ru.andrew.jclazz.core.FieldDescriptor;
import ru.andrew.jclazz.core.attributes.LocalVariableTable;

public class Block implements CodeItem
{
    protected final String NL = System.getProperty("line.separator");

    protected List ops;
    protected long fakeStartByte;
    protected Block parent;
    private MethodSourceView m_info;
    protected String indent = "";
    protected MethodContext context;

    protected Block(Block parent)
    {
        this(parent, new ArrayList());
    }

    public Block(Block parent, List ops)
    {
        this.ops = ops;
        this.parent = parent;
        this.context = parent.context;

        if (parent != null)
        {
            this.m_info = parent.m_info;
        }
    }

    public Block(List ops, MethodSourceView m_info)
    {
        this.ops = ops;
        this.m_info = m_info;
        this.context = m_info.getMethodContext();
    }

    public MethodContext getMethodContext()
    {
        return context;
    }

    public void setParent(Block parent)
    {
        this.parent = parent;
    }

    public Block getParent()
    {
        return parent;
    }

    public void setOperations(List ops)
    {
        this.ops = ops;
    }

    public List getOperations()
    {
        return ops;
    }

    public String getIndent()
    {
        return indent;
    }

    public void setIndent(String indent)
    {
        this.indent = indent;
    }

    public long getFakeStartByte()
    {
        return fakeStartByte;
    }

    public void setFakeStartByte(long fakeStartByte)
    {
        this.fakeStartByte = fakeStartByte;
    }

    // Handling block operations by iterating

    private int ext_pos = 0;

    private static final int END_BLOCK_POS = Integer.MAX_VALUE;

    public boolean hasMoreOperations()
    {
        return ext_pos < ops.size();
    }

    public CodeItem next()
    {
        CodeItem item = (CodeItem) ops.get(ext_pos);
        ext_pos++;
        return item; 
    }

    public void back()
    {
        ext_pos--;
    }

    public void seekEnd()
    {
        ext_pos = END_BLOCK_POS;
    }

    public void reset()
    {
        ext_pos = 0;
    }

    // Business methods

    public void postCreate()
    {
    }

    public void postProcess()
    {
    }

    public List createSubBlock(long startOp, long endOp, Block subblock)
    {
        List subops = new ArrayList();

        boolean moving = false;
        int pos = 0;
        while (pos < ops.size())
        {
            CodeItem citem = (CodeItem) ops.get(pos);
            if (citem.getStartByte() >= startOp) moving = true;
            if (citem.getStartByte() >= endOp) break;

            if (moving)
            {
                subops.add(citem);
                if (subblock != null && citem instanceof Block) ((Block) citem).setParent(subblock);
                ops.remove(pos);
                if (ext_pos > pos && pos != 0) ext_pos--;
            }
            else
            {
                pos++;
            }
        }

        if (subblock != null)
        {
            ops.add(pos, subblock);
            subblock.setOperations(subops);
            if (subops.isEmpty())
            {
                subblock.setFakeStartByte(startOp);
            }
            subblock.setParent(this);
            subblock.postCreate();
        }
        return subops;
    }

    public void addOperation(int index, CodeItem ci)
    {
        if (index <= ext_pos && ext_pos != END_BLOCK_POS) ext_pos++;
        ops.add(index, ci);

        if (ci instanceof Block)
        {
            ((Block) ci).setParent(this);
        }
    }

    public void addOperation(CodeItem ci, long beforeOperation)
    {
        for (int i = 0; i < ops.size(); i++)
        {
            CodeItem ci0 = (CodeItem) ops.get(i);
            if (ci0.getStartByte() == beforeOperation)
            {
                addOperation(i, ci);
                return;
            }
        }
    }

    private CodeItem removeOperation(int pos)
    {
        if (pos <= ext_pos && ext_pos != 0 && ext_pos != END_BLOCK_POS) ext_pos--;
        return (CodeItem) ops.remove(pos);
    }

    public CodeItem removeLastOperation()
    {
        return ops.isEmpty() ? null : removeOperation(ops.size() - 1);
    }

    public CodeItem removeFirstOperation()
    {
        if (!ops.isEmpty())
        {
            return removeOperation(0);
        }
        return null;
    }

    public CodeItem removeOperation(long start_byte)
    {
        for (int i = 0; i < ops.size(); i++)
        {
            if (((CodeItem) ops.get(i)).getStartByte() == start_byte)
            {
                if (i <= ext_pos && ext_pos != 0 && ext_pos != END_BLOCK_POS) ext_pos--;
                return (CodeItem) ops.remove(i);
            }
        }
        return null;
    }

    public CodeItem getOperationPriorTo(long start_byte)
    {
        for (int i = 0; i < ops.size(); i++)
        {
            if (((CodeItem) ops.get(i)).getStartByte() >= start_byte)
            {
                if (i == 0) return null;
                return (CodeItem) ops.get(i - 1);
            }
        }
        return getLastOperation();
    }

    public CodeItem getOperationAfter(long start_byte)
    {
        for (int i = 0; i < ops.size(); i++)
        {
            if (((CodeItem) ops.get(i)).getStartByte() > start_byte) return (CodeItem) ops.get(i);
        }
        return null;
    }

    public CodeItem getOperationByStartByte(long start_byte)
    {
        for (int i = 0; i < ops.size(); i++)
        {
            if (((CodeItem) ops.get(i)).getStartByte() == start_byte) return (CodeItem) ops.get(i);
        }
        return null;
    }

    public OperationView removePriorPushOperation()
    {
        int start_pos = ext_pos != END_BLOCK_POS ? ext_pos - 2 : ops.size() - 1;
        for (int i = start_pos; i >= 0 ; i--)
        {
            if (ops.get(i) instanceof Block)
            {
                OperationView priorPush = getPriorPushFromSubblock((Block) ops.get(i));   // TODO why not remove
                if (priorPush != null) return priorPush;
            }
            else if (((OperationView) ops.get(i)).isPush())
            {
                if (ext_pos != END_BLOCK_POS) ext_pos--;
                return (OperationView) ops.remove(i);
            }
        }
        return null;
    }

    private OperationView getPriorPushFromSubblock(Block subblock)
    {
        subblock.seekEnd();
        return subblock.removePriorPushOperation();
    }

    public CodeItem removeCurrentOperation()
    {
        ext_pos--;
        return (CodeItem) ops.remove(ext_pos);
    }

    public OperationView getPriorPushOperation()
    {
        int start_pos = ext_pos != END_BLOCK_POS ? ext_pos - 2 : ops.size() - 1;
        for (int i = start_pos; i >= 0 ; i--)
        {
            if (ops.get(i) instanceof Block)
            {
                OperationView priorPush = getPriorPushFromSubblock((Block) ops.get(i));
                if (priorPush != null) return priorPush;
            }
            else if (((OperationView) ops.get(i)).isPush())
            {
                return (OperationView) ops.get(i);
            }
        }
        return null;
    }

    public CodeItem getPreviousOperation()
    {
        return (CodeItem) (ext_pos - 2 >= 0 ? ops.get(ext_pos - 2) : null);
    }

    public CodeItem removePreviousOperation()
    {
        if (ext_pos - 2 >= 0)
        {
            CodeItem ci = (CodeItem) ops.remove(ext_pos - 2);
            ext_pos--;
            return ci;
        }
        return null;
    }

    public CodeItem getNextOperation()
    {
        if (hasMoreOperations())
        {
            return (CodeItem) ops.get(ext_pos);
        }
        return null;
    }

    public void replaceOperation(long start_byte, CodeItem newop)
    {
        for (int i = 0; i < ops.size(); i++)
        {
            if (((CodeItem) ops.get(i)).getStartByte() == start_byte)
            {
                ops.remove(i);
                ops.add(i, newop);
                return;
            }
        }
    }

    public void replaceCurrentOperation(CodeItem newop)
    {
        if (ext_pos > 0 && ext_pos != END_BLOCK_POS)
        {
            if (newop != null)
            {
                ops.set(ext_pos - 1, newop);
                if (newop instanceof Block) ((Block) newop).setParent(this);
            }
            else
            {
                ops.remove(ext_pos - 1);
                ext_pos--;
            }
        }
    }

    public void truncate(long fromStartByte)
    {
        Iterator it = ops.iterator();
        while (it.hasNext())
        {
            CodeItem ci = (CodeItem) it.next();
            if (ci.getStartByte() >= fromStartByte) it.remove();
        }
    }

    public MethodSourceView getMethodView()
    {
        Block preBlock = this;
        while (preBlock.parent != null)
        {
            preBlock = preBlock.parent;
        }
        return preBlock.m_info;
    }

    public boolean isEmpty()
    {
        return ops == null || ops.isEmpty(); 
    }

    public int size()
    {
        return ops == null ? 0 : ops.size();
    }

    public int printedSize()
    {
        int s = 0;
        for (int i = 0; i < ops.size(); i++)
        {
            if (ops.get(i) instanceof Block || ((OperationView) ops.get(i)).isPrintable())
            {
                s++;
            }
        }
        return s;
    }

    public CodeItem getFirstOperation()
    {
        return ops.isEmpty() ? null : (CodeItem) ops.get(0);
    }

    public CodeItem getLastOperation()
    {
        return ops.isEmpty() ? null : (CodeItem) ops.get(ops.size() - 1);
    }

    public CodeItem getFirstPrintedOperation()
    {
        for (int i = 0; i < ops.size(); i++)
        {
            CodeItem citem = (CodeItem) ops.get(i);
            if (citem instanceof Block || ((OperationView) citem).isPrintable())
            {
                return citem;
            }
        }
        return null;
    }

    public CodeItem getLastPrintedOperation()
    {
        for (int i = ops.size() - 1; i >= 0; i--)
        {
            CodeItem citem = (CodeItem) ops.get(i);
            if (citem instanceof Block || ((OperationView) citem).isPrintable())
            {
                return citem;
            }
        }
        return null;
    }

    public long getStartByte()
    {
        if (ops == null || ops.isEmpty())
        {
            return fakeStartByte;
        }
        return ((CodeItem) ops.get(0)).getStartByte();
    }

    public String getSource()
    {
        return indent + "{" + NL + getOperationsSource() + indent + "}" + NL;
    }

    public String getOperationsSource()
    {
        return getOperationsSource(-1);
    }

    public String getOperationsSource(int endByte)
    {
        StringBuffer sb = new StringBuffer();
        boolean withLN = "yes".equals(getMethodView().getClazzView().getDecompileParameter(ClazzSourceView.WITH_LINE_NUMBERS)) &&
                         (getMethodView().getMethod().getCodeBlock() != null) &&
                         (getMethodView().getMethod().getLineNumberTable() != null);
        
        for (Iterator i = ops.iterator(); i.hasNext();)
        {
            CodeItem citem = (CodeItem) i.next();
            if (endByte != -1 && citem.getStartByte() >= endByte) break;
            if (withLN &&
                (!(citem instanceof Block) ||
                       (citem instanceof IfBlock) ||
                       (citem instanceof Loop && (((Loop) citem).isPrintPrecondition()))) )
            {
                int instruction;
                if (citem instanceof Loop)
                {
                    Condition cond0 = (Condition) ((List) ((Loop) citem).getConditions().get(0)).get(0);
                    instruction = (int) cond0.getIfOperation().getStartByte();
                }
                else
                {
                    instruction = (int) citem.getStartByte();
                }
                int ln = getMethodView().getMethod().getLineNumberTable().getLineNumber(instruction);
                if (ln != -1)
                {
                    // TODO improve printing line numbers
                    sb.append(indent).append("    ").append("/* ").append(ln).append(" */");
                }
            }
            if (citem instanceof Block)
            {
                ((Block) citem).setIndent(indent + "    ");
                sb.append(((Block) citem).getSource());
            }
            else
            {
                OperationView ov = (OperationView) citem;
                if (ov.isPrintable())
                {
                    String opSource = ov.source2();
                    if (opSource != null) sb.append(indent).append("    ").append(opSource).append(";").append(NL);
                }
            }
        }
        return sb.toString();
    }

    protected String alias(String fqn)
    {
        return ImportManager.getInstance().importClass(fqn, getMethodView().getClazzView());
    }

    public void analyze(Block block)
    {
    }

    // Local Variables management

    protected Map lvars = new HashMap();    // var number > Map (start byte > LocalVariable)

    protected LocalVariable retrieve(Integer num, int start_byte)
    {
        Map lvs = (Map) lvars.get(num);
        if (lvs == null) return null;

        LocalVariable lv = null;
        for (Iterator i = lvs.entrySet().iterator(); i.hasNext();)
        {
            Map.Entry entry = (Entry) i.next();
            if (start_byte >= ((Integer) entry.getKey()).intValue())
            {
                lv = (LocalVariable) entry.getValue();
            }
        }

        return lv;
    }

    public LocalVariable getLocalVariable(int ivar, String type, int start_byte)
    {
        LocalVariable lv = retrieve(new Integer(ivar), start_byte);
        Block parentBlock = this;
        while ((lv == null) && (parentBlock != null))
        {
            lv = (LocalVariable) parentBlock.retrieve(new Integer(ivar), start_byte);
            parentBlock = parentBlock.parent;
        }

        LocalVariableTable.LocalVariable lv_debug = null;
        if (m_info.getMethod().getCodeBlock() != null && m_info.getMethod().getCodeBlock().getLocalVariableTable() != null)
        {
            lv_debug = m_info.getMethod().getCodeBlock().getLocalVariableTable().getLocalVariable(ivar, start_byte);
        }
        if (lv != null && lv_debug != null)
        {
            if (lv.getName() != null && !lv.getName().equals(lv_debug.name.getValue()))
            {
                // Reuse local variable
                lv = null;
            }
        }
        if (lv != null && type != null && !LocalVariable.UNKNOWN_TYPE.equals(lv.getType()) && !lv.getType().equals(type))
        {
            if (lv_debug != null)
            {
                try
                {
                    type = new FieldDescriptor(lv_debug.descriptor.getString()).getFQN();
                }
                catch (ClazzException e)
                {
                    throw new RuntimeException(e);
                }
            }

            if (!lv.isIsMethodArg() && !checkType(type, lv.getType()))
            {
                // Reuse local variable
                lv = null;
            }
        }
        if (lv != null && type != null && LocalVariable.UNKNOWN_TYPE.equals(lv.getType()))
        {
            lv.renewType(type);
        }
        if (lv == null)
        {
            lv = new LocalVariable(ivar, type, getMethodView());
            storeLVInBlock(ivar, lv, start_byte);
        }
        return lv;
    }

    protected void storeLVInBlock(int ivar, LocalVariable lv, int start_byte)
    {
        Block storeBlock = this;
        if (storeBlock instanceof Condition)
        {
            Block parent4cond = storeBlock.parent;
            if (parent4cond instanceof IfBlock)
            {
                storeBlock = parent4cond.parent;
            }
            if (parent4cond instanceof Loop)
            {
                if (((Loop) parent4cond).isBackLoop())
                {
                    storeBlock = parent4cond;
                }
                else
                {
                    storeBlock = parent4cond.parent;
                }
            }
        }

        Map lvs = (Map) storeBlock.lvars.get(new Integer(ivar));
        if (lvs == null)
        {
            lvs = new TreeMap();
            storeBlock.lvars.put(new Integer(ivar), lvs);
        }

        lvs.put(new Integer(start_byte), lv);
    }

    // Checks if fqn can be casted to fqnBase without special cast operator
    private boolean checkType(String fqn, String fqnBase)
    {
        if (fqn.equals(fqnBase)) return true;

        try
        {
            return widePrimitiveConversion(fqn, fqnBase);
        }
        catch (IllegalArgumentException iae)
        {
            // Continue check
        }

        // Widening Reference Conversions
        // Try to instantiate class
        try
        {
            Class fqnClass = Class.forName(fqn);
            Class fqnBaseClass = Class.forName(fqnBase);
            return fqnBaseClass.isAssignableFrom(fqnClass);
        }
        catch (ClassNotFoundException e)
        {
            // If can not instantiate then always return true
            return true;
        }
    }

    public static boolean widePrimitiveConversion(String type, String wideType)
    {
        if ("boolean".equals(wideType) && "int".equals(type)) return true;

        // Widening primitive conversions
        if ("byte".equals(type))
        {
            return "short".equals(wideType) || "int".equals(wideType) || "long".equals(wideType) ||
                   "float".equals(wideType) || "double".equals(wideType);
        }
        else if ("short".equals(type))
        {
            return "int".equals(wideType) || "long".equals(wideType) ||
                   "float".equals(wideType) || "double".equals(wideType);
        }
        else if ("char".equals(type))
        {
            return "int".equals(wideType) || "long".equals(wideType) ||
                   "float".equals(wideType) || "double".equals(wideType);
        }
        else if ("int".equals(type))
        {
            return "long".equals(wideType) ||
                   "float".equals(wideType) || "double".equals(wideType);
        }
        else if ("long".equals(type))
        {
            return "float".equals(wideType) || "double".equals(wideType);
        }
        else if ("float".equals(type))
        {
            return "double".equals(wideType);
        }
        else if ("double".equals(type))
        {
            return false;
        }
        else if ("boolean".equals(type))
        {
            return false;
        }
        else if ("void".equals(type))
        {
            return false;
        }
        else
        {
            throw new IllegalArgumentException("Not a primitive type");
        }
    }

    public void analyze2(Block block)
    {
        analyze(block);
    }

    public void preanalyze(Block block)
    {
    }

    public void postanalyze(Block block)
    {
    }
}
