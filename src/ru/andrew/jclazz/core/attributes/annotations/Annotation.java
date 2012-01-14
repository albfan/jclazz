package ru.andrew.jclazz.core.attributes.annotations;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.constants.*;

import java.io.*;

public class Annotation
{
    private CONSTANT_Utf8 type;
    private ElementValuePair[] element_value_pairs;

    public String getType()
    {
        return type.getString();
    }

    public void setType(CONSTANT_Utf8 type)
    {
        this.type = type;
    }

    public ElementValuePair[] getElementValuePairs()
    {
        return element_value_pairs;
    }

    public void setElementValuePairs(ElementValuePair[] element_value_pairs)
    {
        this.element_value_pairs = element_value_pairs;
    }

    public static Annotation load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        Annotation ann = new Annotation();
        
        int type_index = cis.readU2();
        ann.setType((CONSTANT_Utf8) clazz.getConstant_pool()[type_index]);

        int num_element_value_pairs = cis.readU2();
        ElementValuePair[] pairs = new ElementValuePair[num_element_value_pairs];
        for (int j = 0; j < num_element_value_pairs; j++)
        {
            int element_name_index = cis.readU2();
            CONSTANT_Utf8 element_name = (CONSTANT_Utf8) clazz.getConstant_pool()[element_name_index];
            char tag = (char) cis.readU1();
            pairs[j] = new ElementValuePair(element_name, tag);
            pairs[j].loadValue(cis, clazz);
        }
        ann.setElementValuePairs(pairs);
        return ann;
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU2(type.getIndex());
        cos.writeU2(element_value_pairs.length);
        for (int j = 0; j < element_value_pairs.length; j++)
        {
            element_value_pairs[j].storeValue(cos);
        }
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("#").append(type.getString());
        sb.append("{");
        for (int i = 0; i < element_value_pairs.length; i++)
        {
            sb.append(element_value_pairs[i].toString());
            if (i < element_value_pairs.length - 1) sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }
}
