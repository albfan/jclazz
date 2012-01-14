package ru.andrew.jclazz.core.attributes.verification;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.constants.*;

import java.util.*;
import java.io.*;

public class VerificationTypeInfo
{
    private static Map types;   // int -> String

    private int tag;

    // Object variable
    private CONSTANT_Class object_class;

    // Uninitialized variable
    private int offset;

    static
    {
        types = new HashMap(9);
        types.put(new Integer(0), "Top");
        types.put(new Integer(1), "Integer");
        types.put(new Integer(2), "Float");
        types.put(new Integer(3), "Double");
        types.put(new Integer(4), "Long");
        types.put(new Integer(5), "Null");
        types.put(new Integer(6), "UninitializedThis");
        types.put(new Integer(7), "Object");
        types.put(new Integer(8), "Uninitialized");
    }

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException
    {
        tag = cis.readU1();

        if (tag == 7)   // Object
        {
            int cp_index = cis.readU2();
            object_class = (CONSTANT_Class) clazz.getConstant_pool()[cp_index];
        }
        if (tag == 8)   // Uninitialized
        {
            offset = cis.readU2();
        }
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU1(tag);
        if (tag == 7)   // Object
        {
            cos.writeU2(object_class.getIndex());
        }
        if (tag == 8)   // Uninitialized
        {
            cos.writeU2(offset);
        }
    }

    public int getTag()
    {
        return tag;
    }

    public String getType()
    {
        return (String) types.get(new Integer(tag));
    }

    public CONSTANT_Class getObjectClassForObjectVariable()
    {
        return object_class;
    }

    public int getOffsetForUninitializedVariable()
    {
        return offset;
    }

    public String toString()
    {
        return types.get(new Integer(tag)) +
                (tag == 7 ? "(" + object_class.getFullyQualifiedName() + ")" : "") +
                (tag == 8 ? "(at +" + offset + ")" : "");
    }
}
