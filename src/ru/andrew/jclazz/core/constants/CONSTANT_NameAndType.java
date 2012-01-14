package ru.andrew.jclazz.core.constants;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;

import java.io.*;

public class CONSTANT_NameAndType extends CONSTANT
{
    private int name_index;
    private int descriptor_index;
    private boolean loaded = false;

    private CONSTANT_Utf8 nameUTF8;
    private CONSTANT_Utf8 descriptorUTF8;

    protected CONSTANT_NameAndType(int num, int tag, Clazz clazz)
    {
        super(num, tag, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException
    {
        name_index = cis.readU2();
        descriptor_index = cis.readU2();
    }

    public void update() throws ClazzException
    {
        if (loaded) return;
        loaded = true;
        
        nameUTF8 = (CONSTANT_Utf8) clazz.getConstant_pool()[name_index];
        nameUTF8.update();
        descriptorUTF8 = (CONSTANT_Utf8) clazz.getConstant_pool()[descriptor_index];
        descriptorUTF8.update();
    }

    public String getType()
    {
        return null;
    }

    public String getName()
    {
        return nameUTF8.getString();
    }

    public String getDescriptor()
    {
        return descriptorUTF8.getString();
    }

    public String getValue()
    {
        return getName() + " (" + getDescriptor() + ")";
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        super.store(cos);

        cos.writeU2(nameUTF8.getIndex());
        cos.writeU2(descriptorUTF8.getIndex());
    }
}
