package ru.andrew.jclazz.core.constants;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;

import java.io.*;

public class CONSTANT_Ref extends CONSTANT
{
    private int class_index;
    private int name_and_type_index;
    private boolean loaded = false;

    protected CONSTANT_Class refClazz;
    protected CONSTANT_NameAndType refNameAndType;

    protected CONSTANT_Ref(int num, int tag, Clazz clazz)
    {
        super(num, tag, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException
    {
        class_index = cis.readU2();
        name_and_type_index = cis.readU2();
    }

    public void update() throws ClazzException
    {
        if (loaded) return;

        loaded = true;
        
        refClazz = (CONSTANT_Class) clazz.getConstant_pool()[class_index];
        refClazz.update();
        refNameAndType = ((CONSTANT_NameAndType) clazz.getConstant_pool()[name_and_type_index]);
        refNameAndType.update();
    }

    public String getType()
    {
        return null;
    }

    public CONSTANT_Class getRefClazz()
    {
        return refClazz; 
    }

    public String getName()
    {
        return refNameAndType.getName();
    }

    public String getDescriptor()
    {
        return refNameAndType.getDescriptor();
    }

    public String getValue()
    {
        return refClazz.getFullyQualifiedName() + "." + refNameAndType.getValue();
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        super.store(cos);

        cos.writeU2(refClazz.getIndex());
        cos.writeU2(refNameAndType.getIndex());
    }
}
