package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.*;
import ru.andrew.jclazz.core.code.ops.*;

public class IincView extends OperationView
{
    private LocalVariable lvar;

    public IincView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String getPushType()
    {
        return null;
    }

    public String source()
    {
        /*
        return getLVName(lvar) + (((Iinc) operation).getIncValue() == 1 ? "++" : " += " + ((Iinc) operation).getIncValue());
         */
        return null;
    }

    public void analyze(Block block)
    {
        lvar = block.getLocalVariable(((Iinc) operation).getLocalVariableNumber(), null, (int) getStartByte());
    }

    public void analyze2(Block block)
    {
        lvar = block.getLocalVariable(((Iinc) operation).getLocalVariableNumber(), null, (int) getStartByte());
        view = new Object[]{lvar.getView(), (((Iinc) operation).getIncValue() == 1 ? "++" : " += " + ((Iinc) operation).getIncValue())};
    }
}
