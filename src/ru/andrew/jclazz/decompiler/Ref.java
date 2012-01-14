package ru.andrew.jclazz.decompiler;

import ru.andrew.jclazz.decompiler.engine.LocalVariable;

public class Ref
{
    private String operation;
    private String type;
    private LocalVariable localVariable;

    private boolean isPrinted = false;

    public Ref(String operation, String type)
    {
        this.operation = operation;
        this.type = type;
    }

    public LocalVariable getLocalVariable()
    {
        return localVariable;
    }

    public String getOperation()
    {
        if (localVariable == null)
        {
            return operation;
        }
        else
        {
            if (isPrinted)
            {
                return localVariable.getName();
            }
            else
            {
                isPrinted = true;
                return operation;
            }
        }
    }

    public void setIsPrinted(boolean isPrinted)
    {
        this.isPrinted = isPrinted;
    }

    public String getType()
    {
        return type;
    }

    public void setLocalVariable(LocalVariable localVariable)
    {
        this.localVariable = localVariable;
    }
}
