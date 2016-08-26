package com.nepxion.thunder.console.toggle;

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

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.nepxion.swing.element.IElementNode;
import com.nepxion.thunder.console.workspace.DeploySummaryWorkspace;
import com.nepxion.thunder.console.workspace.FrequencyControlWorkspace;
import com.nepxion.thunder.console.workspace.PasswordControlWorkspace;
import com.nepxion.thunder.console.workspace.RemoteConfigControlWorkspace;
import com.nepxion.thunder.console.workspace.ResetControlWorkspace;
import com.nepxion.thunder.console.workspace.SecretKeyControlWorkspace;
import com.nepxion.thunder.console.workspace.TraceControlWorkspace;
import com.nepxion.thunder.console.workspace.UserControlWorkspace;
import com.nepxion.thunder.console.workspace.VersionControlWorkspace;

public class ConsoleToggleSpace extends JPanel {
    private static final long serialVersionUID = 1L;

    private IElementNode listElementNode;
    private JPanel blankPane = new JPanel();

    public ConsoleToggleSpace(IElementNode listElementNode) {
        this.listElementNode = listElementNode;

        setLayout(new BorderLayout());
        add(createContentPane(), BorderLayout.CENTER);
    }

    private JComponent createContentPane() {
        JComponent contentPane = null;

        String name = listElementNode.getName();
        if (name.equals(ConsoleToggleConstants.DEPLOY_SUMMARY)) {
            contentPane = new DeploySummaryWorkspace();
        } else if (name.equals(ConsoleToggleConstants.REMOTE_CONFIG)) {
            contentPane = new RemoteConfigControlWorkspace();
        } else if (name.equals(ConsoleToggleConstants.INVOKE_TRACE)) {
            contentPane = new TraceControlWorkspace();
        } else if (name.equals(ConsoleToggleConstants.FREQUENCY_CONTROL)) {
            contentPane = new FrequencyControlWorkspace();
        } else if (name.equals(ConsoleToggleConstants.SECRET_KEY_CONTROL)) {
            contentPane = new SecretKeyControlWorkspace();
        } else if (name.equals(ConsoleToggleConstants.VERSION_CONTROL)) {
            contentPane = new VersionControlWorkspace();
        } else if (name.equals(ConsoleToggleConstants.RESET_CONTROL)) {
            contentPane = new ResetControlWorkspace();
        } else if (name.equals(ConsoleToggleConstants.USER_CONTROL)) {
            contentPane = new UserControlWorkspace();
        } else if (name.equals(ConsoleToggleConstants.PASSWORD_CONTROL)) {
            contentPane = new PasswordControlWorkspace();
        } else {
            contentPane = blankPane;
        }

        return contentPane;
    }
}