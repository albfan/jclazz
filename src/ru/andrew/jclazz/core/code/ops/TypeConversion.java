package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.code.*;

/**
 * Opcodes: 133 - 147<BR>
 * Parameters: no<BR>
 * Operand stack: value => value<BR>
 */
public class TypeConversion extends PushOperation
{
    private String castType;

    public TypeConversion(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        String mnemonic = Opcode.getOpcode(opcode).getMnemonic();
        char endChar = mnemonic.charAt(mnemonic.length() - 1);
        switch (endChar)
        {
            case 'd':
                castType = "double";
                break;
            case 'l':
                castType = "long";
                break;
            case 'f':
                castType = "float";
                break;
            case 'i':
                castType = "int";
                break;
            case 'b':
                castType = "byte";
                break;
            case 'c':
                castType = "char";
                break;
            case 's':
                castType = "short";
                break;
            default:
                throw new RuntimeException("TypeConversion: invalid opcode");
        }
    }

    public String getCastType()
    {
        return castType;
    }
}
