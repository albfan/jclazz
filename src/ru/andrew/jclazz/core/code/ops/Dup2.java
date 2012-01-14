package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.Code;

/**
 * Opcodes: 92 - 94<BR>
 * Parameters: no<BR>
 * Operand stack:<BR>
 * dup2: value2, value1 => value2, value1, value2, value1,
 *       if value1 and value2 - Category 1<BR>
 *       value => value, value,
 *       if value is Category 2<BR>
 * dup2_x1: value3, value2, value1  => value2, value1, value3, value2, value1,
 *          if all values - Category 1<BR>
 *          value2, value1 => value1, value2, value1,
 *          if value1 - Category 2 and value2 - Category 1<BR>
 * dup2_x2: value4, value3, value2, value1 => value2, value1, value4, value3, value2, value1,
 *              if all values - Category 1<BR>
 *          value3, value2, value1 => value1, value3, value2, value1,
 *              if value1 - Category 2, value2 and value3 - Category 1<BR>
 *          value3, value2, value1 => value2, value1, value3, value2, value1,
 *              if value1 and value2 - Category 1, value3 - Category 2<BR>
 *          value2, value1 => value1, value2, value1,
 *              if all values are Category 2<BR>
 */
public class Dup2 extends PushOperation
{
    private int stackShift;
    
    public Dup2(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        switch (opcode)
        {
            case 92:
                stackShift = 0;
                break;
            case 93:
                stackShift = 1;
                break;
            case 94:
                stackShift = 2;
                break;
        }
    }

    public int getStackShift()
    {
        return stackShift;
    }
}
