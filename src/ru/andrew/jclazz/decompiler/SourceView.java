package ru.andrew.jclazz.decompiler;

import java.io.*;
import java.util.*;
import ru.andrew.jclazz.decompiler.engine.LocalVariable;
import ru.andrew.jclazz.decompiler.engine.LocalVariable.LVView;

public abstract class SourceView
{
    protected static final String NL = System.getProperty("line.separator");

    private ByteArrayOutputStream baos;
    private PrintWriter pw;

    private String indent = "";

    private String source;
    protected List view;

    protected SourceView()
    {
        baos = new ByteArrayOutputStream();
        try
        {
            pw = new PrintWriter(new OutputStreamWriter(baos, "UTF-16"));
        }
        catch (UnsupportedEncodingException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    protected abstract void parse();

    protected void loadSource()
    {
        parse();
        flush();
        try
        {
            source = baos.toString("UTF-16");
        }
        catch (UnsupportedEncodingException ex)
        {
            throw new RuntimeException(ex);
        }
        close();
    }

    protected void print(String str)
    {
        if (view != null)
        {
            view.add(str);
        }
        else
        {
            pw.print(str);
        }
    }

    protected void printView(Object obj)
    {
        view.add(obj);
    }

    protected void println(String str)
    {
        if (view != null)
        {
            view.add(str + NL);
        }
        else
        {
            pw.println(str);
        }
    }

    protected void flush()
    {
        pw.flush();
    }

    protected void clearAll()
    {
        if (view != null)
        {
            view = null;
        }
        else
        {
            flush();
            baos.reset();
        }
    }

    protected void close()
    {
        try
        {
            baos.close();
        }
        catch (IOException ioe)
        {
            throw new RuntimeException(ioe);
        }
        pw.close();
    }

    // Useful methods

    public void setIndent(String indent)
    {
        this.indent = indent;
    }

    public String getSource()
    {
        String src;
        if (view != null)
        {
            src = getSourceByView();
        }
        else
        {
            src = source;
        }

        src = indent + src.replaceAll(NL, NL + indent);
        if (src.endsWith(indent)) src = src.substring(0, src.length() - indent.length());
        return src;
    }

    private String getSourceByView()
    {
        StringBuffer sb = new StringBuffer();
        for (Iterator it = view.iterator(); it.hasNext();)
        {
            Object item = it.next();
            if (item instanceof String)
            {
                sb.append(item);
            }
            else if (item instanceof LocalVariable.LVView)
            {
                LocalVariable.LVView lvView = (LVView) item;

                // Hack
                lvView.setPrinted(false);
                sb.append(lvView.getView());
                lvView.setPrinted(true);
            }
        }

        return sb.toString();
    }

    public abstract ClazzSourceView getClazzView();

    protected String importClass(String clazz)
    {
        return ImportManager.getInstance().importClass(clazz, getClazzView());
    }
}
