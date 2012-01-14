package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

/**
 * Opcodes: 96 - 119<BR>
 * Parameters: no<BR>
 * Operand stack:<BR>
 * xneg: value => result<BR>
 * other: value1, value2 => result<BR>
 */
public class Arithmetic extends PushOperation
{
    private String operation;
    private String resType;

    public Arithmetic(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        if (opcode >= 96 && opcode <= 99)
        {
            operation = "+";
        }
        else if (opcode >= 100 && opcode <= 103)
        {
            operation = "-";
        }
        else if (opcode >= 104 && opcode <= 107)
        {
            operation = "*";
        }
        else if (opcode >= 108 && opcode <= 111)
        {
            operation = "/";
        }
        else if (opcode >= 112 && opcode <= 115)
        {
            operation = "%";
        }

        int rem = opcode % 4;
        switch (rem)
        {
            case 0:
                resType = "int";
                break;
            case 1:
                resType = "long";
                break;
            case 2:
                resType = "float";
                break;
            case 3:
                resType = "double";
                break;
        }
    }

    public String getOperation()
    {
        return operation;
    }

    public String getResultType()
    {
        return resType;
    }

    public boolean isPrintable()
    {
        return false;
    }
}
