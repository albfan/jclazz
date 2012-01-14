package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.*;
import ru.andrew.jclazz.core.code.ops.*;

public class MonitorEnterView extends OperationView
{
    public MonitorEnterView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String getPushType()
    {
        return null;
    }

    public String source()
    {
        return null;
    }

    public void analyze(Block block)
    {
        /*
        OperationView push = block.removePriorPushOperation();

        // Removing previous pop operation that stores sync object into local variable
        if (block.getPreviousOperation() instanceof PopView)
        {
            block.removePreviousOperation();
        }

        CodeItem ci = block.getNextOperation();
        if (!(ci instanceof Try))
        {
            throw new OperationException("MonitorEnter: try block is missing");
        }
        Sync sync = new Sync(push, block, ((Try) ci).getOperations());
        block.replaceCurrentOperation(sync);

        // Removing next Try and Catch blocks
        block.next();
        block.removeCurrentOperation();
        CodeItem catchBlock = block.next();
        if (!(catchBlock instanceof Catch))
        {
            throw new OperationException("MonitorEnter: catch block is missing");
        }
        block.removeCurrentOperation();
        block.back();
         */
    }

    public void analyze2(Block block)
    {
        OperationView push = context.pop();

        // Removing previous pop operation that stores sync object into local variable
        if (block.getPreviousOperation() instanceof PopView)
        {
            block.removePreviousOperation();
        }

        CodeItem ci = block.getNextOperation();
        if (!(ci instanceof Try))
        {
            throw new OperationException("MonitorEnter: try block is missing");
        }
        Sync sync = new Sync(push, block, ((Try) ci).getOperations());
        block.replaceCurrentOperation(sync);

        // Removing next Try and Catch blocks
        block.next();
        block.removeCurrentOperation();
        CodeItem catchBlock = block.next();
        if (!(catchBlock instanceof Catch))
        {
            throw new OperationException("MonitorEnter: catch block is missing");
        }
        block.removeCurrentOperation();
        block.back();
    }
}
