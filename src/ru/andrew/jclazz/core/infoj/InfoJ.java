package ru.andrew.jclazz.core.infoj;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.ClazzException;
import ru.andrew.jclazz.decompiler.FileInputStreamBuilder;

import java.io.IOException;

public final class InfoJ {
    public static void main(String[] args) throws ClazzException, IOException {
        if (args.length == 0) {
            System.out.println("USAGE: InfoJ classfile\n");
            return;
        }
        String classFile = args[0];
        Clazz clazz = new Clazz(classFile, new FileInputStreamBuilder());
        InfoJCreator.generateInfoFile(clazz);
    }
}
