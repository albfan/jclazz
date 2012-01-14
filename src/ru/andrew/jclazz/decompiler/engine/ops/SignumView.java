package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;
import ru.andrew.jclazz.core.code.ops.*;

public class SignumView extends OperationView
{
    private OperationView var1;
    private OperationView var2;

    public SignumView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String getPushType()
    {
        return ((Signum) operation).getResultType();
    }

    public String source()
    {
        return "signum(" + var1 + " - " + var2 + ")";
    }

    public OperationView getVar1()
    {
        return var1;
    }

    public OperationView getVar2()
    {
        return var2;
    }

    public void analyze(Block block)
    {
        //OperationView prev1 = block.removePriorPushOperation();
        //var1 = prev1.source();
        //OperationView prev2 = block.removePriorPushOperation();
        //var2 = prev2.source();
    }

    public void analyze2(Block block)
    {
        var1 = context.pop();
        var2 = context.pop();
        view = new Object[]{"signum(", var1, " - ", var2, ")"};
        context.push(this);
    }

    public boolean isPrintable()
    {
        return false;
    }
}
