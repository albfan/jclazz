package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;

public class GoToView extends OperationView
{
    private boolean isBreak = false;
    private boolean isContinue = false;

    private Loop loop;  //  inner loop support

    public GoToView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public void setBreak(boolean isBreak)
    {
        this.isBreak = isBreak;
        view = new Object[]{"break"};
    }

    public void setContinue(boolean isContinue)
    {
        this.isContinue = isContinue;
        view = new Object[]{"continue"};
    }

    public boolean isPrintable()
    {
        return isBreak || isContinue;
    }

    public boolean isBreak()
    {
        return isBreak;
    }

    public boolean isContinue()
    {
        return isContinue;
    }

    public Loop getLoop()
    {
        return loop;
    }

    public void setLoop(Loop loop)
    {
        this.loop = loop;
    }

    public String getPushType()
    {
        return null;
    }

    public String source()
    {
        if (isBreak)
        {
            return "break";
        }
        else if (isContinue)
        {
            return "continue";
        }
        else if (loop != null)
        {
            // Print nothing
            return null;
        }
        else
        {
            return "goto " + getTargetOperation();
        }
    }

    public void analyze(Block block)
    {
    }

    public void analyze2(Block block)
    {
    }

    public long getTargetOperation()
    {
        return ((GoTo) operation).getTargetOperation();
    }

    public boolean isForwardBranch()
    {
        return getTargetOperation() > getStartByte();
    }
}
