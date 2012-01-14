package ru.andrew.jclazz.core.code;

import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.code.ops.*;

public class OperationFactory
{
    private static OperationFactory instance = new OperationFactory();

    private OperationFactory()
    {
    }

    public static OperationFactory getInstance()
    {
        return instance;
    }

    public Operation createOperation(int opcode, long start_byte, Code code)
    {
        if (opcode == 0)
        {
            return new Nop(opcode, start_byte, code);
        }
        else if (opcode >= 1 && opcode <= 20)
        {
            return new PushConst(opcode, start_byte, code);
        }
        else if (opcode >= 21 && opcode <= 45)
        {
            return new PushVariable(opcode, start_byte, code);
        }
        else if (opcode >= 46 && opcode <= 53)
        {
            return new ArrayPush(opcode, start_byte, code);
        }
        else if (opcode >= 54 && opcode <= 88)
        {
            return new Pop(opcode, start_byte, code);
        }
        else if (opcode >= 89 && opcode <= 91)
        {
            return new Dup(opcode, start_byte, code);
        }
        else if (opcode >= 92 && opcode <= 94)
        {
            return new Dup2(opcode, start_byte, code);
        }
        else if (opcode == 95)
        {
            return new Swap(opcode, start_byte, code);
        }
        else if (opcode >= 96 && opcode <= 119)
        {
            return new Arithmetic(opcode, start_byte, code);
        }
        else if (opcode >= 120 && opcode <= 131)
        {
            return new BitArithmetic(opcode, start_byte, code);
        }
        else if (opcode == 132)
        {
            return new Iinc(opcode, start_byte, code);
        }
        else if (opcode >= 133 && opcode <= 147)
        {
            return new TypeConversion(opcode, start_byte, code);
        }
        else if ((opcode >= 148 && opcode <= 152))
        {
            return new Signum(opcode, start_byte, code);
        }
        else if ((opcode >= 153 && opcode <= 166) || (opcode == 198) || (opcode == 199))
        {
            return new If(opcode, start_byte, code);
        }
        else if (opcode == 167 || opcode == 200)
        {
            return new GoTo(opcode, start_byte, code);
        }
        else if (opcode == 168 || opcode == 201)
        {
            return new Jsr(opcode, start_byte, code);
        }
        else if (opcode == 169)
        {
            return new Ret(opcode, start_byte, code);
        }
        else if (opcode >= 170 && opcode <= 171)
        {
            return new Switch(opcode, start_byte, code);
        }
        else if (opcode >= 172 && opcode <= 177)
        {
            return new Return(opcode, start_byte, code);
        }
        else if (opcode == 178 || opcode == 180)
        {
            return new GetField(opcode, start_byte, code);
        }
        else if (opcode == 179 || opcode == 181)
        {
            return new PutField(opcode, start_byte, code);
        }
        else if (opcode >= 182 && opcode <= 186)
        {
            return new Invoke(opcode, start_byte, code);
        }
        else if (opcode == 187)
        {
            return new New(opcode, start_byte, code);
        }
        else if (opcode >= 188 && opcode <= 189)
        {
            return new NewArray(opcode, start_byte, code);
        }
        else if (opcode == 190)
        {
            return new ArrayLength(opcode, start_byte, code);
        }
        else if (opcode == 191)
        {
            return new Throw(opcode, start_byte, code);
        }
        else if (opcode == 192)
        {
            return new CheckCast(opcode, start_byte, code);
        }
        else if (opcode == 193)
        {
            return new InstanceOf(opcode, start_byte, code);
        }
        else if (opcode == 194)
        {
            return new MonitorEnter(opcode, start_byte, code);
        }
        else if (opcode == 195)
        {
            return new MonitorExit(opcode, start_byte, code);
        }
        else if (opcode == 196)
        {
            return new Wide(opcode, start_byte, code).loadOperation();
        }
        else if (opcode == 197)
        {
            return new MultiNewArray(opcode, start_byte, code);
        }
        else
        {
            return new Operation(opcode, start_byte, code);
        }
    }
}
