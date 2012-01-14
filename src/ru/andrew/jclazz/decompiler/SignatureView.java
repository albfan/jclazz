package ru.andrew.jclazz.decompiler;

import ru.andrew.jclazz.core.signature.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.decompiler.engine.*;

public class SignatureView
{
    public static String asString(Object signature, ClazzSourceView clazz)
    {
        if (signature instanceof TypeSignature)
        {
            return typeSignature((TypeSignature) signature, clazz);
        }
        else if (signature instanceof TypeArgument)
        {
            return typeArgument((TypeArgument) signature, clazz);
        }
        else if (signature instanceof SimpleClassTypeSignature)
        {
            return simpleClassTypeSignature((SimpleClassTypeSignature) signature, clazz);
        }
        else if (signature instanceof FormalTypeParameter)
        {
            return formalTypeParameter((FormalTypeParameter) signature, clazz);
        }
        else if (signature instanceof FieldTypeSignature)
        {
            return fieldTypeSignature((FieldTypeSignature) signature, clazz);
        }
        else if (signature instanceof ClassTypeSignature)
        {
            return classTypeSignature((ClassTypeSignature) signature, clazz);
        }
        else if (signature instanceof MethodSignature)
        {
            // TODO
            return methodSignature((MethodSignature) signature);
        }
        else if (signature instanceof ClassSignature)
        {
            return classSignature((ClassSignature) signature, clazz);
        }
        else
        {
            throw new RuntimeException("Unknown signature type");
        }
    }

    protected static String typeSignature(TypeSignature sign, ClazzSourceView clazz)
    {
        return sign.getFieldType() != null ? asString(sign.getFieldType(), clazz) : sign.getBaseType();
    }

    protected static String typeArgument(TypeArgument sign, ClazzSourceView clazz)
    {
        StringBuffer sb = new StringBuffer();
        switch (sign.getModifier())
        {
            case '*':
                sb.append("?");
                break;
            case '-':
                sb.append("? super ");
                break;
            case '+':
                sb.append("? extends ");
                break;
            default:
        }
        if (sign.getFieldType() != null) sb.append(asString(sign.getFieldType(), clazz));
        return sb.toString();
    }

    protected static String simpleClassTypeSignature(SimpleClassTypeSignature sign, ClazzSourceView clazz)
    {
        StringBuffer sb = new StringBuffer(sign.getName());
        TypeArgument[] typeArguments = sign.getTypeArguments();
        if (typeArguments != null && typeArguments.length > 0)
        {
            sb.append("<");
            for (int i = 0; i < typeArguments.length; i++)
            {
                if (i > 0) sb.append(", ");
                sb.append(asString(typeArguments[i], clazz));
            }
            sb.append("> ");
        }
        return sb.toString();
    }

    protected static String methodSignature(MethodSignature sign)
    {
        StringBuffer sb = new StringBuffer();
        /*
        FormalTypeParameter[] typeParameters = sign.getTypeParameters();
        if (typeParameters != null && typeParameters.length > 0)
        {
            sb.append("<");
            for (int i = 0; i < typeParameters.length; i++)
            {
                if (i > 0) sb.append(", ");
                sb.append(asString(typeParameters[i]));
            }
            sb.append("> ");
        }
        if (sign.isVoidReturned())
        {
            sb.append("void ");
        }
        else
        {
            sb.append(asString(sign.getReturnType())).append(" ");
        }

        sb.append("$$$NAME$$$");

        sb.append("(");
        int addition = m_info.isStatic() ? 0 : 1;
        TypeSignature[] paramTypes = sign.getParamTypes();
        for (int i = 0; i < paramTypes.length - 1; i++)
        {
            LocalVariable lv = m_info.getCodeBlock().getCodeBlock().getLocalVariable(i + addition, null);
            sb.append(asString(paramTypes[i])).append(" ").append(lv.getName(0)).append(", ");
        }
        if (paramTypes.length > 0)
        {
            LocalVariable lv = m_info.getCodeBlock().getCodeBlock().getLocalVariable(paramTypes.length - 1 + addition, null);
            String lp = asString(paramTypes[paramTypes.length - 1]);
            if (!m_info.isVarargs())
            {
                sb.append(lp);
            }
            else
            {
                lp = lp.substring(0, lp.length() - 2);
                sb.append(lp).append("...");
            }
            sb.append(" ").append(lv.getName(0));
        }
        sb.append(")");

        ClassTypeSignature[] thrownClasses = sign.getThrownClassType();
        String[] thrownVariables = sign.getThrownVariables();
        if (thrownClasses.length > 0 || thrownVariables.length > 0) sb.append(" throws ");
        for (int i = 0; i < thrownClasses.length; i++)
        {
            if (i > 0) sb.append(", ");
            sb.append(asString(thrownClasses[i]));
        }
        for (int i = 0; i < thrownVariables.length; i++)
        {
            if (i > 0) sb.append(", ");
            sb.append(thrownVariables[i]);
        }
        */
        return sb.toString();
    }

