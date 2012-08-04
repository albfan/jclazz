package ru.andrew.jclazz.decompiler.engine.blockdetectors;

import ru.andrew.jclazz.decompiler.engine.blocks.Block;

public interface Detector {
    public void analyze(Block block);
}
