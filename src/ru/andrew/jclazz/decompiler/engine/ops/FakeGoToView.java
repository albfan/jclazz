package ru.andrew.jclazz.decompiler.engine.ops;

public class FakeGoToView extends GoToView
{
    private long fakeStartByte;
    private long fakeTargetOperation;

    public FakeGoToView(long start_byte, long targetOperation)
    {
        super(null, null);
        this.fakeStartByte = start_byte;
        this.fakeTargetOperation = targetOperation;
    }

    public long getStartByte()
    {
        return fakeStartByte;
    }

    public long getTargetOperation()
    {
        return fakeTargetOperation;
    }
}
