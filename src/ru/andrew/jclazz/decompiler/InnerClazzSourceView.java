package ru.andrew.jclazz.decompiler;

import ru.andrew.jclazz.core.Clazz;

public class InnerClazzSourceView extends ClazzSourceView {
    public InnerClazzSourceView(Clazz innerClazz, ClazzSourceView outerClazzSourceView) {
        super(innerClazz, outerClazzSourceView);
    }

    protected void printPackageAndImports() {
        // Print nothing for inner class
    }
}
