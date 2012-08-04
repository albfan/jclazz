package ru.andrew.jclazz.decompiler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Implementacion para ficheros compilados sin empaquetar
 * User: nenopera
 * Date: 16/01/12
 * Time: 3:56
 */
public class FileInputStreamBuilder implements InputStreamBuilder {
    public InputStream getInputStream(String className) throws FileNotFoundException {
        return new FileInputStream(className);
    }
}
