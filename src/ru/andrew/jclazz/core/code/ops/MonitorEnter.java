package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

/**
 * Opcodes: 194<BR>
 * Parameters: no<BR>
 * Operand stack: objectref => <BR>
 */
public class MonitorEnter extends Operation
{
    public MonitorEnter(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
    }
}
