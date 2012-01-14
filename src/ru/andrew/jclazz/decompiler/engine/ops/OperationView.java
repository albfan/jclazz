package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;

// TODO the following opcodes should be implemented
// 90 - 95   <- dup[2][_x[1,2]], swap
public abstract class OperationView implements CodeItem
{
    protected Operation operation;
    protected MethodSourceView methodView;
    protected Ref ref;
    protected MethodContext context;

    protected Object[] view;

    public OperationView(Operation operation, MethodSourceView methodView)
    {
        this.operation = operation;
        this.methodView = methodView;
        if (methodView != null)
        {
            this.context = methodView.getMethodContext();
        }
    }

    public int getOpcode()
    {
        return operation.getOpcode().getOpcode();
    }

    public long getStartByte()
    {
        return operation.getStartByte();
    }

    public boolean isPush()
    {
        return operation instanceof PushOperation;
    }

    public abstract String getPushType();

    protected String alias(String fqn)
    {
        String clazzAlias0 = ImportManager.getInstance().importClass(fqn, methodView.getClazzView());
        String clazzAlias = clazzAlias0;
        int ind = clazzAlias.lastIndexOf('$');
        while (ind != -1)
        {
            if (startWithNumber(clazzAlias.substring(ind + 1)))
            {
                return clazzAlias0;
            }
            clazzAlias = clazzAlias.substring(0, ind) + "." + clazzAlias.substring(ind + 1);
            ind = clazzAlias.indexOf('$');
        }
        return clazzAlias;
    }

    private boolean startWithNumber(String str)
    {
        char ch = str.charAt(0);
        return ch == '0' || ch == '1' || ch == '2' || ch == '3' || ch == '4' ||
                ch == '5' || ch == '6' || ch == '7' || ch == '8' || ch == '9';
    }

    //public abstract String source();

    protected void buildView()
    {
    }

    public String source2()
    {
        buildView();
        if (view != null)
        {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < view.length; i++)
            {
                Object subItem = view[i];
                if (subItem instanceof String)
                {
                    sb.append((String) subItem);
                }
                else if (subItem instanceof LocalVariable.LVView)
                {
                    LocalVariable.LVView lvView = (LocalVariable.LVView) subItem;
                    if (LocalVariable.UNKNOWN_TYPE.equals(lvView.getType()))
                    {
                        lvView.renew("java.lang.Object");
                    }
                    String importedType = alias(lvView.getType());
                    lvView.setAliasedType(importedType);
                    sb.append(lvView.getView());
                    lvView.setPrinted();
                }
                else
                {
                    OperationView ov = (OperationView) subItem;
                    sb.append(ov.source2());
                }
            }
            return sb.toString();
        }
        else
        {
            return null;
        }
    }

    /**
     * @deprecated 
     */
    public String source3()
    {
        buildView();
        if (view != null)
        {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < view.length; i++)
            {
                Object subItem = view[i];
                if (subItem instanceof String)
                {
                    sb.append((String) subItem);
                }
                else if (subItem instanceof LocalVariable.LVView)
                {
                    LocalVariable.LVView lvView = (LocalVariable.LVView) subItem;
                    sb.append(lvView.getView());

                }
                else
                {
                    OperationView ov = (OperationView) subItem;
                    sb.append(ov.source3());
                }
            }
            return sb.toString();
        }
        else
        {
            return null;
        }
    }

    public Object[] getView()
    {
        return view;
    }

    public boolean isPrintable()
    {
        return true;
    }

    //public abstract void analyze(Block block);

    public abstract void analyze2(Block block);
}
