package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.Code;

/**
 * Opcodes: 95<BR>
 * Parameters: no<BR>
 * Operand stack: value2, value1 => value1, value2<BR>
 */
public class Swap extends Operation
{
    public Swap(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
    }
}
