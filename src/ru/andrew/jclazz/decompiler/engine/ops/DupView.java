package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;

public class DupView extends OperationView
{
    public DupView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String getPushType()
    {
        return ref.getType();
    }

    public String source()
    {
        return ref.getOperation();
    }

    public void analyze(Block block)
    {
        /*
        OperationView prev = block.getPriorPushOperation();
        if (prev instanceof NewView)    // For new + init
        {
            // removing current Dup operation
            block.removeCurrentOperation();
            return;
        }
        ref = new Ref(prev.source(), prev.getPushType());
        prev.ref = this.ref;
         * */
    }

    public void analyze2(Block block)
    {
        OperationView value1 = context.pop();

        int shift = ((Dup) operation).getStackShift();
        switch (shift)
        {
            case 0:
            {
                context.push(value1);
                context.push(value1);
                break;
            }
            case 1:
            {
                OperationView value2 = context.pop();
                context.push(value1);
                context.push(value2);
                context.push(value1);
                break;
            }
            case 2:
            {
                OperationView value2 = context.pop();
                String v2pushType = value2.getPushType();
                if ("double".equals(v2pushType) || "long".equals(v2pushType))   //Category 2
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
                break;
            }
        }
        
    }

    public boolean isPrintable()
    {
        return false;
    }
}
