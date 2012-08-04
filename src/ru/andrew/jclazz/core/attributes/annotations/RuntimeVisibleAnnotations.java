package ru.andrew.jclazz.core.attributes.annotations;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.ClazzException;
import ru.andrew.jclazz.core.attributes.AttributeInfo;
import ru.andrew.jclazz.core.constants.CONSTANT_Utf8;
import ru.andrew.jclazz.core.io.ClazzInputStream;
import ru.andrew.jclazz.core.io.ClazzOutputStream;

import java.io.IOException;

public class RuntimeVisibleAnnotations extends AttributeInfo {
    private Annotation[] annotations;

    public RuntimeVisibleAnnotations(CONSTANT_Utf8 attributeName, Clazz clazz) {
        super(attributeName, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException, ClazzException {
        attributeLength = (int) cis.readU4();

        int num_annotations = cis.readU2();
        annotations = new Annotation[num_annotations];
        for (int i = 0; i < num_annotations; i++) {
            annotations[i] = Annotation.load(cis, clazz);
        }
    }

    public void store(ClazzOutputStream cos) throws IOException {
        cos.writeU4(attributeLength);
        cos.writeU2(annotations.length);
        for (int i = 0; i < annotations.length; i++) {
            annotations[i].store(cos);
        }
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public String toString() {
        return toString("RuntimeVisibleAnnotations");
    }

    protected String toString(String attrName) {
        StringBuffer sb = new StringBuffer(ATTR);
        sb.append(attrName).append(": \n");
        for (int i = 0; i < annotations.length; i++) {
            sb.append(INDENT).append(annotations[i].toString());
            if (i < annotations.length - 1) sb.append("\n");
        }
        return sb.toString();
    }
}
