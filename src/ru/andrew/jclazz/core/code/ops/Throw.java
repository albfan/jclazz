package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

/**
 * Opcodes: 191<BR>
 * Parameters: no<BR>
 * Operand stack: no change<BR>
 */
public class Throw extends Operation
{
    public Throw(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
    }
}
