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

public class SecretKeyControlWorkspace extends ConfigWorkspace {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(SecretKeyControlWorkspace.class);

    private JPanel configContainer;
    private ConfigComboBoxPane serviceConfigComboBoxPane;
    private ConfigPane serviceSecretKeyConfigPane;
    private ConfigComboBoxPane referenceConfigComboBoxPane;
    private ConfigPane referenceSecretKeyConfigPane;

    @Override
    protected JPanel createConfigContainer() {
        if (configContainer == null) {
            configContainer = new JPanel();
            configContainer.setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 5));
            configContainer.setBorder(createTitledBorder(ConsoleLocale.getString("secret_key_control")));
            configContainer.add(createTitleLabel());
            configContainer.add(createSeparator());
            configContainer.add(createServiceConfigComboBoxPane());
            configContainer.add(createServiceSecretKeyConfigPane());
            configContainer.add(createSeparator());
            configContainer.add(createReferenceConfigComboBoxPane());
            configContainer.add(createReferenceSecretKeyConfigPane());
        }

        return configContainer;
    }

    private ConfigComboBoxPane createServiceConfigComboBoxPane() {
        if (serviceConfigComboBoxPane == null) {
            serviceConfigComboBoxPane = new ConfigComboBoxPane(ConsoleLocale.getString("service_list_label")) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void itemSelection(ElementNode elementNode) {
                    serviceSecretKeyConfigPane.avaliable(elementNode != null);
                    if (elementNode != null) {
                        service = elementNode.getText();

                        try {
                            ServiceConfig serviceConfig = ConfigController.getServiceConfig(protocolType, service, applicationEntity);
                            serviceSecretKeyConfigPane.setText(serviceConfig.getSecretKey());
                        } catch (Exception e) {
                            ExceptionTracer.traceException(HandleManager.getFrame(SecretKeyControlWorkspace.this), ConsoleLocale.getString("get_service_config_exception"), e);
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
                    referenceSecretKeyConfigPane.avaliable(elementNode != null);
                    if (elementNode != null) {
                        reference = elementNode.getText();

                        try {
                            ReferenceConfig referenceConfig = ConfigController.getReferenceConfig(protocolType, reference, applicationEntity);
                            referenceSecretKeyConfigPane.setText(referenceConfig.getSecretKey());
                        } catch (Exception e) {
                            ExceptionTracer.traceException(HandleManager.getFrame(SecretKeyControlWorkspace.this), ConsoleLocale.getString("get_reference_config_exception"), e);
                        }
                    }
                }
            };
        }

        return referenceConfigComboBoxPane;
    }

    private ConfigPane createServiceSecretKeyConfigPane() {
        if (serviceSecretKeyConfigPane == null) {
            serviceSecretKeyConfigPane = new ConfigPane(ConsoleLocale.getString("secret_key_label"), ConsoleLocale.getString("secret_key_description")) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void save() {
                    String text = getText();

                    try {
                        ConfigController.modifyServiceSecretKey(protocolType, service, applicationEntity, text);
                    } catch (Exception e) {
                        ExceptionTracer.traceException(HandleManager.getFrame(SecretKeyControlWorkspace.this), ConsoleLocale.getString("modify_service_secretkey_exception"), e);
                    }
                }
            };
        }

        return serviceSecretKeyConfigPane;
    }

    private ConfigPane createReferenceSecretKeyConfigPane() {
        if (referenceSecretKeyConfigPane == null) {
            referenceSecretKeyConfigPane = new ConfigPane(ConsoleLocale.getString("secret_key_label"), ConsoleLocale.getString("secret_key_description")) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void save() {
                    String text = getText();

                    try {
                        ConfigController.modifyReferenceSecretKey(protocolType, reference, applicationEntity, text);
                    } catch (Exception e) {
                        ExceptionTracer.traceException(HandleManager.getFrame(SecretKeyControlWorkspace.this), ConsoleLocale.getString("modify_reference_secretkey_exception"), e);
                    }
                }
            };
        }

        return referenceSecretKeyConfigPane;
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