package ru.andrew.jclazz.core.attributes.annotations;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.constants.*;

import java.io.*;

public class ElementValuePair
{
    private CONSTANT_Utf8 element_name;
    private char tag;

    private boolean isConstValue;
    private CONSTANT const_value;

    private boolean isEnumConstValue;
    private CONSTANT_Utf8 enum_const_type_name;
    private CONSTANT_Utf8 enum_const_name;

    private boolean isClassInfo;
    private CONSTANT_Utf8 returnClassCPInfo;
    private FieldDescriptor returnClassInfo;

    private boolean isAnnotationValue;
    private Annotation annotationValue;

    private boolean isArrayValue;
    private ElementValuePair[] arrayValue;

    public ElementValuePair(CONSTANT_Utf8 element_name, char tag) throws ClazzException
    {
        this.element_name = element_name;
        this.tag = tag;

        switch (tag)
        {
            case 'B':   // byte
                isConstValue = true;
                break;
            case 'C':   // char
                isConstValue = true;
                break;
            case 'D':   // double
                isConstValue = true;
                break;
            case 'F':   // float
                isConstValue = true;
                break;
            case 'I':   // int
                isConstValue = true;
                break;
            case 'J':   // long
                isConstValue = true;
                break;
            case 'S':   // short
                isConstValue = true;
                break;
            case 'Z':   // boolean
                isConstValue = true;
                break;
            case 's':   // String
                isConstValue = true;
                break;
            case 'e':   // enum constant
                isEnumConstValue = true;
                break;
            case 'c':   // Class
                isClassInfo = true;
                break;
            case '@':   // annotation type
                isAnnotationValue = true;
                break;
            case '[':   // array
                isArrayValue = true;
                break;
            default:
                throw new ClazzException("Illegal ElementValuePair tag");
        }
    }

    public void loadValue(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        if (isConstValue)
        {
            int const_value_index = cis.readU2();
            const_value = clazz.getConstant_pool()[const_value_index];
        }
        else if (isEnumConstValue)
        {
            int ec_type_name_index = cis.readU2();
            enum_const_type_name = (CONSTANT_Utf8) clazz.getConstant_pool()[ec_type_name_index];

            int ec_name_index = cis.readU2();
            enum_const_name = (CONSTANT_Utf8) clazz.getConstant_pool()[ec_name_index];
        }
        else if (isClassInfo)
        {
            int class_info_index = cis.readU2();
            returnClassCPInfo = (CONSTANT_Utf8) clazz.getConstant_pool()[class_info_index];
            returnClassInfo = new FieldDescriptor(returnClassCPInfo.getString());
        }
        else if (isAnnotationValue)
        {
            annotationValue = Annotation.load(cis, clazz);
        }
        else if (isArrayValue)
        {
            int num_values = cis.readU2();
            arrayValue = new ElementValuePair[num_values];
            for (int k = 0; k < num_values; k++)
            {
                arrayValue[k] = new ElementValuePair(null, (char) cis.readU1());
                arrayValue[k].loadValue(cis, clazz);
            }
        }
    }

    public void storeValue(ClazzOutputStream cos) throws IOException
    {
        if (isConstValue)
        {
            cos.writeU2(const_value.getIndex());
        }
        else if (isEnumConstValue)
        {
            cos.writeU2(enum_const_type_name.getIndex());
            cos.writeU2(enum_const_name.getIndex());
        }
        else if (isClassInfo)
        {
            cos.writeU2(returnClassCPInfo.getIndex());
        }
        else if (isAnnotationValue)
        {
            annotationValue.store(cos);
        }
        else if (isArrayValue)
        {
            cos.writeU2(arrayValue.length);
            for (int k = 0; k < arrayValue.length; k++)
            {
                arrayValue[k].storeValue(cos);
            }
        }
    }

    // Getters

    public String getElementName()
    {
        return element_name.getString();
    }

    public CONSTANT getConstValue()
    {
        return const_value;
    }

    public String getEnumConstTypeName()
    {
        return enum_const_type_name.getString();
    }

    public String getEnumConstName()
    {
        return enum_const_name.getString();
    }

    public FieldDescriptor getReturnClassInfo()
    {
        return returnClassInfo;
    }

    public Annotation getAnnotationValue()
    {
        return annotationValue;
    }

    public ElementValuePair[] getArrayValue()
    {
        return arrayValue;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        if (element_name != null)
        {
            sb.append(element_name.getString());
        }
        else
        {
            sb.append("default");
        }
        sb.append(" = ");
        if (isConstValue) sb.append(const_value.getValue());
        if (isEnumConstValue) sb.append(enum_const_name.getString()).append(" of ").append(enum_const_type_name.getString());
        if (isClassInfo) sb.append(returnClassInfo.getFQN());
        if (isAnnotationValue) sb.append(annotationValue.toString());
        if (isArrayValue)
        {
            sb.append("[");
            for (int i = 0; i < arrayValue.length; i++)
            {
                sb.append(arrayValue[i].toString());
                if (i < arrayValue.length - 1) sb.append(", ");
            }
            sb.append("]");
        }
        sb.append(")");
        return sb.toString();
    }
}
