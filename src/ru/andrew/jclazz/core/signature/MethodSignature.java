package ru.andrew.jclazz.core.signature;

import java.util.*;

/*
MethodTypeSignature:
   <FormalTypeParameter+>opt (TypeSignature*) ReturnType ThrowsSignature*
ReturnType:
   TypeSignature
   V
ThrowsSignature:
   ^ClassTypeSignature
   ^T Identifer;         - type variable
 */
public class MethodSignature
{
    private FormalTypeParameter[] typeParameters;
    private TypeSignature[] paramTypes;

    private boolean isVoidReturned;
    private TypeSignature retType;

    private ClassTypeSignature[] thrownClasses;
    private String[] thrownVariables;

    public MethodSignature(String signature)
    {
        StringBuffer sb = new StringBuffer(signature);

        // Loading type parameters
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

        // Loading method parameters
        List pars = new ArrayList();
        sb.deleteCharAt(0); // (
        while (sb.charAt(0) != ')')
        {
            pars.add(TypeSignature.parse(sb));
        }
        paramTypes = new TypeSignature[pars.size()];
        pars.toArray(paramTypes);
        sb.deleteCharAt(0); // )

        // Loading return type
        if (sb.charAt(0) == 'V')
        {
            isVoidReturned = true;
            sb.deleteCharAt(0);
        }
        else
        {
            isVoidReturned = false;
            retType = TypeSignature.parse(sb);
        }

        // Loading throws signatures
        List thClasses = new ArrayList();
        List thVars = new ArrayList();
        while (sb.length() > 0)
        {
            sb.deleteCharAt(0); // ^
            if (sb.charAt(0) == 'T')
            {
                sb.deleteCharAt(0);
                String id = sb.substring(0, sb.indexOf(";"));
                sb.delete(0, sb.indexOf(";"));
                thVars.add(id);
                sb.deleteCharAt(0);
            }
            else
            {
                thClasses.add(ClassTypeSignature.parse(sb));
            }
        }
        thrownClasses = new ClassTypeSignature[thClasses.size()];
        thClasses.toArray(thrownClasses);
        thrownVariables = new String[thVars.size()];
        thVars.toArray(thrownVariables);
    }

    public FormalTypeParameter[] getTypeParameters()
    {
        return typeParameters;
    }

    public TypeSignature[] getParamTypes()
    {
        return paramTypes;
    }

    public boolean isVoidReturned()
    {
        return isVoidReturned;
    }

    public TypeSignature getReturnType()
    {
        return retType;
    }

    public ClassTypeSignature[] getThrownClassType()
    {
        return thrownClasses;
    }

    public String[] getThrownVariables()
    {
        return thrownVariables;
    }

    public boolean hasThrowClause()
    {
        return (thrownClasses.length > 0) || (thrownVariables.length > 0);
    }
}
