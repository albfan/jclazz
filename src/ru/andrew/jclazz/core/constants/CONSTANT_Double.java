package ru.andrew.jclazz.core.constants;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.ClazzException;
import ru.andrew.jclazz.core.io.ClazzInputStream;
import ru.andrew.jclazz.core.io.ClazzOutputStream;

import java.io.IOException;

public class CONSTANT_Double extends CONSTANT {
    private double doubleValue;

    protected CONSTANT_Double(int num, int tag, Clazz clazz) {
        super(num, tag, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException {
        doubleValue = cis.readDouble();
    }

    public void update() throws ClazzException {
    }

    public String getType() {
        return "double";
    }

    public double getDouble() {
        return doubleValue;
    }

    public String getValue() {
        return String.valueOf(doubleValue);
    }

    public void store(ClazzOutputStream cos) throws IOException {
        super.store(cos);

        cos.writeDouble(doubleValue);
    }
}
