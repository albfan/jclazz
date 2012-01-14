package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

/**
 * Opcodes: 132<BR>
 * Parameters: index(1), const(1)<BR>
 * Operand stack: no change<BR>
 */
public class Iinc extends Operation
{
    private int localVariableNumber;
    private int incValue;

    public Iinc(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        localVariableNumber = params[0];
        incValue = params[1];
    }

    //wide iinc
    public Iinc(Wide woper)
    {
        super(132, woper.getStartByte(), woper.getCode(), false);

        this.localVariableNumber = woper.getLocalVariableNumber();
        this.incValue = woper.getIncrementValue();
    }

    public int getLocalVariableNumber()
    {
        return localVariableNumber;
    }

    public int getIncValue()
    {
        return incValue;
    }

    public String asString()
    {
        return start_byte + " " + opcode.getMnemonic() + " LV-" + localVariableNumber + " by " + incValue;
    }
}
