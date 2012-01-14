package ru.andrew.jclazz.decompiler;

import ru.andrew.jclazz.core.*;

import java.util.*;

public final class ImportManager
{
    private static ImportManager instance = new ImportManager();

    private static HashMap classes;

    private ImportManager()
    {
        classes = new HashMap();
    }

    public static ImportManager getInstance()
    {
        return instance;
    }

    public String importClass(String fqn, ClazzSourceView clazzView)
    {
        ClazzSourceView csv = clazzView;
        while (csv.getOuterClazz() != null)
        {
            csv = csv.getOuterClazz();
        }

        ImportManager im = (ImportManager) classes.get(csv.getClazz().getThisClassInfo().getFullyQualifiedName());
        if (im == null)
        {
            im = new ImportManager(csv.getClazz());
            classes.put(csv.getClazz().getThisClassInfo().getFullyQualifiedName(), im);
        }

        return im.importClass(fqn);
    }

    public Collection getImports(Clazz clazz)
    {
        ImportManager im = (ImportManager) classes.get(clazz.getThisClassInfo().getFullyQualifiedName());
        if (im == null)
        {
            return null;
        }
        return im.getImports();
    }

    public boolean isSingleClassImport(Clazz clazz)
    {
        ImportManager im = (ImportManager) classes.get(clazz.getThisClassInfo().getFullyQualifiedName());
        return im != null && im.isSingleClassImport();
    }

    public void setSingleClassImport(Clazz clazz, boolean singleClassImport)
    {
        ImportManager im = (ImportManager) classes.get(clazz.getThisClassInfo().getFullyQualifiedName());
        if (im == null) return;
        im.setSingleClassImport(singleClassImport);
    }

    // Imports part

    private Map imports;
    private Set packs;
    private String currentPackage;
    private boolean isSingleClassImport = false;

    private ImportManager(Clazz clazz)
    {
        imports = new HashMap();
        imports.put("byte", "byte");
        imports.put("char", "char");
        imports.put("double", "double");
        imports.put("float", "float");
        imports.put("int", "int");
        imports.put("long", "long");
        imports.put("short", "short");
        imports.put("boolean", "boolean");
        imports.put("void", "void");

        packs = new HashSet();
        packs.add("java.lang");
        currentPackage = clazz.getThisClassInfo().getPackageName();
        if (currentPackage != null && !"".equals(currentPackage))
        {
            packs.add(currentPackage);
        }
    }

    private boolean isSingleClassImport()
    {
        return isSingleClassImport;
    }

    private void setSingleClassImport(boolean singleClassImport)
    {
        isSingleClassImport = singleClassImport;
    }

    private Collection getImports()
    {
        List imps = new ArrayList();

        currentPackage = currentPackage != null ? currentPackage : "";
        for (Iterator it = packs.iterator(); it.hasNext();)
        {
            String p = (String) it.next();
            if (!"java.lang".equals(p) && !currentPackage.equals(p))
            {
                imps.add(p + ".*");
            }
        }

        for (Iterator it = imports.keySet().iterator(); it.hasNext();)
        {
            String fqn = (String) it.next();
            if ("byte".equals(fqn) || "char".equals(fqn) ||
                    "double".equals(fqn) || "float".equals(fqn) ||
                    "int".equals(fqn) || "long".equals(fqn) ||
                    "short".equals(fqn) || "boolean".equals(fqn) ||
                    "void".equals(fqn) )
            {
                continue;
            }
            imps.add(fqn);
        }

        return imps;
    }

    /**
     * Imports class.
     * @param fqn_0 class fully qualified name
     * @return class name without package part or unchanged FQN
     */
    private String importClass(String fqn_0)
    {
        String fqn = fqn_0;
        String dotClass = "";
        // Due to Push support pushing Classes (java.lang.Integer.class)
        // we need to remove last ".class" part
        if (fqn.endsWith(".class"))
        {
            fqn = fqn.substring(0, fqn.length() - 6);
            dotClass = ".class";
        }

        String pureFQN = fqn;
        String arrayQN = "";
        int index = fqn.indexOf('[');
        if (index != -1)
        {
            pureFQN = pureFQN.substring(0, index);
            arrayQN = fqn.substring(index);
        }

        String classAlias = (String) imports.get(pureFQN);
        if (classAlias != null)
        {
            return classAlias + arrayQN + dotClass;
        }
        String iname;
        String ipackage;
        if (pureFQN.lastIndexOf('.') == -1)
        {
            return fqn + dotClass;
        }
        else
        {
            iname = pureFQN.substring(pureFQN.lastIndexOf('.') + 1);
            ipackage = pureFQN.substring(0, pureFQN.lastIndexOf('.'));
        }

        // Finding in package imports
        if (packs.contains(ipackage))
        {
            return iname + arrayQN + dotClass;
        }
        for (Iterator i = packs.iterator(); i.hasNext();)
        {
            if (isClassExists(iname, (String) i.next()))
            {
                imports.put(pureFQN, pureFQN);
                return fqn + dotClass;
            }
        }
        if (!isSingleClassImport)   // Importing whole package
        {
            packs.add(ipackage);
            return iname + arrayQN + dotClass;
        }

        // Finding in single class imports
        if (imports.containsValue(iname))
        {
            imports.put(pureFQN, pureFQN);
        }
        else
        {
            imports.put(pureFQN, iname);
        }
        return imports.get(pureFQN) + arrayQN + dotClass;
    }

    private boolean isClassExists(String iname, String ipackage)
    {
        try
        {
            Class.forName(ipackage + "." + iname);
            return true;
        }
        catch (ClassNotFoundException e)
        {
            return false;
        }
    }
}
