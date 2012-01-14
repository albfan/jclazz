package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.constants.*;
import ru.andrew.jclazz.core.attributes.*;

/**
 * Opcodes: 193<BR>
 * Parameters: cast_to_class(2)<BR>
 * Operand stack: objectref => result<BR>
 */
public class InstanceOf extends PushOperation
{
    private String toClass;

    public InstanceOf(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        CONSTANT_Class cl_info = (CONSTANT_Class) code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]];
        toClass = cl_info.getFullyQualifiedName();
    }

    public String getPushType()
    {
        return "boolean";
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
