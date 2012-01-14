package ru.andrew.jclazz.core.attributes.verification;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;

import java.io.*;

public class ChopFrame extends StackMapFrame
{
    private int offset_delta;

    public ChopFrame(int frame_type)
    {
        super(frame_type);
    }

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        offset_delta = cis.readU2();
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU2(offset_delta);
    }

    public int getOffsetDelta()
    {
        return offset_delta;
    }

    public int getLastAbsentLocals()
    {
        return 251 - frame_type;
    }

    public String toString()
    {
        return prefix(this) + getLastAbsentLocals() + " last absent locals";
    }
}
