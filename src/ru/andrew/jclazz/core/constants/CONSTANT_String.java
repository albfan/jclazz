package ru.andrew.jclazz.core.constants;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;

import java.io.*;

public class CONSTANT_String extends CONSTANT
{
    private int string_index;
    private CONSTANT_Utf8 utf8;
    private boolean loaded = false;

    private String string;

    protected CONSTANT_String(int num, int tag, Clazz clazz)
    {
        super(num, tag, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException
    {
        string_index = cis.readU2();
    }

    public void update() throws ClazzException
    {
        if (loaded) return;

        loaded = true;

        utf8 = (CONSTANT_Utf8) clazz.getConstant_pool()[string_index];
        string = escape(utf8.getString());
    }

    private String escape(String str)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++)
        {
            char ch = str.charAt(i);
            switch(ch)
            {
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;
                //case '\'':
                //    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                default:
                    // Support for octal escape (up ot \377) and non-printable characters
                    sb.append(ch);
            }
        }
        return sb.toString();
    }

    public String getType()
    {
        return "java.lang.String";
    }

    public String getValue()
    {
        return "\"" + string + "\"";
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        super.store(cos);

        cos.writeU2(utf8.getIndex());
    }
}
