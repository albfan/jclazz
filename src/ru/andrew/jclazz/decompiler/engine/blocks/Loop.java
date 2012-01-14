package ru.andrew.jclazz.decompiler.engine.blocks;

import ru.andrew.jclazz.decompiler.engine.ops.*;
import ru.andrew.jclazz.decompiler.engine.*;

import java.util.*;

public class Loop extends Block
{
    private List andConditions = new ArrayList();
    private boolean alwaysTrueCondition = false;

    private boolean printPrecondition;   //WHILE(){} vs DO{}WHILE()
    private boolean isBackLoop;          // If conditions at the end of block then true

    private long begin_pc;

    // For-style specific variables
    private boolean isInForStyle = false;
    private long incPartStartByte;

    public Loop(Block parent, boolean isBackLoop)
    {
        super(parent);
        this.isBackLoop = isBackLoop;
        this.printPrecondition = !isBackLoop;
    }

    public void setPrintPrecondition(boolean precondition)
    {
        this.printPrecondition = precondition;
    }

    public boolean isPrintPrecondition()
    {
        return printPrecondition;
    }

    public boolean isAlwaysTrueCondition() {
        return alwaysTrueCondition;
    }

    public void setAlwaysTrueCondition(boolean alwaysTrueCondition) {
        this.alwaysTrueCondition = alwaysTrueCondition;
    }

    public void postCreate()
    {
        // Remove last goto
        if (!isBackLoop && (getLastOperation() instanceof GoToView))
        {
            // TODO bug for inner loops
            //GoToView lastGoto = (GoToView) removeLastOperation();
            GoToView lastGoto = (GoToView) getLastOperation();
            lastGoto.setLoop(this);
            begin_pc = lastGoto.getTargetOperation();
        }
        else
        {
            begin_pc = getFirstOperation().getStartByte();
        }
    }
    
    public void addAndConditions(List ops)
    {
        List orConditions = new ArrayList();
        List newOps = new ArrayList();
        Iterator i = ops.iterator();
        while (i.hasNext())
        {
            CodeItem ci = (CodeItem) i.next();
            newOps.add(ci);
            if (ci instanceof IfView)
            {
                Condition cond = new Condition((IfView) ci, this, new ArrayList(newOps));
                cond.setNeedReverseOperation(!isBackLoop);
                orConditions.add(cond);
                newOps.clear();
            }
        }
        andConditions.add(orConditions);
    }

    public void addAndConditions2(List conditions)
    {
        andConditions.addAll(conditions);
    }

    public List getConditions()
    {
        return andConditions;
    }

    public boolean isBackLoop()
    {
        return isBackLoop;
    }

    public void setIncrementalPartStartOperation(long incPartStartByte)
    {
        this.incPartStartByte = incPartStartByte;
        isInForStyle = true;
    }

    public long getBeginPc()
    {
        return begin_pc;
    }

    public String getSource()
    {
        StringBuffer sb = new StringBuffer();
        if (isInForStyle)
        {
            sb.append(indent).append("for (; ");
            for (Iterator i = andConditions.iterator(); i.hasNext();)
            {
                List orConditions = (List) i.next();
                if (orConditions.size() > 1) sb.append("(");
                for (Iterator j = orConditions.iterator(); j.hasNext();)
                {
                    Condition cond = (Condition) j.next();
                    if (j.hasNext() && orConditions.size() > 1) cond.setNeedReverseOperation(false);
                    sb.append(cond.str());
                    if (j.hasNext()) sb.append(" || ");
                }
                if (orConditions.size() > 1) sb.append(")");
                if (i.hasNext()) sb.append(" && ");
            }
            sb.append(";");
            for (Iterator i = ops.iterator(); i.hasNext();)
            {
                CodeItem citem = (CodeItem) i.next();
                if (citem.getStartByte() >= incPartStartByte)
                {
                    // Suppose there are no Blocks
                    String opStr = ((OperationView) citem).source2();
                    if (opStr != null && !"".equals(opStr)) sb.append(" " + opStr);
                }
            }
            sb.append(")").append(NL);

            sb.append(indent).append("{").append(NL);
            sb.append(super.getOperationsSource((int) incPartStartByte));
            sb.append(indent).append("}").append(NL);
            return sb.toString();
        }
        if (!printPrecondition)
        {
            sb.append(indent).append("do").append(NL).append(super.getSource());
        }
        sb.append(indent).append("while ");
        if (alwaysTrueCondition)
        {
            sb.append("(true)");
        }
        else
        {
            if (andConditions.size() > 1) sb.append("(");
            for (Iterator i = andConditions.iterator(); i.hasNext();)
            {
                List orConditions = (List) i.next();
                if (orConditions.size() > 1) sb.append("(");
                for (Iterator j = orConditions.iterator(); j.hasNext();)
                {
                    Condition cond = (Condition) j.next();
                    if (j.hasNext() && orConditions.size() > 1) cond.setNeedReverseOperation(false);
                    sb.append("(" + cond.str() + ")");
                    if (j.hasNext()) sb.append(isBackLoop ? " && " : " || ");
                }
                if (orConditions.size() > 1) sb.append(")");
                if (i.hasNext()) sb.append(isBackLoop ? " || " : " && ");
            }
            if (andConditions.size() > 1) sb.append(")");
            sb.append(printPrecondition ? "" : ";");
        }
        sb.append(NL);
        if (printPrecondition)
        {
            sb.append(super.getSource());
        }
        return sb.toString();
    }

    public void preanalyze(Block block)
    {
        if (isBackLoop)
        {
            return;
        }

        // All other conditions are analyzed with themselves
        for (Iterator i = andConditions.iterator(); i.hasNext();)
        {
            List orConditions = (List) i.next();
            for (Iterator j = orConditions.iterator(); j.hasNext();)
            {
                Condition cond = (Condition) j.next();
                cond.analyze(block);
            }
        }
    }

    public void postanalyze(Block block)
    {
        if (!isBackLoop)
        {
            return;
        }

        this.seekEnd();
        // All other conditions are analyzed with themselves
        for (Iterator i = andConditions.iterator(); i.hasNext();)
        {
            List orConditions = (List) i.next();
            for (Iterator j = orConditions.iterator(); j.hasNext();)
            {
                Condition cond = (Condition) j.next();
                cond.analyze(this);
            }
        }
    }
}
