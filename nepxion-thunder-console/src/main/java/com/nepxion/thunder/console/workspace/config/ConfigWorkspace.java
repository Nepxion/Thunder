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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.tree.TreeNode;

import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.ButtonManager;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.container.ContainerManager;
import com.nepxion.swing.element.ElementNode;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.scrollpane.JBasicScrollPane;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.console.icon.ConsoleIconFactory;
import com.nepxion.thunder.console.locale.ConsoleLocale;
import com.nepxion.thunder.console.workspace.common.ButtonSpace;
import com.nepxion.thunder.console.workspace.common.Workspace;

public abstract class ConfigWorkspace extends Workspace {
    private static final long serialVersionUID = 1L;

    private JPanel container;
    private JPanel blankPane;

    private JBasicLabel titleLabel;
    private ButtonSpace buttonSpace;
    private ConfigTree configTree;

    protected ProtocolType protocolType;
    protected ApplicationEntity applicationEntity;
    protected String service;
    protected String reference;

    public ConfigWorkspace() {
        super();

        container = new JPanel();
        container.setLayout(new BorderLayout());

        blankPane = new JPanel();
        container.add(blankPane, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setLeftComponent(new JBasicScrollPane(createConfigTree()));
        splitPane.setRightComponent(container);
        splitPane.setDividerLocation(200);

        double[][] size = {
                { TableLayout.FILL },
                { TableLayout.FILL, TableLayout.PREFERRED }
        };

        TableLayout tableLayout = new TableLayout(size);
        tableLayout.setHGap(0);
        tableLayout.setVGap(5);

        setLayout(tableLayout);
        add(splitPane, "0, 0");
        add(createButtonSpace(), "0, 1");
    }

    protected JBasicLabel createTitleLabel() {
        if (titleLabel == null) {
            titleLabel = new JBasicLabel();
        }

        return titleLabel;
    }

    protected ConfigTree createConfigTree() {
        if (configTree == null) {
            configTree = new ConfigTree(createCategory()) {
                private static final long serialVersionUID = 1L;

                @Override
                public void executeSelection(TreeNode oldSelectionTreeNode, TreeNode newSelectionTreeNode) {
                    container.removeAll();
                    if (newSelectionTreeNode != null && newSelectionTreeNode.isLeaf() && newSelectionTreeNode.getParent() != getModel().getRoot()) {
                        container.add(createConfigContainer(), BorderLayout.CENTER);
                        configTreeNodeSelection((ElementNode) newSelectionTreeNode);

                        if (applicationEntity != null && titleLabel != null) {
                            titleLabel.setText(ConsoleLocale.getString("application") + " [" + applicationEntity.getApplication() + "]  " + ConsoleLocale.getString("group") + " [" + applicationEntity.getGroup() + "]");
                        }
                    } else {
                        container.add(blankPane, BorderLayout.CENTER);
                    }

                    ContainerManager.update(container);
                }
            };
        }

        return configTree;
    }

    protected ButtonSpace createButtonSpace() {
        if (buttonSpace == null) {
            JClassicButton refreshConfigTreeButton = new JClassicButton(createRefreshConfigTreeAction());
            JClassicButton refreshConfigContainerButton = new JClassicButton(createRefreshConfigContainerAction());

            buttonSpace = new ButtonSpace();
            buttonSpace.add(refreshConfigTreeButton);
            buttonSpace.add(Box.createHorizontalStrut(5));
            buttonSpace.add(refreshConfigContainerButton);
            buttonSpace.updateLayout();
        }

        return buttonSpace;
    }

    protected JSecurityAction createRefreshConfigTreeAction() {
        JSecurityAction action = new JSecurityAction(ButtonManager.getHtmlText(ConsoleLocale.getString("refresh_config_tree")), ConsoleIconFactory.getSwingIcon("stereo/refresh_24.png"), ConsoleLocale.getString("refresh_config_tree")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                configTree.reloadRoot(createCategory());
            }
        };

        return action;
    }

    protected JSecurityAction createRefreshConfigContainerAction() {
        JSecurityAction action = new JSecurityAction(ButtonManager.getHtmlText(ConsoleLocale.getString("refresh_config_pane")), ConsoleIconFactory.getSwingIcon("stereo/refresh_24.png"), ConsoleLocale.getString("refresh_config_pane")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                refreshConfigContainer();
            }
        };

        return action;
    }

    protected abstract String createCategory();

    protected abstract JPanel createConfigContainer();

    protected abstract void refreshConfigContainer();

    protected abstract void configTreeNodeSelection(ElementNode elementNode);
}