package ru.andrew.jclazz.decompiler;

import java.util.*;
import ru.andrew.jclazz.core.MethodInfo;
import ru.andrew.jclazz.decompiler.engine.CodeItem;
import ru.andrew.jclazz.decompiler.engine.blocks.Block;
import ru.andrew.jclazz.decompiler.engine.ops.InvokeView;
import ru.andrew.jclazz.decompiler.engine.ops.PushVariableView;
import ru.andrew.jclazz.decompiler.engine.ops.PutFieldView;
import ru.andrew.jclazz.decompiler.engine.ops.ReturnView;

public class AnonymousInitMethodView extends MethodSourceView
{
    public AnonymousInitMethodView(MethodInfo methodInfo, ClazzSourceView clazzView)
    {
        super(methodInfo, clazzView);
    }

//    0 aload_0
//    1 aload_1
//    2 putfield this$0
//
//    5 aload_0
//    6 aload_2
//    7 putfield val$str
//
//    10 aload_0
//    11 invokespecial <init>
//    14 return
    protected String codeBlockSource(Block block)
    {
        this.block = block;
        CodeItem mainOp;
        do
        {
            List ops = getNext();
            mainOp = (CodeItem) ops.get(ops.size() - 1);
            if (mainOp instanceof PutFieldView)
            {
                String fieldName = ((PutFieldView) mainOp).getFieldName();
                if (!"this$0".equals(fieldName))
                {
                    PushVariableView pushvView = (PushVariableView) ops.get(1);
                    ((AnonymousClazzSourceView) getClazzView()).putInnerMapping(fieldName, pushvView.getLocalVariable().getLVNumber() - 2);
                }
            }
            else if (mainOp instanceof InvokeView)
            {
                // Counting parameters
                int count = ops.size() - 1;
                ((AnonymousClazzSourceView) getClazzView()).setInParamCount(count);
            }
        }
        while (!(mainOp instanceof ReturnView));

        return null;
    }

    private int current = -1;
    private Block block;

    private List getNext()
    {
        List ops = new ArrayList();

        CodeItem op;
        do
        {
            op = block.getOperationAfter(current);
            current = (int) op.getStartByte();
            ops.add(op);
        }
        while (op instanceof PushVariableView);

        return ops;
    }
}
