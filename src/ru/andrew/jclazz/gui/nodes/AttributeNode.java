package ru.andrew.jclazz.gui.nodes;

import ru.andrew.jclazz.core.attributes.AttributeInfo;
import ru.andrew.jclazz.gui.ClazzTreeNode;

public class AttributeNode extends ClazzTreeNode
{
    public AttributeNode(AttributeInfo attrInfo)
    {
        super(attrInfo.getAttributeName().getString(), attrInfo, "");

        StringBuffer sb = new StringBuffer("<html>");
        String attrDescr = attrInfo.toString();
        attrDescr = attrDescr.replaceAll("\n", "<BR>");
        sb.append(attrDescr);
        sb.append("</html>");
        description = sb.toString();
    }
}
