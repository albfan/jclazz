package ru.andrew.jclazz.core.constants;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;

import java.io.*;

public class CONSTANT_Class extends CONSTANT
{
    private int fqn_index;
    private CONSTANT_Utf8 utf8;
    private boolean loaded = false;

    private String name = null;
    private String packageName = null;
    private String baseType = null;
    private String arrayQN = "";

    protected CONSTANT_Class(int num, int tag, Clazz clazz)
    {
        super(num, tag, clazz);
    }

    public String getType()
    {
        return "java.lang.Class";
    }

    public void load(ClazzInputStream cis) throws IOException
    {
        fqn_index = cis.readU2();
    }

    void update() throws ClazzException
    {
        if (loaded) return;

        loaded = true;

        clazz.getConstant_pool()[fqn_index].update();
        utf8 = (CONSTANT_Utf8) clazz.getConstant_pool()[fqn_index];
        if (utf8 == null)
        {
            throw new ClazzException("CONSTANT_Class, null name_index = " + fqn_index);
        }
        String descriptor = utf8.getString();

        int arrayDimensions = 0;
        while (descriptor.charAt(arrayDimensions) == '[')
        {
            arrayDimensions++;
            arrayQN += "[]";
        }
        int currentPos = arrayDimensions;
        if (currentPos == 0)
        {
            String typeL = descriptor.substring(currentPos, descriptor.length());
            typeL = typeL.replace('/', '.');
            if (typeL.lastIndexOf('.') != -1)
            {
                packageName = typeL.substring(0, typeL.lastIndexOf('.'));
                name = typeL.substring(typeL.lastIndexOf('.') + 1);
            }
            else
            {
                packageName = null;
                name = typeL;
            }
        }
        else
        {
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
                    if (type.lastIndexOf('.') != -1)
                    {
                        packageName = type.substring(0, type.lastIndexOf('.'));
                        name = type.substring(type.lastIndexOf('.') + 1);
                    }
                    else
                    {
                        name = type;
                    }
                    break;
            }
        }
    }

    public String getName()
    {
        return name;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public String getFullyQualifiedName()
    {
        if (baseType != null) return baseType + arrayQN;

        String str = "";
        if (packageName != null) str = packageName + ".";
        str += name + arrayQN;
        return str;
    }

    public String getValue()
    {
        return getFullyQualifiedName();
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        super.store(cos);

        cos.writeU2(utf8.getIndex());
    }
}
