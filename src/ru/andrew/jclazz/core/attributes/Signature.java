package ru.andrew.jclazz.core.attributes;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.constants.*;

import java.io.*;

public class Signature extends AttributeInfo
{
    private CONSTANT_Utf8 signature;

    public Signature(CONSTANT_Utf8 attributeName, Clazz clazz)
    {
        super(attributeName, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException, ClazzException
    {
        attributeLength = (int) cis.readU4();

        signature = (CONSTANT_Utf8) clazz.getConstant_pool()[cis.readU2()];
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU4(attributeLength);
        cos.writeU2(signature.getIndex());
    }

    public String getSignature()
    {
        return signature.getString();
    }

    public String toString()
    {
        return ATTR + "Signature: " + signature.getString();
    }
}
