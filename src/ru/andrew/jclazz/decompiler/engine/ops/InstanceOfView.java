package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;

public class InstanceOfView extends OperationView
{
    private String var;

    public InstanceOfView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String getPushType()
    {
        return "boolean";
    }

    public String source()
    {
        return var + " instanceof " + alias(((InstanceOf) operation).getCastClass());
    }

    public void analyze(Block block)
    {
        /*
        OperationView prev = block.removePriorPushOperation();
        var = prev.source();
         * */
    }

    public void analyze2(Block block)
    {
        OperationView prev = context.pop();
        view = new Object[]{prev, " instanceof " + alias(((InstanceOf) operation).getCastClass())};
        context.push(this);
    }

    public boolean isPrintable()
    {
        return false;
    }
}
