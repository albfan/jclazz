package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.constants.*;

/**
 * Opcodes: 178, 180<BR>
 * Parameters: ref to field (2)<BR>
 * Operand stack: <BR>
 * getstatic: => value<BR>
 * getfield: objectref => value<BR>
 */
public class GetField extends PushOperation
{
    private String staticRef;
    private String name;
    private String fieldType;

    public GetField(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        CONSTANT_Fieldref f_info = (CONSTANT_Fieldref) code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]];
        name = f_info.getName();
        fieldType = f_info.getFieldDescriptor().getFQN();
        if (opcode == 178)   // getstatic
        {
            staticRef = f_info.getRefClazz().getFullyQualifiedName();
        }
    }

    public String getFieldType()
    {
        return fieldType;
    }

    public String getFieldName()
    {
        return name;
    }

    public String getClassForStaticField()
    {
        return staticRef;
    }

    public String asString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(start_byte).append(" ").append(opcode.getMnemonic()).append(" ");
        if (opcode.getOpcode() == 178)  //getstatic
        {
            sb.append(staticRef).append(".");
        }
        sb.append(name);
        return sb.toString();
    }
}
