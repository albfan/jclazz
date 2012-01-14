package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

/**
 * Opcodes: 21 - 45<BR>
 * Parameters: <BR>
 * xload(21-25): lv_num(1)
 * other: no<BR>
 * Operand stack: => value<BR>
 */
public class PushVariable extends PushOperation
{
    private int varN;
    
    public PushVariable(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        String mnemonic = this.opcode.getMnemonic();
        if (params != null && params.length > 0)
        {
            varN = params[0];
        }
        else
        {
            varN = Integer.valueOf(mnemonic.substring(mnemonic.indexOf('_') + 1)).intValue();
        }
    }

    public int getLocalVariableNumber()
    {
        return varN;
    }

    public String asString()
    {
        if (opcode.getOpcode() >= 21 && opcode.getOpcode() <= 25)
        {
            return start_byte + " " + opcode.getMnemonic() + " LV-" + varN;
        }
        return super.asString();
    }
}
