package ru.andrew.jclazz.decompiler.engine;

import ru.andrew.jclazz.decompiler.engine.blocks.*;

public interface CodeItem
{
    public long getStartByte();

    public void analyze(Block block);

    public void analyze2(Block block);
}
