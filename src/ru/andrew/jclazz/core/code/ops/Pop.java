package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

/**
 * Opcodes: 54 - 88<BR>
 * Parameters: local_var(1) for xstore(54-58), no for others<BR>
 * Operand stack: <BR>
 * xstore(54-58), xstore_Y(59-78), pop(87): value => <BR>
 * xastore(79-86): arrayref, index, value => <BR>
 * pop2(88): 1) type1: value1, value2 => 2) type2: value => <BR>  
 */
public class Pop extends Operation
{
    private int localVariableNumber = -1;

    public Pop(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        // Pop variables
        if (opcode >= 54 && opcode <= 78)
        {
            String mnemonic = this.opcode.getMnemonic();
            if (params != null && params.length > 0)
            {
                localVariableNumber = params[0];
            }
            else
            {
                localVariableNumber = Integer.valueOf(mnemonic.substring(mnemonic.indexOf('_') + 1)).intValue();
            }
        }
    }

    public int getLocalVariableNumber()
    {
        return localVariableNumber;
    }

    public String asString()
    {
        if (opcode.getOpcode() >= 54 && opcode.getOpcode() <= 58)
        {
            return start_byte + " " + opcode.getMnemonic() + " LV-" + localVariableNumber;
        }
        return super.asString();
    }
}
