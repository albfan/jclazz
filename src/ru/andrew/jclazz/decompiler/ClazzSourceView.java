package ru.andrew.jclazz.decompiler;

import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.attributes.*;

import java.util.*;
import java.io.*;

public class ClazzSourceView extends SourceView
{
    public static final String WITH_LINE_NUMBERS = "--ln";
    public static boolean SUPPRESS_EXCESSED_THIS = true;

    protected Clazz clazz;
    protected boolean isInnerClass = false;
    protected ClazzSourceView outerClazz;

    protected MethodSourceView[] methodViews;
    private InnerClassView[] innerClassViews;

    protected boolean printAsAnonymous = false;

    public ClazzSourceView(Clazz clazz, ClazzSourceView outerClazz)
    {
        super();

        this.clazz = clazz;
        if (outerClazz != null)
        {
            isInnerClass = true;
            this.outerClazz = outerClazz;
        }
        
        loadSource();
    }

    protected void printClassSignature(PrintWriter pw)
    {
        if (clazz.isDeprecated())
        {
            pw.println("/**");
            pw.println(" * @deprecated");
            pw.println(" */");
        }

        if (clazz.isPublic()) pw.print("public ");
        if (clazz.isStatic()) pw.print("static ");
        if (clazz.isFinal() && !clazz.isEnumeration()) pw.print("final ");
        if (clazz.isAbstract() && !clazz.isInterface()) pw.print("abstract ");
        if (clazz.isInterface())
        {
            if (clazz.isAnnotation()) pw.print("@");
            pw.print("interface ");
        }
        else if (clazz.isEnumeration())
        {
            pw.print("enum ");
        }
        else
        {
            pw.print("class ");
        }

        String clazzName = clazz.getThisClassInfo().getName();
        if (isInnerClass())
        {
            clazzName = clazzName.substring(clazzName.lastIndexOf('$') + 1);

            // Search for field starting with $SwitchMap$
            FieldInfo[] fields = clazz.getFields();
            for (int i = 0; i < fields.length; i++)
            {
                if (fields[i].getName().startsWith("$SwitchMap$"))
                {
                    clazzName = "SM_" + clazzName;
                    break;
                }
            }
        }
        pw.print(clazzName);

        if (clazz.getClassSignature() == null)
        {
            if ((clazz.getSuperClassInfo() != null) && (!"java.lang.Object".equals(clazz.getSuperClassInfo().getFullyQualifiedName())))
            {
                pw.print(" extends ");
                String superClassName = importClass(clazz.getSuperClassInfo().getFullyQualifiedName());
                pw.print(superClassName);
            }

            if (clazz.getInterfaces().length > 0 && !clazz.isAnnotation())
            {
                pw.print(" implements ");
                for (int i = 0; i < clazz.getInterfaces().length; i++)
                {
                    String intName = importClass(clazz.getInterfaces()[i].getFullyQualifiedName());
                    pw.print(intName);
                    if (i != clazz.getInterfaces().length - 1) pw.print(", ");
                }
            }
        }
        else if (!clazz.isEnumeration())
        {
            pw.print(SignatureView.asString(clazz.getClassSignature(), this));
        }

        pw.println();
    }

