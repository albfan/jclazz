package ru.andrew.jclazz.core.attributes.verification;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;

import java.io.*;

public class SameLocals1StackItemFrame extends StackMapFrame
{
    private VerificationTypeInfo vti;

    public SameLocals1StackItemFrame(int frame_type)
    {
        super(frame_type);
    }

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        vti = new VerificationTypeInfo();
        vti.load(cis, clazz);
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        vti.store(cos);
    }

    public int getOffsetDelta()
    {
        return frame_type - 64;
    }

    public VerificationTypeInfo getVerificationTypeInfo()
    {
        return vti;
    }

    public String toString()
    {
        return prefix(this) + "stack item = " + vti.toString();
    }
}
