package ru.andrew.jclazz.decompiler;

import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.engine.ops.*;

import java.util.*;

public class EnumClinitMethodSourceView extends MethodSourceView
{
    public EnumClinitMethodSourceView(MethodInfo methodInfo, ClazzSourceView clazzView)
    {
        super(methodInfo, clazzView);
    }

    protected String codeBlockSource(Block block)
    {
        // First operations: PutFieldView, + PutFieldView array to $VALUES variable
        Iterator it = getTopBlock().getOperations().iterator();
        while (it.hasNext())
        {
            CodeItem citem = (CodeItem) it.next();
            if (citem instanceof PutFieldView)
            {
                if ("$VALUES".equals(((PutFieldView) citem).getFieldName()))
                {
                    it.remove();
                    CodeItem returnItem = (CodeItem) it.next();
                    if (returnItem instanceof ReturnView && !it.hasNext())
                    {
                        return null;
                    }
                }
                it.remove();
            }
        }
//        if (getTopBlock().getOperations().size() == 1 &&
//                getTopBlock().getOperations().get(0) instanceof ReturnView)
//        {
//            return null;
//        }

        return super.codeBlockSource(block);
    }
}
