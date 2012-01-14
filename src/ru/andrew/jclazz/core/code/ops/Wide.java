package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

/**
 * Opcodes: 196<BR>
 * Parameters: <BR>
 * For xload, xstore and ret: opcode(1), indexbyte(2)
 * For iinc: iinc, indexbyte(2), constbyte(2)
 * Operand stack: same as modified instruction<BR>
 */
public class Wide extends Operation
{
    private int w_opcode;
    private int lvarN;
    private int constInc;

    private Code wcode;

    public Wide(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        this.wcode = code;

        w_opcode = params[0];
        lvarN = (params[1] << 8) | params[2];
        if (w_opcode == 132) constInc = (params[3] << 8) | params[4]; 
    }

    protected void loadParams(Code code)
    {
        int next_opcode = code.getNextByte();
        int p_next_count = (next_opcode == 132 /* iinc */ ? 4 : 2);
        params = new int[p_next_count + 1];
        params[0] = next_opcode;
        for (int k = 1; k <= p_next_count; k++)
        {
            params[k] = code.getNextByte();
        }
    }

    public int getLength()
    {
        return w_opcode == 132 ? 6 : 4;
    }

    Code getCode()
    {
        return wcode;
    }

    int getLocalVariableNumber()
    {
        return lvarN;
    }

    int getIncrementValue()
    {
        return constInc;
    }

    public Operation loadOperation()
    {
        if (w_opcode == 132)    // iinc
        {
            return new Iinc(this);
        }
        else
        {
            // TODO
            return null;
        }
    }
}
