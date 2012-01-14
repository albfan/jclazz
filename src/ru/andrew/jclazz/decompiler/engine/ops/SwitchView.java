package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;
import ru.andrew.jclazz.core.code.ops.*;

import java.util.*;

public class SwitchView extends OperationView
{
    public SwitchView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String getPushType()
    {
        return null;
    }

    public String source()
    {
        return "SWITCH VIEW";
    }

    public List getCases()
    {
        return ((Switch) operation).getCaseBlocks();
    }

    public void analyze(Block block)
    {
    }

    public void analyze2(Block block)
    {
    }
}
