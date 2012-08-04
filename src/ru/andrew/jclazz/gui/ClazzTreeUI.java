package ru.andrew.jclazz.gui;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.gui.nodes.ClazzNode;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

public class ClazzTreeUI {
    private Clazz clazz;
    private DefaultTreeModel model;

    public ClazzTreeUI(Clazz clazz) {
        this.clazz = clazz;

        ClazzTreeNode root = new ClazzNode(clazz);

        model = new DefaultTreeModel(root);
    }

    public TreeModel getTreeModel() {
        return model;
    }
}
