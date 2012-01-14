package ru.andrew.jclazz.decompiler;

import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.engine.ops.*;

import java.util.*;

public class EnumInitMethodSourceView extends MethodSourceView
{
    public EnumInitMethodSourceView(MethodInfo methodInfo, ClazzSourceView clazzView)
    {
        super(methodInfo, clazzView);
    }

    protected void printMethodParameters()
    {
        int addition = 1;
        List params = new ArrayList(methodInfo.getDescriptor().getParams());

        // Inner Class support
        if (clazzView.isInnerClass() &&
                INIT_METHOD.equals(methodInfo.getName()) &&
                clazzView.getOuterClazz().getClazz().getThisClassInfo().getFullyQualifiedName().equals(((FieldDescriptor) params.get(0)).getFQN()))
        {
            addition++;
        }

        for (int i = addition - 1; i < addition + 1; i++)
        {
            FieldDescriptor fd = (FieldDescriptor) params.get(i);
            topBlock.getLocalVariable(i + addition, fd.getFQN(), 0);
        }

        super.printMethodParametersBySignature();
    }

    protected String codeBlockSource(Block block)
    {
        // Remove first super(String, int)
        block.removeFirstOperation();   // aload_0
        block.removeFirstOperation();   // aload_1
        block.removeFirstOperation();   // iload_2
        block.removeFirstOperation();   // invokespecial <init>

        if (getTopBlock().getOperations().size() == 1 &&
                getTopBlock().getOperations().get(0) instanceof ReturnView)
        {
            return null;
        }
        
        return super.codeBlockSource(block);
    }
}
