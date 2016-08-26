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

import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.locale.SwingLocale;
import com.nepxion.swing.textfield.JBasicTextField;
import com.nepxion.thunder.console.icon.ConsoleIconFactory;

public abstract class ConfigPane extends JPanel {
    private static final long serialVersionUID = 1L;

    private JBasicLabel label;
    private JBasicTextField textField;
    private JClassicButton saveButton;
    private JBasicLabel hintLabel;

    public ConfigPane(String labelText, String hintText) {
        label = new JBasicLabel(labelText);
        textField = new JBasicTextField();
        saveButton = new JClassicButton(createSaveAction());
        hintLabel = new JBasicLabel(hintText, ConsoleIconFactory.getSwingIcon("question_message.png"), SwingConstants.LEADING);

        double[][] size = {
                { 100, TableLayout.FILL, TableLayout.PREFERRED },
                { TableLayout.PREFERRED, TableLayout.PREFERRED }
        };

        TableLayout tableLayout = new TableLayout(size);
        tableLayout.setHGap(0);
        tableLayout.setVGap(5);

        setLayout(tableLayout);
        add(label, "0, 0");
        add(textField, "1, 0");
        add(saveButton, "2, 0");
        add(hintLabel, "1, 1, 2, 1");
    }

    public String getText() {
        return textField.getText().trim();
    }

    public void setText(String text) {
        textField.setText(text);
    }

    public void avaliable(boolean avaliable) {
        if (!avaliable) {
            textField.setText("");
        }
        textField.setEnabled(avaliable);
        saveButton.setEnabled(avaliable);
    }

    public void showError(String error) {
        textField.showTip(error, ConsoleIconFactory.getSwingIcon("error.png"));
    }

    private JSecurityAction createSaveAction() {
        JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("save.png"), SwingLocale.getString("save")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                save();
            }
        };

        return action;
    }

    protected abstract void save();
}