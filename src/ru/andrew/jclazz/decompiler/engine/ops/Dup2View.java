package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.Dup2;
import ru.andrew.jclazz.core.code.ops.Operation;
import ru.andrew.jclazz.decompiler.MethodSourceView;
import ru.andrew.jclazz.decompiler.engine.blocks.Block;

public class Dup2View extends OperationView
{
    public Dup2View(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String getPushType()
    {
        return null;
    }

    public void analyze2(Block block)
    {
        int shift = ((Dup2) operation).getStackShift();
        switch (shift)
        {
            case 0:
            {
                OperationView value1 = context.pop();
                if (isCat2(value1))
                {
                    context.push(value1);
                    context.push(value1);
                }
                else
                {
                    OperationView value2 = context.pop();
                    context.push(value2);
                    context.push(value1);
                    context.push(value2);
                    context.push(value1);
                }
                break;
            }
            case 1:
            {
                OperationView value1 = context.pop();
                if (isCat2(value1))
                {
                    OperationView value2 = context.pop();
                    context.push(value1);
                    context.push(value2);
                    context.push(value1);
                }
                else
                {
                    OperationView value2 = context.pop();
                    OperationView value3 = context.pop();
                    context.push(value2);
                    context.push(value1);
                    context.push(value3);
                    context.push(value2);
                    context.push(value1);
                }
                break;
            }
            case 2:
            {
                OperationView value1 = context.pop();
                if (isCat2(value1))
                {
                    OperationView value2 = context.pop();
                    if (isCat2(value2))
                    {
                        context.push(value1);
                        context.push(value2);
                        context.push(value1);
                    }
                    else
                    {
                        OperationView value3 = context.pop();
                        context.push(value1);
                        context.push(value3);
                        context.push(value2);
                        context.push(value1);
                    }
                }
                else
                {
                    OperationView value2 = context.pop();
                    OperationView value3 = context.pop();
                    if (isCat2(value3))
                    {
                        context.push(value2);
                        context.push(value1);
                        context.push(value3);
                        context.push(value2);
                        context.push(value1);
                    }
                    else
                    {
                        OperationView value4 = context.pop();
                        context.push(value2);
                        context.push(value1);
                        context.push(value4);
                        context.push(value3);
                        context.push(value2);
                        context.push(value1);
                    }
                }
                break;
            }
        }
    }

    private boolean isCat2(OperationView op)
    {
        String v2pushType = op.getPushType();
        return "double".equals(v2pushType) || "long".equals(v2pushType);
    }

    public void analyze(Block block)
    {
    }
    
    public boolean isPrintable()
    {
        return false;
    }
}
