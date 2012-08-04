package ru.andrew.jclazz.decompiler;

import ru.andrew.jclazz.core.MethodInfo;
import ru.andrew.jclazz.decompiler.engine.CodeItem;
import ru.andrew.jclazz.decompiler.engine.blocks.Block;
import ru.andrew.jclazz.decompiler.engine.ops.PutFieldView;
import ru.andrew.jclazz.decompiler.engine.ops.ReturnView;

import java.util.Iterator;

public class EnumClinitMethodSourceView extends MethodSourceView {
    public EnumClinitMethodSourceView(MethodInfo methodInfo, ClazzSourceView clazzView) {
        super(methodInfo, clazzView);
    }

    protected String codeBlockSource(Block block) {
        // First operations: PutFieldView, + PutFieldView array to $VALUES variable
        Iterator it = getTopBlock().getOperations().iterator();
        while (it.hasNext()) {
            CodeItem citem = (CodeItem) it.next();
            if (citem instanceof PutFieldView) {
                if ("$VALUES".equals(((PutFieldView) citem).getFieldName())) {
                    it.remove();
                    CodeItem returnItem = (CodeItem) it.next();
                    if (returnItem instanceof ReturnView && !it.hasNext()) {
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
