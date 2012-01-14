package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

/**
 * Opcodes: 148 - 152<BR>
 * Parameters: no<BR>
 * Operand stack: value1, value2 => result<BR>
 */
public class Signum extends PushOperation
{
    public Signum(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
    }

    public String getResultType()
    {
        return "int";
    }
}