    public static String preMethodSignature(MethodSignature sign, MethodInfo methodInfo, ClazzSourceView clazz)
    {
        StringBuffer sb = new StringBuffer();
        FormalTypeParameter[] typeParameters = sign.getTypeParameters();
        if (typeParameters != null && typeParameters.length > 0)
        {
            sb.append("<");
            for (int i = 0; i < typeParameters.length; i++)
            {
                if (i > 0) sb.append(", ");
                sb.append(asString(typeParameters[i], clazz));
            }
            sb.append("> ");
        }
        if (sign.isVoidReturned())
        {
            if (!methodInfo.isInit()) sb.append("void ");
        }
        else
        {
            sb.append(asString(sign.getReturnType(), clazz)).append(" ");
        }
        return sb.toString();
    }

    public static String postMethodSignature(MethodSignature sign, MethodSourceView m_info, ClazzSourceView clazz)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        int addition = m_info.getMethod().isStatic() ? 0 : 1;
        if (m_info.getMethod().isInit() && clazz.getClazz().isEnumeration()) addition += 2;
        TypeSignature[] paramTypes = sign.getParamTypes();
        int lvi = 0;
        for (int i = 0; i < paramTypes.length - 1; i++)
        {
            String genType = asString(paramTypes[i], clazz);
            String rawgenType = genType.indexOf('<') != -1 ? genType.substring(0, genType.indexOf('<')) : genType;
            LocalVariable lv = m_info.getTopBlock().getLocalVariable(lvi + addition, rawgenType, 0);
            lv.ensure(0);
            lv.setPrinted(true);
            lv.setIsMethodArg(true);
            if ("long".equals(rawgenType) || "double".equals(rawgenType))
            {
                lvi += 2;
            }
            else
            {
                lvi++;
            }
            sb.append(genType).append(" ").append(lv.getName()).append(", ");
        }
        if (paramTypes.length > 0)
        {
            String lp = asString(paramTypes[paramTypes.length - 1], clazz);
            LocalVariable lv = m_info.getTopBlock().getLocalVariable(lvi + addition, lp.indexOf('<') != -1 ? lp.substring(0, lp.indexOf('<')) : lp, 0);
            lv.ensure(0);
            lv.setPrinted(true);
            lv.setIsMethodArg(true);

            if (!m_info.getMethod().isVarargs())
            {
                sb.append(lp);
            }
            else
            {
                lp = lp.substring(0, lp.length() - 2);
                sb.append(lp).append("...");
            }
            sb.append(" ").append(lv.getName());
        }
        sb.append(")");

