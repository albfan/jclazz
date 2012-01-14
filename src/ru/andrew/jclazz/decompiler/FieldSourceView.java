package ru.andrew.jclazz.decompiler;

import ru.andrew.jclazz.core.*;

public class FieldSourceView extends SourceView
{
    protected FieldInfo fieldInfo;
    private ClazzSourceView clazzSourceView;

    public FieldSourceView(FieldInfo fieldInfo, ClazzSourceView clazzSourceView)
    {
        super();
        this.fieldInfo = fieldInfo;
        this.clazzSourceView = clazzSourceView;

        loadSource();
    }

    protected void parse()
    {
        if (fieldInfo.isSynthetic() && fieldInfo.getName().startsWith("$SwitchMap$"))
        {
            print("static int[] switchMap;");
        }

        if (fieldInfo.isSynthetic()) return;

        if (fieldInfo.isDeprecated())
        {
            println("/**");
            println("  * @deprecated");
            println(" */");
        }
        if (fieldInfo.isPublic()) print("public ");
        if (fieldInfo.isPrivate()) print("private ");
        if (fieldInfo.isProtected()) print("protected ");
        if (fieldInfo.isStatic()) print("static ");
        if (fieldInfo.isFinal()) print("final ");
        if (fieldInfo.isVolatile()) print("volatile ");
        if (fieldInfo.isTransient()) print("transient ");

        if (fieldInfo.getSignature() == null)
        {
            String descriptor = fieldInfo.getDescriptor().getFQN();

            // Inner Class support
            if (descriptor.indexOf('$') != -1)
            {
                InnerClassView icv = getClazzView().getInnerClassView(descriptor);
                if (icv != null)
                {
                    descriptor = descriptor.substring(descriptor.indexOf('$') + 1);
                }
            }

            print(importClass(descriptor));
        }
        else
        {
            print(SignatureView.asString(fieldInfo.getSignature(), getClazzView()));
        }

        print(" " + fieldInfo.getName());
        if (fieldInfo.getConstantValue() != null)
        {
            print(" = ");
            print(fieldInfo.getConstantValue());
        }
        println(";");
    }

    public FieldInfo getFieldInfo()
    {
        return fieldInfo;
    }

    public ClazzSourceView getClazzView()
    {
        return clazzSourceView;
    }
}
