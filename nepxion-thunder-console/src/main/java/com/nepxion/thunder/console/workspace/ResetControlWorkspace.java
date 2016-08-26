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

import javax.swing.Box;

import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.ButtonManager;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.exception.ExceptionTracer;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.console.controller.ConfigController;
import com.nepxion.thunder.console.icon.ConsoleIconFactory;
import com.nepxion.thunder.console.locale.ConsoleLocale;
import com.nepxion.thunder.console.workspace.common.ButtonSpace;
import com.nepxion.thunder.console.workspace.common.Workspace;

public class ResetControlWorkspace extends Workspace {
    private static final long serialVersionUID = 1L;

    private ButtonSpace buttonSpace;

    public ResetControlWorkspace() {
        super();

        double[][] size = {
                { TableLayout.PREFERRED },
                { TableLayout.PREFERRED }
        };

        setLayout(new TableLayout(size));
        add(createButtonSpace(), "0, 0");
    }

    private ButtonSpace createButtonSpace() {
        if (buttonSpace == null) {
            buttonSpace = new ButtonSpace();

            for (ProtocolType protocolType : ProtocolType.values()) {
                JClassicButton resetAllButton = new JClassicButton(createResetAllAction(protocolType));
                buttonSpace.add(resetAllButton);
                buttonSpace.add(Box.createHorizontalStrut(5));
            }

            buttonSpace.updateLayout();
        }

        return buttonSpace;
    }

    private JSecurityAction createResetAllAction(final ProtocolType protocolType) {
        JSecurityAction action = new JSecurityAction(ButtonManager.getHtmlText(ConsoleLocale.getString("reset_all") + "\n" + protocolType), ConsoleIconFactory.getSwingIcon("stereo/redo_24.png"), ButtonManager.getHtmlText(ConsoleLocale.getString("reset_all") + "\n" + protocolType)) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                try {
                    ConfigController.resetAll(protocolType);
                } catch (Exception ex) {
                    ExceptionTracer.traceException(HandleManager.getFrame(ResetControlWorkspace.this), ConsoleLocale.getString("reset_all_exception"), ex);
                }
            }
        };

        return action;
    }
}