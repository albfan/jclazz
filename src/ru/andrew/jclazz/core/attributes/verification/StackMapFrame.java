package ru.andrew.jclazz.core.attributes.verification;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;

import java.io.*;

public abstract class StackMapFrame
{
    protected int frame_type;

    public StackMapFrame(int frame_type)
    {
        this.frame_type = frame_type;
    }

    public abstract void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException;

    public abstract void store(ClazzOutputStream cos) throws IOException;

    public abstract int getOffsetDelta();

    public static StackMapFrame loadStackMapFrame(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        int tag = cis.readU1();
        StackMapFrame smf;

        if (tag >= 0 && tag <= 63)
        {
            smf = new SameFrame(tag);
        }
        else if (tag >= 64 && tag <= 127)
        {
            smf = new SameLocals1StackItemFrame(tag);
        }
        else if (tag >= 128 && tag <= 246)  // Reserved for future use
        {
            throw new ClazzException("Unsupported StackMapFrame type");
        }
        else if (tag == 247)
        {
            smf = new SameLocals1StackItemFrameExtended(tag);
        }
        else if (tag >= 248 && tag <= 250)
        {
            smf = new ChopFrame(tag);
        }
        else if (tag == 251)
        {
            smf = new SameFrameExtended(tag);
        }
        else if (tag >= 252 && tag <= 254)
        {
            smf = new AppendFrame(tag);
        }
        else if (tag == 255)
        {
            smf = new FullFrame(tag);
        }
        else
        {
            throw new ClazzException("Unknown StackMapFrame type");
        }

        smf.load(cis, clazz);
        return smf;
    }

    // For printing
    protected String prefix(StackMapFrame obj)
    {
        return obj.getClass().getName().substring(obj.getClass().getName().lastIndexOf('.') + 1) + " (+" + obj.getOffsetDelta() + "): ";
    }
}
