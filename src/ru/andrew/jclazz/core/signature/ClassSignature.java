package ru.andrew.jclazz.core.signature;

import java.util.*;

/*
ClassSignature:
   <FormalTypeParameter+> ClassTypeSignature ClassTypeSignature*
   optional               superclass         interfaces
 */
public class ClassSignature
{
    private ClassTypeSignature superClass;
    private ClassTypeSignature[] interfaces;
    private FormalTypeParameter[] typeParameters;

    public ClassSignature(String signature)
    {
        StringBuffer sb = new StringBuffer(signature);
        if (signature.charAt(0) == '<')
        {
            sb.deleteCharAt(0);
            List ftps = new ArrayList();
            while (sb.charAt(0) != '>')
            {
                ftps.add(FormalTypeParameter.parse(sb));
            }
            typeParameters = new FormalTypeParameter[ftps.size()];
            ftps.toArray(typeParameters);
            sb.deleteCharAt(0);
        }

        // Loading super class
        if (sb.length() > 0)
        {
            superClass = ClassTypeSignature.parse(sb);
        }
        List intfs = new ArrayList();
        while (sb.length() > 0)
        {
            intfs.add(ClassTypeSignature.parse(sb));
        }
        interfaces = new ClassTypeSignature[intfs.size()];
        intfs.toArray(interfaces);
    }

    public ClassTypeSignature getSuperClass()
    {
        return superClass;
    }

    public ClassTypeSignature[] getInterfaces()
    {
        return interfaces;
    }

    public FormalTypeParameter[] getTypeParameters()
    {
        return typeParameters;
    }
}
