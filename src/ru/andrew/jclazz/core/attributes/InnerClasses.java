package ru.andrew.jclazz.core.attributes;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.constants.*;

import java.io.*;

public class InnerClasses extends AttributeInfo
{
    private InnerClass classes[];

    public InnerClasses(CONSTANT_Utf8 attributeName, Clazz clazz)
    {
        super(attributeName, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException, ClazzException
    {
        attributeLength = (int) cis.readU4();
        int number_of_classes = cis.readU2();
        classes = new InnerClass[number_of_classes];
        for (int i = 0; i < number_of_classes; i++)
        {
            classes[i] = new InnerClass();
            int inner_class_info_index = cis.readU2();
            if (inner_class_info_index != 0)
            {
                classes[i].inner_class = (CONSTANT_Class) clazz.getConstant_pool()[inner_class_info_index];
            }
            int outer_class_info_index = cis.readU2();
            if (outer_class_info_index != 0)
            {
                classes[i].outer_class = (CONSTANT_Class) clazz.getConstant_pool()[outer_class_info_index];
            }
            int inner_name_index = cis.readU2();
            if (inner_name_index != 0)
            {
                classes[i].inner_name = (CONSTANT_Utf8) clazz.getConstant_pool()[inner_name_index];
            }
            classes[i].inner_class_access_flags = cis.readU2();
        }
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU4(attributeLength);
        for (int i = 0; i < classes.length; i++)
        {
            if (classes[i].inner_class == null)
            {
                cos.writeU2(0);
            }
            else
            {
                cos.writeU2(classes[i].inner_class.getIndex());
            }
            if (classes[i].outer_class == null)
            {
                cos.writeU2(0);
            }
            else
            {
                cos.writeU2(classes[i].outer_class.getIndex());
            }
            if (classes[i].inner_name == null)
            {
                cos.writeU2(0);
            }
            else
            {
                cos.writeU2(classes[i].inner_name.getIndex());
            }

            cos.writeU2(classes[i].inner_class_access_flags);
        }
    }

    public InnerClass[] getInnerClasses()
    {
        return classes;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(ATTR);
        sb.append("InnerClasses: \n");
        for (int i = 0; i < classes.length; i++)
        {
            InnerClass ic = classes[i];
            sb.append(INDENT).append("* ");
            String _innerClass = ic.getInnerClass() != null ? ic.getInnerClass().getFullyQualifiedName() : "";
            sb.append("Inner class - ").append(_innerClass).append("\n");
            String _outerClass = ic.getOuterClass() != null ? ic.getOuterClass().getFullyQualifiedName() : "";
            sb.append(INDENT).append("  ").append("Outer class - ").append(_outerClass).append("\n");
            String _innerName = ic.getInnerName() != null ? ic.getInnerName() : "";
            sb.append(INDENT).append("  ").append("Inner name - ").append(_innerName).append("\n");
            sb.append(INDENT).append("  ").append("Inner class access flags - ").append(ic.getInnerClassAccessFlags());
            if (i < classes.length - 1) sb.append("\n");
        }
        return sb.toString();
    }
}
