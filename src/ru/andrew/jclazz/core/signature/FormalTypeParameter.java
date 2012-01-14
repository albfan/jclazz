package ru.andrew.jclazz.core.signature;

import java.util.*;

/*
FormalTypeParameter:
   Identifier : FieldTypeSignature   (:FieldTypeSignature)*
                classbound, optional interfacebounds
 */
public class FormalTypeParameter
{
    private String identifier;
    private FieldTypeSignature classBound;
    private FieldTypeSignature[] intfBounds;

    private FormalTypeParameter(String identifier, FieldTypeSignature classBound, List intfBounds)
    {
        this.identifier = identifier;
        this.classBound = classBound;
        this.intfBounds = new FieldTypeSignature[intfBounds.size()];
        intfBounds.toArray(this.intfBounds);
    }

    public static FormalTypeParameter parse(StringBuffer sign)
    {
        int ind = sign.indexOf(":");
        String id = sign.substring(0, ind);
        sign.delete(0, ind);

        FieldTypeSignature clBound = null;
        sign.deleteCharAt(0);
        if (sign.length() > 0 && sign.charAt(0) != ':') // Loading classBound
        {
            clBound = FieldTypeSignature.parse(sign);
        }

        // Loading interface bounds
        List intBounds = new ArrayList();
        while (sign.charAt(0) == ':')
        {
            sign.deleteCharAt(0);
            intBounds.add(FieldTypeSignature.parse(sign));
        }

        return new FormalTypeParameter(id, clBound, intBounds);
    }

    public String getName()
    {
        return identifier;
    }

    public FieldTypeSignature getClassBound()
    {
        return classBound;
    }

    public FieldTypeSignature[] getInterfaceBounds()
    {
        return intfBounds;
    }
}
