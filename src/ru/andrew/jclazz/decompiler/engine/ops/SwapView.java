package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.Operation;
import ru.andrew.jclazz.decompiler.MethodSourceView;
import ru.andrew.jclazz.decompiler.engine.blocks.Block;

public class SwapView extends OperationView
{
    public SwapView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }
    
    public String getPushType()
    {
        return null;
    }

    public void analyze(Block block)
    {
    }

    public void analyze2(Block block)
    {
        OperationView value1 = context.pop();
        OperationView value2 = context.pop();
        context.push(value1);
        context.push(value2);
    }

    public boolean isPrintable()
    {
        return false;
    }
}
