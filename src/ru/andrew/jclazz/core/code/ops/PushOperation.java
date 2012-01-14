package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

public abstract class PushOperation extends Operation
{
    public PushOperation(int opcode, long start_byte, Code code, boolean loadParams)
    {
        super(opcode, start_byte, code, loadParams);
    }

    public PushOperation(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
    }
}