    protected void parse()
    {
        // Registering clazz view in cache
        registerInnerClazz(clazz.getThisClassInfo().getFullyQualifiedName(), this);

        // Initializing Inner Classes
        InnerClass[] inners = clazz.getInnerClasses();
        if (inners != null)
        {
            innerClassViews = new InnerClassView[inners.length];
            for (int i = 0; i < inners.length; i++)
            {
                innerClassViews[i] = new InnerClassView(inners[i]);
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw;
        try
        {
            pw = new PrintWriter(new OutputStreamWriter(baos, "UTF-16"));
        }
        catch (UnsupportedEncodingException ex)
        {
            throw new RuntimeException(ex);
        }

        printClassSignature(pw);

        pw.println("{");

        printFields(pw);

        pw.println();

        printMethods(pw);

        // Printing Inner Classes
        if (innerClassViews != null)
        {
            for (int i = 0; i < innerClassViews.length; i++)
            {
                if (innerClassViews[i].getInnerFQN() == null) continue;
                if (innerClassViews[i].getClazzView() != null &&
                    innerClassViews[i].getClazzView() instanceof AnonymousClazzSourceView) continue;
                
                pw.flush();

                if (!innerClassViews[i].getInnerFQN().startsWith(getClazz().getThisClassInfo().getFullyQualifiedName()))
                {
                    continue;
                }

                if (getInnerClazzView(innerClassViews[i].getInnerFQN()) != null)
                {
                    continue;
                }
                String inname = innerClassViews[i].getInnerClass().getInnerClass().getName();
                String path = clazz.getFileName().substring(0, clazz.getFileName().lastIndexOf(System.getProperty("file.separator")) + 1);
                Clazz innerClazz;
                try
                {
                    innerClazz = new Clazz(path + inname + ".class");
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
                ClazzSourceView innerClassPrinter = new InnerClazzSourceView(innerClazz, this);
                innerClassViews[i].setClazzView(innerClassPrinter);
                innerClassPrinter.setIndent("    ");
                pw.println(innerClassPrinter.getSource());
                pw.println();
            }
        }

        pw.print("}");
        //if (!printAsAnonymous) pw.println();

        printPackageAndImports();
        pw.flush();
        try
        {
            print(baos.toString("UTF-16"));
            baos.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected void printFields(PrintWriter pw)
    {
        for (int i = 0; i < clazz.getFields().length; i++)
        {
            FieldSourceView fsv = new FieldSourceView(clazz.getFields()[i], this);
            fsv.setIndent("    ");
            printField(pw, fsv);
        }
    }

    protected void printField(PrintWriter pw, FieldSourceView fsv)
    {
        pw.flush();
        pw.print(fsv.getSource());
    }

    protected MethodSourceView createMethodView(MethodInfo method)
    {
        MethodSourceView msv = new MethodSourceView(method, this);
        msv.setIndent("    ");
        return msv;
    }

    protected void printMethods(PrintWriter pw)
    {
        methodViews = new MethodSourceView[clazz.getMethods().length];

        // First load synthetic methods
        int methodsNum = 0;
        for (int i = 0; i < clazz.getMethods().length; i++)
        {
            if (!clazz.getMethods()[i].isSynthetic()) continue;

            MethodSourceView msv = createMethodView(clazz.getMethods()[i]);
            methodViews[methodsNum] = msv;
            methodsNum++;

            printMethod(pw, msv);
        }

        // Load all other methods
        for (int i = 0; i < clazz.getMethods().length; i++)
        {
            if (clazz.getMethods()[i].isSynthetic()) continue;
            MethodSourceView msv = createMethodView(clazz.getMethods()[i]);
            methodViews[methodsNum] = msv;
            methodsNum++;

            printMethod(pw, msv);
        }
    }

    protected void printMethod(PrintWriter pw, MethodSourceView msv)
    {
        if (msv.getMethod().isInit() && printAsAnonymous) return;
        if (msv.getMethod().isSynthetic()) return;

        pw.flush();
        pw.print(msv.getSource());
    }

    protected void printPackageAndImports()
    {
     //   if ("yes".equals(clazz.getDecompileParameter(Params.PRINT_HEADER)))
     //   {
//            println("/*");
//            println(" Decompiled by Andrew");
//            println(" SourceFile: " + (clazz.getSourceFile() != null ? clazz.getSourceFile() : "na"));
//            println(" Class Version: " + String.valueOf(clazz.getMajorVersion()) + "." + String.valueOf(clazz.getMinorVersion()));
//            println("*/");
//            println("");
     //   }

        if (clazz.getThisClassInfo().getPackageName() != null)
        {
            print("package ");
            print(clazz.getThisClassInfo().getPackageName());
            println(";");
            println("");
        }

        Collection imports = ImportManager.getInstance().getImports(clazz);
        if (imports == null || imports.isEmpty())
        {
            //println("");
        }
        else
        {
            for (Iterator iit = imports.iterator(); iit.hasNext();)
            {
                println("import " + iit.next() + ";");
            }
            println("");
        }

        flush();
    }

    public void setPrintAsAnonymous(boolean printAsAnonymous)
    {
        this.printAsAnonymous = printAsAnonymous;
    }

    public Clazz getClazz()
    {
        return clazz;
    }

    public ClazzSourceView getClazzView()
    {
        return this;
    }

    public InnerClassView getInnerClassView(String fqn)
    {
        for (int i = 0; i < innerClassViews.length; i++)
        {
            if (fqn.equals(innerClassViews[i].getInnerFQN()))
            {
                return innerClassViews[i];
            }
        }
        return null;
    }

    public InnerClass getInnerClass(String fqn)
    {
        InnerClass[] innerClasses = clazz.getInnerClasses();
        for (int i = 0; i < innerClasses.length; i++)
        {
            if (innerClasses[i].getInnerClass() == null) continue;
            if (fqn.equals(innerClasses[i].getInnerClass().getFullyQualifiedName()))
            {
                return innerClasses[i];
            }
        }
        return null;
    }

    public FieldInfo getFieldByName(String name)
    {
        FieldInfo[] fields = clazz.getFields();
        for (int i = 0; i < fields.length; i++)
        {
            if (fields[i].getName().equals(name))
            {
                return fields[i];
            }
        }
        return null;
    }

    // Class decompilation parameters
    
    private Map params = new HashMap();

    public void setDecompileParameters(Map params)
    {
        this.params = params;
    }

    public String getDecompileParameter(String key)
    {
        if (outerClazz != null) outerClazz.getDecompileParameter(key);
        return (String) params.get(key);
    }

    // Inner Class support

    public boolean isInnerClass()
    {
        return isInnerClass;
    }

    public ClazzSourceView getOuterClazz()
    {
        return outerClazz;
    }

    public MethodSourceView getSyntheticMethodForIC(String methodName)
    {
        for (int i = 0; i < methodViews.length; i++)
        {
            // TODO this can be invoked while printing methods, i.e. some methods are not initialized
            if (methodViews[i] == null) continue;

            if (methodViews[i].getMethod().getName().equals(methodName))
            {
                if (methodViews[i].isForIC())
                {
                    return methodViews[i];
                }
            }
        }
        return null;
    }

    private Map innerClasses = new HashMap();

    public void registerInnerClazz(String fqn, ClazzSourceView innerClazzView)
    {
        ClazzSourceView mainView = this;
        while (mainView.getOuterClazz() != null)
        {
            mainView = mainView.getOuterClazz();
        }
        mainView.innerClasses.put(fqn, innerClazzView);
    }

    public ClazzSourceView getInnerClazzView(String fqn)
    {
        ClazzSourceView mainView = this;
        while (mainView.getOuterClazz() != null)
        {
            mainView = mainView.getOuterClazz();
        }
        return (ClazzSourceView) mainView.innerClasses.get(fqn);
    }
}
