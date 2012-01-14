package ru.andrew.jclazz.core.io;

import java.io.*;

public class ClazzOutputStream
{
    private DataOutputStream dos = null;

    public ClazzOutputStream(String fileName) throws FileNotFoundException
    {
        dos = new DataOutputStream(new FileOutputStream(fileName));
    }

    public void close() throws IOException
    {
        if (dos != null) dos.close();
    }

    public void writeU1(int u1) throws IOException
    {
        dos.writeByte(u1);
    }

    public void writeU2(int u2) throws IOException
    {
        dos.writeShort(u2);
    }

    public void writeU4(long u4) throws IOException
    {
        dos.writeLong(u4);
    }

    public void writeDouble(double v) throws IOException
    {
        dos.writeDouble(v);
    }

    public void writeFloat(float v) throws IOException
    {
        dos.writeFloat(v);
    }

    public void writeInt(int v) throws IOException
    {
        dos.writeInt(v);
    }

    public void writeLong(long v) throws IOException
    {
        dos.writeLong(v);
    }

    public void writeUTF(String v) throws IOException
    {
        dos.writeUTF(v);
    }

    public int size()
    {
        return dos.size();
    }
}
