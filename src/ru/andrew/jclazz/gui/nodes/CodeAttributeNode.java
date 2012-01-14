package ru.andrew.jclazz.gui.nodes;

import java.util.Iterator;
import ru.andrew.jclazz.core.attributes.AttributeInfo;
import ru.andrew.jclazz.core.attributes.Code;
import ru.andrew.jclazz.core.code.ops.Operation;
import ru.andrew.jclazz.gui.ClazzTreeNode;

public class CodeAttributeNode extends ClazzTreeNode
{
    public CodeAttributeNode(Code codeAttr)
    {
        super(codeAttr.getAttributeName().getString(), codeAttr, "");

        StringBuffer sb = new StringBuffer("<html>");
        sb.append("Max stack = " + codeAttr.getMaxStack()).append("<BR>");
        sb.append("Max locals = " + codeAttr.getMaxLocals()).append("<BR>");
        sb.append("<b>Exceptions:</b><BR>");
        for (int j = 0; j < codeAttr.getExceptionTable().length; j++)
        {
            Code.ExceptionTable cet = codeAttr.getExceptionTable()[j];
            sb.append("[" + cet.start_pc + "-" + cet.end_pc + "): " + cet.handler_pc + (cet.catch_type != null ? " - " + cet.catch_type.getFullyQualifiedName() : "")).append("<BR>");
        }
        sb.append("<HR>");
        for (Iterator i = codeAttr.getOperations().iterator(); i.hasNext();)
        {
            Operation oper = (Operation) i.next();
            sb.append("        " + oper.asString()).append("<BR>");
        }

        sb.append("</html>");
        description = sb.toString();

        AttributeInfo[] attrs = codeAttr.getAttributes();
        for (int i = 0; i < attrs.length; i++)
        {
            ClazzTreeNode node = createAttribtueNode(attrs[i]);
            add(node);
        }
    }
}
