package ru.andrew.jclazz.core.attributes;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.ClazzException;
import ru.andrew.jclazz.core.constants.CONSTANT_Utf8;
import ru.andrew.jclazz.core.io.ClazzInputStream;
import ru.andrew.jclazz.core.io.ClazzOutputStream;

import java.io.IOException;

public class Deprecated extends AttributeInfo {
    public Deprecated(CONSTANT_Utf8 attributeName, Clazz clazz) {
        super(attributeName, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException, ClazzException {
        attributeLength = (int) cis.readU4();
    }

    public void store(ClazzOutputStream cos) throws IOException {
        cos.writeU4(attributeLength);
    }

    public String toString() {
        return ATTR + "Deprecated";
    }
}
