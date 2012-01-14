package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;

public class CheckCastView extends OperationView
{
    private String var;

    public CheckCastView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String getPushType()
    {
        return ((CheckCast) operation).getCastClass();
    }

    public String source()
    {
        return "(" + alias(((CheckCast) operation).getCastClass()) + ") " + var;
    }

    public void analyze(Block block)
    {
        /*
        OperationView prev = block.removePriorPushOperation();

        // Strange behaviour, found on jre 1.6: doubling of identical checkcast
        if (prev instanceof CheckCastView)
        {
            CheckCastView cc = (CheckCastView) prev;
            if (((CheckCast) operation).getCastClass().equals(cc.getPushType()))
            {
                var = cc.var;
                return;
            }
        }

        var = prev.source();
         * */
    }

    public void analyze2(Block block)
    {
        OperationView prev = context.pop();

        // Strange behaviour, found on jre 1.6: doubling of identical checkcast
        if (prev instanceof CheckCastView)
        {
            CheckCastView cc = (CheckCastView) prev;
            if (((CheckCast) operation).getCastClass().equals(cc.getPushType()))
            {
                view = prev.view;
                context.push(this);
                return;
            }
        }

        view = new Object[]{"((" + alias(((CheckCast) operation).getCastClass()) + ") ", prev, ")"};
        context.push(this);
    }

    public boolean isPrintable()
    {
        return false;
    }
}
