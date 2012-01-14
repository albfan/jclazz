package ru.andrew.jclazz.core.attributes;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.constants.*;

import java.io.*;

public class ConstantValue extends AttributeInfo
{
    private CONSTANT constant;

    public ConstantValue(CONSTANT_Utf8 attributeName, Clazz clazz)
    {
        super(attributeName, clazz);
    }

    public void load(ClazzInputStream cis) throws ClazzException, IOException
    {
        attributeLength = (int) cis.readU4();
        if (attributeLength != 2) throw new ClazzException("Invalid length of ConstantValue attribute");
        int constantvalue_index = cis.readU2();
        constant = clazz.getConstant_pool()[constantvalue_index];
        if ( (!(constant instanceof CONSTANT_Long)) &&
                (!(constant instanceof CONSTANT_Float)) &&
                (!(constant instanceof CONSTANT_Double)) &&
                (!(constant instanceof CONSTANT_Integer)) &&
                (!(constant instanceof CONSTANT_String)) )
        {
            throw new ClazzException("ConstantValue is of illegal type");
        }
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU4(attributeLength);
        cos.writeU2(constant.getIndex());
    }

    public CONSTANT getConstant()
    {
        return constant;
    }

    public String toString()
    {
        return ATTR + "ConstantValue: " + constant.getValue();
    }
}
