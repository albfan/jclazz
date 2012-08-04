package ru.andrew.jclazz.core.attributes;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.ClazzException;
import ru.andrew.jclazz.core.constants.CONSTANT_Utf8;
import ru.andrew.jclazz.core.io.ClazzInputStream;
import ru.andrew.jclazz.core.io.ClazzOutputStream;

import java.io.IOException;

public class Signature extends AttributeInfo {
    private CONSTANT_Utf8 signature;

    public Signature(CONSTANT_Utf8 attributeName, Clazz clazz) {
        super(attributeName, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException, ClazzException {
        attributeLength = (int) cis.readU4();

        signature = (CONSTANT_Utf8) clazz.getConstant_pool()[cis.readU2()];
    }

    public void store(ClazzOutputStream cos) throws IOException {
        cos.writeU4(attributeLength);
        cos.writeU2(signature.getIndex());
    }

    public String getSignature() {
        return signature.getString();
    }

    public String toString() {
        return ATTR + "Signature: " + signature.getString();
    }
}
