package ru.andrew.jclazz.core;

import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.attributes.Deprecated;
import ru.andrew.jclazz.core.constants.*;
import ru.andrew.jclazz.core.signature.*;
import ru.andrew.jclazz.core.io.*;

import java.io.*;

public class FieldInfo
{
    public static final int ACC_PUBLIC = 0x0001;
    public static final int ACC_PRIVATE = 0x0002;
    public static final int ACC_PROTECTED = 0x0004;
    public static final int ACC_STATIC = 0x0008;
    public static final int ACC_FINAL = 0x0010;
    public static final int ACC_VOLATILE = 0x0040;
    public static final int ACC_TRANSIENT = 0x0080;
    public static final int ACC_SYNTHETIC = 0x1000;
    public static final int ACC_ENUM = 0x4000;

    private int access_flags; 
    private CONSTANT_Utf8 name;
    private FieldDescriptor descriptor;
    private CONSTANT_Utf8 descriptorUTF8;
    private AttributeInfo[] attrs;

    private boolean isDeprecated;
    private boolean isSynthetic;
    private CONSTANT constantValue = null;
    private FieldTypeSignature signature;

    private Clazz clazz;

    public void load(ClazzInputStream cis, Clazz clazz) throws ClazzException, IOException
    {
        this.clazz = clazz;

        access_flags = cis.readU2();

        int name_index = cis.readU2();
        name = (CONSTANT_Utf8) clazz.getConstant_pool()[name_index];

        int descriptor_index = cis.readU2();
        descriptorUTF8 = (CONSTANT_Utf8) clazz.getConstant_pool()[descriptor_index];
        descriptor = new FieldDescriptor(descriptorUTF8.getString());

        int attributes_count = cis.readU2();
        attrs = new AttributeInfo[attributes_count];
        for (int i = 0; i < attributes_count; i++)
        {
            attrs[i] = AttributesLoader.loadAttribute(cis, clazz, null);
            if (attrs[i] instanceof Deprecated)
            {
                isDeprecated = true;
            }
            else if (attrs[i] instanceof Synthetic)
            {
                isSynthetic = true;
            }
            else if (attrs[i] instanceof ConstantValue)
            {
                constantValue = ((ConstantValue) attrs[i]).getConstant();
            }
            else if (attrs[i] instanceof Signature)
            {
                signature = FieldTypeSignature.parse(new StringBuffer(((Signature) attrs[i]).getSignature()));
            }
            else
            {
                // TODO
                System.out.println("FIELD INFO : attribute : " + attrs[i].getClass() + ", " + attrs[i]);
            }
        }
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU2(access_flags);
        cos.writeU2(name.getIndex());
        cos.writeU2(descriptorUTF8.getIndex());
        for (int i = 0; i < attrs.length; i++)
        {
            attrs[i].store(cos);
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

    public FieldDescriptor getDescriptor()
    {
        return descriptor;
    }

    public AttributeInfo[] getAttributes()
    {
        return attrs;
    }

    public Clazz getClazz()
    {
        return clazz;
    }

    public boolean isDeprecated()
    {
        return isDeprecated;
    }

    public String getConstantValue()
    {
        if (constantValue == null) return null;
        String val = constantValue.getValue();
        if ("boolean".equals(descriptor.getFQN()))
        {
            if ("1".equals(val))
            {
                return "true";
            }
            else if ("0".equals(val))
            {
                return "false";
            }
            else
            {
                throw new RuntimeException("FieldInfo: invalid boolean type");
            }
        }
        return val;
    }

    public FieldTypeSignature getSignature()
    {
        return signature;
    }

    // Access flags methods
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

    public boolean isVolatile()
    {
        return (access_flags & ACC_VOLATILE) > 0;
    }

    public boolean isTransient()
    {
        return (access_flags & ACC_TRANSIENT) > 0;
    }

    public boolean isSynthetic()
    {
        return ((access_flags & ACC_SYNTHETIC) > 0) || isSynthetic;
    }

    public boolean isEnum()
    {
        return (access_flags & ACC_ENUM) > 0;
    }
}
