package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.*;

public class PushVariableView extends OperationView
{
    private LocalVariable lvar;

    public PushVariableView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String getPushType()
    {
        lvar.ensure((int) getStartByte());
        if (LocalVariable.UNKNOWN_TYPE.equals(lvar.getType()))
        {
            lvar.renewType("java.lang.Object");
        }
        return lvar.getType();
    }

    public LocalVariable getLocalVariable()
    {
        return lvar;
    }

    public String source()
    {
        /*
        return getLVName(lvar);
         */
        return null;
    }

    public void analyze(Block block)
    {
        if (getOpcode() >= 21 && getOpcode() <= 45)
        {
            String suffix = "";
            char mn = operation.getOpcode().getMnemonic().charAt(0);
            if (mn == 'f') suffix = "float";
            else if (mn == 'l') suffix = "long";
            else if (mn == 'd') suffix = "double";
            else if (mn == 'i') suffix = "int";
            else if (mn == 'a') suffix = null;   // TODO what is ref?
            lvar = block.getLocalVariable(((PushVariable) operation).getLocalVariableNumber(), suffix, (int) getStartByte());
        }
    }

    public void analyze2(Block block)
    {
        if (getOpcode() >= 21 && getOpcode() <= 45)
        {
            String suffix = "";
            char mn = operation.getOpcode().getMnemonic().charAt(0);
            if (mn == 'f') suffix = "float";
            else if (mn == 'l') suffix = "long";
            else if (mn == 'd') suffix = "double";
            else if (mn == 'i') suffix = "int";
            else if (mn == 'a') suffix = null;   // TODO what is ref?
            lvar = block.getLocalVariable(((PushVariable) operation).getLocalVariableNumber(), suffix, (int) getStartByte());

            lvar.ensure((int) getStartByte());
            view = new Object[]{lvar.getView()};
            context.push(this);
        }
    }

    public boolean isPrintable()
    {
        return false;
    }
}
