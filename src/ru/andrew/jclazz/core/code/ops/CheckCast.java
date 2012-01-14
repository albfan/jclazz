package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.constants.*;
import ru.andrew.jclazz.core.attributes.*;

/**
 * Opcodes: 192<BR>
 * Parameters: to_class(2)<BR>
 * Operand stack: objectref => objectref<BR>
 */
public class CheckCast extends PushOperation
{
    private String toClass;

    public CheckCast(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        CONSTANT_Class cl_info = (CONSTANT_Class) code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]];
        toClass = cl_info.getFullyQualifiedName();
    }

    public String getCastClass()
    {
        return toClass;
    }

    public String asString()
    {
        return start_byte + " " + opcode.getMnemonic() + " " + toClass;
    }
}
