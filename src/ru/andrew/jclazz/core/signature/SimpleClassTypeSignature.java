package ru.andrew.jclazz.core.signature;

import java.util.*;

/*
SimpleClassTypeSignature:
   Identifier <TypeArgument+>opt

Note: terminal symbols are absent
Used by: ClassTypeSignature
 */
public class SimpleClassTypeSignature
{
    private String identifier;
    private TypeArgument[] typeArguments;

    private SimpleClassTypeSignature(String identifier, List typeArguments)
    {
        this.identifier = identifier;
        this.typeArguments = new TypeArgument[typeArguments.size()];
        typeArguments.toArray(this.typeArguments);
    }

    public static SimpleClassTypeSignature parse(StringBuffer sign)
    {
        int d_ind = sign.indexOf(".");
        d_ind = d_ind != -1 ? d_ind : Integer.MAX_VALUE;
        int sc_ind = sign.indexOf(";");
        sc_ind = sc_ind != -1 ? sc_ind : Integer.MAX_VALUE;
        int g_ind = sign.indexOf("<");
        g_ind = g_ind != -1 ? g_ind : Integer.MAX_VALUE;

        List tArgs = new ArrayList();
        String id;
        if (g_ind < d_ind && g_ind < sc_ind)
        {
            id = sign.substring(0, g_ind);
            sign.delete(0, g_ind);
            sign.deleteCharAt(0);   // <
            while (sign.charAt(0) != '>')
            {
                tArgs.add(TypeArgument.parse(sign));
            }
            sign.deleteCharAt(0);   // >
        }
        else
        {
            id = sign.substring(0, (d_ind < sc_ind ? d_ind : sc_ind));
            sign.delete(0, (d_ind < sc_ind ? d_ind : sc_ind));
        }

        return new SimpleClassTypeSignature(id, tArgs);
    }

    public String getName()
    {
        return identifier;
    }

    public TypeArgument[] getTypeArguments()
    {
        return typeArguments;
    }
}
