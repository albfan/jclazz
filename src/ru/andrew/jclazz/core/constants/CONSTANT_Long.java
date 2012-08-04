package ru.andrew.jclazz.core.constants;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.ClazzException;
import ru.andrew.jclazz.core.io.ClazzInputStream;
import ru.andrew.jclazz.core.io.ClazzOutputStream;

import java.io.IOException;

public class CONSTANT_Long extends CONSTANT {
    private long longValue;

    protected CONSTANT_Long(int num, int tag, Clazz clazz) {
        super(num, tag, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException {
        longValue = cis.readLong();
    }

    public void update() throws ClazzException {
    }

    public String getType() {
        return "long";
    }

    public long getLong() {
        return longValue;
    }

    public String getValue() {
        return String.valueOf(longValue) + "L";
    }

    public void store(ClazzOutputStream cos) throws IOException {
        super.store(cos);

        cos.writeLong(longValue);
    }
}
