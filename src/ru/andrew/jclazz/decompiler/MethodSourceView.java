package ru.andrew.jclazz.decompiler;

import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.core.constants.*;
import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;

import java.util.*;
import java.util.ArrayList;

public class MethodSourceView extends SourceView
{
    public static final String INIT_METHOD = "<init>";
    public static final String CLASS_INIT_METHOD = "<clinit>";

    protected MethodInfo methodInfo;
    protected ClazzSourceView clazzView;
    protected Block topBlock;
    protected MethodContext context;

    public MethodSourceView(MethodInfo methodInfo, ClazzSourceView clazzView)
    {
        this.methodInfo = methodInfo;
        this.clazzView = clazzView;
        this.context = new MethodContext(methodInfo);

        // InnerClass support
        if (methodInfo.isSynthetic() && methodInfo.isStatic() && methodInfo.getName().startsWith("access$"))
        {
            List syn_pars = methodInfo.getDescriptor().getParams();
            List ops = methodInfo.getOperations() != null ? methodInfo.getOperations() : null;
            // Synthetic method for getting field from inner class
            if (syn_pars.size() == 1 && ((FieldDescriptor) syn_pars.get(0)).getFQN().equals(clazzView.getClazz().getThisClassInfo().getFullyQualifiedName()) &&
                    ops != null && ops.size() == 3)
            {
                // Standard bytecode for synthetic Inner Class support method:
                // 0 aload_0
                // 1 getfield XXX
                // 4 areturn
                int op1 = ((Operation) ops.get(0)).getOpcode().getOpcode();
                int op2 = ((Operation) ops.get(1)).getOpcode().getOpcode();
                int op3 = ((Operation) ops.get(2)).getOpcode().getOpcode();
                if (op1 == 42 && op2 == 180 && op3 >= 172 && op3 <= 176)
                {
                    fieldName = ((GetField) ops.get(1)).getFieldName();
                    isForIC = true;
                }
            }
            // Synthetic method for invoking method from inner class
            if (ops != null)
            {
                // Standard bytecode for synthetic Inner Class support method:
                // 0 aload_0
                // 1 aload_Y
                // ...
                // N invokespecial XXX
                // K Xreturn
                int pos = 0;
                int op = 0;
                do
                {
                    op = ((Operation) ops.get(pos)).getOpcode().getOpcode();
                    pos++;
                }
                while (op >= 21 && op <= 53 && (pos < ops.size()));
                if (pos + 1 == ops.size())
                {
                    int opInvoke = op;
                    int opReturn = ((Operation) ops.get(pos)).getOpcode().getOpcode();
                    if (opInvoke == 183 && opReturn >= 172 && opReturn <= 177)    // invokespecial & Xreturn
                    {
                        methodNameForIC = ((Invoke) ops.get(pos - 1)).getMethodName();
                        isForIC = true;
                    }
                }
            }
        }

        loadSource();
    }

    public MethodInfo getMethod()
    {
        return methodInfo;
    }

    public MethodContext getMethodContext()
    {
        return context;
    }

    public Clazz getClazz()
    {
        return methodInfo.getClazz();
    }

    public ClazzSourceView getClazzView()
    {
        return clazzView;
    }

    public Block getTopBlock()
    {
        return topBlock;
    }

    protected void parse()
    {
        if (methodInfo.isSynthetic()) return;

        if (methodInfo.getCodeBlock() != null)
        {
            topBlock = ByteCodeConverter.convertToViewOperations(methodInfo.getCodeBlock().getOperations(), this);
        }
        else
        {
            topBlock = new Block(new ArrayList(), this);
        }

        view = new ArrayList();

        if (methodInfo.isDeprecated())
        {
            println("/**");
            println("  * @deprecated");
            println(" */");
        }

        if (CLASS_INIT_METHOD.equals(methodInfo.getName()))
        {
            print("static ");
        }
        else
        {
            if (methodInfo.isPublic()) print("public ");
            if (methodInfo.isPrivate()) print("private ");
            if (methodInfo.isProtected()) print("protected ");
            if (methodInfo.isStatic()) print("static ");
            if (methodInfo.isFinal()) print("final ");
            if (methodInfo.isStrictFP()) print("strictfp ");
            if (methodInfo.isSynchronized()) print("synchronized ");
            if (methodInfo.isNative()) print("native ");
            if (methodInfo.isAbstract()) print("abstract ");
        }

        if (methodInfo.getSignature() != null)
        {
            print(SignatureView.preMethodSignature(methodInfo.getSignature(), methodInfo, getClazzView()));
            //print(methodInfo.getSignature().strPreName(methodInfo.getClazz()));
        }
        else if (!INIT_METHOD.equals(methodInfo.getName()) && !CLASS_INIT_METHOD.equals(methodInfo.getName()))
        {
            String retClass = importClass(methodInfo.getDescriptor().getReturnType().getFQN());
            print(retClass);
            print(" ");
        }

        if (INIT_METHOD.equals(methodInfo.getName()))
        {
            String initName = clazzView.getClazz().getThisClassInfo().getName();
            if (clazzView.isInnerClass())
            {
                initName = initName.substring(initName.lastIndexOf('$') + 1);
            }
            print(initName);
        }
        else if (CLASS_INIT_METHOD.equals(methodInfo.getName()))
        {
        }
        else
        {
            print(methodInfo.getName());
        }

        // Initializing local variables, passed as params to this method
        if (!methodInfo.isStatic())
        {
            LocalVariable lv_this = topBlock.getLocalVariable(0, null, 0);
            lv_this.forceThis();
        }

        printMethodParameters();

        if (methodInfo.getSignature() == null || !methodInfo.getSignature().hasThrowClause())
        {
            if (methodInfo.getExceptions() != null)
            {
                print(" throws ");
                CONSTANT_Class[] exceptions = methodInfo.getExceptions().getExceptionTable();
                for (int e = 0; e < exceptions.length; e++)
                {
                    print(importClass(exceptions[e].getFullyQualifiedName()));
                    if (e != exceptions.length - 1) print(", ");
                }
            }
        }
        else
        {
            print(SignatureView.throwMethodSignature(methodInfo.getSignature(), methodInfo, getClazzView()));
            //print(methodInfo.getSignature().strThrows(methodInfo.getClazz()));
        }

        if (methodInfo.getCodeBlock() != null)
        {
            try
            {
                println("");
                String codeView = codeBlockSource(ByteCodeConverter.convert(topBlock, this));
                if (codeView == null)
                {
                    // Aborting method printing
                    clearAll();
                    return;
                }
                print(codeView);
                println("");
            }
            catch (OperationException oe)
            {
                println("!!! EXCEPTION OCCURED !!!");
                oe.printStackTrace();
            }
        }
        else
        {
            println(";");
            println("");
        }
    }

