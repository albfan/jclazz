package ru.andrew.jclazz.decompiler;

import ru.andrew.jclazz.core.*;

public class InnerClazzSourceView extends ClazzSourceView
{
    public InnerClazzSourceView(Clazz clazz, ClazzSourceView outerClazz)
    {
        super(clazz, outerClazz);
    }

    protected void printPackageAndImports()
    {
        // Print nothing for inner class
    }
}
