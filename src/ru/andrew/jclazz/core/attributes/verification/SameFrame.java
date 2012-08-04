package ru.andrew.jclazz.core.attributes.verification;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.ClazzException;
import ru.andrew.jclazz.core.io.ClazzInputStream;
import ru.andrew.jclazz.core.io.ClazzOutputStream;

import java.io.IOException;

public class SameFrame extends StackMapFrame {
    public SameFrame(int frame_type) {
        super(frame_type);
    }

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException {
    }

    public void store(ClazzOutputStream cos) throws IOException {
    }

    public int getOffsetDelta() {
        return frame_type;
    }

    public String toString() {
        return prefix(this);
    }
}
