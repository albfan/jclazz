package ru.andrew.jclazz.core.attributes;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.constants.*;

import java.io.*;

public class SourceDebugExtension extends AttributeInfo
{
    private String debugInfo;

    public SourceDebugExtension(CONSTANT_Utf8 attributeName, Clazz clazz)
    {
        super(attributeName, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException, ClazzException
    {
        attributeLength = (int) cis.readU4();
        byte[] bytes = new byte[attributeLength];
        for (int i = 0; i < attributeLength; i++)
        {
            bytes[i] = (byte) cis.readU1();
        }
        debugInfo = new String(bytes, "UTF-8");
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        byte[] bytes = debugInfo.getBytes();
        if (attributeLength != bytes.length)
        {
            throw new RuntimeException("SourceDebugExtension: store: attribute length differs from String byte array length");
        }
        cos.writeU4(bytes.length);
        for (int i = 0; i < bytes.length; i++)
        {
            cos.writeU1(bytes[i]);
        }
    }

    public String getDebugInfo()
    {
        return debugInfo;
    }

    public String toString()
    {
        return ATTR + "SourceDebugExtension: " + debugInfo;
    }
}
