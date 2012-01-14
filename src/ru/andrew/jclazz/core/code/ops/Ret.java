package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

/**
 * Opcodes: 169<BR>
 * Parameters: lv_index<BR>
 * Operand stack: no change<BR>
 */
public class Ret extends Operation
{
    private int lv_num;

    public Ret(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        lv_num = params[0];
    }

    public int getLocalVariableNumber()
    {
        return lv_num;
    }

    public String asString()
    {
        return start_byte + " " + opcode.getMnemonic() + " LV-" + lv_num;
    }
}
