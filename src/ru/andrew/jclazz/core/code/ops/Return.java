package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.*;

/**
 * Opcodes: 172 - 177<BR>
 * Parameters: no<BR>
 * Operand stack: return(177): no change; others: value => <BR>
 */
public class Return extends Operation
{
    private String retType;

    public Return(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        FieldDescriptor returnType = code.getMethod().getDescriptor().getReturnType();
        if (MethodInfo.INIT_METHOD.equals(code.getMethod().getName()))
        {
            retType = "<init>";
        }
        else
        {
            retType = returnType.getBaseType();
        }
    }

    public String getReturnType()
    {
        return retType;
    }
}
