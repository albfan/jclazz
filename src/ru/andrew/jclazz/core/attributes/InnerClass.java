package ru.andrew.jclazz.core.attributes;

import ru.andrew.jclazz.core.constants.*;

public class InnerClass
{
    CONSTANT_Class inner_class;
    CONSTANT_Class outer_class;
    CONSTANT_Utf8 inner_name;
    int inner_class_access_flags;

    public CONSTANT_Class getInnerClass()
    {
        return inner_class;
    }

    public CONSTANT_Class getOuterClass()
    {
        return outer_class;
    }

    public String getInnerName()
    {
        return inner_name != null ? inner_name.getString() : null;
    }

    public int getInnerClassAccessFlags()
    {
        return inner_class_access_flags;
    }
}
