package ru.andrew.jclazz.core.attributes;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.ClazzException;
import ru.andrew.jclazz.core.constants.CONSTANT_Utf8;
import ru.andrew.jclazz.core.io.ClazzInputStream;
import ru.andrew.jclazz.core.io.ClazzOutputStream;

import java.io.IOException;

public class SourceFile extends AttributeInfo {
    private CONSTANT_Utf8 sourceFile;

    public SourceFile(CONSTANT_Utf8 attributeName, Clazz clazz) {
        super(attributeName, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException, ClazzException {
        attributeLength = (int) cis.readU4();
        if (attributeLength != 2) throw new ClazzException("SourceFile Attribute length is invalid");

        int sourcefile_index = cis.readU2();
        sourceFile = (CONSTANT_Utf8) clazz.getConstant_pool()[sourcefile_index];
    }

    public void store(ClazzOutputStream cos) throws IOException {
        cos.writeU4(attributeLength);
        cos.writeU2(sourceFile.getIndex());
    }

    public String getSourceFile() {
        return sourceFile.getString();
    }

    public String toString() {
        return ATTR + "SourceFile: " + sourceFile.getString();
    }
}
