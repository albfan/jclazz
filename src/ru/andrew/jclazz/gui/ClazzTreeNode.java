package ru.andrew.jclazz.gui;

import java.net.URL;
import javax.swing.tree.*;
import javax.swing.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.constants.*;
import ru.andrew.jclazz.gui.nodes.*;

public class ClazzTreeNode extends DefaultMutableTreeNode
{
    protected String description;
    protected Icon icon;

    public ClazzTreeNode(Object userObject, Object clazzObject, String description)
    {
        super(userObject);

        if (clazzObject instanceof AttributeInfo)
        {
            icon = loadIconFromJar("/res/attribute.png");
        }
        else if (clazzObject instanceof FieldInfo)
        {
            icon = loadIconFromJar("/res/field.png");
        }
        else if (clazzObject instanceof MethodInfo)
        {
            icon = loadIconFromJar("/res/method.png");
        }
        else if (clazzObject instanceof Clazz)
        {
            if (((Clazz) clazzObject).isInterface())
            {
                icon = loadIconFromJar("/res/interface.png");
            }
            else
            {
                icon = loadIconFromJar("/res/class.png");
            }
        }
        else if (clazzObject instanceof InnerClass)
        {
            icon = loadIconFromJar("/res/class.png");
        }
        else if (clazzObject instanceof CONSTANT[])
        {
            icon = loadIconFromJar("/res/other.png");
        }
        else
        {
            icon = loadIconFromJar("/res/other.png");
        }

        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public Icon getIcon()
    {
        return icon;
    }

    protected Icon loadIconFromJar(String path)
    {
        URL url = this.getClass().getResource(path);
        return new ImageIcon(url);
    }

    // Nodes creation

    protected ClazzTreeNode createAttribtueNode(AttributeInfo attrInfo)
    {
        if (attrInfo instanceof Code)
        {
            return new CodeAttributeNode((Code) attrInfo);
        }
        else
        {
            return new AttributeNode(attrInfo);
        }
    }

    protected ClazzTreeNode createFieldNode(FieldInfo fieldInfo)
    {
        return new FieldNode(fieldInfo);
    }

    protected ClazzTreeNode createMethodNode(MethodInfo methodInfo)
    {
        return new MethodNode(methodInfo);
    }

    protected ClazzTreeNode createClazzNode(Clazz clazz)
    {
        return new ClazzNode(clazz);
    }

    protected ClazzTreeNode createInnerClassNode(InnerClass innerClass)
    {
        return new ClazzTreeNode(innerClass.getInnerClass().getName(), innerClass, "File not found: " + innerClass.getInnerClass().getFullyQualifiedName());
    }

    protected ClazzTreeNode createConstantPoolNode(CONSTANT[] consts)
    {
        return new ConstantPoolNode(consts);
    }

    protected String escape(String s)
    {
        return s.replaceAll("<", "&lt;");
    }
}
