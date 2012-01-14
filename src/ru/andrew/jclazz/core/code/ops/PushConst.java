package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.constants.*;

/**
 * Opcodes: 1 - 20<BR>
 * Parameters: <BR>
 * bipush(16): byte(1); sipush(17): byte(2)
 * ldc(18): res(1); ldc_w(19), ldc2_w(20): res(2)
 * other: no<BR>
 * Operand stack: => value<BR>
 */
public class PushConst extends PushOperation
{
    private String pushValue;
    private String pushType;
    private boolean isClassPushed;

    public PushConst(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        isClassPushed = false;
        // Push constants
        switch (opcode)
        {
            case 1:
                pushValue = "null";
                pushType = null;
                break;
            case 2:
                pushValue = "-1";
                pushType = "int";
                break;
            case 3:
                pushValue = "0";
                pushType = "int";
                break;
            case 4:
                pushValue = "1";
                pushType = "int";
                break;
            case 5:
                pushValue = "2";
                pushType = "int";
                break;
            case 6:
                pushValue = "3";
                pushType = "int";
                break;
            case 7:
                pushValue = "4";
                pushType = "int";
                break;
            case 8:
                pushValue = "5";
                pushType = "int";
                break;
            case 9:
                pushValue = "0L";
                pushType = "long";
                break;
            case 10:
                pushValue = "1L";
                pushType = "long";
                break;
            case 11:
                pushValue = "0.0f";
                pushType = "float";
                break;
            case 12:
                pushValue = "1.0f";
                pushType = "float";
                break;
            case 13:
                pushValue = "2.0f";
                pushType = "float";
                break;
            case 14:
                pushValue = "0.0";
                pushType = "double";
                break;
            case 15:
                pushValue = "1.0";
                pushType = "double";
                break;
            case 16:
                pushValue = String.valueOf(params[0]);
                pushType = "int";
                break;
            case 17:
                int short_value = (params[0] << 8) | params[1];
                pushValue = String.valueOf(short_value);
                pushType = "int";
                break;
            case 18:
                CONSTANT cp_info_18 = code.getClazz().getConstant_pool()[params[0]];
                if (cp_info_18 instanceof CONSTANT_Class)
                {
                    pushValue = ((CONSTANT_Class) cp_info_18).getFullyQualifiedName() + ".class";
                    isClassPushed = true;
                }
                else
                {
                    pushValue = cp_info_18.getValue();
                }
                pushType = cp_info_18.getType();
                break;
            case 19:
                CONSTANT cp_info_19 = code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]];
                if (cp_info_19 instanceof CONSTANT_Class)
                {
                    pushValue = ((CONSTANT_Class) cp_info_19).getFullyQualifiedName() + ".class";
                    isClassPushed = true;
                }
                else
                {
                    pushValue = cp_info_19.getValue();
                }
                pushType = cp_info_19.getType();
                break;
            case 20:
                CONSTANT cp_info_20 = code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]];
                if (cp_info_20 instanceof CONSTANT_Class)
                {
                    pushValue = ((CONSTANT_Class) cp_info_20).getFullyQualifiedName() + ".class";
                    isClassPushed = true;
                }
                else
                {
                    pushValue = cp_info_20.getValue();
                }
                pushType = cp_info_20.getType();
                break;
        }
    }

    public String getPushType()
    {
        return pushType;
    }

    public String getPushValue()
    {
        return pushValue;
    }

    public boolean isClassPushed()
    {
        return isClassPushed;
    }

    public String asString()
    {
        if (opcode.getOpcode() >= 16 && opcode.getOpcode() <= 20)
        {
            return start_byte + " " + opcode.getMnemonic() + " " + pushValue + " (" + pushType + ")";
        }
        return super.asString();
    }
}
