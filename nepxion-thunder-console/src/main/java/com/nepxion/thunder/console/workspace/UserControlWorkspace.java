package com.nepxion.thunder.console.workspace;

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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.container.ContainerManager;
import com.nepxion.swing.element.ElementNode;
import com.nepxion.swing.exception.ExceptionTracer;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.list.BasicListModel;
import com.nepxion.swing.list.JBasicList;
import com.nepxion.swing.locale.SwingLocale;
import com.nepxion.swing.optionpane.JBasicOptionPane;
import com.nepxion.swing.renderer.combobox.ElementComboBoxCellRenderer;
import com.nepxion.swing.renderer.list.ElementListCellRenderer;
import com.nepxion.swing.scrollpane.JBasicScrollPane;
import com.nepxion.swing.selector.checkbox.JCheckBoxListPanel;
import com.nepxion.swing.textfield.JBasicTextField;
import com.nepxion.thunder.common.entity.UserEntity;
import com.nepxion.thunder.common.entity.UserFactory;
import com.nepxion.thunder.common.entity.UserOperation;
import com.nepxion.thunder.common.entity.UserType;
import com.nepxion.thunder.console.context.UserContext;
import com.nepxion.thunder.console.controller.UserController;
import com.nepxion.thunder.console.icon.ConsoleIconFactory;
import com.nepxion.thunder.console.locale.ConsoleLocale;
import com.nepxion.thunder.console.workspace.common.ButtonSpace;
import com.nepxion.thunder.console.workspace.common.Workspace;

public class UserControlWorkspace extends Workspace {
    private static final long serialVersionUID = 1L;

    private JPanel container;
    private JPanel blankPane;

    private JBasicLabel titleLabel;
    private ButtonSpace buttonSpace;
    private JBasicList userList;

    private JPanel userPropertyContainer;
    private UserPropertyPane userPropertyPane;

    public UserControlWorkspace() {
        super();

        container = new JPanel();
        container.setLayout(new BorderLayout());

        blankPane = new JPanel();
        container.add(blankPane, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setLeftComponent(new JBasicScrollPane(createUserList()));
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

    private JBasicLabel createTitleLabel() {
        if (titleLabel == null) {
            titleLabel = new JBasicLabel();
        }

        return titleLabel;
    }

    @SuppressWarnings("unchecked")
    private JBasicList createUserList() {
        if (userList == null) {
            userList = new JBasicList() {
                private static final long serialVersionUID = 1L;

                @Override
                public void executeSelection(int oldSelectedRow, int newSelectedRow) {
                    container.removeAll();
                    if (newSelectedRow != -1) {
                        ElementNode userEntityNode = (ElementNode) userList.getModel().getElementAt(newSelectedRow);
                        UserEntity userEntity = (UserEntity) userEntityNode.getUserObject();
                        try {
                            userEntity = UserController.getUser(userEntity.getName());
                        } catch (Exception e) {
                            ExceptionTracer.traceException(HandleManager.getFrame(this), ConsoleLocale.getString("get_user_info_exception"), e);

                            return;
                        }
                        container.add(createUserPropertyContainer(), BorderLayout.CENTER);
                        userPropertyPane.setUser(userEntity);

                        if (titleLabel != null) {
                            UserType type = userEntity.getType();
                            String typeName = UserController.getUserTypeName(type);
                            titleLabel.setText(typeName);
                        }
                    } else {
                        container.add(blankPane, BorderLayout.CENTER);
                    }

                    ContainerManager.update(container);
                }
            };
            userList.setFixedCellHeight(28);
            userList.setCellRenderer(new ElementListCellRenderer());
            userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            updateModel();
        }

        return userList;
    }

    private JPanel createUserPropertyContainer() {
        if (userPropertyContainer == null) {
            JPanel layoutPane = new JPanel();
            layoutPane.setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 5));
            layoutPane.add(createTitleLabel());
            layoutPane.add(createSeparator());

            userPropertyContainer = new JPanel();
            userPropertyContainer.setBorder(createTitledBorder(ConsoleLocale.getString("user_control")));
            userPropertyContainer.setLayout(new BorderLayout());
            userPropertyContainer.add(layoutPane, BorderLayout.NORTH);
            userPropertyContainer.add(createUserPropertyPane(), BorderLayout.CENTER);
        }

        return userPropertyContainer;
    }

