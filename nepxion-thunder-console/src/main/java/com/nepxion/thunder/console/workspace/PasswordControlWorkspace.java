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

import java.awt.event.ActionEvent;

import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.ButtonManager;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.locale.SwingLocale;
import com.nepxion.swing.optionpane.JBasicOptionPane;
import com.nepxion.swing.panel.JPasswordPanel;
import com.nepxion.thunder.common.entity.UserEntity;
import com.nepxion.thunder.console.context.UserContext;
import com.nepxion.thunder.console.controller.UserController;
import com.nepxion.thunder.console.icon.ConsoleIconFactory;
import com.nepxion.thunder.console.locale.ConsoleLocale;
import com.nepxion.thunder.console.workspace.common.ButtonSpace;
import com.nepxion.thunder.console.workspace.common.Workspace;

public class PasswordControlWorkspace extends Workspace {
    private static final long serialVersionUID = 1L;

    private JPasswordPanel passwordPane;
    private ButtonSpace buttonSpace;

    public PasswordControlWorkspace() {
        super();

        double[][] size = {
                { TableLayout.PREFERRED },
                { TableLayout.PREFERRED, TableLayout.PREFERRED }
        };

        setLayout(new TableLayout(size));
        add(createPasswordPane(), "0, 0");
        add(createButtonSpace(), "0, 1");
    }

    private JPasswordPanel createPasswordPane() {
        if (passwordPane == null) {
            passwordPane = new JPasswordPanel();
            passwordPane.setBorder(createTitledBorder(ConsoleLocale.getString("password_control")));
        }

        return passwordPane;
    }

    private ButtonSpace createButtonSpace() {
        if (buttonSpace == null) {
            buttonSpace = new ButtonSpace();

            JClassicButton modifyPasswordButton = new JClassicButton(createModifyPasswordAction());
            buttonSpace.add(modifyPasswordButton);

            buttonSpace.updateLayout();
        }

        return buttonSpace;
    }

    protected JSecurityAction createModifyPasswordAction() {
        JSecurityAction action = new JSecurityAction(ButtonManager.getHtmlText(ConsoleLocale.getString("modify_password")), ConsoleIconFactory.getSwingIcon("stereo/control_go_24.png"), ConsoleLocale.getString("modify_password")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                boolean verified = passwordPane.verify();
                if (verified) {
                    String oldPassword = passwordPane.getOldPasswordTextField().getPasswordText().trim();
                    UserEntity userEntity = UserContext.getUserEntity();
                    if (!userEntity.validatePassword(oldPassword)) {
                        JBasicOptionPane.showMessageDialog(HandleManager.getFrame(PasswordControlWorkspace.this), SwingLocale.getString("old_password_not_correct"), SwingLocale.getString("error"), JBasicOptionPane.ERROR_MESSAGE);

                        return;
                    }

                    String newPassword = passwordPane.getNewPasswordTextField().getPasswordText().trim();
                    userEntity.setPassword(newPassword);

                    try {
                        UserController.createUser(userEntity);

                        JBasicOptionPane.showMessageDialog(HandleManager.getFrame(PasswordControlWorkspace.this), SwingLocale.getString("modify_password_success"), SwingLocale.getString("information"), JBasicOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JBasicOptionPane.showMessageDialog(HandleManager.getFrame(PasswordControlWorkspace.this), SwingLocale.getString("modify_password_failure"), SwingLocale.getString("error"), JBasicOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };

        return action;
    }
}