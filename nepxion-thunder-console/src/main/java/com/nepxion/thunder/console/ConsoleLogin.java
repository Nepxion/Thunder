package com.nepxion.thunder.console;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JPanel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.dialog.JLoginDialog;
import com.nepxion.swing.exception.ExceptionTracer;
import com.nepxion.swing.font.FontContext;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.locale.SwingLocale;
import com.nepxion.swing.optionpane.JBasicOptionPane;
import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.entity.UserEntity;
import com.nepxion.thunder.common.entity.UserFactory;
import com.nepxion.thunder.common.entity.UserOperation;
import com.nepxion.thunder.console.context.CacheContext;
import com.nepxion.thunder.console.context.PropertiesContext;
import com.nepxion.thunder.console.context.RegistryContext;
import com.nepxion.thunder.console.context.UserContext;
import com.nepxion.thunder.console.controller.UserController;
import com.nepxion.thunder.console.locale.ConsoleLocale;
import com.nepxion.thunder.registry.zookeeper.ZookeeperUserWatcherCallback;
import com.nepxion.util.locale.LocaleContext;

public class ConsoleLogin extends JLoginDialog {
    private static final long serialVersionUID = 1L;

    private JBasicLabel registryCenterLabel;
    private JBasicComboBox registryCenterComboBox;

    public ConsoleLogin(Frame parent) {
        super(parent);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initEditorPanelLayout() {
        registryCenterLabel = new JBasicLabel();
        registryCenterLabel.setFont(new Font(FontContext.getFontName(), FONT_STYLE, FONT_SIZE));
        accountLabel.setFont(new Font(FontContext.getFontName(), FONT_STYLE, FONT_SIZE));

        String registryAddress = null;
        try {
            registryAddress = PropertiesContext.getRegistryAddress();
        } catch (Exception e) {

        }

        Vector<String> registryAddresses = new Vector<String>();
        if (StringUtils.isNotEmpty(registryAddress)) {
            registryAddresses.add(registryAddress);
        }

        registryCenterComboBox = new JBasicComboBox(registryAddresses);
        registryCenterComboBox.setEditable(true);
        registryCenterComboBox.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("rawtypes")
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                setToolTipText(value.toString());

                return this;
            }
        });

        double[][] size = {
                { 80, 180 },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }
        };

        TableLayout tableLayout = new TableLayout(size);
        tableLayout.setVGap(10);

        editorPanel = new JPanel();
        editorPanel.setLayout(tableLayout);
        editorPanel.add(registryCenterLabel, "0, 0");
        editorPanel.add(registryCenterComboBox, "1, 0");
        editorPanel.add(accountLabel, "0, 1");
        editorPanel.add(accountTextField, "1, 1");
        editorPanel.add(passwordLabel, "0, 2");
        editorPanel.add(passwordField, "1, 2");
        editorPanel.add(localeLabel, "0, 3");
        editorPanel.add(localeComboBox, "1, 3");
    }

    @Override
    protected void initLocale(Locale locale) {
        super.initLocale(locale);

        registryCenterLabel.setText(ConsoleLocale.getString("registry", locale));
    }

    @Override
    public boolean login(String account, String password, Locale locale) throws Exception {
        validateRegistry();

        return validateUser(account, password, locale);
    }

    private void validateRegistry() throws Exception {
        Object registryAddress = registryCenterComboBox.getSelectedItem();
        if (registryAddress == null || StringUtils.isEmpty(registryAddress.toString().trim())) {
            throw new IllegalArgumentException("Registry address can't be null");
        }

        PropertiesContext.setRegistryAddress(registryAddress.toString().trim());

        RegistryContext.start();

        UserEntity userEntity = null;
        try {
            userEntity = UserController.getUser(ThunderConstants.USER_ADMIN_NAME);
        } catch (Exception e) {

        }

        if (userEntity == null) {
            userEntity = UserFactory.createAdministrator();
            UserController.createUser(userEntity);
        }
    }

    private boolean validateUser(String account, String password, Locale locale) throws Exception {
        UserEntity userEntity = null;
        try {
            userEntity = UserController.validateUser(account, password);
        } catch (Exception e) {

        }

        if (userEntity != null) {
            UserContext.setUserEntity(userEntity);
            LocaleContext.registerLocale(locale);

            addUserWatcher(userEntity);
        }

        return userEntity != null;
    }

    private void addUserWatcher(final UserEntity oldUserEntity) {
        try {
            UserController.addUserWatcher(oldUserEntity, new ZookeeperUserWatcherCallback<UserEntity>() {
                @Override
                public void onUserDeleted() {
                    JBasicOptionPane.showMessageDialog(ConsoleLogin.this, ConsoleLocale.getString("user_deleted_message"), SwingLocale.getString("information"), JBasicOptionPane.INFORMATION_MESSAGE);

                    destroy();
                }

                @Override
                public void onUserChanged(UserEntity newUserEntity) {
                    List<UserOperation> oldOperations = oldUserEntity.getOperations();
                    List<UserOperation> newOperations = newUserEntity.getOperations();

                    if (!oldUserEntity.validatePassword(newUserEntity)) {
                        JBasicOptionPane.showMessageDialog(ConsoleLogin.this, ConsoleLocale.getString("user_password_changed_message"), SwingLocale.getString("information"), JBasicOptionPane.INFORMATION_MESSAGE);

                        destroy();
                    }

                    if (!CollectionUtils.isEqualCollection(oldOperations, newOperations)) {
                        JBasicOptionPane.showMessageDialog(ConsoleLogin.this, ConsoleLocale.getString("user_operation_changed_message"), SwingLocale.getString("information"), JBasicOptionPane.INFORMATION_MESSAGE);

                        destroy();
                    }
                }
            });
        } catch (Exception e) {
            ExceptionTracer.traceException(this, ConsoleLocale.getString("add_user_watcher_exception"), e);
        }
    }

    @Override
    public void destroy() {
        RegistryContext.stop();
        CacheContext.stop();

        super.destroy();
    }

    public void launch() {
        setVisible(true);
        toFront();
    }
}