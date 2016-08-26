package com.nepxion.thunder.console.workspace.config;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import javax.swing.tree.DefaultTreeModel;

import com.nepxion.swing.element.ElementNode;
import com.nepxion.swing.exception.ExceptionTracer;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.renderer.tree.ElementTreeCellRenderer;
import com.nepxion.swing.tree.JBasicTree;
import com.nepxion.thunder.console.controller.ConfigController;
import com.nepxion.thunder.console.locale.ConsoleLocale;

public class ConfigTree extends JBasicTree {
    private static final long serialVersionUID = 1L;

    public ConfigTree(String category) {
        reloadRoot(category);
    }

    public void reloadRoot(String category) {
        ElementNode root = null;
        try {
            root = ConfigController.assembleRoot(category);
        } catch (Exception e) {
            ExceptionTracer.traceException(HandleManager.getFrame(this), ConsoleLocale.getString("assemble_tree_exception"), e);

            return;
        }

        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        setModel(treeModel);
        setCellRenderer(new ElementTreeCellRenderer(ElementTreeCellRenderer.TREE_THEME_NIMBUS, 25));
        expandAll();
    }
}