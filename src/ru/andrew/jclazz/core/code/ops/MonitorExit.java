package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

/**
 * Opcodes: 195<BR>
 * Parameters: no<BR>
 * Operand stack: objectref => <BR>
 */
public class MonitorExit extends Operation
{
    public MonitorExit(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
    }
}
