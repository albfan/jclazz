package ru.andrew.jclazz.gui.nodes;

import java.io.IOException;
import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.ClazzException;
import ru.andrew.jclazz.core.FieldInfo;
import ru.andrew.jclazz.core.MethodInfo;
import ru.andrew.jclazz.core.attributes.AttributeInfo;
import ru.andrew.jclazz.core.attributes.InnerClass;
import ru.andrew.jclazz.core.constants.CONSTANT_Class;
import ru.andrew.jclazz.gui.ClazzTreeNode;

public class ClazzNode extends ClazzTreeNode
{
    public ClazzNode(Clazz clazz)
    {
        super(clazz.getThisClassInfo().getName(), clazz, "");

        StringBuffer sb = new StringBuffer("<html>");

        sb.append("Version: ").append(clazz.getVersion()).append("<br>");
        sb.append("JVM supported: ").append(clazz.getJVMSupportedVersion()).append("<br>");
        if (clazz.getClassSignature() != null)
        {
            sb.append("Signature: ").append(clazz.getClassSignature()).append("<br>");
        }
        sb.append("Extends <i>").append(clazz.getSuperClassInfo().getFullyQualifiedName()).append("</i><br>");
        CONSTANT_Class[] intfs = clazz.getInterfaces();
        if (intfs != null && intfs.length > 0)
        {
            sb.append("Implements <i>");
            for (int i = 0; i < intfs.length; i++)
            {
                if (i != 0) sb.append(", <i>");
                sb.append(intfs[i].getFullyQualifiedName()).append("</i>");
            }
            sb.append("<br>");
        }

        sb.append("Access flags: ").append(clazz.getAccessFlags()).append("&nbsp;(");
        if (clazz.isAbstract()) sb.append("abstract ");
        if (clazz.isAnnotation()) sb.append("annotation ");
        if (clazz.isDeprecated()) sb.append("deprecated ");
        if (clazz.isEnumeration()) sb.append("enum ");
        if (clazz.isFinal()) sb.append("final ");
        // TODO if (clazz.isInnerClass()) sb.append("inner ");
        if (clazz.isInterface()) sb.append("interface ");
        if (clazz.isPublic()) sb.append("public ");
        if (clazz.isSuper()) sb.append("super ");
        if (clazz.isSynthetic()) sb.append("synthetic ");
        sb.append(")<br>");

        sb.append("<b>").append(clazz.getThisClassInfo().getFullyQualifiedName()).append("</b>");

        sb.append("</html>");

        description = sb.toString();

        loadTree(clazz);
    }

    private void loadTree(Clazz clazz)
    {
        AttributeInfo[] attrs = clazz.getAttributes();
        for (int i = 0; i < attrs.length; i++)
        {
            ClazzTreeNode node = createAttribtueNode(attrs[i]);
            add(node);
        }

        FieldInfo[] fields = clazz.getFields();
        for (int i = 0; i < fields.length; i++)
        {
            ClazzTreeNode node = createFieldNode(fields[i]);
            add(node);
        }

        MethodInfo[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++)
        {
            ClazzTreeNode node = createMethodNode(methods[i]);
            add(node);
        }

        InnerClass[] ics = clazz.getInnerClasses();
        if (ics != null)
        {
            for (int i = 0; i < ics.length; i++)
            {
                InnerClass ic = ics[i];
                if (ic.getInnerClass() == null) continue;
                if (!ic.getInnerClass().getFullyQualifiedName().startsWith(clazz.getThisClassInfo().getFullyQualifiedName()))
                {
                    continue;
                }
                if (ic.getInnerClass().getFullyQualifiedName().equals(clazz.getThisClassInfo().getFullyQualifiedName()))
                {
                    continue;
                }

                String inname = ic.getInnerClass().getName();
                String path = clazz.getFileName().substring(0, clazz.getFileName().lastIndexOf(System.getProperty("file.separator")) + 1);
                Clazz innerClazz;
                try
                {
                    innerClazz = new Clazz(path + inname + ".class");
                    add(createClazzNode(innerClazz));
                }
                catch (ClazzException ex)
                {
                    throw new RuntimeException(ex);
                }
                catch (IOException ioex)
                {
                    add(createInnerClassNode(ic));
                }
            }
        }

        add(createConstantPoolNode(clazz.getConstant_pool()));
    }
}
