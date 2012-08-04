package ru.andrew.jclazz.core.attributes;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.ClazzException;
import ru.andrew.jclazz.core.constants.CONSTANT_Utf8;
import ru.andrew.jclazz.core.io.ClazzInputStream;
import ru.andrew.jclazz.core.io.ClazzOutputStream;

import java.io.IOException;

public abstract class AttributeInfo {
    protected static final String INDENT = "    ";
    protected static final String ATTR = "@";

    protected CONSTANT_Utf8 attributeName;
    protected int attributeLength;
    protected Clazz clazz;

    protected AttributeInfo(CONSTANT_Utf8 attributeName, Clazz clazz) {
        this.attributeName = attributeName;
        this.clazz = clazz;
    }

    public CONSTANT_Utf8 getAttributeName() {
        return attributeName;
    }

    public int getAttributeNameIndex() {
        return attributeName.getIndex();
    }

    public int getAttributeLength() {
        return attributeLength;
    }

    public Clazz getClazz() {
        return clazz;
    }

    public abstract void load(ClazzInputStream cis) throws IOException, ClazzException;

    public abstract void store(ClazzOutputStream cos) throws IOException;
}
