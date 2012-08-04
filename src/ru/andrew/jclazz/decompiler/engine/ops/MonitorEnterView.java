package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.Operation;
import ru.andrew.jclazz.decompiler.MethodSourceView;
import ru.andrew.jclazz.decompiler.engine.CodeItem;
import ru.andrew.jclazz.decompiler.engine.blocks.Block;
import ru.andrew.jclazz.decompiler.engine.blocks.Catch;
import ru.andrew.jclazz.decompiler.engine.blocks.Sync;
import ru.andrew.jclazz.decompiler.engine.blocks.Try;

public class MonitorEnterView extends OperationView {
    public MonitorEnterView(Operation operation, MethodSourceView methodView) {
        super(operation, methodView);
    }

    public String getPushType() {
        return null;
    }

    public String source() {
        return null;
    }

    public void analyze(Block block) {
        /*
        OperationView push = block.removePriorPushOperation();

        // Removing previous pop operation that stores sync object into local variable
        if (block.getPreviousOperation() instanceof PopView)
        {
            block.removePreviousOperation();
        }

        CodeItem ci = block.getOperation();
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

    public void analyze2(Block block) {
        OperationView push = context.pop();

        // Removing previous pop operation that stores sync object into local variable
        if (block.getPreviousOperation() instanceof PopView) {
            block.removePreviousOperation();
        }

        if (block.hasMoreOperations()) {
            CodeItem ci = block.getOperation();
            if (!(ci instanceof Try)) {
                //TODO: Por ahora no lo detenemos
                //            throw new OperationException("MonitorEnter: try block is missing");
                System.out.println("MonitorEnter: try block is missing, block is: " + (ci instanceof Block ? ((Block) ci).getSource() : ((OperationView) ci).source2()));
            } else {
                Sync sync = new Sync(push, block, ((Try) ci).getOperations());
                block.replaceCurrentOperation(sync);
            }
        }

        // Removing next Try and Catch blocks
        block.removeOperation();
        if (block.hasMoreOperations()) {
            CodeItem catchBlock = block.next();
            if (!(catchBlock instanceof Catch)) {
                //TODO: Por ahora no lo detenemos
                //throw new OperationException("MonitorEnter: catch block is missing");
                System.out.println("MonitorEnter: catch block is missing");
            }
            block.removeCurrentOperation();
            block.back();
        }
    }
}
