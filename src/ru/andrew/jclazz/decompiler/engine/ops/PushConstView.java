package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;

public class PushConstView extends OperationView
{
    private String pushValue;

    public PushConstView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);

        pushValue = ((PushConst) operation).isClassPushed() ? alias(((PushConst) operation).getPushValue()) : ((PushConst) operation).getPushValue();
    }

    public String getPushType()
    {
        return ((PushConst) operation).getPushType();
    }

    public void forceBoolean()
    {
        if ("0".equals(pushValue))
        {
            pushValue = "false";
        }
        if ("1".equals(pushValue))
        {
            pushValue = "true";
        }
    }

    protected void buildView()
    {
        view = new Object[]{pushValue};
    }

    public String getPushValue()
    {
        return ((PushConst) operation).getPushValue();
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
        context.push(this);
    }

    public boolean isPrintable()
    {
        return false;
    }
}
