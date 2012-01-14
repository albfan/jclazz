package ru.andrew.jclazz.core.attributes.verification;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.constants.*;
import ru.andrew.jclazz.core.attributes.*;

import java.io.*;

public class StackMapTable extends AttributeInfo
{
    private StackMapFrame[] stack_map_frame;

    public StackMapTable(CONSTANT_Utf8 attributeName, Clazz clazz)
    {
        super(attributeName, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException, ClazzException
    {
        attributeLength = (int) cis.readU4();
        
        int number_of_entries = cis.readU2();
        stack_map_frame = new StackMapFrame[number_of_entries];
        for (int i = 0; i < number_of_entries; i++)
        {
            stack_map_frame[i] = StackMapFrame.loadStackMapFrame(cis, clazz);
        }
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU4(attributeLength);
        cos.writeU2(stack_map_frame.length);
        for (int i = 0; i < stack_map_frame.length; i++)
        {
            stack_map_frame[i].store(cos);
        }
    }

    public StackMapFrame[] getStackMapFrames()
    {
        return stack_map_frame;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(ATTR);
        sb.append("StackMapTable: \n");
        for (int i = 0; i < stack_map_frame.length; i++)
        {
            sb.append(INDENT).append(stack_map_frame[i].toString()).append("\n");
        }
        return sb.toString();
    }
}
