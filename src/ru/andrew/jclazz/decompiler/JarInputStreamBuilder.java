package ru.andrew.jclazz.decompiler;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;

/**
 * Implementacion para ficheros contenidos en un jar
 * User: alberto
 * Date: 16/01/12
 * Time: 3:58
 */
public class JarInputStreamBuilder implements InputStreamBuilder {
    private JarFile jarFile;

    public JarInputStreamBuilder(JarFile jarFile) {
        this.jarFile = jarFile;
    }

    public InputStream getInputStream(String className) throws Exception {
        try {
            return jarFile.getInputStream(jarFile.getJarEntry(className));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}