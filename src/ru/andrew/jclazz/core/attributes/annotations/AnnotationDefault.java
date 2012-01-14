package ru.andrew.jclazz.core.attributes.annotations;

import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.constants.*;

import java.io.*;

public class AnnotationDefault extends AttributeInfo
{
    private ElementValuePair defaultValue;

    public AnnotationDefault(CONSTANT_Utf8 attributeName, Clazz clazz)
    {
        super(attributeName, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException, ClazzException
    {
        attributeLength = (int) cis.readU4();

        defaultValue = new ElementValuePair(null, (char) cis.readU1());
        defaultValue.loadValue(cis, clazz);
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU4(attributeLength);
        defaultValue.storeValue(cos);
    }

    public ElementValuePair getDefaultValue()
    {
        return defaultValue;
    }

    public String toString()
    {
        return ATTR + "AnnotationDefault: " + defaultValue.toString();
    }
}
