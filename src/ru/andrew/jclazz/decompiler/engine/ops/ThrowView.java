package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;

public class ThrowView extends OperationView
{
    private String thrownValue;

    public ThrowView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String getPushType()
    {
        return null;
    }

    public String source()
    {
        return "throw " + thrownValue;
    }

    public void analyze(Block block)
    {
        /*
        OperationView prev = block.removePriorPushOperation();
        thrownValue = prev.source();
         */
    }

    public void analyze2(Block block)
    {
        OperationView prev = context.pop();
        view = new Object[]{"throw ", prev};
        context.push(this);
    }
}
