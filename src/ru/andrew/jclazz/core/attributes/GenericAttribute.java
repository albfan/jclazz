package ru.andrew.jclazz.core.attributes;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.ClazzException;
import ru.andrew.jclazz.core.constants.CONSTANT_Utf8;
import ru.andrew.jclazz.core.io.ClazzInputStream;
import ru.andrew.jclazz.core.io.ClazzOutputStream;

import java.io.IOException;

public class GenericAttribute extends AttributeInfo {
    private int[] bytes;

    public GenericAttribute(CONSTANT_Utf8 attributeName, Clazz clazz) {
        super(attributeName, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException, ClazzException {
        attributeLength = (int) cis.readU4();
        bytes = new int[attributeLength];
        for (int i = 0; i < attributeLength; i++) {
            bytes[i] = cis.readU1();
        }
    }

    public void store(ClazzOutputStream cos) throws IOException {
        cos.writeU4(attributeLength);
        for (int i = 0; i < attributeLength; i++) {
            cos.writeU1(bytes[i]);
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(ATTR).append("(GEN)");
        sb.append(attributeName.getString()).append(": ");
        if (bytes.length > 0) {
            for (int i = 0; i < bytes.length; i++) {
                sb.append(bytes[i]).append(" ");
            }
        } else {
            sb.append("empty");
        }
        return sb.toString();
    }
}
