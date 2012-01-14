package ru.andrew.jclazz.decompiler;

import ru.andrew.jclazz.core.attributes.*;

public class InnerClassView
{
    private InnerClass innerClass;
    private ClazzSourceView clazzView;

    public InnerClassView(InnerClass innerClass)
    {
        this.innerClass = innerClass;
    }

    public ClazzSourceView getClazzView()
    {
        return clazzView;
    }

    public void setClazzView(ClazzSourceView clazzView)
    {
        this.clazzView = clazzView;
    }

    public String getInnerFQN()
    {
        return innerClass.getInnerClass() == null ? null : innerClass.getInnerClass().getFullyQualifiedName();
    }

    public InnerClass getInnerClass()
    {
        return innerClass;
    }
}
