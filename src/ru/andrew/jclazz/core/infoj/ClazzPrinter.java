package ru.andrew.jclazz.core.infoj;

import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.constants.*;

import java.io.*;
import java.util.*;

public class ClazzPrinter
{
    private PrintWriter pw;

    public ClazzPrinter(PrintWriter pw)
    {
        this.pw = pw;
    }

    public void close()
    {
        pw.close();
    }

    public void print(Clazz clazz)
    {
        pw.println("This Class  : " + clazz.getThisClassInfo().getFullyQualifiedName());
        pw.println("Super Class : " + (clazz.getSuperClassInfo() != null ? clazz.getSuperClassInfo().getFullyQualifiedName() : ""));
        pw.println("Implemented Interfaces:");
        CONSTANT_Class[] intfs = clazz.getInterfaces();
        for (int i = 0; i < intfs.length; i++)
        {
            pw.println("   " + intfs[i].getFullyQualifiedName());
        }
        pw.println();
        
        pw.println("Version: " + clazz.getVersion());
        pw.println("JVM supported: " + clazz.getJVMSupportedVersion());
        pw.println();
        pw.print("Access Flags: " + clazz.getAccessFlags() + " (");
        if ((clazz.getAccessFlags() & Clazz.ACC_PUBLIC) > 0) pw.print(" PUBLIC ");
        if ((clazz.getAccessFlags() & Clazz.ACC_FINAL) > 0) pw.print(" FINAL ");
        if ((clazz.getAccessFlags() & Clazz.ACC_SUPER) > 0) pw.print(" SUPER ");
        if ((clazz.getAccessFlags() & Clazz.ACC_INTERFACE) > 0) pw.print(" INTERFACE ");
        if ((clazz.getAccessFlags() & Clazz.ACC_ABSTRACT) > 0) pw.print(" ABSTRACT ");
        if ((clazz.getAccessFlags() & Clazz.ACC_SYNTHETIC) > 0) pw.print(" SYNTHETIC ");
        if ((clazz.getAccessFlags() & Clazz.ACC_ANNOTATION) > 0) pw.print(" ANNOTATION ");
        if ((clazz.getAccessFlags() & Clazz.ACC_ENUM) > 0) pw.print(" ENUM ");
        pw.println(")");
        pw.println();

        pw.println("Attributes:");
        AttributeInfo[] attrs = clazz.getAttributes();
        for (int i = 0; i < attrs.length; i++)
        {
            pw.println(attrs[i].toString());
        }
        pw.println();

        pw.println("Fields:");
        FieldInfo[] fields = clazz.getFields();
        for (int i = 0; i < fields.length; i++)
        {
            printField(pw, fields[i]);
            pw.println();
        }
        pw.println();

        pw.println("Methods:");
        MethodInfo[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++)
        {
            printMethod(pw, methods[i]);
            pw.println();
        }

        pw.println("Constant Pool:");
        CONSTANT[] cps = clazz.getConstant_pool();
        for (int i = 0; i < cps.length; i++)
        {
            pw.print(i + ": ");
            if (cps[i] != null)
            {
                String type = cps[i].getClass().getName(); // TODO check
                type = type.substring(type.indexOf('_') + 1);
                pw.print(type + ": ");
                pw.print(cps[i].getValue());
            }
            pw.println();
        }
    }

    public static void printField(PrintWriter pw, FieldInfo f_info)
    {
        pw.println(f_info.getName() + " : " + f_info.getDescriptor().getFQN());
        pw.println("{");
        pw.print("    Access Flags: " + f_info.getAccessFlags() + " (");
        if ((f_info.getAccessFlags() & FieldInfo.ACC_PUBLIC) > 0)    pw.print(" PUBLIC ");
        if ((f_info.getAccessFlags() & FieldInfo.ACC_PRIVATE) > 0)   pw.print(" PRIVATE ");
        if ((f_info.getAccessFlags() & FieldInfo.ACC_PROTECTED) > 0) pw.print(" PROTECTED ");
        if ((f_info.getAccessFlags() & FieldInfo.ACC_STATIC) > 0)    pw.print(" STATIC ");
        if ((f_info.getAccessFlags() & FieldInfo.ACC_FINAL) > 0)     pw.print(" FINAL ");
        if ((f_info.getAccessFlags() & FieldInfo.ACC_VOLATILE) > 0)  pw.print(" VOLATILE ");
        if ((f_info.getAccessFlags() & FieldInfo.ACC_TRANSIENT) > 0) pw.print(" TRANSIENT ");
        if ((f_info.getAccessFlags() & FieldInfo.ACC_SYNTHETIC) > 0) pw.print(" SYNTHETIC ");
        if ((f_info.getAccessFlags() & FieldInfo.ACC_ENUM) > 0)      pw.print(" ENUM ");
        pw.println(")");
        pw.println();

        pw.println("    Attributes:");
        AttributeInfo[] attrs = f_info.getAttributes();
        for (int i = 0; i < attrs.length; i++)
        {
            pw.println("        " + attrs[i]);
        }
        pw.println("}");
    }

