package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.decompiler.MethodSourceView;
import ru.andrew.jclazz.decompiler.engine.LocalVariable;
import ru.andrew.jclazz.decompiler.engine.blocks.Block;

public class FakePopView extends OperationView
{
    private LocalVariable lvar;
    private String fakeValue;

    public FakePopView(MethodSourceView methodView, LocalVariable lvar, String fakeValue)
    {
        super(null, methodView);

        this.lvar = lvar;
        this.fakeValue = fakeValue;

        view = new Object[]{lvar.getView(), " = ", fakeValue};
    }

    public String getPushType()
    {
        return null;
    }

    public String source()
    {
        /*
        String src;
        if (!lvar.isPrinted())
        {
            src = alias(getLVType(lvar)) + " ";
            lvar.setPrinted(true);
        }
        else
        {
            src = "";
        }
        src += getLVName(lvar);
        src += " = " + fakeValue;
        return src;
         */
        return null;
    }

    public void analyze(Block block)
    {
    }

    public void analyze2(Block block)
    {
    }

    public int getOpcode()
    {
        return 58;  // astore
    }

    public long getStartByte()
    {
        // TODO may be incorrect
        return -1;
    }

    public boolean isPush()
    {
        return false;
    }
}
