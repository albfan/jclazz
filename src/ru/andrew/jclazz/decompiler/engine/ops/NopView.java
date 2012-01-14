package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;
import ru.andrew.jclazz.core.code.ops.*;

public class NopView extends OperationView
{
    public NopView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String getPushType()
    {
        return null;
    }

    public String source()
    {
        return null;
    }

    public void analyze(Block block)
    {
    }

    public void analyze2(Block block)
    {
    }

    public boolean isPrintable()
    {
        return false;
    }
}
