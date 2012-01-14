package ru.andrew.jclazz.core;

import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.attributes.Deprecated;
import ru.andrew.jclazz.core.constants.*;
import ru.andrew.jclazz.core.signature.*;
import ru.andrew.jclazz.core.io.*;

import java.io.*;
import java.util.*;

public class MethodInfo
{
    public static final String INIT_METHOD = "<init>";
    public static final String CLASS_INIT_METHOD = "<clinit>";

    public static final int ACC_PUBLIC = 0x0001;
    public static final int ACC_PRIVATE = 0x0002;
    public static final int ACC_PROTECTED = 0x0004;
    public static final int ACC_STATIC = 0x0008;
    public static final int ACC_FINAL = 0x0010;
    public static final int ACC_SYNCHRONIZED = 0x0020;
    public static final int ACC_BRIDGE = 0x0040;
    public static final int ACC_VARARGS = 0x0080;
    public static final int ACC_NATIVE = 0x0100;
    public static final int ACC_ABSTRACT = 0x0400;
    public static final int ACC_STRICT = 0x0800;
    public static final int ACC_SYNTHETIC = 0x1000;

    private int access_flags;
    private CONSTANT_Utf8 name;
    private MethodDescriptor descriptor;
    private CONSTANT_Utf8 descriptorUTF8;
    private AttributeInfo[] attributes;

    private boolean isDeprecated;
    private boolean isSynthetic;
    private Exceptions exc = null;
    private Code code = null;
    private List operations = null;
    private MethodSignature signature;
    private Clazz clazz;

    public void load(ClazzInputStream cis, Clazz clazz) throws ClazzException, IOException
    {
        this.clazz = clazz;

        access_flags = cis.readU2();

        int name_index = cis.readU2();
        name = (CONSTANT_Utf8) clazz.getConstant_pool()[name_index];

        int descriptor_index = cis.readU2();
        descriptorUTF8 = (CONSTANT_Utf8) clazz.getConstant_pool()[descriptor_index];
        descriptor = new MethodDescriptor(descriptorUTF8.getString());

        // Loading attributes

        int attributes_count = cis.readU2();
        attributes = new AttributeInfo[attributes_count];
        for (int i = 0; i < attributes_count; i++)
        {
            AttributeInfo attribute = AttributesLoader.loadAttribute(cis, clazz, this);
            attributes[i] = attribute;
            if (attribute instanceof Deprecated)
            {
                isDeprecated = true;
            }
            else if (attribute instanceof Synthetic)
            {
                isSynthetic = true;
            }
            else if (attribute instanceof Exceptions)
            {
                if (exc != null) throw new ClazzException("Doubling exceptions attribute in method");
                exc = (Exceptions) attribute;
            }
            else if (attribute instanceof Code)
            {
                if (code != null) throw new ClazzException("Doubling code attribute in method");
                code = (Code) attribute;
                operations = code.getOperations();
            }
            else if (attribute instanceof Signature)
            {
                signature = new MethodSignature(((Signature) attribute).getSignature());
            }
            else
            {
                // TODO
                System.out.println("METHOD INFO : attribute : " + attribute.getClass() + ", " + attribute);
            }
        }
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU2(access_flags);
        cos.writeU2(name.getIndex());
        cos.writeU2(descriptorUTF8.getIndex());
        for (int i = 0; i < attributes.length; i++)
        {
            attributes[i].store(cos);
        }
    }

    public int getAccessFlags()
    {
        return access_flags;
    }

    public String getName()
    {
        return name.getString();
    }

    public MethodDescriptor getDescriptor()
    {
        return descriptor;
    }

    public MethodSignature getSignature()
    {
        return signature;
    }

    public Clazz getClazz()
    {
        return clazz;
    }

    public boolean isDeprecated()
    {
        return isDeprecated;
    }

    public Exceptions getExceptions()
    {
        return exc;
    }

    public Code getCodeBlock()
    {
        return code;
    }

    public List getOperations()
    {
        return operations;
    }

    public LineNumberTable getLineNumberTable()
    {
        return code != null ? code.getLineNumberTable() : null;
    }

    public AttributeInfo[] getAttributes()
    {
        return attributes;
    }

    public boolean isInit()
    {
        return INIT_METHOD.equals(name.getString());
    }

    public boolean isStaticInit()
    {
        return CLASS_INIT_METHOD.equals(name.getString());
    }

    // Access checks

    public boolean isPublic()
    {
        return (access_flags & ACC_PUBLIC) > 0;
    }

    public boolean isPrivate()
    {
        return (access_flags & ACC_PRIVATE) > 0;
    }

    public boolean isProtected()
    {
        return (access_flags & ACC_PROTECTED) > 0;
    }

    public boolean isStatic()
    {
        return (access_flags & ACC_STATIC) > 0;
    }

    public boolean isFinal()
    {
        return (access_flags & ACC_FINAL) > 0;
    }

    public boolean isSynchronized()
    {
        return (access_flags & ACC_SYNCHRONIZED) > 0;
    }

    public boolean isNative()
    {
        return (access_flags & ACC_NATIVE) > 0;
    }

    public boolean isAbstract()
    {
        return (access_flags & ACC_ABSTRACT) > 0;
    }

    public boolean isStrictFP()
    {
        return (access_flags & ACC_STRICT) > 0;
    }

    public boolean isSynthetic()
    {
        return ((access_flags & ACC_SYNTHETIC) > 0) || isSynthetic;
    }

    public boolean isBridge()
    {
        return (access_flags & ACC_BRIDGE) > 0;
    }

    public boolean isVarargs()
    {
        return (access_flags & ACC_VARARGS) > 0;
    }
}
