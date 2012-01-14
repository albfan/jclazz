package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;
import ru.andrew.jclazz.core.code.ops.*;

public class MultiNewArrayView extends OperationView
{
    private String dims[];

    public MultiNewArrayView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
        dims = new String[((MultiNewArray) operation).getDimensions()];
    }

    public String getPushType()
    {
        return ((MultiNewArray) operation).getArrayType();
    }

    public String source()
    {
//        if (ref != null)
//        {
//            return ref.getOperation();
//        }
        StringBuffer sb = new StringBuffer("new " + alias(((MultiNewArray) operation).getArrayType().substring(0, ((MultiNewArray) operation).getArrayType().indexOf("[]"))));
        for (int i = 0; i < ((MultiNewArray) operation).getDimensions(); i++)
        {
            sb.append("[").append(dims[i]).append("]");
        }
        return sb.toString();
    }

    public void analyze(Block block)
    {
        /*
        for (int i = 0; i < ((MultiNewArray) operation).getDimensions(); i++)
        {
            OperationView push = block.removePriorPushOperation();
            dims[((MultiNewArray) operation).getDimensions() - i - 1] = push.source();
        }
         * */
    }

    public void analyze2(Block block)
    {
        OperationView[] dims2 = new OperationView[((MultiNewArray) operation).getDimensions()];
        for (int i = 0; i < ((MultiNewArray) operation).getDimensions(); i++)
        {
            OperationView push = context.pop();
            dims2[((MultiNewArray) operation).getDimensions() - i - 1] = push;
        }

        Object[] items = new Object[3 * dims2.length + 1];
        items[0] = "new " + alias(((MultiNewArray) operation).getArrayType().substring(0, ((MultiNewArray) operation).getArrayType().indexOf("[]")));
        for (int i = 0; i < ((MultiNewArray) operation).getDimensions(); i++)
        {
            items[i * 3 + 1] = "[";
            items[i * 3 + 2] = dims2[i];
            items[i * 3 + 3] = "]";
        }
        view = items;
        context.push(this);
    }

    public boolean isPrintable()
    {
        return false;
    }
}
