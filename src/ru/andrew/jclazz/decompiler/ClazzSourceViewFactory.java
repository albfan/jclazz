package ru.andrew.jclazz.decompiler;

import ru.andrew.jclazz.core.Clazz;

public class ClazzSourceViewFactory {

    public static ClazzSourceView getClazzSourceView(Clazz clazz) {
        if (clazz.isEnumeration()) {
            return new EnumSourceView(clazz, null);
        } else {
            return new ClazzSourceView(clazz, null);
        }
    }
}


