package ru.andrew.jclazz.gui;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;

class ClazzTreeNodeCellRenderer extends DefaultTreeCellRenderer
{
    public ClazzTreeNodeCellRenderer()
    {
        super();
    }

    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value, boolean sel, boolean expanded, boolean leaf,
                                                  int row, boolean hasFocus)
    {
        if (!(value instanceof ClazzTreeNode))
        {
            return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        }
        ClazzTreeNode node = (ClazzTreeNode) value;
        setText(node.getUserObject().toString());
        Color fg;
        if (sel)
        {
            fg = getTextSelectionColor();
        }
        else
        {
            fg = getTextNonSelectionColor();
        }
        setForeground(fg);
        setEnabled(true);
		setIcon(node.getIcon());
        setComponentOrientation(tree.getComponentOrientation());
        selected = sel;

        return this;
    }
}
