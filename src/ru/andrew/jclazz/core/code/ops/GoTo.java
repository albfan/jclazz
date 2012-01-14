package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

/**
 * Opcodes: 167, 200<BR>
 * Parameters: branch(2), branch(4) for wide<BR>
 * Operand stack: no change<BR>
 */
public class GoTo extends Operation
{
    private long targetOperation;

    public GoTo(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        long offset;
        if (opcode == 167)   // goto
        {
            offset = (params[0] << 8) | params[1];
            if (offset > 32767) offset = offset - 65536;
        }
        else if (opcode == 200) // goto_w
        {
            offset = (params[0] << 24) | (params[1] << 16) | (params[2] << 8) | params[3];
            if (offset > 2147483647L) offset = offset - 4294967295L;
        }
        else
        {
            throw new RuntimeException("Not a goto operation");
        }
        targetOperation = start_byte + offset;
    }

    public long getTargetOperation()
    {
        return targetOperation;
    }

    public boolean isForwardBranch()
    {
        return targetOperation > start_byte;
    }

    public String asString()
    {
        return start_byte + " " + opcode.getMnemonic() + " " + targetOperation;
    }
}
