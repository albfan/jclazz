package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

/**
 * Opcodes: 190<BR>
 * Parameters: no<BR>
 * Operand stack: arrayref => length<BR>
 */
public class ArrayLength extends PushOperation
{
    public ArrayLength(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
    }

    public String getResultType()
    {
        return "int";
    }
}
