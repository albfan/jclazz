package ru.andrew.jclazz.decompiler;

import java.util.*;

public class DContext extends ThreadLocal
{
    private static DContext context = new DContext();

    private DContext()
    {
    }

    protected synchronized Object initialValue()
    {
        return new DContext();
    }

    public static DContext getInstance()
    {
        return (DContext) context.get();
    }

    // *** Methods *** //

    private Map op2ref = new HashMap(); // push opcode start byte > Ref

    public Ref getRef(int start_byte)
    {
        return (Ref) op2ref.get(Integer.toString(start_byte));
    }

    public void setRef(int start_byte, Ref ref)
    {
        op2ref.put(Integer.toString(start_byte), ref);
    }
}
