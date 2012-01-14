package ru.andrew.jclazz.core.attributes;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.constants.*;

import java.io.*;

public class Exceptions extends AttributeInfo
{
    private CONSTANT_Class[] exception_table;

    public Exceptions(CONSTANT_Utf8 attributeName, Clazz clazz)
    {
        super(attributeName, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException, ClazzException
    {
        attributeLength = (int) cis.readU4();

        int number_of_exceptions = cis.readU2();
        exception_table = new CONSTANT_Class[number_of_exceptions];
        for (int i = 0; i < number_of_exceptions; i++)
        {
            int index = cis.readU2();
            exception_table[i] = (CONSTANT_Class) clazz.getConstant_pool()[index];
        }
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU4(attributeLength);
        for (int i = 0; i < exception_table.length; i++)
        {
            cos.writeU2(exception_table[i].getIndex());
        }
    }

    public CONSTANT_Class[] getExceptionTable()
    {
        return exception_table;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(ATTR);
        sb.append("Exceptions: \n");
        for (int i = 0; i < exception_table.length; i++)
        {
            sb.append(INDENT).append(exception_table[i].getFullyQualifiedName());
            if (i < exception_table.length - 1) sb.append("\n");
        }
        return sb.toString();
    }
}
