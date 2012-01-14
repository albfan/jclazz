package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

/**
 * Opcodes: 89 - 91<BR>
 * Parameters: no<BR>
 * Operand stack:<BR>
 * dup: value => value, value<BR>
 * dup_x1: value2, value1 => value1, value2, value1<BR>
 * dup_x2: value3, value2, value1 => value1, value3, value2, value1, 
 *         if all values are of Category 1 computational type<BR>
 *         value2, value1 => value1, value2, value1,
 *         if value1 - Category 1, value2 - Category 2<BR>
 */
public class Dup extends PushOperation
{
    private int stackShift;

    public Dup(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        switch (opcode)
        {
            case 89:
                stackShift = 0;
                break;
            case 90:
                stackShift = 1;
                break;
            case 91:
                stackShift = 2;
                break;
        }
    }

    public int getStackShift()
    {
        return stackShift;
    }
}
