package ru.andrew.jclazz.core.attributes.annotations;

import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.constants.*;

import java.io.*;

public class RuntimeVisibleParameterAnnotations extends AttributeInfo
{
    private Annotation[][] parameter_annotations;

    public RuntimeVisibleParameterAnnotations(CONSTANT_Utf8 attributeName, Clazz clazz)
    {
        super(attributeName, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException, ClazzException
    {
        attributeLength = (int) cis.readU4();

        int num_parameters = cis.readU1();
        parameter_annotations = new Annotation[num_parameters][];
        for (int i = 0; i < num_parameters; i++)
        {
            int num_annotations = cis.readU2();
            parameter_annotations[i] = new Annotation[num_annotations];
            for (int j = 0; j < num_annotations; j++)
            {
                parameter_annotations[i][j] = Annotation.load(cis, clazz);
            }
        }
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU4(attributeLength);
        cos.writeU1(parameter_annotations.length);
        for (int i = 0; i < parameter_annotations.length; i++)
        {
            cos.writeU2(parameter_annotations[i].length);
            for (int j = 0; j < parameter_annotations[i].length; j++)
            {
                parameter_annotations[i][j].store(cos);
            }
        }
    }

    public Annotation[][] getParameterAnnotations()
    {
        return parameter_annotations;
    }

    public String toString()
    {
        return toString("RuntimeVisibleParameterAnnotations");
    }

    protected String toString(String attrName)
    {
        StringBuffer sb = new StringBuffer(ATTR);
        sb.append(attrName).append(": \n");
        for (int i = 0; i < parameter_annotations.length; i++)
        {
            sb.append(INDENT).append("*\n");
            for (int j = 0; j < parameter_annotations[i].length; j++)
            {
                sb.append(INDENT).append(parameter_annotations[i][j].toString());
                if (j < parameter_annotations[i].length - 1) sb.append("\n");
            }
            if (i < parameter_annotations.length - 1) sb.append("\n");
        }
        return sb.toString();
    }
}
