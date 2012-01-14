package ru.andrew.jclazz.core.attributes.verification;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;

import java.io.*;

public class FullFrame extends StackMapFrame
{
    private int offset_delta;
    private VerificationTypeInfo[] locals;
    private VerificationTypeInfo[] stack;

    public FullFrame(int frame_type)
    {
        super(frame_type);
    }

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        offset_delta = cis.readU2();

        int number_of_locals = cis.readU2();
        locals = new VerificationTypeInfo[number_of_locals];
        for (int i = 0; i < locals.length; i++)
        {
            locals[i] = new VerificationTypeInfo();
            locals[i].load(cis, clazz);
        }
        
        int number_of_stack_items = cis.readU2();
        stack = new VerificationTypeInfo[number_of_stack_items];
        for (int i = 0; i < stack.length; i++)
        {
            stack[i] = new VerificationTypeInfo();
            stack[i].load(cis, clazz);
        }
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU2(offset_delta);

        cos.writeU2(locals.length);
        for (int i = 0; i < locals.length; i++)
        {
            locals[i].store(cos);
        }

        cos.writeU2(stack.length);
        for (int i = 0; i < stack.length; i++)
        {
            stack[i].store(cos);
        }
    }

    public int getOffsetDelta()
    {
        return offset_delta;
    }

    public VerificationTypeInfo[] getLocals()
    {
        return locals;
    }

    public VerificationTypeInfo[] getStack()
    {
        return stack;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(prefix(this));
        sb.append("locals - ");
        for (int i = 0; i < locals.length; i++)
        {
            sb.append(locals[i].toString());
            if (i < locals.length - 1) sb.append(" ");
        }
        sb.append(", stack - ");
        for (int i = 0; i < stack.length; i++)
        {
            sb.append(stack[i].toString());
            if (i < stack.length - 1) sb.append(" ");
        }
        return sb.toString();
    }
}
