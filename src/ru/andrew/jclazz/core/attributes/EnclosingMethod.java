package ru.andrew.jclazz.core.attributes;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.constants.*;

import java.io.*;

public class EnclosingMethod extends AttributeInfo
{
    private CONSTANT_Class enclosingClass;
    private CONSTANT_NameAndType enclosingMethod;

    public EnclosingMethod(CONSTANT_Utf8 attributeName, Clazz clazz)
    {
        super(attributeName, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException, ClazzException
    {
        attributeLength = (int) cis.readU4();

        enclosingClass = (CONSTANT_Class) clazz.getConstant_pool()[cis.readU2()];
        int enclosing_method_index = cis.readU2();
        if (enclosing_method_index > 0)
        {
            enclosingMethod = (CONSTANT_NameAndType) clazz.getConstant_pool()[enclosing_method_index];
        }
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU4(attributeLength);
        cos.writeU2(enclosingClass.getIndex());
        if (enclosingMethod == null)
        {
            cos.writeU2(0);
        }
        else
        {
            cos.writeU2(enclosingMethod.getIndex());
        }
    }

    public CONSTANT_Class getEnclosingClass()
    {
        return enclosingClass;
    }

    public CONSTANT_NameAndType getEnclosingMethod()
    {
        return enclosingMethod;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(ATTR);
        sb.append("EnclosingMethod: \n");
        sb.append(INDENT).append("Enclosing class: ").append(enclosingClass.getFullyQualifiedName());
        if (enclosingMethod != null)
        {
            sb.append("\n");
            sb.append(INDENT).append("Enclosing method: ").append(enclosingMethod.getValue());
        }
        return sb.toString();
    }
}
