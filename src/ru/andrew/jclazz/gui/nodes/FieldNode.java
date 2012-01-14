package ru.andrew.jclazz.gui.nodes;

import ru.andrew.jclazz.core.FieldInfo;
import ru.andrew.jclazz.core.attributes.AttributeInfo;
import ru.andrew.jclazz.gui.ClazzTreeNode;

public class FieldNode extends ClazzTreeNode
{
    public FieldNode(FieldInfo fieldInfo)
    {
        super(fieldInfo.getName(), fieldInfo, "");

        StringBuffer sb = new StringBuffer("<html>");

        sb.append("<b>").append(fieldInfo.getName()).append(":").append(fieldInfo.getDescriptor().getFQN()).append("</b><br>");
        sb.append("Access flags: ").append(fieldInfo.getAccessFlags()).append("&nbsp;(");
        if (fieldInfo.isDeprecated()) sb.append("deprecated ");
        if (fieldInfo.isEnum()) sb.append("enum ");
        if (fieldInfo.isFinal()) sb.append("final ");
        if (fieldInfo.isPrivate()) sb.append("private ");
        if (fieldInfo.isProtected()) sb.append("protected ");
        if (fieldInfo.isPublic()) sb.append("public ");
        if (fieldInfo.isStatic()) sb.append("static ");
        if (fieldInfo.isSynthetic()) sb.append("synthetic ");
        if (fieldInfo.isTransient()) sb.append("transient ");
        if (fieldInfo.isVolatile()) sb.append("volatile ");
        sb.append(")<br>");

        //sb.append("Signature: ").append(fieldInfo.getSignature()).append("<br>");
        //sb.append("Descriptor: ").append(fieldInfo.getDescriptor()).append("<br>");
        //sb.append("Constant Value: ").append(fieldInfo.getConstantValue()).append("<br>");

        sb.append("</html>");

        description = sb.toString();

        AttributeInfo[] attrs = fieldInfo.getAttributes();
        if (attrs != null)
        {
            for (int i = 0; i < attrs.length; i++)
            {
                ClazzTreeNode attrNode = createAttribtueNode(attrs[i]);
                this.add(attrNode);
            }
        }
    }
}
