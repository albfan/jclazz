package ru.andrew.jclazz.decompiler;

import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.engine.ops.*;

import java.io.*;
import java.util.*;

public class EnumSourceView extends ClazzSourceView
{
    public EnumSourceView(Clazz clazz, ClazzSourceView outerClazz)
    {
        super(clazz, outerClazz);

        this.clazz = clazz;
        if (outerClazz != null)
        {
            isInnerClass = true;
            this.outerClazz = outerClazz;
        }

        loadSource();
    }

    protected void printFields(PrintWriter pw)
    {
        // Loading static init
        for (int i = 0; i < clazz.getMethods().length; i++)
        {
            if (!clazz.getMethods()[i].isStaticInit()) continue;

            pw.print("    ");
            boolean isFirstVar = true;

            MethodSourceView cl_msv = new MethodSourceView(clazz.getMethods()[i], this);
            // First operations: PutFieldView, + PutFieldView array to $VALUES variable
            Iterator it = cl_msv.getTopBlock().getOperations().iterator();
            while (it.hasNext())
            {
                CodeItem citem = (CodeItem) it.next();
                if (!(citem instanceof PutFieldView)) continue;
                if ("$VALUES".equals(((PutFieldView) citem).getFieldName()))
                {
                    break;
                }
                if (!isFirstVar) pw.print(", ");

                pw.print(((PutFieldView) citem).getFieldName());
                String src = ((PutFieldView) citem).source3();
                int index = src.indexOf(',', src.indexOf(',') + 1);
                if (index != -1)
                {
                    pw.print("(");
                    src = src.substring(index + 1, src.lastIndexOf(')'));
                    src = src.trim();
                    pw.print(src);
                    pw.print(")");
                }
                isFirstVar = false;
            }
            pw.println(";");
            break;
        }

        super.printFields(pw);
    }

    protected void printField(PrintWriter pw, FieldSourceView fsv)
    {
        if (fsv.getFieldInfo().isEnum()) return;

        super.printField(pw, fsv);
    }

    protected MethodSourceView createMethodView(MethodInfo method)
    {
        if (method.isInit())
        {
            MethodSourceView msv = new EnumInitMethodSourceView(method, this);
            msv.setIndent("    ");
            return msv;
        }

        if (method.isStaticInit())
        {
            MethodSourceView msv = new EnumClinitMethodSourceView(method, this);
            msv.setIndent("    ");
            return msv;
        }

        return super.createMethodView(method);
    }

    protected void printMethod(PrintWriter pw, MethodSourceView msv)
    {
        MethodInfo meth = msv.getMethod();

        // Don't print values() method
        if ("values".equals(meth.getName()) &&
                (msv.getClazz().getThisClassInfo().getFullyQualifiedName() + "[]").equals(meth.getDescriptor().getReturnType().getFQN()) &&
                meth.isStatic() &&
                meth.isPublic())
        {
            return;
        }

        // Don't print valueOf() method
        if ("valueOf".equals(meth.getName()) &&
                msv.getClazz().getThisClassInfo().getFullyQualifiedName().equals(meth.getDescriptor().getReturnType().getFQN()) &&
                meth.isStatic() &&
                meth.isPublic())
        {
            return;
        }

        super.printMethod(pw, msv);
    }
}
