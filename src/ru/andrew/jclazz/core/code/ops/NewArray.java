package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.constants.*;

/**
 * Opcodes: 188, 189<BR>
 * Parameters: type(1) for newarray(188), class(2) for anewarray(189)<BR>
 * Operand stack: count => arrayref<BR>
 */
public class NewArray extends PushOperation
{
    public static final int T_BOOLEAN = 4;
    public static final int T_CHAR = 5;
    public static final int T_FLOAT = 6;
    public static final int T_DOUBLE = 7;
    public static final int T_BYTE = 8;
    public static final int T_SHORT = 9;
    public static final int T_INT = 10;
    public static final int T_LONG = 11;

    private String baseType;
    private String classType;

    public NewArray(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        if (opcode == 188)      // newarray
        {
            switch(params[0])
            {
                case T_BOOLEAN:
                    baseType = "boolean";
                    break;
                case T_CHAR:
                    baseType = "char";
                    break;
                case T_FLOAT:
                    baseType = "float";
                    break;
                case T_DOUBLE:
                    baseType = "double";
                    break;
                case T_BYTE:
                    baseType = "byte";
                    break;
                case T_SHORT:
                    baseType = "short";
                    break;
                case T_INT:
                    baseType = "int";
                    break;
                case T_LONG:
                    baseType = "long";
                    break;
            }
        }
        else
        {
            classType = ((CONSTANT_Class) code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]]).getFullyQualifiedName();
        }
    }

    public String getNewArrayType()
    {
        return (baseType != null ? baseType : classType);
    }

    public String asString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(start_byte).append(" ").append(opcode.getMnemonic());
        sb.append(" of ");
        if (baseType != null)
        {
            sb.append(baseType);
        }
        else
        {
            sb.append(classType);
        }
        return sb.toString();
    }
}
