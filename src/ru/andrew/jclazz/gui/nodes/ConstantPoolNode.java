package ru.andrew.jclazz.gui.nodes;

import ru.andrew.jclazz.core.constants.CONSTANT;
import ru.andrew.jclazz.gui.ClazzTreeNode;

public class ConstantPoolNode extends ClazzTreeNode
{
    public ConstantPoolNode(CONSTANT[] consts)
    {
        super("Constant Pool", consts, "");

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < consts.length; i++)
        {
            if (consts[i] == null) continue;
            sb.append(i).append(": ");
            String type = consts[i].getClass().getName();
            type = type.substring(type.indexOf('_') + 1);
            sb.append(type).append(": ").append(consts[i].getValue()).append("<br>");
        }

        description = sb.toString();
    }
}
