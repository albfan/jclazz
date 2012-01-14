package ru.andrew.jclazz.decompiler.engine.blocks;

import ru.andrew.jclazz.core.constants.*;
import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.engine.ops.*;

public class Catch extends Block
{
    private CONSTANT_Class exceptionClassInfo;
    private long handler_pc;

    private long retrieve_pc = Integer.MAX_VALUE;

    private LocalVariable lv;

    public Catch(long handler_pc, CONSTANT_Class cl_info, Block parent)
    {
        super(parent);
        this.handler_pc = handler_pc;
        exceptionClassInfo = cl_info;
    }

    public long getStartByte()
    {
        return handler_pc;
    }

    public void postCreate()
    {
        if (getLastOperation() instanceof GoToView)
        {
            if (!checkLastGoTo((GoToView) getLastOperation()))
            {
                retrieve_pc = ((GoToView) getLastOperation()).getTargetOperation();
            }
        }
    }

    public void postProcess()
    {
        // Removing first POP operation
        if (getFirstOperation() instanceof PopView)
        {
            PopView pop = (PopView) removeFirstOperation();
            if (exceptionClassInfo != null)
            {
                lv = getLocalVariable(pop.getLocalVariableNumber(), exceptionClassInfo.getFullyQualifiedName(), (int) pop.getStartByte());
                // +1 because in catch block first operation usually "store"
                lv.ensure((int) handler_pc + 1);
            }
        }

        // Removing last goto operation for catch blocks
        if (getLastOperation() instanceof GoToView)
        {
            if (!checkLastGoTo((GoToView) getLastOperation()))
            {
                removeLastOperation();
            }
        }

        // Removing last jsr operation for catch blocks
        if (getLastOperation() instanceof JsrView)
        {
            removeLastOperation();
        }

        // Removing last PUSH and THROW operations for finally blocks
        if (exceptionClassInfo == null)
        {
            // Support jsr-ret finally constructions
            if (getFirstOperation() instanceof JsrView)
            {
                // Here we have the following instructions: jsr, push, throw, store, ..., ret
                removeFirstOperation();
                removeFirstOperation();
                removeFirstOperation();
                removeFirstOperation();
                removeLastOperation();
            }
            else
            {
                if (getLastOperation() instanceof ThrowView)
                {
                    removeLastOperation();
                    if (getLastOperation() instanceof PushVariableView)
                    {
                        removeLastOperation();
                    }
                }
            }
        }
    }

    private boolean checkLastGoTo(GoToView oper)
    {
        Block ff_block = this;
        CodeItem ff_oper;
        do
        {
            ff_oper = ff_block.getOperationByStartByte(oper.getTargetOperation());
            if (ff_oper != null) break;
            ff_block = ff_block.getParent();
        }
        while (ff_block != null);
        if (ff_oper == null) return false;

        CodeItem prevFF = ff_block.getOperationPriorTo(ff_oper.getStartByte());
        if (prevFF instanceof Loop)
        {
            oper.setBreak(true);
            return true;
        }
        if (ff_block instanceof Loop)
        {
            Loop ff_loop = (Loop) ff_block;
            if (!ff_loop.isBackLoop() && (ff_loop.getBeginPc() == oper.getTargetOperation()))
            {
                oper.setContinue(true);
                return true;
            }
            else if (ff_loop.isBackLoop())
            {
                if (!(prevFF instanceof Catch))
                {
                    oper.setContinue(true);
                    return true;
                }
            }
        }

        return false;
    }

    public long getRetrievePc()
    {
        return retrieve_pc;
    }

    public String getExceptionType()
    {
        return exceptionClassInfo != null ? exceptionClassInfo.getFullyQualifiedName() : null;
    }

    public String getSource()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(indent);
        if (exceptionClassInfo != null)
        {
            sb.append("catch (").append(alias(exceptionClassInfo.getFullyQualifiedName())).append(" ").append(lv != null ? lv.getName() : "").append(")");
            lv.setPrinted(true);
        }
        else
        {
            sb.append("finally");
        }
        sb.append(NL).append(super.getSource());
        return sb.toString();
    }

    public boolean isFinally()
    {
        return exceptionClassInfo == null;
    }
}
