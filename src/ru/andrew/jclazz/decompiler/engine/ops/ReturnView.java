package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;
import ru.andrew.jclazz.core.code.ops.*;

public class ReturnView extends OperationView
{
    private String retValue;
    private boolean isBooleanReturned = false;

    private boolean isLastInBlock;

    public ReturnView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);

        if ("boolean".equals(methodView.getMethod().getDescriptor().getReturnType().getBaseType()))
        {
            isBooleanReturned = true;
        }
    }

    public String getPushType()
    {
        return null;
    }

    public String source()
    {
        if (getOpcode() == 177)   // Return void
        {
            return isLastInBlock ? null : "return";
        }
        else
        {
            if (isBooleanReturned)
            {
                if ("1".equals(retValue))
                {
                    return "return true";
                }
                else if ("0".equals(retValue))
                {
                    return "return false";
                }
                else
                {
                    return "return " + retValue;
                }
            }
            else
            {
                return "return " + retValue;
            }
        }
    }

    public void analyze(Block block)
    {
    }

    public void analyze2(Block block)
    {
        isLastInBlock = !block.hasMoreOperations();
        Block parent = block;
        long start = getStartByte();
        while (parent != null)
        {
            if (parent.getOperationAfter(start) != null)
            {
                isLastInBlock = false;
                break;
            }
            start = parent.getStartByte();
            parent = parent.getParent();
        }

        if ("<init>".equals(((Return) operation).getReturnType()))
        {
            // Return in constructor
            return;
        }
        
        if (getOpcode() == 177)   // Return void
        {
            if (!isLastInBlock)
            {
                view = new Object[]{"return"};
            }
        }
        else
        {
            OperationView retVal = context.pop();
            retValue = retVal.source3();
            if (isBooleanReturned)
            {
                if ("1".equals(retValue))
                {
                    view = new Object[]{"return true"};
                }
                else if ("0".equals(retValue))
                {
                    view = new Object[]{"return false"};
                }
                else
                {
                    if (retVal instanceof FakeConditionalPushView)
                    {
                        ((FakeConditionalPushView) retVal).forceBoolean();
                    }
                    view = new Object[]{"return ", retVal};
                }
            }
            else
            {
                view = new Object[]{"return ", retVal};
            }
        }
    }
}
