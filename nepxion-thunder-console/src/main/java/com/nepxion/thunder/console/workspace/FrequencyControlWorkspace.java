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
import com.nepxion.thunder.common.config.ApplicationConfig;
import com.nepxion.thunder.common.config.ServiceConfig;
import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.console.controller.ConfigController;
import com.nepxion.thunder.console.locale.ConsoleLocale;
import com.nepxion.thunder.console.workspace.config.ConfigComboBoxPane;
import com.nepxion.thunder.console.workspace.config.ConfigPane;
import com.nepxion.thunder.console.workspace.config.ConfigWorkspace;

public class FrequencyControlWorkspace extends ConfigWorkspace {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(FrequencyControlWorkspace.class);

    private JPanel configContainer;
    private ConfigPane frequencyRefreshConfigPane;
    private ConfigComboBoxPane serviceConfigComboBoxPane;
    private ConfigPane serviceTokenConfigPane;

    @Override
    protected JPanel createConfigContainer() {
        if (configContainer == null) {
            configContainer = new JPanel();
            configContainer.setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 5));
            configContainer.setBorder(createTitledBorder(ConsoleLocale.getString("frequency_control")));
            configContainer.add(createTitleLabel());
            configContainer.add(createSeparator());
            configContainer.add(createFrequencyRefreshConfigPane());
            configContainer.add(createSeparator());
            configContainer.add(createServiceConfigComboBoxPane());
            configContainer.add(createServiceTokenConfigPane());
        }

        return configContainer;
    }

    private ConfigPane createFrequencyRefreshConfigPane() {
        if (frequencyRefreshConfigPane == null) {
            frequencyRefreshConfigPane = new ConfigPane(ConsoleLocale.getString("frequency_refresh_label"), ConsoleLocale.getString("frequency_refresh_description")) {
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
                        ConfigController.modifyApplicationFrequency(protocolType, applicationEntity, value);
                    } catch (Exception e) {
                        ExceptionTracer.traceException(HandleManager.getFrame(FrequencyControlWorkspace.this), ConsoleLocale.getString("modify_frequency_exception"), e);
                    }
                }
            };
        }

        return frequencyRefreshConfigPane;
    }

    private ConfigComboBoxPane createServiceConfigComboBoxPane() {
        if (serviceConfigComboBoxPane == null) {
            serviceConfigComboBoxPane = new ConfigComboBoxPane(ConsoleLocale.getString("service_list_label")) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void itemSelection(ElementNode elementNode) {
                    serviceTokenConfigPane.avaliable(elementNode != null);
                    if (elementNode != null) {
                        service = elementNode.getText();

                        try {
                            ServiceConfig serviceConfig = ConfigController.getServiceConfig(protocolType, service, applicationEntity);
                            serviceTokenConfigPane.setText(String.valueOf(serviceConfig.getToken()));
                        } catch (Exception e) {
                            ExceptionTracer.traceException(HandleManager.getFrame(FrequencyControlWorkspace.this), ConsoleLocale.getString("get_service_config_exception"), e);
                        }
                    }
                }
            };
        }

        return serviceConfigComboBoxPane;
    }

    private ConfigPane createServiceTokenConfigPane() {
        if (serviceTokenConfigPane == null) {
            serviceTokenConfigPane = new ConfigPane(ConsoleLocale.getString("max_token_label"), ConsoleLocale.getString("max_token_description")) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void save() {
                    String text = getText();

                    long value;
                    try {
                        value = Long.parseLong(text);
                        if (value < 0) {
                            showError(ConsoleLocale.getString("not_less_than_zero"));

                            return;
                        }
                    } catch (NumberFormatException e) {
                        showError(ConsoleLocale.getString("invalid_input"));

                        return;
                    }

                    try {
                        ConfigController.modifyServiceToken(protocolType, service, applicationEntity, value);
                    } catch (Exception e) {
                        ExceptionTracer.traceException(HandleManager.getFrame(FrequencyControlWorkspace.this), ConsoleLocale.getString("modify_service_token_exception"), e);
                    }
                }
            };
        }

        return serviceTokenConfigPane;
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

        try {
            ApplicationConfig applicationConfig = ConfigController.getApplicationConfig(protocolType, applicationEntity);
            frequencyRefreshConfigPane.setText(String.valueOf(applicationConfig.getFrequency()));
        } catch (Exception e) {
            ExceptionTracer.traceException(HandleManager.getFrame(FrequencyControlWorkspace.this), ConsoleLocale.getString("get_application_config_exception"), e);
        }

        ElementNode[] serviceNodes = null;
        try {
            serviceNodes = ConfigController.getServiceNodes(applicationEntity, protocolType);
        } catch (Exception e) {
            LOG.warn("Get service list failed, protocol={}, application={}, group={}", protocolType, applicationEntity.getApplication(), applicationEntity.getGroup());
        }
        serviceConfigComboBoxPane.setData(serviceNodes);
    }
}