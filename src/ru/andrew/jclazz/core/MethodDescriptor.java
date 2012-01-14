package ru.andrew.jclazz.core;

import java.util.*;

public class MethodDescriptor
{
    private FieldDescriptor returnType;
    private List params;

    public MethodDescriptor(String descriptor) throws ClazzException
    {
        if (descriptor.charAt(0) != '(') throw new ClazzException("Invalid method descriptor");
        params = new ArrayList();
        int currentPos = 1;
        while (descriptor.charAt(currentPos) != ')')
        {
            int nextPos = currentPos;
            while (descriptor.charAt(nextPos) == '[') nextPos++;
            if (descriptor.charAt(nextPos) == 'L')
            {
                nextPos = descriptor.indexOf(';', nextPos);
            }
            nextPos++;
            params.add(new FieldDescriptor(descriptor.substring(currentPos, nextPos)));
            currentPos = nextPos;
        }
        returnType = new FieldDescriptor(descriptor.substring(currentPos + 1));
    }

    public FieldDescriptor getReturnType()
    {
        return returnType;
    }

    public List getParams()
    {
        return params;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();

        sb.append("(");
        for (Iterator it = params.iterator(); it.hasNext();)
        {
            FieldDescriptor fd = (FieldDescriptor) it.next();
            sb.append(fd.toString());
            if (it.hasNext()) sb.append(", ");
        }
        sb.append(") : ");
        sb.append(returnType.toString());

        return sb.toString();
    }
}
