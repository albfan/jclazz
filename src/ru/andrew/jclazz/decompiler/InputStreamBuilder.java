package ru.andrew.jclazz.decompiler;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface para obtener el contenido de una clase compilada
 * User: nenopera
 * Date: 16/01/12
 * Time: 3:55
 */
public interface InputStreamBuilder {
    public InputStream getInputStream(String className) throws IOException;
}
