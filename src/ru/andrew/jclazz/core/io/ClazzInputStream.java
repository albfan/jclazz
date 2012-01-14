package ru.andrew.jclazz.core.io;

import java.io.*;

public class ClazzInputStream
{
    private DataInputStream dis = null;
    private int cnt = 0;

    public ClazzInputStream(String fileName) throws FileNotFoundException
    {
        dis = new DataInputStream(new FileInputStream(fileName));
    }

    public void close() throws IOException
    {
        if (dis != null) dis.close();
    }

    public void read(int[] buf) throws IOException
    {
        for (int i = 0; i < buf.length; i++)
        {
            buf[i] = dis.readUnsignedByte();
            cnt++;
        }
    }

    public int readU1() throws IOException
    {
        cnt++;
        return dis.readUnsignedByte();
    }

    public int readU2() throws IOException
    {
        cnt += 2;
        return dis.readUnsignedShort();
    }

    public long readU4() throws IOException
    {
        long high = dis.readUnsignedShort();
        long low = dis.readUnsignedShort();
        cnt += 4;
        return (high << 16) + low;
    }

    public double readDouble() throws IOException
    {
        cnt += 8;
        return dis.readDouble();
    }

    public float readFloat() throws IOException
    {
        cnt += 4;
        return dis.readFloat();
    }

    public int readInt() throws IOException
    {
        cnt += 4;
        return dis.readInt();
    }

    public long readLong() throws IOException
    {
        cnt += 8;
        return dis.readLong();
    }

    public String readUTF() throws IOException
    {
        return dis.readUTF();
    }

    public int getPosition()
    {
        return cnt;
    }
}
