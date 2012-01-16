package ru.andrew.jclazz.decompiler;

import ru.andrew.jclazz.core.Clazz;

import java.util.jar.JarFile;

public class ClazzSourceViewFactory {

    public static ClazzSourceView getClazzSourceView(Clazz clazz, final InputStreamBuilder builder) {
        if (clazz.isEnumeration()) {
            return new EnumSourceView(clazz, null);
        } else {
            return new ClazzSourceView(clazz, null, builder);
        }
    }

    public static ClazzSourceView getFileClazzSourceView(Clazz clazz) {
        return new ClazzSourceViewFactory().getClazzSourceView(clazz, new FileInputStreamBuilder());
    }

    public static ClazzSourceView getJarClazzSourceView(Clazz clazz, JarFile jarFile) {
        return new ClazzSourceViewFactory().getClazzSourceView(clazz, new JarInputStreamBuilder(jarFile));
    }
}


