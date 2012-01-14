package ru.andrew.jclazz.gui.nodes;

import ru.andrew.jclazz.core.MethodInfo;
import ru.andrew.jclazz.core.attributes.AttributeInfo;
import ru.andrew.jclazz.core.constants.CONSTANT_Class;
import ru.andrew.jclazz.gui.ClazzTreeNode;

public class MethodNode extends ClazzTreeNode
{
    public MethodNode(MethodInfo methodInfo)
    {
        super(methodInfo.getName(), methodInfo, "");

        StringBuffer sb = new StringBuffer("<html>");

        sb.append("<b>").append(escape(methodInfo.getName())).append(":").append(methodInfo.getDescriptor()).append("</b><br>");
        sb.append("Access flags: ").append(methodInfo.getAccessFlags()).append("&nbsp;(");
        if (methodInfo.isAbstract()) sb.append("abstract ");
        if (methodInfo.isBridge()) sb.append("bridge ");
        if (methodInfo.isDeprecated()) sb.append("deprecated ");
        if (methodInfo.isFinal()) sb.append("final ");
        if (methodInfo.isNative()) sb.append("native ");
        if (methodInfo.isPrivate()) sb.append("private ");
        if (methodInfo.isProtected()) sb.append("protected ");
        if (methodInfo.isPublic()) sb.append("public ");
        if (methodInfo.isStatic()) sb.append("static ");
        if (methodInfo.isStrictFP()) sb.append("strictfp ");
        if (methodInfo.isSynchronized()) sb.append("synchronized ");
        if (methodInfo.isSynthetic()) sb.append("synthetic ");
        if (methodInfo.isVarargs()) sb.append("varargs ");
        sb.append(")<br>");

        //sb.append("Signature: ").append(methodInfo.getSignature()).append("<br>");
        //sb.append("Descriptor: ").append(methodInfo.getDescriptor()).append("<br>");

//        if (methodInfo.getExceptions() != null)
//        {
//            CONSTANT_Class[] exc = methodInfo.getExceptions().getExceptionTable();
//            sb.append("Throws: ");
//            for (int i = 0; i <exc.length; i++)
//            {
//                if (i > 0) sb.append(", ");
//                sb.append(exc[i].getFullyQualifiedName());
//            }
//            sb.append("<br>");
//        }

        sb.append("</html>");

        description = sb.toString();

        AttributeInfo[] attrs = methodInfo.getAttributes();
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
