package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.Operation;
import ru.andrew.jclazz.core.code.ops.Switch;
import ru.andrew.jclazz.decompiler.MethodSourceView;
import ru.andrew.jclazz.decompiler.engine.blocks.Block;

import java.util.List;

public class SwitchView extends OperationView {
    public SwitchView(Operation operation, MethodSourceView methodView) {
        super(operation, methodView);
    }

    public String getPushType() {
        return null;
    }

    public String source() {
        return "SWITCH VIEW";
    }

    public List getCases() {
        return ((Switch) operation).getCaseBlocks();
    }

    public void analyze(Block block) {
    }

    public void analyze2(Block block) {
    }
}
