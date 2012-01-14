package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

/**
 * Opcodes: 46 - 53<BR>
 * Parameters: no<BR>
 * Operand stack: arrayref, index => value<BR>
 */
public class ArrayPush extends PushOperation
{
    public ArrayPush(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
    }
}
