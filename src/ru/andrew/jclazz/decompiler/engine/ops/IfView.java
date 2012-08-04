package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.If;
import ru.andrew.jclazz.core.code.ops.Operation;
import ru.andrew.jclazz.decompiler.MethodSourceView;
import ru.andrew.jclazz.decompiler.engine.blocks.Block;

public class IfView extends OperationView {
    public IfView(Operation operation, MethodSourceView methodView) {
        super(operation, methodView);
    }

    public String getPushType() {
        return null;
    }

    public String source() {
        return "<If operation>";
    }

    public void analyze(Block block) {
    }

    public void analyze2(Block block) {
    }

    public long getTargetOperation() {
        return ((If) operation).getTargetOperation();
    }

    public boolean isForwardBranch() {
        return ((If) operation).getTargetOperation() > getStartByte();
    }
}