    private ButtonSpace createButtonSpace() {
        if (buttonSpace == null) {
            JClassicButton addUserButton = new JClassicButton(createAddUserAction());
            JClassicButton deleteUserButton = new JClassicButton(createDeleteUserAction());
            JClassicButton refreshUserButton = new JClassicButton(createRefreshUserAction());
            JClassicButton saveUserButton = new JClassicButton(createSaveUserAction());

            buttonSpace = new ButtonSpace();
            buttonSpace.add(addUserButton);
            buttonSpace.add(Box.createHorizontalStrut(5));
            buttonSpace.add(deleteUserButton);
            buttonSpace.add(Box.createHorizontalStrut(5));
            buttonSpace.add(refreshUserButton);
            buttonSpace.add(Box.createHorizontalStrut(5));
            buttonSpace.add(saveUserButton);
            buttonSpace.updateLayout();
        }

        return buttonSpace;
    }

    private UserPropertyPane createUserPropertyPane() {
        if (userPropertyPane == null) {
            userPropertyPane = new UserPropertyPane();
        }

        return userPropertyPane;
    }

    private class UserPropertyPane extends JPanel {
        private static final long serialVersionUID = 1L;

        private JBasicTextField nameTextField = new JBasicTextField();
        private JBasicTextField passwordTextField = new JBasicTextField();
        private JBasicComboBox typeComboBox = new JBasicComboBox();
        private JCheckBoxListPanel operationCheckBoxListPanel = new JCheckBoxListPanel();

        @SuppressWarnings("unchecked")
        public UserPropertyPane() {
            typeComboBox = new JBasicComboBox();
            typeComboBox.setRenderer(new ElementComboBoxCellRenderer());

            operationCheckBoxListPanel.getList().setSelectionForeground(Color.white);

            double[][] size = {
                    { TableLayout.PREFERRED, TableLayout.FILL },
                    { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }
            };

            TableLayout tableLayout = new TableLayout(size);
            tableLayout.setHGap(5);
            tableLayout.setVGap(5);

            setLayout(tableLayout);
            add(new JBasicLabel(SwingLocale.getString("account")), "0, 0");
            add(nameTextField, "1, 0");
            add(new JBasicLabel(SwingLocale.getString("password")), "0, 1");
            add(passwordTextField, "1, 1");
            add(new JBasicLabel(ConsoleLocale.getString("type")), "0, 2");
            add(typeComboBox, "1, 2");
            add(new JBasicLabel(ConsoleLocale.getString("operation")), "0, 3");
            add(operationCheckBoxListPanel, "1, 3");
            setPreferredSize(new Dimension(300, getPreferredSize().height));
        }

