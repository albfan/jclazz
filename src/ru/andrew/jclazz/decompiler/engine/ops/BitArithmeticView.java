package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;
import ru.andrew.jclazz.core.code.ops.*;

public class BitArithmeticView extends OperationView
{
    private String value1;
    private String value2;

    public BitArithmeticView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String getPushType()
    {
        return ((BitArithmetic) operation).getPushType();
    }

    public String source()
    {
        return value2 + " " + ((BitArithmetic) operation).getOperation() + " " + value1;
    }

    public void analyze(Block block)
    {
        /*
        OperationView prev1 = block.removePriorPushOperation();
        value1 = prev1.source();
        OperationView prev2 = block.removePriorPushOperation();
        value2 = prev2.source();
         * */
    }

    public void analyze2(Block block)
    {
        OperationView prev1 = context.pop();
        OperationView prev2 = context.pop();
        view = new Object[]{"(", prev2, " " + ((BitArithmetic) operation).getOperation() + " ", prev1, ")"};
        context.push(this);
    }

    public boolean isPrintable()
    {
        return false;
    }
}