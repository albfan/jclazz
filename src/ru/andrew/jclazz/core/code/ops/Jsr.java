package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

/**
 * Opcodes: 168, 201<BR>
 * Parameters: 168 - branch(2), 201 - branch(4)<BR>
 * Operand stack: => address<BR>
 */
public class Jsr extends Operation
{
    private long targetOperation;

    public Jsr(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        long offset;
        if (opcode == 168)   // jsr
        {
            offset = (params[0] << 8) | params[1];
            if (offset > 32767) offset = offset - 65536;
        }
        else if (opcode == 201) // jsr_w
        {
            offset = (params[0] << 24) | (params[1] << 16) | (params[2] << 8) | params[3];
            if (offset > 2147483647L) offset = offset - 4294967295L;
        }
        else
        {
            throw new RuntimeException("Not a jsr operation");
        }
        targetOperation = start_byte + offset;
    }

    public long getTargetOperation()
    {
        return targetOperation;
    }

    public String asString()
    {
        return start_byte + " " + opcode.getMnemonic() + " " + targetOperation;
    }
}
