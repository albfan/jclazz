package ru.andrew.jclazz.core.attributes.verification;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;

import java.io.*;

public class SameFrame extends StackMapFrame
{
    public SameFrame(int frame_type)
    {
        super(frame_type);
    }

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
    }

    public int getOffsetDelta()
    {
        return frame_type;
    }

    public String toString()
    {
        return prefix(this);
    }
}
