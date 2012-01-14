package ru.andrew.jclazz.core;

import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.attributes.Deprecated;
import ru.andrew.jclazz.core.constants.*;
import ru.andrew.jclazz.core.signature.*;
import ru.andrew.jclazz.core.io.*;

import java.io.*;

/**
 * This is representation of Java class. Last updated according to Java5 ClassFileFormat.
 */
public class Clazz
{
    private String fileName;

    public static final int ACC_PUBLIC = 1;
    public static final int ACC_PRIVATE = 2;
    public static final int ACC_PROTECTED = 4;
    public static final int ACC_STATIC = 8;
    public static final int ACC_FINAL = 16;
    public static final int ACC_SUPER = 32;
    public static final int ACC_INTERFACE = 512;
    public static final int ACC_ABSTRACT = 1024;
    public static final int ACC_SYNTHETIC = 4096;
    public static final int ACC_ANNOTATION = 8192;
    public static final int ACC_ENUM = 16384;       

    private static final long MAGIC_NUMBER = 0xCAFEBABEL;
    private int minor_version;
    private int major_version;
    private CONSTANT constant_pool[];
    private int access_flags;
    private CONSTANT_Class this_class;
    private CONSTANT_Class super_class;
    private CONSTANT_Class[] interfaces;
    private FieldInfo fields[];
    private MethodInfo methods[];
    private AttributeInfo[] attributes;

    private boolean isDeprecated;
    private boolean isSynthetic;
    private String sourceFile;
    private ClassSignature classSignature;
    private InnerClass[] innerClasses;

    public Clazz(String inputFileName) throws ClazzException, IOException
    {
        this.fileName = inputFileName.endsWith(".class") ? inputFileName : inputFileName + ".class";

        ClazzInputStream cis = new ClazzInputStream(fileName);

        long magic = cis.readU4();
        if (magic != MAGIC_NUMBER)
        {
            throw new ClazzException("Magic number is wrong");
        }

        minor_version = cis.readU2();
        major_version = cis.readU2();

        constant_pool = ConstantPoolItemLoader.loadConstants(cis, this);
        ConstantPoolItemLoader.updateConstants(constant_pool);

        access_flags = cis.readU2();

        this_class = (CONSTANT_Class) constant_pool[cis.readU2()];

        super_class = null;
        int super_class_ind = cis.readU2();
        if (super_class_ind != 0) super_class = (CONSTANT_Class) constant_pool[super_class_ind];

        int interfaces_count = cis.readU2();
        interfaces = new CONSTANT_Class[interfaces_count];
        for (int i = 0; i < interfaces_count; i++)
        {
            int index = cis.readU2();
            interfaces[i] = (CONSTANT_Class) constant_pool[index];
        }

        int fields_count = cis.readU2();
        fields = new FieldInfo[fields_count];
        loadFields(cis);

        int methods_count = cis.readU2();
        methods = new MethodInfo[methods_count];
        loadMethods(cis);

        int attributes_count = cis.readU2();
        attributes = loadAttributes(cis, attributes_count);
        for (int i = 0; i < attributes_count; i++)
        {
            if (attributes[i] instanceof Deprecated)
            {
                isDeprecated = true;
            }
            else if (attributes[i] instanceof Synthetic)
            {
                isSynthetic = true;
            }
            else if (attributes[i] instanceof Signature)
            {
                classSignature = new ClassSignature(((Signature) attributes[i]).getSignature());
            }
            else if (attributes[i] instanceof EnclosingMethod)
            {
                //TODO
            }
            else if (attributes[i] instanceof SourceFile)
            {
                sourceFile = ((SourceFile) attributes[i]).getSourceFile();
            }
            else if (attributes[i] instanceof InnerClasses) 
            {
                innerClasses = ((InnerClasses) attributes[i]).getInnerClasses();

                // Actualizing access flags for inner classes
                for (int k = 0; k < innerClasses.length; k++)
                {
                    if (getThisClassInfo().getFullyQualifiedName().equals(innerClasses[k].getInnerClass().getFullyQualifiedName()))
                    {
                        access_flags = innerClasses[k].getInnerClassAccessFlags();
                    }
                }
            }
            else
            {
                // TODO
                System.out.println("Clazz: unknown class attribute: " + attributes[i].getClass() + " - " + attributes[i].toString());
            }
        }

        cis.close();
    }

    private void loadFields(ClazzInputStream cis) throws ClazzException, IOException
    {
        for (int i = 0; i < fields.length; i++)
        {
            fields[i] = new FieldInfo();
            fields[i].load(cis, this);
        }
    }

