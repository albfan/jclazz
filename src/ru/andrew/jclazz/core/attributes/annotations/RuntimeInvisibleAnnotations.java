package ru.andrew.jclazz.core.attributes.annotations;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.constants.CONSTANT_Utf8;

public class RuntimeInvisibleAnnotations extends RuntimeVisibleAnnotations {
    public RuntimeInvisibleAnnotations(CONSTANT_Utf8 attributeName, Clazz clazz) {
        super(attributeName, clazz);
    }

    public String toString() {
        return toString("RuntimeInvisibleAnnotations");
    }
}
