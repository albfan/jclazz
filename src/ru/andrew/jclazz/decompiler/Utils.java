package ru.andrew.jclazz.decompiler;

import java.util.HashMap;
import java.util.Map;

public final class Utils {
    public static Map parseArguments(String[] args) {
        Map params = new HashMap();
        for (int index = 0; index < args.length; index++) {
            String arg = args[index];

            if (arg.startsWith("--")) {
                params.put(arg, "yes");
            } else if (arg.startsWith("-")) {
                int ind = arg.indexOf('=');
                params.put(arg.substring(0, ind), arg.substring(ind + 1));
            }
        }
        return params;
    }

    /**
     * Metodo para poner controles y debugear o no (Usar log4j con niveles INFO,TRACE,ETC...
     * @return
     */
    public static boolean hasDebug() {
        return Boolean.parseBoolean(System.getProperty("debug", "false"));
    }
}
