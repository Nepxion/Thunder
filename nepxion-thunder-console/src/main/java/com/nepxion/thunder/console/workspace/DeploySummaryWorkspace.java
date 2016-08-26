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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.ButtonManager;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.list.BasicListModel;
import com.nepxion.swing.list.JBasicList;
import com.nepxion.swing.locale.SwingLocale;
import com.nepxion.swing.optionpane.JBasicOptionPane;
import com.nepxion.swing.scrollpane.JBasicScrollPane;
import com.nepxion.swing.tabbedpane.JBasicTabbedPane;
import com.nepxion.swing.table.BasicTableModel;
import com.nepxion.swing.table.JBasicTable;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.common.entity.SummaryEntity;
import com.nepxion.thunder.console.controller.SummaryController;
import com.nepxion.thunder.console.icon.ConsoleIconFactory;
import com.nepxion.thunder.console.locale.ConsoleLocale;
import com.nepxion.thunder.console.workspace.common.ButtonSpace;
import com.nepxion.thunder.console.workspace.common.Workspace;
import com.nepxion.thunder.console.workspace.topology.DeployTopology;

public class DeploySummaryWorkspace extends Workspace {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(DeploySummaryWorkspace.class);

    private static final String[] SERVICE_TITLES = { ConsoleLocale.getString("table_interface"), ConsoleLocale.getString("table_service_address"), ConsoleLocale.getString("table_protocol"), ConsoleLocale.getString("table_application"), ConsoleLocale.getString("table_group"), ConsoleLocale.getString("table_online_time") };
    private static final String[] REFERENCE_TITLES = { ConsoleLocale.getString("table_interface"), ConsoleLocale.getString("table_reference_address"), ConsoleLocale.getString("table_protocol"), ConsoleLocale.getString("table_application"), ConsoleLocale.getString("table_group"), ConsoleLocale.getString("table_access_time") };
    private static final String[] MONITOR_TITLES = { ConsoleLocale.getString("table_monitor_address") };

    private JBasicTabbedPane tabbedPane;
    private JBasicTable serviceSummaryTable;
    private JBasicTable referenceSummaryTable;
    private JBasicTable monitorSummaryTable;

    private JBasicLabel interfaceLabel;
    private JBasicList methodList;
    private JPanel methodListPane;

    private ButtonSpace buttonSpace;

