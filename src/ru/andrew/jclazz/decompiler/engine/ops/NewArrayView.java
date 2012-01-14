package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;
import ru.andrew.jclazz.core.code.ops.*;

import java.util.*;
import java.util.ArrayList;

/**
 * View of {@link NewArray}.
 */
public class NewArrayView extends OperationView
{
    private OperationView count;
    private List initVariables = new ArrayList();

    public NewArrayView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public void addInitVariable(OperationView pushVar)
    {
        initVariables.add(pushVar);
    }

    public String source()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("new ").append(alias(((NewArray) operation).getNewArrayType())).append("[");
        if (initVariables.isEmpty()) sb.append(count);
        sb.append("]");
        if (!initVariables.isEmpty())
        {
            sb.append("{");
            for (Iterator it = initVariables.iterator(); it.hasNext();)
            {
                sb.append(it.next());
                if (it.hasNext()) sb.append(", ");
            }
            sb.append("}");
        }
        return sb.toString();
    }

    public String getPushType()
    {
        return ((NewArray) operation).getNewArrayType() + "[]";
    }

    public void analyze(Block block)
    {
        //OperationView prev = block.removePriorPushOperation();
        //count = prev.source();
    }

    public void analyze2(Block block)
    {
        count = context.pop();
        context.push(this);
    }

    protected void buildView()
    {
        String type = alias(((NewArray) operation).getNewArrayType());
        int ind0 = type.indexOf('[');
        String arrType = null;
        if (ind0 >= 0)
        {
            arrType = type.substring(ind0);
            type = type.substring(0, ind0);
        }

        List items = new ArrayList();
        items.add("new " + type + "[");
        if (initVariables.isEmpty()) items.add(count);
        items.add("]");
        if (arrType != null) items.add(arrType);

        if (!initVariables.isEmpty())
        {
            items.add("{");
            for (Iterator it = initVariables.iterator(); it.hasNext();)
            {
                items.add(it.next());
                if (it.hasNext()) items.add(", ");
            }
            items.add("}");
        }

        view = new Object[items.size()];
        view = items.toArray(view);
    }

    public boolean isPrintable()
    {
        return false;
    }
}
