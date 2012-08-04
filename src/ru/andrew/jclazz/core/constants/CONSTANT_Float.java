package ru.andrew.jclazz.core.constants;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.ClazzException;
import ru.andrew.jclazz.core.io.ClazzInputStream;
import ru.andrew.jclazz.core.io.ClazzOutputStream;

import java.io.IOException;

public class CONSTANT_Float extends CONSTANT {
    private float floatValue;

    protected CONSTANT_Float(int num, int tag, Clazz clazz) {
        super(num, tag, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException {
        floatValue = cis.readFloat();
    }

    public void update() throws ClazzException {
    }

    public String getType() {
        return "float";
    }

    public float getFloat() {
        return floatValue;
    }

    public String getValue() {
        return String.valueOf(floatValue) + "f";
    }

    public void store(ClazzOutputStream cos) throws IOException {
        super.store(cos);

        cos.writeFloat(floatValue);
    }
}
