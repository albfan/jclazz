package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

public class Nop extends Operation
{
    public Nop(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
    }
}
