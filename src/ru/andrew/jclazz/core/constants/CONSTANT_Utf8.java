package ru.andrew.jclazz.core.constants;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;

import java.io.*;

public class CONSTANT_Utf8 extends CONSTANT
{
    private String utf8m;

    protected CONSTANT_Utf8(int num, int tag, Clazz clazz)
    {
        super(num, tag, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException
    {
        utf8m = cis.readUTF();
    }

    public void update() throws ClazzException
    {
    }

    public String getType()
    {
        return "java.lang.String";
    }

    public String getString()
    {
        return utf8m;
    }

    public String getValue()
    {
        return utf8m;
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        super.store(cos);

        cos.writeUTF(utf8m);
    }
}
