package ru.andrew.jclazz.gui;

import ru.andrew.jclazz.*;

import javax.swing.tree.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.gui.nodes.*;

public class ClazzTreeUI
{
    private Clazz clazz;
    private DefaultTreeModel model;

    public ClazzTreeUI(Clazz clazz)
    {
        this.clazz = clazz;

        ClazzTreeNode root = new ClazzNode(clazz);

        model = new DefaultTreeModel(root);
    }

    public TreeModel getTreeModel()
    {
        return model;
    }
}
