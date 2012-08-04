package ru.andrew.jclazz.core.constants;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.ClazzException;
import ru.andrew.jclazz.core.io.ClazzInputStream;
import ru.andrew.jclazz.core.io.ClazzOutputStream;

import java.io.IOException;

public class CONSTANT_Integer extends CONSTANT {
    private int intValue;

    protected CONSTANT_Integer(int num, int tag, Clazz clazz) {
        super(num, tag, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException {
        intValue = cis.readInt();
    }

    public void update() throws ClazzException {
    }

    public String getType() {
        return "int";
    }

    public int getInt() {
        return intValue;
    }

    public String getValue() {
        return String.valueOf(intValue);
    }

    public void store(ClazzOutputStream cos) throws IOException {
        super.store(cos);

        cos.writeInt(intValue);
    }
}
