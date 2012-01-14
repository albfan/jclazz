package ru.andrew.jclazz.core;

public class FieldDescriptor
{
    private String baseType = null;
    private String _package = null;
    private String _class = null;
    private int arrayDimensions;

    public FieldDescriptor(String descriptor) throws ClazzException
    {
        arrayDimensions = 0;
        while (descriptor.charAt(arrayDimensions) == '[')
        {
            arrayDimensions++;
        }
        int currentPos = arrayDimensions;
        switch (descriptor.charAt(currentPos))
        {
            case 'B':
                baseType = "byte";
                break;
            case 'C':
                baseType = "char";
                break;
            case 'D':
                baseType = "double";
                break;
            case 'F':
                baseType = "float";
                break;
            case 'I':
                baseType = "int";
                break;
            case 'J':
                baseType = "long";
                break;
            case 'S':
                baseType = "short";
                break;
            case 'Z':
                baseType = "boolean";
                break;
            case 'L':
                String type = descriptor.substring(currentPos + 1, descriptor.length() - 1);
                type = type.replace('/', '.');
                int ind = type.lastIndexOf('.');
                if (ind >= 0)
                {
                    _package = type.substring(0, ind);
                    _class = type.substring(ind + 1);
                }
                else
                {
                    _package = "";
                    _class = type;
                }
                break;
            case 'V':
                baseType = "void";
                break;
            default:
                throw new ClazzException("Unsupported field type [descriptor: " + descriptor + "]");
        }
    }

    public String getBaseType()
    {
        return baseType;
    }

    public String getPackage()
    {
        return _package;
    }

    public String getClazz()
    {
        return _class;
    }

    public int getArrayDimensions()
    {
        return arrayDimensions;
    }

    public boolean isBaseType()
    {
        return baseType != null;
    }

    public String getFQN()
    {
        StringBuffer sb = new StringBuffer();
        if (_package != null && !"".equals(_package))
        {
            sb.append(_package).append(".").append(_class);
        }
        else if (_class != null)
        {
            sb.append(_class);
        }
        else
        {
            sb.append(baseType);
        }
        for (int i = 0; i < arrayDimensions; i++)
        {
            sb.append("[]");
        }
        return sb.toString();
    }

    public String toString()
    {
        return getFQN();
    }
}
