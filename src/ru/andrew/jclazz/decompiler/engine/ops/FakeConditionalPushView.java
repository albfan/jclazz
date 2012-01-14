package ru.andrew.jclazz.decompiler.engine.ops;

import java.util.*;
import ru.andrew.jclazz.decompiler.MethodSourceView;
import ru.andrew.jclazz.decompiler.engine.blocks.*;

public class FakeConditionalPushView extends OperationView
{
    private long start_byte;
    private String pushType;

    private OperationView ifPush;
    private OperationView elsePush;

    public FakeConditionalPushView(MethodSourceView methodView, IfBlock ifBlock, OperationView ifPush, OperationView elsePush)
    {
        super(null, methodView);

        this.start_byte = ifBlock.getStartByte();
        this.pushType = ifPush.getPushType();

        this.ifPush = ifPush;
        this.elsePush = elsePush;

        List viewItems = new ArrayList();
        viewItems.add("(");
        viewItems.addAll(ifBlock.getSourceConditionsView());
        viewItems.add(" ? ");
        viewItems.add(ifPush);
        viewItems.add(" : ");
        viewItems.add(elsePush);
        viewItems.add(")");
        this.view = new Object[viewItems.size()];
        this.view = viewItems.toArray(this.view);
    }

    public void forceBoolean()
    {
        if (ifPush instanceof PushConstView)
        {
            ((PushConstView) ifPush).forceBoolean();
        }
        if (elsePush instanceof PushConstView)
        {
            ((PushConstView) elsePush).forceBoolean();
        }
    }

    public String getPushType()
    {
        return pushType;
    }

    public void analyze(Block block)
    {
    }

    public void analyze2(Block block)
    {
    }

     public long getStartByte()
    {
        return start_byte;
    }

    public boolean isPush()
    {
        return true;
    }
}
