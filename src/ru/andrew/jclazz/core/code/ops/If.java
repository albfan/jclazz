package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

/**
 * Opcodes: 153 - 166, 198, 199<BR>
 * Parameters: branch(2) for 153 - 166, 198, 199<BR>
 * Operand stack: <BR>
 * if[non]null(198, 199), if[cond](153-158): value => <BR>
 * if_[a,i]cmp[cond](159-166): value1, value2 => <BR>
 */
public class If extends Operation
{
    private long targetOperation;

    public If(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        long offset = (params[0] << 8) | params[1];
        if (offset > 32767) offset = offset - 65536;
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
