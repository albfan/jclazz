package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.constants.*;

/**
 * Opcodes: 187<BR>
 * Parameters: class(2)<BR>
 * Operand stack: => objectref<BR>
 */
public class New extends PushOperation
{
    private String clazzName;

    public New(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        int _newindex = (params[0] << 8) | params[1];
        CONSTANT_Class cl_new_info = (CONSTANT_Class) code.getClazz().getConstant_pool()[_newindex];
        clazzName = cl_new_info.getFullyQualifiedName();
    }

    public String getNewType()
    {
        return clazzName;
    }

    public String asString()
    {
        return start_byte + " " + opcode.getMnemonic() + " " + clazzName;
    }
}
