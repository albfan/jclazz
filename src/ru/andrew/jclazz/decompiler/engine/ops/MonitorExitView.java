package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.Operation;
import ru.andrew.jclazz.decompiler.MethodSourceView;
import ru.andrew.jclazz.decompiler.engine.blocks.Block;

public class MonitorExitView extends OperationView {
    public MonitorExitView(Operation operation, MethodSourceView methodView) {
        super(operation, methodView);
    }

    public String getPushType() {
        return null;
    }

    public String source() {
        return null;
    }

    public void analyze(Block block) {
        //block.removePriorPushOperation();
    }

    public void analyze2(Block block) {
        context.pop();
    }
}