        @SuppressWarnings("unchecked")
        public void initializeUser() {
            UserEntity userEntity = UserContext.getUserEntity();
            UserType type = userEntity.getType();

            DefaultComboBoxModel<ElementNode> comboBoxModel = new DefaultComboBoxModel<ElementNode>(UserController.getNewUserTypeNodes(type));
            typeComboBox.setModel(comboBoxModel);
            typeComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ElementNode elementNode = (ElementNode) typeComboBox.getSelectedItem();
                    UserType type = (UserType) elementNode.getUserObject();
                    operationCheckBoxListPanel.setAllElementNodes(Arrays.asList(UserController.getUserOperationNodes(type)));
                }
            });
            typeComboBox.setSelectedIndex(0);
        }

        @SuppressWarnings("unchecked")
        public void setUser(UserEntity userEntity) {
            String name = userEntity.getName();
            UserType type = userEntity.getType();

            boolean isForbidden = isForbidden(userEntity, Action.SAVE);
            nameTextField.setText(name);
            nameTextField.setEditable(false);
            nameTextField.setBackground(Color.LIGHT_GRAY);
            passwordTextField.setText(!isForbidden ? "" : ConsoleLocale.getString("no_permission_view"));
            passwordTextField.setEditable(!isForbidden);
            passwordTextField.setBackground(!isForbidden ? UIManager.getColor("TextField.background") : Color.LIGHT_GRAY);
            DefaultComboBoxModel<ElementNode> comboBoxModel = new DefaultComboBoxModel<ElementNode>(UserController.getUserTypeNodes(type));
            typeComboBox.setModel(comboBoxModel);
            operationCheckBoxListPanel.setAllElementNodes(Arrays.asList(UserController.getUserOperationNodes(userEntity)));
        }

        @SuppressWarnings("unchecked")
        public UserEntity createUser() throws Exception {
            String name = nameTextField.getText().trim();
            if (StringUtils.isEmpty(name)) {
                throw new IllegalArgumentException("User name can't be empty");
            }

            String password = passwordTextField.getText().trim();
            if (StringUtils.isEmpty(password)) {
                throw new IllegalArgumentException("User password can't be empty");
            }

            ElementNode typeNode = (ElementNode) typeComboBox.getSelectedItem();
            UserType type = (UserType) typeNode.getUserObject();

            List<UserOperation> operations = operationCheckBoxListPanel.getSelectedUserObjects();

            UserEntity userEntity = new UserEntity();
            userEntity.setName(name);
            userEntity.setPassword(password);
            userEntity.setType(type);
            userEntity.setOperations(operations);

            return userEntity;
        }
    }

    @SuppressWarnings("unchecked")
    private void updateModel() {
        ElementNode[] userEntityNodes = null;
        try {
            userEntityNodes = UserController.getUserNodes();
        } catch (Exception e) {
            ExceptionTracer.traceException(HandleManager.getFrame(this), ConsoleLocale.getString("get_user_list_exception"), e);
        }

        if (userEntityNodes == null) {
            return;
        }

        String selectedName = null;
        ElementNode selectedNode = (ElementNode) userList.getSelectedValue();
        if (selectedNode != null) {
            selectedName = selectedNode.getText();
        } else {
            selectedName = UserContext.getUserEntity().getName();
        }

        userList.setModel(new BasicListModel(new Vector<ElementNode>(Arrays.asList(userEntityNodes))));

        int selectedIndex = getSelectedIndex(userEntityNodes, selectedName);
        userList.setSelectedIndex(selectedIndex);
    }

    private int getSelectedIndex(ElementNode[] nodes, String name) {
        for (int i = 0; i < nodes.length; i++) {
            String text = nodes[i].getText();
            if (StringUtils.equals(text, name)) {
                return i;
            }
        }

        return 0;
    }

    private boolean isForbidden(UserEntity selectedUserEntity, Action action) {
        String selectedName = selectedUserEntity.getName();
        UserType selectedType = selectedUserEntity.getType();
        int selectedTypeValue = UserFactory.getUserCompareValue(selectedType);

        UserEntity loginedUserEntity = UserContext.getUserEntity();
        String loginedName = loginedUserEntity.getName();
        UserType loginedType = loginedUserEntity.getType();
        int loginedTypeValue = UserFactory.getUserCompareValue(loginedType);

        switch (action) {
            case DELETE:
                return selectedTypeValue <= loginedTypeValue;
            case SAVE:
                return selectedTypeValue <= loginedTypeValue && !StringUtils.equals(selectedName, loginedName);
        }

        return true;
    }

    private JSecurityAction createAddUserAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocale.getString("add_user"), ConsoleIconFactory.getSwingIcon("stereo/add_24.png"), ConsoleLocale.getString("add_user")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                UserPropertyPane userPropertyPane = new UserPropertyPane();
                userPropertyPane.initializeUser();
                int selectedValue = JBasicOptionPane.showOptionDialog(HandleManager.getFrame(UserControlWorkspace.this), userPropertyPane, ConsoleLocale.getString("add_user"), JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/user.png"), new Object[] { SwingLocale.getString("yes"), SwingLocale.getString("no") }, null, true);
                if (selectedValue == 0) {
                    try {
                        UserEntity userEntity = userPropertyPane.createUser();
                        String name = userEntity.getName();

                        UserEntity existUserEntity = null;
                        try {
                            existUserEntity = UserController.getUser(name);
                        } catch (Exception ex) {

                        }

                        if (existUserEntity != null) {
                            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(UserControlWorkspace.this), ConsoleLocale.getString("user_exist"), SwingLocale.getString("error"), JBasicOptionPane.ERROR_MESSAGE);

                            return;
                        }

                        UserController.createUser(userEntity);

                        updateModel();

                        JBasicOptionPane.showMessageDialog(HandleManager.getFrame(UserControlWorkspace.this), ConsoleLocale.getString("add_user_success"), SwingLocale.getString("information"), JBasicOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        ExceptionTracer.traceException(HandleManager.getFrame(UserControlWorkspace.this), ConsoleLocale.getString("add_user_exception"), ex);
                    }
                }
            }
        };

        return action;
    }

    private JSecurityAction createDeleteUserAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocale.getString("delete_user"), ConsoleIconFactory.getSwingIcon("stereo/delete_24.png"), ConsoleLocale.getString("delete_user")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                Object selectedObject = userList.getSelectedValue();
                if (selectedObject == null) {
                    return;
                }

                ElementNode selectedElementNode = (ElementNode) selectedObject;
                UserEntity selectedUserEntity = (UserEntity) selectedElementNode.getUserObject();

                boolean isForbidden = isForbidden(selectedUserEntity, Action.DELETE);
                if (isForbidden) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(UserControlWorkspace.this), ConsoleLocale.getString("no_permission_delete_user"), SwingLocale.getString("error"), JBasicOptionPane.ERROR_MESSAGE);

                    return;
                }

                int selectedValue = JBasicOptionPane.showConfirmDialog(UserControlWorkspace.this, ConsoleLocale.getString("confirm_to_delete_user"), SwingLocale.getString("confirm"), JBasicOptionPane.YES_NO_OPTION);
                if (selectedValue != JBasicOptionPane.YES_OPTION) {
                    return;
                }

                try {
                    UserController.deleteUser(selectedUserEntity);

                    updateModel();

                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(UserControlWorkspace.this), ConsoleLocale.getString("delete_user_success"), SwingLocale.getString("information"), JBasicOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    updateModel();

                    ExceptionTracer.traceException(HandleManager.getFrame(UserControlWorkspace.this), ConsoleLocale.getString("delete_user_exception"), ex);
                }
            }
        };

        return action;
    }

    private JSecurityAction createRefreshUserAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocale.getString("refresh_user"), ConsoleIconFactory.getSwingIcon("stereo/refresh_24.png"), ConsoleLocale.getString("refresh_user")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                updateModel();
            }
        };

        return action;
    }

    private JSecurityAction createSaveUserAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocale.getString("save_user"), ConsoleIconFactory.getSwingIcon("stereo/control_go_24.png"), ConsoleLocale.getString("save_user")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                Object selectedObject = userList.getSelectedValue();
                if (selectedObject == null) {
                    return;
                }

                try {
                    UserEntity userEntity = userPropertyPane.createUser();

                    boolean isForbidden = isForbidden(userEntity, Action.SAVE);
                    if (isForbidden) {
                        JBasicOptionPane.showMessageDialog(HandleManager.getFrame(UserControlWorkspace.this), ConsoleLocale.getString("no_permission_save_user"), SwingLocale.getString("error"), JBasicOptionPane.ERROR_MESSAGE);

                        return;
                    }

                    UserController.createUser(userEntity);

                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(UserControlWorkspace.this), ConsoleLocale.getString("save_user_success"), SwingLocale.getString("information"), JBasicOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    ExceptionTracer.traceException(HandleManager.getFrame(UserControlWorkspace.this), ConsoleLocale.getString("save_user_exception"), ex);
                }
            }
        };

        return action;
    }

    private enum Action {
        DELETE,
        SAVE
    }
}