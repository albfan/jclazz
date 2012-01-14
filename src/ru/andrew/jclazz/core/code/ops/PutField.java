package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.constants.*;
import ru.andrew.jclazz.core.*;

/**
 * Opcodes: 179, 181<BR>
 * Parameters: ref_to_field(2)<BR>
 * Operand stack: putfield: objectref, value => ; putstatic: value => <BR>
 */
public class PutField extends Operation
{
    private String staticRef;
    private String name;
    private FieldDescriptor fd;

    public PutField(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        CONSTANT_Fieldref f_info = (CONSTANT_Fieldref) code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]];
        name = f_info.getName();
        fd = f_info.getFieldDescriptor();
        if (opcode == 179)   // putstatic
        {
            staticRef = f_info.getRefClazz().getFullyQualifiedName();
        }
    }

    public String getFieldName()
    {
        return name;
    }

    public FieldDescriptor getFieldDescriptor()
    {
        return fd;
    }

    public String getClassForStaticField()
    {
        return staticRef;
    }

    public String asString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(start_byte).append(" ").append(opcode.getMnemonic()).append(" ");
        if (opcode.getOpcode() == 179) // putstatic
        {
            sb.append(staticRef).append(".");
        }
        sb.append(name);
        return sb.toString();
    }
}
