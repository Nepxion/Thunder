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
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.ButtonManager;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.element.ElementNode;
import com.nepxion.swing.exception.ExceptionTracer;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.scrollpane.JBasicScrollPane;
import com.nepxion.swing.searchable.JSearchableContainer;
import com.nepxion.swing.searchable.JSearchableFactory;
import com.nepxion.swing.textarea.JBasicTextArea;
import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.console.controller.PropertyController;
import com.nepxion.thunder.console.icon.ConsoleIconFactory;
import com.nepxion.thunder.console.locale.ConsoleLocale;
import com.nepxion.thunder.console.workspace.common.ButtonSpace;
import com.nepxion.thunder.console.workspace.config.ConfigWorkspace;

public class RemoteConfigControlWorkspace extends ConfigWorkspace {
    private static final long serialVersionUID = 1L;

    private JPanel configContainer;
    private JBasicTextArea propertyTextArea;

    public RemoteConfigControlWorkspace() {
        super();
    }

    @Override
    protected ButtonSpace createButtonSpace() {
        ButtonSpace buttonSpace = super.createButtonSpace();
        
        JClassicButton savePropertyButton = new JClassicButton(createSavePropertyAction());

        buttonSpace.add(Box.createHorizontalStrut(5));
        buttonSpace.add(savePropertyButton);
        buttonSpace.updateLayout();
        
        return buttonSpace;
    }

    protected JSecurityAction createSavePropertyAction() {
        JSecurityAction action = new JSecurityAction(ButtonManager.getHtmlText(ConsoleLocale.getString("save_config_pane")), ConsoleIconFactory.getSwingIcon("stereo/control_go_24.png"), ConsoleLocale.getString("save_config_pane")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                if (propertyTextArea == null) {
                    return;
                }
                
                String property = propertyTextArea.getText();
                try {
                    PropertyController.modifyProperty(applicationEntity, property);
                } catch (Exception ex) {
                    ExceptionTracer.traceException(HandleManager.getFrame(RemoteConfigControlWorkspace.this), ConsoleLocale.getString("save_property_exception"), ex);
                }
            }
        };

        return action;
    }

    @Override
    protected JPanel createConfigContainer() {
        if (configContainer == null) {
            JPanel layoutPane = new JPanel();
            layoutPane.setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 5));
            layoutPane.add(createTitleLabel());
            layoutPane.add(createSeparator());

            JSearchableContainer container = new JSearchableContainer();
            container.add(new JBasicScrollPane(createPropertyTextArea()));

            JSearchableFactory.installSearchableBar(propertyTextArea, container, true);
            
            configContainer = new JPanel();
            configContainer.setBorder(createTitledBorder(ConsoleLocale.getString("remote_config")));
            configContainer.setLayout(new BorderLayout());
            configContainer.add(layoutPane, BorderLayout.NORTH);
            configContainer.add(container, BorderLayout.CENTER);
        }

        return configContainer;
    }

    private JBasicTextArea createPropertyTextArea() {
        if (propertyTextArea == null) {
            propertyTextArea = new JBasicTextArea();
        }

        return propertyTextArea;
    }

    @Override
    protected String createCategory() {
        return ThunderConstants.CONFIGURATION_ELEMENT_NAME;
    }

    @Override
    protected void refreshConfigContainer() {
        loadConfig();
    }

    @Override
    protected void configTreeNodeSelection(ElementNode elementNode) {
        String application = elementNode.getText();
        String group = ((ElementNode) elementNode.getParent()).getText();

        applicationEntity = new ApplicationEntity();
        applicationEntity.setApplication(application);
        applicationEntity.setGroup(group);

        loadConfig();
    }

    private void loadConfig() {
        if (configContainer == null || configContainer.getParent() == null) {
            return;
        }

        String property = null;
        try {
            property = PropertyController.getProperty(applicationEntity);
        } catch (Exception e) {
            ExceptionTracer.traceException(HandleManager.getFrame(RemoteConfigControlWorkspace.this), ConsoleLocale.getString("load_property_exception"), e);
        }

        if (StringUtils.isNotEmpty(property)) {
            propertyTextArea.setText(property.trim());
            propertyTextArea.setCaretPosition(0);
        } else {
            propertyTextArea.setText(null);
        }
    }
}