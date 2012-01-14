package ru.andrew.jclazz.decompiler.engine;

import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.decompiler.*;

public class LocalVariable
{
    public static final String UNKNOWN_TYPE = "_UNKNOWN_TYPE_";

    private int lv_num;
    private String type;
    private String name;
    private boolean isFinal = false;
    private boolean isMethodArg = false;

    private boolean forceThis = false;

    private MethodSourceView methodView;

    private boolean isPrinted = false;

    private LVView view;

    public LocalVariable(int ivar, String type, MethodSourceView methodView)
    {
        this.lv_num = ivar;
        this.type = type != null ? type : UNKNOWN_TYPE;
        this.name = type != null ? methodView.getLVName(type) : null;

        this.methodView = methodView;

        this.view = new LVView();
    }

    public int getLVNumber()
    {
        return lv_num;
    }

    public void setIsMethodArg(boolean isMethodArg)
    {
        this.isMethodArg = isMethodArg;
    }

    public boolean isIsMethodArg()
    {
        return isMethodArg;
    }

    public LVView getView()
    {
        return view;
    }

    private LocalVariableTable.LocalVariable cachedLV;

    public void ensure(int start_byte)
    {
        if (forceThis) return;
        if (methodView.getMethod().getCodeBlock() != null && methodView.getMethod().getCodeBlock().getLocalVariableTable() != null)
        {
            LocalVariableTable.LocalVariable lv = methodView.getMethod().getCodeBlock().getLocalVariableTable().getLocalVariable(lv_num, start_byte);
            if (lv == cachedLV) return;
            cachedLV = lv;
            name = lv.name.getString();
            try
            {
                type = new FieldDescriptor(lv.descriptor.getString()).getFQN();
            }
            catch (ClazzException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public String getType()
    {
        return type;
    }

    public void renewType(String type)
    {
        if (forceThis) return;
        this.type = type;
        this.name = methodView.getLVName(type);
    }

    public String getName()
    {
        return name;
    }

    public void forceThis()
    {
        forceThis = true;
        this.name = "this";
    }

    public void forceFinal()
    {
        isFinal = true;
    }

    public boolean isPrinted()
    {
        return isPrinted;
    }

    public void setPrinted(boolean printed)
    {
        isPrinted = printed;
    }

    public String toString()
    {
        return "LV-" + lv_num + "(" + type + " as " + name + ")";
    }

    public class LVView
    {
        private String aliasedType = null;

        public String getView()
        {
            if (isPrinted || forceThis)
            {
                return name;
            }
            else
            {
                return (isFinal ? "final " : "") + (aliasedType != null ? aliasedType : type) + " " + name;
            }
        }

        public String getType()
        {
            return type;
        }

        public void setAliasedType(String aliasedType)
        {
            this.aliasedType = aliasedType;
        }
        
        public void renew(String newType)
        {
            renewType(newType);
        }

        public void setPrinted()
        {
            isPrinted = true;
        }

        // Hack methos
        public void setPrinted(boolean printed)
        {
            isPrinted = printed;
        }
    }
}
