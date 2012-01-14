package ru.andrew.jclazz.decompiler.engine.blocks;

import ru.andrew.jclazz.decompiler.*;
import ru.andrew.jclazz.decompiler.engine.ops.*;
import ru.andrew.jclazz.decompiler.engine.*;

import java.util.*;
import java.util.ArrayList;

public class Condition extends Block
{
    private IfView ifOp;

    private OperationView var1;
    private OperationView var2;
    private String var2str = "";
    private String operation;
    private boolean isVar1Boolean;

    private boolean needReverseOperation = true;
    private static final Map reversedOps;
    private static final Map negOps;

    static
    {
        reversedOps = new HashMap(6);
        reversedOps.put("!=", "==");
        reversedOps.put("==", "!=");
        reversedOps.put(">=", "<");
        reversedOps.put("<", ">=");
        reversedOps.put("<=", ">");
        reversedOps.put(">", "<=");

        negOps = new HashMap(6);
        negOps.put("!=", "!=");
        negOps.put("==", "==");
        negOps.put(">=", "<=");
        negOps.put("<", ">");
        negOps.put("<=", ">=");
        negOps.put(">", "<");
    }

    public void setNeedReverseOperation(boolean needReverseOperation)
    {
        this.needReverseOperation = needReverseOperation;
    }

    public IfView getIfOperation()
    {
        return ifOp;
    }

    public Condition(IfView ifOp, Block parent, List ops)
    {
        super(parent, ops != null ? ops : new ArrayList());
        this.ifOp = ifOp;

        switch (ifOp.getOpcode())
        {
            case 153:
                operation = "==";
                break;
            case 159:
                operation = "==";
                break;
            case 154:
                operation = "!=";
                break;
            case 160:
                operation = "!=";
                break;
            case 155:
                operation = "<";
                break;
            case 161:
                operation = "<";
                break;
            case 156:
                operation = ">=";
                break;
            case 162:
                operation = ">=";
                break;
            case 157:
                operation = ">";
                break;
            case 163:
                operation = ">";
                break;
            case 158:
                operation = "<=";
                break;
            case 164:
                operation = "<=";
                break;
            case 165:
                operation = "==";
                break;
            case 166:
                operation = "!=";
                break;
            case 198:
                operation = "==";
                break;
            case 199:
                operation = "!=";
                break;
            default:
                throw new OperationException("BrachCondition: unknown opcode");
        }
    }

    public long getStartByte()
    {
        return ifOp.getStartByte();
    }

    public String getSource()
    {
        return "";
    }

    public void analyze(Block block)
    {
        reset();
        while (hasMoreOperations())
        {
            CodeItem citem = next();
            if (hasMoreOperations()) citem.analyze2(this);    // Don't analyze last If
        }

        analyzeIf(block);
    }

    private void analyzeIf(Block blockA)
    {
        Block block = getPriorPushOperation() != null ? this : blockA;
        int opcode = ifOp.getOpcode();

        block.removePriorPushOperation();
        OperationView prev1 = context.pop();
        var1 = prev1;

        isVar1Boolean = "boolean".equals(prev1.getPushType());

        if (opcode == 198 || opcode == 199)
        {
            var2str = "null";
        }
        else if (opcode >= 153 && opcode <= 158)
        {
            var2str = "0";
            if (prev1 instanceof SignumView)
            {
                var2 = ((SignumView) prev1).getVar2();
                var2str = "";
                var1 = ((SignumView) prev1).getVar1();
            }
            else
            {
                operation = (String) negOps.get(operation);
            }
        }
        else
        {
            PopView prevPop = null;
            if (getOperationPriorTo(prev1.getStartByte()) instanceof PopView)
            {
                prevPop = (PopView) getOperationPriorTo(prev1.getStartByte());
            }
            block.removePriorPushOperation();
            OperationView prev2 = context.pop();
            if (prevPop != null)
            {
                //LocalVariable popedLV = block.getLocalVariable(prevPop.getLocalVariableNumber(), null, (int) prevPop.getStartByte());
                LocalVariable popedLV = prevPop.getLocalVariable();
                //popedLV.setPrinted(true);
                var2 = prevPop;
                //popedLV.setPrinted(false);
                //popedLV.ensure((int) prevPop.getStartByte() + 1);
                FakePopView fakePop = new FakePopView(getMethodView(), popedLV, "0");
                // insert fake pop before loop
                getParent().getParent().addOperation(fakePop, block.getParent().getStartByte());
            }
            else
            {
                var2 = prev2;
            }
            // TODO what to do with signum
        }
    }

    /**
     * @deprecated 
     */
    public String str()
    {
        /*
        if (isVar1Boolean && "0".equals(var2str))
        {
            String oper = needReverseOperation ? (String) reversedOps.get(operation) : operation;
            if ("==".equals(oper))
            {
                return "!" + var1.source2();
            }
            else if ("!=".equals(oper))
            {
                return var1.source2();
            }
        }
        return ("".equals(var2str) ? (var2 instanceof PopView ? "(" + var2.source2() + ")" : var2.source2()) : var2str) + " " + (needReverseOperation ? (String) reversedOps.get(operation) : operation) + " " + var1.source2();
         */
        StringBuffer sb = new StringBuffer();
        Iterator i = getView().iterator();
        while (i.hasNext())
        {
            Object obj = i.next();
            if (obj instanceof String)
            {
                sb.append((String) obj);
            }
            else
            {
                sb.append(((OperationView) obj).source2());
            }
        }
        return sb.toString();
    }

    public List getView()
    {
        List src = new ArrayList();
        if (isVar1Boolean && "0".equals(var2str))
        {
            String oper = needReverseOperation ? (String) reversedOps.get(operation) : operation;
            if ("==".equals(oper))
            {
                src.add("!");
                src.add(var1);
                return src;
            }
            else if ("!=".equals(oper))
            {
                src.add(var1);
                return src;
            }
        }
        if ("".equals(var2str))
        {
            if (var2 instanceof PopView)
            {
                src.add("(");
                src.add(var2);
                src.add(")");
            }
            else
            {
                src.add(var2);
            }
        }
        else
        {
            src.add(var2str);
        }
        src.add(" ");
        if (needReverseOperation)
        {
            src.add(reversedOps.get(operation));
        }
        else
        {
            src.add(operation);
        }
        src.add(" ");
        src.add(var1);
        return src;
    }

    protected void storeLVInBlock(int ivar, LocalVariable lv)
    {
        parent.lvars.put(new Integer(ivar), lv);
    }
}
