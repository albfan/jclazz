package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

/**
 * Opcodes: 120 - 131<BR>
 * Parameters: no<BR>
 * Operand stack: value1, value2 => result<BR>
 */
public class BitArithmetic extends PushOperation
{
    private String type;
    private String oper;

    public BitArithmetic(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        switch (opcode)
        {
            case 120:
                oper = "<<";
                type = "int";
                break;
            case 121:
                oper = "<<";
                type = "long";
                break;
            case 122:
                oper = ">>";
                type = "int";
                break;
            case 123:
                oper = ">>";
                type = "long";
                break;
            case 124:
                oper = ">>>";
                type = "int";
                break;
            case 125:
                oper = ">>>";
                type = "long";
                break;
            case 126:
                oper = "&";
                type = "int";
                break;
            case 127:
                oper = "&";
                type = "long";
                break;
            case 128:
                oper = "|";
                type = "int";
                break;
            case 129:
                oper = "|";
                type = "long";
                break;
            case 130:
                oper = "^";
                type = "int";
                break;
            case 131:
                oper = "^";
                type = "long";
                break;
            default:
                throw new RuntimeException("BitArithmetic: unknown opcode");
        }
    }

    public String getPushType()
    {
        return type;
    }

    public String getOperation()
    {
        return oper;
    }
}