        return sb.toString();
    }

    public static String throwMethodSignature(MethodSignature sign, MethodInfo m_info, ClazzSourceView clazz)
    {
        StringBuffer sb = new StringBuffer();
        ClassTypeSignature[] thrownClasses = sign.getThrownClassType();
        String[] thrownVariables = sign.getThrownVariables();
        if (thrownClasses.length > 0 || thrownVariables.length > 0) sb.append(" throws ");
        for (int i = 0; i < thrownClasses.length; i++)
        {
            if (i > 0) sb.append(", ");
            sb.append(asString(thrownClasses[i], clazz));
        }
        for (int i = 0; i < thrownVariables.length; i++)
        {
            if (i > 0) sb.append(", ");
            sb.append(thrownVariables[i]);
        }
        return sb.toString();
    }

    protected static String formalTypeParameter(FormalTypeParameter sign, ClazzSourceView clazz)
    {
        StringBuffer sb = new StringBuffer(sign.getName());

        ClassTypeSignature sup = sign.getClassBound().getClassType();
        boolean exists = false;
        if ((sup != null) && (!"java.lang.Object".equals(sup.getPackage() + "." + sup.getClassType().getName())))
        {
            sb.append(" extends ").append(asString(sign.getClassBound(), clazz));
            exists = true;
        }

        FieldTypeSignature[] intfBounds = sign.getInterfaceBounds();
        for (int i = 0; i < intfBounds.length; i++)
        {
            if (!exists)
            {
                sb.append(" extends ").append(asString(intfBounds[i], clazz));
            }
            else
            {
                sb.append("&").append(asString(intfBounds[i], clazz));
                exists = true;
            }
        }

        return sb.toString();
    }

    protected static String fieldTypeSignature(FieldTypeSignature sign, ClazzSourceView clazz)
    {
        if (sign.getClassType() != null)
        {
            return asString(sign.getClassType(), clazz);
        }
        else if (sign.getArrayType() != null)
        {
            return asString(sign.getArrayType(), clazz) + "[]";
        }
        else
        {
            return sign.getVariable();
        }
    }

    protected static String classTypeSignature(ClassTypeSignature sign, ClazzSourceView clazz)
    {
        StringBuffer sb = new StringBuffer();
        SimpleClassTypeSignature cl = sign.getClassType();
        String fqnClassName = (sign.getPackage().length() > 0 ? sign.getPackage() + "." : "") + cl.getName();
        sb.append(ImportManager.getInstance().importClass(fqnClassName, clazz));
        if (cl.getTypeArguments().length > 0)
        {
            sb.append("<");
            for (int i = 0; i < cl.getTypeArguments().length; i++)
            {
                if (i > 0) sb.append(", ");
                sb.append(asString(cl.getTypeArguments()[i], clazz));
            }
            sb.append(">");
        }

        for (int j = 0; j < sign.getSuffix().length; j++)
        {
            // TODO check
            sb.append(".").append(cl.getName());
            if (cl.getTypeArguments().length > 0)
            {
                sb.append("<");
                for (int i = 0; i < cl.getTypeArguments().length; i++)
                {
                    if (i > 0) sb.append(", ");
                    sb.append(asString(cl.getTypeArguments()[i], clazz));
                }
                sb.append(">");
            }
        }

        return sb.toString();
    }

    public static String classSignature(ClassSignature sign, ClazzSourceView clazz)
    {
        StringBuffer sb = new StringBuffer();
        FormalTypeParameter[] typeParameters = sign.getTypeParameters();
        if (typeParameters != null && typeParameters.length > 0)
        {
            sb.append("<");
            for (int i = 0; i < typeParameters.length; i++)
            {
                if (i > 0) sb.append(", ");
                sb.append(asString(typeParameters[i], clazz));
            }
            sb.append(">");
        }

        ClassTypeSignature superClass = sign.getSuperClass();
        if ((superClass != null) && (!"java.lang.Object".equals(superClass.getPackage() + "." + superClass.getClassType().getName())))
        {
            sb.append(" extends ").append(asString(superClass, clazz));
        }

        ClassTypeSignature[] interfaces = sign.getInterfaces();
        if (interfaces.length > 0) sb.append(" implements ");
        for (int i = 0; i < interfaces.length; i++)
        {
            if (i > 0) sb.append(", ");
            sb.append(asString(interfaces[i], clazz));
        }

        return sb.toString();
    }
}