    private void loadMethods(ClazzInputStream cis) throws ClazzException, IOException
    {
        for (int i = 0; i < methods.length; i++)
        {
            methods[i] = new MethodInfo();
            methods[i].load(cis, this);
        }
    }

    private AttributeInfo[] loadAttributes(ClazzInputStream cis, int attributes_count) throws ClazzException, IOException
    {
        AttributeInfo[] attributes = new AttributeInfo[attributes_count];
        for (int i = 0; i < attributes_count; i++)
        {
            attributes[i] = AttributesLoader.loadAttribute(cis, this, null);
        }
        return attributes;
    }

    public void saveToFile(String path) throws IOException
    {
        ClazzOutputStream cos = new ClazzOutputStream(path);

        cos.writeU4(MAGIC_NUMBER);
        cos.writeU2(minor_version);
        cos.writeU2(major_version);
        for (int i = 0; i < constant_pool.length; i++)
        {
            if (constant_pool[i] != null) constant_pool[i].store(cos);
        }
        cos.writeU2(access_flags);

        cos.writeU2(this_class.getIndex());
        if (super_class != null)
        {
            cos.writeU2(super_class.getIndex());
        }
        else
        {
            cos.writeU2(0);
        }

        for (int i = 0; i > interfaces.length; i++)
        {
            cos.writeU2(interfaces[i].getIndex());
        }

        for (int i = 0; i > fields.length; i++)
        {
            fields[i].store(cos);
        }
        for (int i = 0; i > methods.length; i++)
        {
            methods[i].store(cos);
        }
        for (int i = 0; i > attributes.length; i++)
        {
            attributes[i].store(cos);
        }

        cos.close();
    }

    public String getFileName()
    {
        return fileName;
    }

    public int getAccessFlags()
    {
        return access_flags;
    }

    public String getSourceFile()
    {
        return sourceFile;
    }

    public int getMinorVersion()
    {
        return minor_version;
    }

    public int getMajorVersion()
    {
        return major_version;
    }

    public String getVersion()
    {
        return major_version + "." + minor_version;
    }

    public AttributeInfo[] getAttributes()
    {
        return attributes;
    }

    public CONSTANT_Class getThisClassInfo()
    {
        return this_class;
    }

    public CONSTANT_Class getSuperClassInfo()
    {
        return super_class;
    }

    public CONSTANT[] getConstant_pool()
    {
        return constant_pool;
    }

    public boolean isDeprecated()
    {
        return isDeprecated;
    }

    public CONSTANT_Class[] getInterfaces()
    {
        return interfaces;
    }

    public FieldInfo[] getFields()
    {
        return fields;
    }

    public MethodInfo[] getMethods()
    {
        return methods;
    }

    public ClassSignature getClassSignature()
    {
        return classSignature;
    }

    public InnerClass[] getInnerClasses()
    {
        return innerClasses;
    }

    // TODO remove?
    public InnerClass getInnerClass(String fqn)
    {
        for (int i = 0; i < innerClasses.length; i++)
        {
            if (innerClasses[i].getInnerClass() == null) continue;
            if (fqn.equals(innerClasses[i].getInnerClass().getFullyQualifiedName()))
            {
                return innerClasses[i];
            }
        }
        return null;
    }

    public String getJVMSupportedVersion()
    {
        if (major_version == 45 && minor_version >= 0 && minor_version <= 3)
        {
            return "1.0.2 and greater";
        }
        else if (major_version == 45 && minor_version > 3 && minor_version <= 65535)
        {
            return "1.1.X and greater";
        }
        else
        {
            int mv = major_version - 44;
            if (minor_version > 0) mv++;
            return "1." + mv + " and greater";
        }
    }

    // Access checks

    public boolean isPublic()
    {
        return (access_flags & ACC_PUBLIC) > 0;
    }

    public boolean isFinal()
    {
        return (access_flags & ACC_FINAL) > 0;
    }

    public boolean isSuper()
    {
        return (access_flags & ACC_SUPER) > 0;
    }

    public boolean isInterface()
    {
        return (access_flags & ACC_INTERFACE) > 0;
    }

    public boolean isAbstract()
    {
        return (access_flags & ACC_ABSTRACT) > 0;
    }

    public boolean isSynthetic()
    {
        return (access_flags & ACC_SYNTHETIC) > 0 || isSynthetic;
    }

    public boolean isStatic()
    {
        return (access_flags & ACC_STATIC) > 0;
    }

    public boolean isEnumeration()
    {
        return (access_flags & ACC_ENUM) > 0;
    }

    public boolean isAnnotation()
    {
        return (access_flags & ACC_ANNOTATION) > 0;
    }
}
