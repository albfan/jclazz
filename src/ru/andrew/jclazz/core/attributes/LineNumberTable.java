package ru.andrew.jclazz.core.attributes;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.constants.*;

import java.io.*;

public class LineNumberTable extends AttributeInfo
{
    class LineNumber
    {
        int start_pc;
        int line_number;
    }

    private LineNumber line_number_table[];

    public LineNumberTable(CONSTANT_Utf8 attributeName, Clazz clazz)
    {
        super(attributeName, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException, ClazzException
    {
        attributeLength = (int) cis.readU4();
        
        int line_number_table_length = cis.readU2();
        line_number_table = new LineNumber[line_number_table_length];
        for (int i = 0; i < line_number_table_length; i++)
        {
            line_number_table[i] = new LineNumber();
            line_number_table[i].start_pc = cis.readU2();
            line_number_table[i].line_number = cis.readU2();
        }
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU4(attributeLength);
        cos.writeU2(line_number_table.length);
        for (int i = 0; i < line_number_table.length; i++)
        {
            cos.writeU2(line_number_table[i].start_pc);
            cos.writeU2(line_number_table[i].line_number);
        }
    }

    public int getLineNumber(int start_pc)
    {
        int ln = -1;
        for (int i = 0; i < line_number_table.length; i++)
        {
            if (start_pc >= line_number_table[i].start_pc)
            {
                ln = line_number_table[i].line_number;
            }
            else
            {
                return ln;
            }
        }
        return ln;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(ATTR);
        sb.append("LineNumberTable:\n");
        for (int i = 0; i < line_number_table.length; i++)
        {
            LineNumber ln = line_number_table[i];
            sb.append(INDENT).append(ln.start_pc).append(": line ").append(ln.line_number);
            if (i < line_number_table.length - 1) sb.append("\n");
        }
        return sb.toString();
    }
}
