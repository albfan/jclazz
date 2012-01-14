package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.constants.*;

/**
 * Opcodes: 197<BR>
 * Parameters: type(2), dimensions(1)<BR>
 * Operand stack: count1, [count2, ...] => arrayref<BR>
 */
public class MultiNewArray extends PushOperation
{
    private int dimensions;
    private String arrayClass;

    public MultiNewArray(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        CONSTANT_Class cl_info = (CONSTANT_Class) code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]];
        arrayClass = cl_info.getFullyQualifiedName();

        dimensions = params[2];
    }

    public String getArrayType()
    {
        return arrayClass;
    }

    public int getDimensions()
    {
        return dimensions;
    }

    public String asString()
    {
        return start_byte + " " + opcode.getMnemonic() + " " + dimensions + "-dim array of " + arrayClass;
    }
}
