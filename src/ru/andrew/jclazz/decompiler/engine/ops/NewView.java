package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;
import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.code.ops.*;

public class NewView extends OperationView
{
    private boolean isICConstructor = false;
    private boolean isACConstructor = false;
    private InnerClass anonymousClass;

    private String clazzName;

    public NewView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String getPushType()
    {
        return clazzName;
    }

    public boolean isICConstructor()
    {
        return isICConstructor;
    }

    public boolean isACConstructor()
    {
        return isACConstructor;
    }

    public InnerClass getAnonymousClass()
    {
        return anonymousClass;
    }

    public String source()
    {
        return alias(clazzName);
    }

    public void analyze(Block block)
    {
        // Inner Class support
        clazzName = ((New) operation).getNewType();
        if (clazzName.indexOf('$') != -1)
        {
            InnerClass ic = methodView.getClazzView().getInnerClass(clazzName);
            if (ic != null)
            {
                if (ic.getInnerName() == null)
                {
                    isACConstructor = true;
                    anonymousClass = ic;
                }
                clazzName = clazzName.substring(clazzName.indexOf('$') + 1);
                isICConstructor = true;
            }
        }
    }

    public void analyze2(Block block)
    {
        // Inner Class support
        clazzName = ((New) operation).getNewType();
        if (clazzName.indexOf('$') != -1)
        {
            InnerClass ic = methodView.getClazzView().getInnerClass(clazzName);
            if (ic != null)
            {
                if (ic.getInnerName() == null)
                {
                    isACConstructor = true;
                    anonymousClass = ic;
                }
                clazzName = clazzName.substring(clazzName.indexOf('$') + 1);
                isICConstructor = true;
            }
        }

        view = new Object[]{alias(clazzName)};
        context.push(this);
    }

    public boolean isPrintable()
    {
        return false;
    }
}
