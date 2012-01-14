package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.code.*;
import ru.andrew.jclazz.core.*;

public class Operation
{
    protected Code codeAttribute;
    protected long start_byte;
    protected Opcode opcode;
    protected int params[];

    public Operation(int opcode, long start_byte, Code code, boolean loadParams)
    {
        this.codeAttribute = code;
        this.opcode = Opcode.getOpcode(opcode);
        this.start_byte = start_byte;
        if (loadParams) loadParams(code);
    }

    public Operation(int opcode, long start_byte, Code code)
    {
        this(opcode, start_byte, code, true);
    }

    // Loads only fixed-length opcodes
    protected void loadParams(Code code)
    {
        int params_count = this.opcode.getParamsCount();
        params = new int[params_count];
        for (int i = 0; i < params_count; i++)
        {
            params[i] = code.getNextByte();
        }
    }

    public int getLength()
    {
        return this.opcode.getParamsCount() + 1;
    }

    public Opcode getOpcode()
    {
        return opcode;
    }

    public int[] getParams()
    {
        return params;
    }

    public long getStartByte()
    {
        return start_byte;
    }

    public MethodInfo getMethod()
    {
        return codeAttribute.getMethod();
    }

    public Clazz getClazz()
    {
        return codeAttribute.getMethod().getClazz();
    }

    public String asString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(start_byte).append(" ").append(opcode.getMnemonic());
        for (int i = 0; i < getParams().length; i++)
        {
            sb.append(" ").append(getParams()[i]);
        }
        return sb.toString();
    }
}
