package ru.andrew.jclazz.core.attributes.annotations;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.constants.CONSTANT_Utf8;

public class RuntimeInvisibleParameterAnnotations extends RuntimeVisibleParameterAnnotations {
    public RuntimeInvisibleParameterAnnotations(CONSTANT_Utf8 attributeName, Clazz clazz) {
        super(attributeName, clazz);
    }

    public String toString() {
        return toString("RuntimeInvisibleParameterAnnotations");
    }
}
