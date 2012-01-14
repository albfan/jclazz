package ru.andrew.jclazz.core.attributes;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.attributes.annotations.*;
import ru.andrew.jclazz.core.attributes.verification.*;
import ru.andrew.jclazz.core.constants.*;

import java.io.*;

public final class AttributesLoader
{
    private static final String SOURCE_FILE = "SourceFile";
    private static final String CONSTANT_VALUE = "ConstantValue";
    private static final String CODE = "Code";
    private static final String EXCEPTIONS = "Exceptions";
    private static final String INNERCLASSES = "InnerClasses";
    private static final String ENCLOSING_METHOD = "EnclosingMethod";
    private static final String SYNTHETIC = "Synthetic";
    private static final String SIGNATURE = "Signature";
    private static final String SOURCE_DEBUG_EXTENSION = "SourceDebugExtension";
    private static final String LINE_NUMBER_TABLE = "LineNumberTable";
    private static final String LOCAL_VARIABLE_TABLE = "LocalVariableTable";
    private static final String LOCAL_VARIABLE_TYPE_TABLE = "LocalVariableTypeTable";
    private static final String DEPRECATED = "Deprecated";
    private static final String RUNTIME_VISIBLE_ANNOTATIONS = "RuntimeVisibleAnnotations";
    private static final String RUNTIME_INVISIBLE_ANNOTATIONS = "RuntimeInvisibleAnnotations";
    private static final String RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS = "RuntimeVisibleParameterAnnotations";
    private static final String RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS = "RuntimeInvisibleParameterAnnotations";
    private static final String ANNOTATION_DEFAULT = "AnnotationDefault";
    private static final String STACK_MAP_TABLE = "StackMapTable";

    public static AttributeInfo loadAttribute(ClazzInputStream cis, Clazz clazz, MethodInfo method) throws ClazzException, IOException
    {
        AttributeInfo attrInfo;
        int attributeNameIndex = cis.readU2();
        CONSTANT_Utf8 attrName = (CONSTANT_Utf8) clazz.getConstant_pool()[attributeNameIndex];
        String name = attrName.getString();

        if (CONSTANT_VALUE.equals(name))
        {
            attrInfo = new ConstantValue(attrName, clazz);
        }
        else if (CODE.equals(name))
        {
            attrInfo = new Code(attrName, clazz, method);
        }
        else if (EXCEPTIONS.equals(name))
        {
            attrInfo = new Exceptions(attrName, clazz);
        }
        else if (INNERCLASSES.equals(name))
        {
            attrInfo = new InnerClasses(attrName, clazz);
        }
        else if (SYNTHETIC.equals(name))
        {
            attrInfo = new Synthetic(attrName, clazz);
        }
        else if (SOURCE_FILE.equals(name))
        {
            attrInfo = new SourceFile(attrName, clazz);
        }
        else if (LINE_NUMBER_TABLE.equals(name))
        {
            attrInfo = new LineNumberTable(attrName, clazz);
        }
        else if (LOCAL_VARIABLE_TABLE.equals(name))
        {
            attrInfo = new LocalVariableTable(attrName, clazz);
        }
        else if (DEPRECATED.equals(name))
        {
            attrInfo = new Deprecated(attrName, clazz);
        }
        else if (ENCLOSING_METHOD.equals(name))
        {
            attrInfo = new EnclosingMethod(attrName, clazz);
        }
        else if (SIGNATURE.equals(name))
        {
            attrInfo = new Signature(attrName, clazz);
        }
        else if (SOURCE_DEBUG_EXTENSION.equals(name))
        {
            attrInfo = new SourceDebugExtension(attrName, clazz);
        }
        else if (LOCAL_VARIABLE_TYPE_TABLE.equals(name))
        {
            attrInfo = new LocalVariableTypeTable(attrName, clazz);
        }
        else if (RUNTIME_VISIBLE_ANNOTATIONS.equals(name))
        {
            attrInfo = new RuntimeVisibleAnnotations(attrName, clazz);
        }
        else if (RUNTIME_INVISIBLE_ANNOTATIONS.equals(name))
        {
            attrInfo = new RuntimeInvisibleAnnotations(attrName, clazz);
        }
        else if (RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS.equals(name))
        {
            attrInfo = new RuntimeVisibleParameterAnnotations(attrName, clazz);
        }
        else if (RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS.equals(name))
        {
            attrInfo = new RuntimeInvisibleParameterAnnotations(attrName, clazz);
        }
        else if (ANNOTATION_DEFAULT.equals(name))
        {
            attrInfo = new AnnotationDefault(attrName, clazz);
        }
        else if (STACK_MAP_TABLE.equals(name))
        {
            attrInfo = new StackMapTable(attrName, clazz);
        }
        else
        {
            attrInfo = new GenericAttribute(attrName, clazz);
        }
        attrInfo.load(cis);

        return attrInfo;
    }
}
