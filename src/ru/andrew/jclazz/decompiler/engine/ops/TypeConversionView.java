package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;

public class TypeConversionView extends OperationView
{
    private String convValue;

    public TypeConversionView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String getPushType()
    {
        return ((TypeConversion) operation).getCastType();
    }

    public String source()
    {
        return "(" + ((TypeConversion) operation).getCastType() + ") " + convValue;
    }

    public void analyze(Block block)
    {
        /*
        OperationView prev = block.removePriorPushOperation();
        convValue = prev.source();
         */
    }

    public void analyze2(Block block)
    {
        OperationView prev = context.pop();
        view = new Object[]{"(" + ((TypeConversion) operation).getCastType() + ") ", prev};
        context.push(this);
    }

    public boolean isPrintable()
    {
        return false;
    }
}