    protected void printMethodParameters()
    {
        if (methodInfo.getSignature() == null)
        {
            printMethodParametersByDescription();
        }
        else
        {
            printMethodParametersBySignature();
        }
    }

    protected void printMethodParametersByDescription()
    {
        int addition = methodInfo.isStatic() ? 0 : 1;
        if (!CLASS_INIT_METHOD.equals(methodInfo.getName()))
        {
            print("(");
            List params = new ArrayList(methodInfo.getDescriptor().getParams());

            // Inner Class support
            if (clazzView.isInnerClass() &&
                    INIT_METHOD.equals(methodInfo.getName()) &&
                    !params.isEmpty() &&
                    clazzView.getOuterClazz().getClazz().getThisClassInfo().getFullyQualifiedName().equals(((FieldDescriptor) params.get(0)).getFQN()))
            {
                addition++;
                params.remove(0);
            }

            int lvi = 0;
            for (int i = 0; i < params.size() - 1; i++)
            {
                FieldDescriptor fd = (FieldDescriptor) params.get(i);
                String rawType = fd.getFQN();
                LocalVariable lv = topBlock.getLocalVariable(lvi + addition, rawType, 0);
                lv.ensure(0);
                lv.setIsMethodArg(true);
                printView(lv.getView());
                //print(importClass(fd.getFQN()) + " " + lv.getName());

                String lvAliasType = importClass(fd.getFQN());
                lv.setPrinted(true);
                lv.getView().setAliasedType(lvAliasType);
                if ("long".equals(rawType) || "double".equals(rawType))
                {
                    lvi += 2;
                }
                else
                {
                    lvi++;
                }
                print(", ");
            }
            if (params.size() > 0)
            {
                FieldDescriptor fd = (FieldDescriptor) params.get(params.size() - 1);
                LocalVariable lv = topBlock.getLocalVariable(lvi + addition, fd.getFQN(), 0);
                lv.ensure(0);
                lv.setIsMethodArg(true);
                String lpFQN = importClass(fd.getFQN());
                if (!methodInfo.isVarargs())
                {
                    //print(lpFQN + " " + lv.getName());
                    printView(lv.getView());
                }
                else
                {
                    lpFQN = lpFQN.substring(0, lpFQN.length() - 2) + "...";
                    //print(lpFQN + "... " + lv.getName());
                    printView(lv.getView());
                }
                lv.getView().setAliasedType(lpFQN);
                lv.setPrinted(true);
            }
            print(")");
        }
    }

    protected void printMethodParametersBySignature()
    {
        print(SignatureView.postMethodSignature(methodInfo.getSignature(), this, getClazzView()));
        //print(methodInfo.getSignature().strPostName(methodInfo.getClazz()));
    }

    protected String codeBlockSource(Block block)
    {
        return block.getSource();
    }

    // Inner Class support
    private boolean isForIC = false;
    private String fieldName;
    private String methodNameForIC;

    public String getFieldNameForIC()
    {
        return fieldName;
    }

    public String getMethodNameForIC()
    {
        return methodNameForIC;
    }

    public boolean isForIC()
    {
        return isForIC;
    }
    
    // Local variable naming support

    public static final int VAR_N_LOWER = 1;
    public static final int VAR_N_BL = 2;
    public static int VAR_NAMING = VAR_N_LOWER;
    private HashMap lvarNames = new HashMap();

    public String getLVName(String ftype)
    {
        // Removing full package path
        int ind0 = ftype.lastIndexOf('.');
        String type = ind0 != -1 ? ftype.substring(ind0 + 1) : ftype;

        String m_name;
        switch (VAR_NAMING)
        {
            case VAR_N_LOWER:
                m_name = type.toLowerCase();
                break;
            case VAR_N_BL:
                m_name = "";
                String low_name = type.toLowerCase();
                for (int j = 0; j < type.length(); j++)
                {
                    if (type.charAt(j) != low_name.charAt(j)) m_name = m_name + low_name.charAt(j);
                }
                if ("".equals(m_name)) m_name = type.toLowerCase();
                break;
            default:
                m_name = type;
        }

        // Array variables
        while (m_name.indexOf('[') != -1)
        {
            int ind = m_name.indexOf('[');
            m_name = m_name.substring(0, ind) + "s" + m_name.substring(ind + 2);
        }

        String str = (String) lvarNames.get(m_name);
        if (str == null)
        {
            str = m_name + "_1";
        }
        else
        {
            str = m_name + "_" + (Integer.valueOf(str.substring(str.lastIndexOf('_') + 1)).intValue() + 1);
        }
        lvarNames.put(m_name, str);

        return str;
    }
}
