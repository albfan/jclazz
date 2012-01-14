package ru.andrew.jclazz.decompiler;

import ru.andrew.jclazz.core.*;

import java.util.*;
import java.io.*;

public final class ClassDecompiler
{
    public static String generateJavaFile(String in, Map params) throws ClazzException, IOException
    {
        Clazz clazz = new Clazz(in);
        ClazzSourceView csv = ClazzSourceViewFactory.getClazzSourceView(clazz);
        csv.setDecompileParameters(params);

        String outFile;
        outFile = clazz.getFileName();
        outFile = outFile.substring(0, outFile.lastIndexOf('.') + 1);
        if (csv.getDecompileParameter(Params.EXTENSION) != null)
        {
            outFile += csv.getDecompileParameter(Params.EXTENSION);
        }
        else
        {
            outFile += "jav_";
        }

        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
        pw.println(csv.getSource());
        pw.flush();
        pw.close();

        return outFile;
    }

    public static void main(String[] args) throws ClazzException, IOException
    {
        if ( (args.length == 0) ||
             (args.length == 1 && Params.HELP.equals(args[0])) )
        {
            if (args.length == 0) System.out.println("No input file specified!");
            System.out.println("USAGE:\n" +
                    "Decomp [OPTIONS...] classfile\n" +
                    "Options:\n" +
                    "   --print_header: prints comment header at the top od decompiled class\n" +
                    "   -out=FILE: specify output file\n" +
                    "   -ext=EXT: specify extension of output file\n" +
                    "   --ln: print line numbers\n");
            return;
        }
        Map params = Utils.parseArguments(args);
        String classFile = args[args.length - 1];

        ClassDecompiler.generateJavaFile(classFile, params);
    }

    public static String getVersion()
    {
        return "1.2";
    }
}
