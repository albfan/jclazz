package ru.andrew.jclazz.core.infoj;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.ClazzException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public final class InfoJCreator {
    public static String generateInfoFile(Clazz clazz) throws FileNotFoundException {
        String outFile = clazz.getFileName();
        outFile = outFile.substring(0, outFile.lastIndexOf('.')) + ".jinfo";

        ClazzPrinter rcp = new ClazzPrinter(new PrintWriter(new FileOutputStream(outFile)));
        rcp.print(clazz);
        rcp.close();

        return outFile;
    }

    public static String generateInfoFile(String path) throws IOException, ClazzException {
        return generateInfoFile(new Clazz(path));
    }
}
