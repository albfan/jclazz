package ru.andrew.jclazz.core.attributes.verification;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.ClazzException;
import ru.andrew.jclazz.core.io.ClazzInputStream;
import ru.andrew.jclazz.core.io.ClazzOutputStream;

import java.io.IOException;

public class SameLocals1StackItemFrameExtended extends StackMapFrame {
    private int offset_delta;
    private VerificationTypeInfo vti;

    public SameLocals1StackItemFrameExtended(int frame_type) {
        super(frame_type);
    }

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException {
        offset_delta = cis.readU2();
        vti = new VerificationTypeInfo();
        vti.load(cis, clazz);
    }

    public void store(ClazzOutputStream cos) throws IOException {
        cos.writeU2(offset_delta);
        vti.store(cos);
    }

    public int getOffsetDelta() {
        return offset_delta;
    }

    public VerificationTypeInfo getVerificationTypeInfo() {
        return vti;
    }

    public String toString() {
        return prefix(this) + "stack item = " + vti.toString();
    }
}