    public static void printMethod(PrintWriter pw, MethodInfo m_info)
    {
        pw.println(m_info.getName() + " : " + m_info.getDescriptor());
        pw.println("{");
        pw.print("    Access Flags: " + m_info.getAccessFlags() + " (");
        if ((m_info.getAccessFlags() & MethodInfo.ACC_PUBLIC) > 0) pw.print(" PUBLIC ");
        if ((m_info.getAccessFlags() & MethodInfo.ACC_PRIVATE) > 0) pw.print(" PRIVATE ");
        if ((m_info.getAccessFlags() & MethodInfo.ACC_PROTECTED) > 0) pw.print(" PROTECTED ");
        if ((m_info.getAccessFlags() & MethodInfo.ACC_STATIC) > 0) pw.print(" STATIC ");
        if ((m_info.getAccessFlags() & MethodInfo.ACC_FINAL) > 0) pw.print(" FINAL ");
        if ((m_info.getAccessFlags() & MethodInfo.ACC_SYNCHRONIZED) > 0) pw.print(" SYNCHRONIZED ");
        if ((m_info.getAccessFlags() & MethodInfo.ACC_BRIDGE) > 0) pw.print(" BRIDGE ");
        if ((m_info.getAccessFlags() & MethodInfo.ACC_VARARGS) > 0) pw.print(" VARARGS ");
        if ((m_info.getAccessFlags() & MethodInfo.ACC_NATIVE) > 0) pw.print(" NATIVE ");
        if ((m_info.getAccessFlags() & MethodInfo.ACC_ABSTRACT) > 0) pw.print(" ABSTRACT ");
        if ((m_info.getAccessFlags() & MethodInfo.ACC_STRICT) > 0) pw.print(" STRICT ");
        if ((m_info.getAccessFlags() & MethodInfo.ACC_SYNTHETIC) > 0) pw.print(" SYNTHETIC ");
        pw.println(")");
        pw.println();

        pw.println("    Attributes:");
        AttributeInfo[] attrs = m_info.getAttributes();
        Code code = null;
        for (int i = 0; i < attrs.length; i++)
        {
            if (attrs[i] instanceof Code)
            {
                code = (Code) attrs[i];
            }
            else
            {
                String attrInfo = attrs[i].toString();
                attrInfo = attrInfo.replaceAll("\n", "\n        ");
                pw.println("    " + attrInfo);
            }
        }

        if (code != null)
        {
            pw.println("    CODE");
            pw.println("       (");
            pw.println("        Max stack = " + code.getMaxStack());
            pw.println("        Max locals = " + code.getMaxLocals());
            pw.println("        Attributes:");
            for (int j = 0; j < code.getAttributes().length; j++)
            {
                String attrInfo = code.getAttributes()[j].toString();
                attrInfo = attrInfo.replaceAll("\n", "\n            ");
                pw.println("            " + attrInfo);
            }
            pw.println("        Exceptions:");
            for (int j = 0; j < code.getExceptionTable().length; j++)
            {
                Code.ExceptionTable cet = code.getExceptionTable()[j];
                pw.println("            " + "[" + cet.start_pc + "-" + cet.end_pc + "): " + cet.handler_pc + (cet.catch_type != null ? " - " + cet.catch_type.getFullyQualifiedName() : ""));
            }
            pw.println("       )");
            pw.println("    {");
            for (Iterator i = code.getOperations().iterator(); i.hasNext();)
            {
                Operation oper = (Operation) i.next();
                pw.println("        " + oper.asString());
            }
            pw.println("    }");
        }
        pw.println("}");
    }
}
