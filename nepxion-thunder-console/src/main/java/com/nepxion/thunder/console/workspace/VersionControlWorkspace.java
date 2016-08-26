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

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.swing.element.ElementNode;
import com.nepxion.swing.exception.ExceptionTracer;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.thunder.common.config.ReferenceConfig;
import com.nepxion.thunder.common.config.ServiceConfig;
import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.console.controller.ConfigController;
import com.nepxion.thunder.console.locale.ConsoleLocale;
import com.nepxion.thunder.console.workspace.config.ConfigComboBoxPane;
import com.nepxion.thunder.console.workspace.config.ConfigPane;
import com.nepxion.thunder.console.workspace.config.ConfigWorkspace;

public class VersionControlWorkspace extends ConfigWorkspace {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(VersionControlWorkspace.class);

    private JPanel configContainer;
    private ConfigComboBoxPane serviceConfigComboBoxPane;
    private ConfigPane serviceVersionConfigPane;
    private ConfigComboBoxPane referenceConfigComboBoxPane;
    private ConfigPane referenceVersionConfigPane;

    @Override
    protected JPanel createConfigContainer() {
        if (configContainer == null) {
            configContainer = new JPanel();
            configContainer.setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 5));
            configContainer.setBorder(createTitledBorder(ConsoleLocale.getString("version_control")));
            configContainer.add(createTitleLabel());
            configContainer.add(createSeparator());
            configContainer.add(createServiceConfigComboBoxPane());
            configContainer.add(createServiceVersionConfigPane());
            configContainer.add(createSeparator());
            configContainer.add(createReferenceConfigComboBoxPane());
            configContainer.add(createReferenceVersionConfigPane());
        }

        return configContainer;
    }

    private ConfigComboBoxPane createServiceConfigComboBoxPane() {
        if (serviceConfigComboBoxPane == null) {
            serviceConfigComboBoxPane = new ConfigComboBoxPane(ConsoleLocale.getString("service_list_label")) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void itemSelection(ElementNode elementNode) {
                    serviceVersionConfigPane.avaliable(elementNode != null);
                    if (elementNode != null) {
                        service = elementNode.getText();

                        try {
                            ServiceConfig serviceConfig = ConfigController.getServiceConfig(protocolType, service, applicationEntity);
                            serviceVersionConfigPane.setText(String.valueOf(serviceConfig.getVersion()));
                        } catch (Exception e) {
                            ExceptionTracer.traceException(HandleManager.getFrame(VersionControlWorkspace.this), ConsoleLocale.getString("get_service_config_exception"), e);
                        }
                    }
                }
            };
        }

        return serviceConfigComboBoxPane;
    }

    private ConfigComboBoxPane createReferenceConfigComboBoxPane() {
        if (referenceConfigComboBoxPane == null) {
            referenceConfigComboBoxPane = new ConfigComboBoxPane(ConsoleLocale.getString("reference_list_label")) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void itemSelection(ElementNode elementNode) {
                    referenceVersionConfigPane.avaliable(elementNode != null);
                    if (elementNode != null) {
                        reference = elementNode.getText();

                        try {
                            ReferenceConfig referenceConfig = ConfigController.getReferenceConfig(protocolType, reference, applicationEntity);
                            referenceVersionConfigPane.setText(String.valueOf(referenceConfig.getVersion()));
                        } catch (Exception e) {
                            ExceptionTracer.traceException(HandleManager.getFrame(VersionControlWorkspace.this), ConsoleLocale.getString("get_reference_config_exception"), e);
                        }
                    }
                }
            };
        }

        return referenceConfigComboBoxPane;
    }

    private ConfigPane createServiceVersionConfigPane() {
        if (serviceVersionConfigPane == null) {
            serviceVersionConfigPane = new ConfigPane(ConsoleLocale.getString("version_label"), ConsoleLocale.getString("version_description")) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void save() {
                    String text = getText();

                    int value;
                    try {
                        value = Integer.parseInt(text);
                        if (value < 0) {
                            showError(ConsoleLocale.getString("not_less_than_zero"));

                            return;
                        }
                    } catch (NumberFormatException e) {
                        showError(ConsoleLocale.getString("invalid_input"));

                        return;
                    }

                    try {
                        ConfigController.modifyServiceVersion(protocolType, service, applicationEntity, value);
                    } catch (Exception e) {
                        ExceptionTracer.traceException(HandleManager.getFrame(VersionControlWorkspace.this), ConsoleLocale.getString("modify_service_version_exception"), e);
                    }
                }
            };
        }

        return serviceVersionConfigPane;
    }

    private ConfigPane createReferenceVersionConfigPane() {
        if (referenceVersionConfigPane == null) {
            referenceVersionConfigPane = new ConfigPane(ConsoleLocale.getString("version_label"), ConsoleLocale.getString("version_description")) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void save() {
                    String text = getText();

                    int value;
                    try {
                        value = Integer.parseInt(text);
                        if (value < 0) {
                            showError(ConsoleLocale.getString("not_less_than_zero"));

                            return;
                        }
                    } catch (NumberFormatException e) {
                        showError(ConsoleLocale.getString("invalid_input"));

                        return;
                    }

                    try {
                        ConfigController.modifyReferenceVersion(protocolType, reference, applicationEntity, value);
                    } catch (Exception e) {
                        ExceptionTracer.traceException(HandleManager.getFrame(VersionControlWorkspace.this), ConsoleLocale.getString("modify_reference_version_exception"), e);
                    }
                }
            };
        }

        return referenceVersionConfigPane;
    }

    @Override
    protected String createCategory() {
        return ThunderConstants.APPLICATION_ELEMENT_NAME;
    }

    @Override
    protected void refreshConfigContainer() {
        loadConfig();
    }

    @Override
    protected void configTreeNodeSelection(ElementNode elementNode) {
        protocolType = ProtocolType.fromString(elementNode.getName());

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

        ElementNode[] serviceNodes = null;
        try {
            serviceNodes = ConfigController.getServiceNodes(applicationEntity, protocolType);
        } catch (Exception e) {
            LOG.warn("Get service list failed, protocol={}, application={}, group={}", protocolType, applicationEntity.getApplication(), applicationEntity.getGroup());
        }
        serviceConfigComboBoxPane.setData(serviceNodes);

        ElementNode[] referenceNodes = null;
        try {
            referenceNodes = ConfigController.getReferenceNodes(applicationEntity, protocolType);
        } catch (Exception e) {
            LOG.warn("Get reference list failed, protocol={}, application={}, group={}", protocolType, applicationEntity.getApplication(), applicationEntity.getGroup());
        }
        referenceConfigComboBoxPane.setData(referenceNodes);
    }
}