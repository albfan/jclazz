package ru.andrew.jclazz.core.constants;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;

import java.io.*;

public final class ConstantPoolItemLoader
{
    private static final int CONSTANT_Class = 7;
    private static final int CONSTANT_Fieldref = 9;
    private static final int CONSTANT_Methodref = 10;
    private static final int CONSTANT_InterfaceMethodref = 11;
    private static final int CONSTANT_String = 8;
    private static final int CONSTANT_Integer = 3;
    private static final int CONSTANT_Float = 4;
    private static final int CONSTANT_Long = 5;
    private static final int CONSTANT_Double = 6;
    private static final int CONSTANT_NameAndType = 12;
    private static final int CONSTANT_Utf8 = 1;

    private static CONSTANT loadConstant(int num, ClazzInputStream cis, Clazz clazz) throws ClazzException, IOException
    {
        CONSTANT cpi;
        int tag = cis.readU1();
        switch(tag)
        {
            case CONSTANT_Class:
                cpi = new CONSTANT_Class(num, tag, clazz);
                break;
            case CONSTANT_Fieldref:
                cpi = new CONSTANT_Fieldref(num, tag, clazz);
                break;
            case CONSTANT_Methodref:
                cpi = new CONSTANT_Methodref(num, tag, clazz);
                break;
            case CONSTANT_InterfaceMethodref:
                cpi = new CONSTANT_InterfaceMethodref(num, tag, clazz);
                break;
            case CONSTANT_String:
                cpi = new CONSTANT_String(num, tag, clazz);
                break;
            case CONSTANT_Integer:
                cpi = new CONSTANT_Integer(num, tag, clazz);
                break;
            case CONSTANT_Float:
                cpi = new CONSTANT_Float(num, tag, clazz);
                break;
            case CONSTANT_Long:
                cpi = new CONSTANT_Long(num, tag, clazz);
                break;
            case CONSTANT_Double:
                cpi = new CONSTANT_Double(num, tag, clazz);
                break;
            case CONSTANT_NameAndType:
                cpi = new CONSTANT_NameAndType(num, tag, clazz);
                break;
            case CONSTANT_Utf8:
                cpi = new CONSTANT_Utf8(num, tag, clazz);
                break;
            default:
                throw new ClazzException("Invalid constant pool item type: " + tag);
        }
        cpi.load(cis);
        return cpi;
    }

    public static CONSTANT[] loadConstants(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        int constantPoolCount = cis.readU2();
        CONSTANT[] constantPool = new CONSTANT[constantPoolCount];
        constantPool[0] = null;
        for (int i = 1; i < constantPoolCount; i++)
        {
            constantPool[i] = loadConstant(i, cis, clazz);

            // Long and double occupies 2 entries
            if (constantPool[i] instanceof CONSTANT_Long ||
                constantPool[i] instanceof CONSTANT_Double)
            {
                i++;
            }
        }
        return constantPool;
    }

    public static void updateConstants(CONSTANT[] constantPool) throws ClazzException
    {
        // Update loaded constants
        for (int i = 0; i < constantPool.length; i++)
        {
            if (constantPool[i] != null) constantPool[i].update();
        }
    }

}