    public DeploySummaryWorkspace() {
        super();

        tabbedPane = new JBasicTabbedPane();
        tabbedPane.addTab(ConsoleLocale.getString("deploy_topology"), new DeployTopology(), ConsoleLocale.getString("deploy_topology"));
        tabbedPane.addTab(ConsoleLocale.getString("service_list_label"), new JBasicScrollPane(createServiceSummaryTable()), ConsoleLocale.getString("service_list_label"));
        tabbedPane.addTab(ConsoleLocale.getString("reference_list_label"), new JBasicScrollPane(createReferenceSummaryTable()), ConsoleLocale.getString("reference_list_label"));
        tabbedPane.addTab(ConsoleLocale.getString("monitor_list_label"), new JBasicScrollPane(createMonitorSummaryTable()), ConsoleLocale.getString("monitor_list_label"));
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                switch (tabbedPane.getSelectedIndex()) {
                    case 0:
                        buttonSpace.getComponent(0).setVisible(false);
                        buttonSpace.getComponent(1).setVisible(false);
                        buttonSpace.getComponent(2).setVisible(false);
                        buttonSpace.getComponent(3).setVisible(false);
                        buttonSpace.getComponent(4).setVisible(false);
                        break;
                    case 1:
                        buttonSpace.getComponent(0).setVisible(true);
                        buttonSpace.getComponent(1).setVisible(true);
                        buttonSpace.getComponent(2).setVisible(true);
                        buttonSpace.getComponent(3).setVisible(false);
                        buttonSpace.getComponent(4).setVisible(false);
                        break;
                    case 2:
                        buttonSpace.getComponent(0).setVisible(false);
                        buttonSpace.getComponent(1).setVisible(false);
                        buttonSpace.getComponent(2).setVisible(false);
                        buttonSpace.getComponent(3).setVisible(true);
                        buttonSpace.getComponent(4).setVisible(false);
                        break;
                    case 3:
                        buttonSpace.getComponent(0).setVisible(false);
                        buttonSpace.getComponent(1).setVisible(false);
                        buttonSpace.getComponent(2).setVisible(false);
                        buttonSpace.getComponent(3).setVisible(false);
                        buttonSpace.getComponent(4).setVisible(true);
                        break;
                }
            }
        });

        double[][] size = {
                { TableLayout.FILL },
                { TableLayout.FILL, TableLayout.PREFERRED }
        };

        TableLayout tableLayout = new TableLayout(size);
        tableLayout.setHGap(0);
        tableLayout.setVGap(5);
        
        setLayout(tableLayout);
        add(tabbedPane, "0, 0");
        add(createButtonSpace(), "0, 1");

        tabbedPane.setSelectedIndex(-1);
        tabbedPane.setSelectedIndex(0);
    }

    private JBasicTable createServiceSummaryTable() {
        if (serviceSummaryTable == null) {
            serviceSummaryTable = createTable();

            setServiceSummaryTableModel();
        }

        return serviceSummaryTable;
    }

    private JBasicTable createReferenceSummaryTable() {
        if (referenceSummaryTable == null) {
            referenceSummaryTable = createTable();

            setReferenceSummaryTableModel();
        }

        return referenceSummaryTable;
    }

    private JBasicTable createMonitorSummaryTable() {
        if (monitorSummaryTable == null) {
            monitorSummaryTable = createTable();

            setMonitorSummaryTableModel();
        }

        return monitorSummaryTable;
    }

    private void setServiceSummaryTableModel() {
        List<SummaryEntity> allSummaryEntities = new ArrayList<SummaryEntity>();
        for (ProtocolType protocolType : ProtocolType.values()) {
            try {
                List<SummaryEntity> serviceSummaryEntities = SummaryController.getServiceSummaries(protocolType);
                allSummaryEntities.addAll(serviceSummaryEntities);
            } catch (Exception e) {
                LOG.warn("Get service summaries failed, protocol={}", protocolType);
            }
        }

        setTableModel(serviceSummaryTable, allSummaryEntities, SERVICE_TITLES, false);
    }

    private void setReferenceSummaryTableModel() {
        List<SummaryEntity> allSummaryEntities = new ArrayList<SummaryEntity>();
        for (ProtocolType protocolType : ProtocolType.values()) {
            try {
                List<SummaryEntity> referenceSummaryEntities = SummaryController.getReferenceSummaries(protocolType);
                allSummaryEntities.addAll(referenceSummaryEntities);
            } catch (Exception e) {
                LOG.warn("Get reference summaries failed, protocol={}", protocolType);
            }
        }

        setTableModel(referenceSummaryTable, allSummaryEntities, REFERENCE_TITLES, false);
    }

    private void setMonitorSummaryTableModel() {
        try {
            List<SummaryEntity> allSummaryEntities = SummaryController.getMonitorSummaries();
            setTableModel(monitorSummaryTable, allSummaryEntities, MONITOR_TITLES, true);
        } catch (Exception e) {
            LOG.warn("Get monitor summaries failed");
        }
    }

    private JBasicTable createTable() {
        JBasicTable table = new JBasicTable();
        table.setRowHeightGap(5);

        return table;
    }

    @SuppressWarnings("unchecked")
    private void setTableModel(final JBasicTable table, final List<SummaryEntity> rows, final String[] titles, final boolean monitorSummary) {
        BasicTableModel tableModel = new BasicTableModel(rows, titles) {
            private static final long serialVersionUID = 1L;

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                SummaryEntity summaryEntity = rows.get(rowIndex);
                if (monitorSummary) {
                    switch (columnIndex) {
                        case 0:
                            return summaryEntity.getAddressString();
                    }
                } else {
                    switch (columnIndex) {
                        case 0:
                            return summaryEntity.getInterface();
                        case 1:
                            return summaryEntity.getAddressString();
                        case 2:
                            return summaryEntity.getProtocolType();
                        case 3:
                            return summaryEntity.getApplication();
                        case 4:
                            return summaryEntity.getGroup();
                        case 5:
                            return summaryEntity.getTimeString();
                    }
                }

                return null;
            }
        };
        table.setModel(tableModel);
        table.adaptLayout(JBasicTable.ROW_COLUMN_LAYOUT_MODE);
    }

    private ButtonSpace createButtonSpace() {
        if (buttonSpace == null) {
            JClassicButton refreshServiceSummaryButton = new JClassicButton(createRefreshServiceSummaryAction());
            JClassicButton methodActionButton = new JClassicButton(createMethodListAction());
            JClassicButton refreshReferenceSummaryButton = new JClassicButton(createRefreshReferenceSummaryAction());
            JClassicButton refreshMonitorSummaryButton = new JClassicButton(createRefreshMonitorSummaryAction());

            buttonSpace = new ButtonSpace();
            buttonSpace.add(refreshServiceSummaryButton);
            buttonSpace.add(Box.createHorizontalStrut(5));
            buttonSpace.add(methodActionButton);
            buttonSpace.add(refreshReferenceSummaryButton);
            buttonSpace.add(refreshMonitorSummaryButton);
            buttonSpace.updateLayout();
        }

        return buttonSpace;
    }

    @SuppressWarnings("unchecked")
    private JPanel createMethodListPane() {
        if (methodListPane == null) {
            interfaceLabel = new JBasicLabel();
            methodList = new JBasicList();
            methodList.setFixedCellHeight(28);
            methodList.setCellRenderer(new DefaultListCellRenderer() {
                private static final long serialVersionUID = 1L;

                @SuppressWarnings("rawtypes")
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                    setIcon(ConsoleIconFactory.getSwingIcon("component/java.png"));
                    setToolTipText(value.toString());

                    return this;
                }
            });

            double[][] size = {
                    { TableLayout.FILL },
                    { TableLayout.PREFERRED, TableLayout.FILL }
            };
            
            TableLayout tableLayout = new TableLayout(size);
            tableLayout.setHGap(0);
            tableLayout.setVGap(5);
            
            methodListPane = new JPanel();
            methodListPane.setLayout(new TableLayout(size));
            methodListPane.add(interfaceLabel, "0, 0");
            methodListPane.add(new JBasicScrollPane(methodList), "0, 1");
            methodListPane.setPreferredSize(new Dimension(400, 400));
        }

        return methodListPane;
    }

    private JSecurityAction createRefreshServiceSummaryAction() {
        JSecurityAction action = new JSecurityAction(ButtonManager.getHtmlText(ConsoleLocale.getString("refresh_service_summary")), ConsoleIconFactory.getSwingIcon("stereo/refresh_24.png"), ConsoleLocale.getString("refresh_service_summary")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                setServiceSummaryTableModel();
            }
        };

        return action;
    }

    private JSecurityAction createMethodListAction() {
        JSecurityAction action = new JSecurityAction(ButtonManager.getHtmlText(ConsoleLocale.getString("method_list")), ConsoleIconFactory.getSwingIcon("stereo/web_24.png"), ConsoleLocale.getString("method_list_title")) {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            public void execute(ActionEvent e) {
                if (serviceSummaryTable.getSelectedRowCount() > 1 || serviceSummaryTable.getSelectedRow() < 0) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(DeploySummaryWorkspace.this), ConsoleLocale.getString("interface_not_selection"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                JPanel methodListPane = createMethodListPane();

                BasicTableModel tableModel = (BasicTableModel) serviceSummaryTable.getModel();
                SummaryEntity summaryEntity = (SummaryEntity) tableModel.getRow(serviceSummaryTable.getSelectedRow());
                
                interfaceLabel.setText(ConsoleLocale.getString("table_interface") + " : " + summaryEntity.getInterface());
                
                List<String> methods = summaryEntity.getMethods();
                if (CollectionUtils.isEmpty(methods)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(DeploySummaryWorkspace.this), ConsoleLocale.getString("interface_not_injection"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }
                methodList.setModel(new BasicListModel(new Vector<String>(methods)));

                JBasicOptionPane.showOptionDialog(HandleManager.getFrame(DeploySummaryWorkspace.this), methodListPane, ConsoleLocale.getString("method_list_title"), JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/net.png"), new Object[] { SwingLocale.getString("close") }, null, true);
            }
        };

        return action;
    }

    private JSecurityAction createRefreshReferenceSummaryAction() {
        JSecurityAction action = new JSecurityAction(ButtonManager.getHtmlText(ConsoleLocale.getString("refresh_reference_summary")), ConsoleIconFactory.getSwingIcon("stereo/refresh_24.png"), ConsoleLocale.getString("refresh_reference_summary")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                setReferenceSummaryTableModel();
            }
        };

        return action;
    }

    private JSecurityAction createRefreshMonitorSummaryAction() {
        JSecurityAction action = new JSecurityAction(ButtonManager.getHtmlText(ConsoleLocale.getString("refresh_monitor_summary")), ConsoleIconFactory.getSwingIcon("stereo/refresh_24.png"), ConsoleLocale.getString("refresh_monitor_summary")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                setMonitorSummaryTableModel();
            }
        };

        return action;
    }
}