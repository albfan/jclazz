package ru.andrew.jclazz.decompiler;

import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.constants.*;

import java.io.*;
import java.util.*;

public class AnonymousClazzSourceView extends ClazzSourceView
{
    private Map innerMapping;
    private Map outerMapping;
    private int inParamsCount = 0;
    
    public AnonymousClazzSourceView(Clazz clazz, ClazzSourceView outerClazz)
    {
        super(clazz, outerClazz);
    }

    protected void loadSource()
    {
    }

    protected void printPackageAndImports()
    {
        // Print nothing for anonymous class
    }

    protected void printClassSignature(PrintWriter pw)
    {
        // Do nothing
    }

    protected void printMethod(PrintWriter pw, MethodSourceView msv)
    {
        if (msv.getMethod().isInit()) return;

        super.printMethod(pw, msv);
    }

    protected MethodSourceView createMethodView(MethodInfo method)
    {
        if (method.isInit())
        {
            MethodSourceView msv = new AnonymousInitMethodView(method, this);
            msv.setIndent("    ");
            return msv;
        }

        return super.createMethodView(method);
    }

    public void putInnerMapping(String inner, int lvNum)
    {
        if (innerMapping == null)
        {
            innerMapping = new HashMap();
        }
        innerMapping.put(inner, new Integer(lvNum));
    }

    public void putOuterMapping(int paramNum, String lvName)
    {
        if (outerMapping == null)
        {
            outerMapping = new HashMap();
        }
        outerMapping.put(new Integer(paramNum), lvName);
    }

    public void setInParamCount(int count)
    {
        inParamsCount = count;
    }

    public int getInParamsCount()
    {
        return inParamsCount;
    }

    public String getOuterClassParam(String fieldName)
    {
        if (innerMapping == null || outerMapping == null) return null;
        Integer lvNum = (Integer) innerMapping.get(fieldName);
        return (String) outerMapping.get(lvNum);
    }

    public String getAnonymousSuperClassFQN()
    {
        CONSTANT_Class[] intfs = clazz.getInterfaces();
        if (intfs != null && intfs.length > 0)
        {
            return intfs[0].getFullyQualifiedName();
        }
        else
        {
            return clazz.getSuperClassInfo().getFullyQualifiedName();
        }
    }

    public String getSource()
    {
        super.loadSource();
        return NL + super.getSource();
    }
}
