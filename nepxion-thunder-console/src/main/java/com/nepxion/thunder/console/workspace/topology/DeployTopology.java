package com.nepxion.thunder.console.workspace.topology;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.Timer;
import javax.swing.tree.TreeNode;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import twaver.AlarmSeverity;

import com.nepxion.cots.twaver.element.TElementManager;
import com.nepxion.cots.twaver.element.TGroup;
import com.nepxion.cots.twaver.element.TGroupType;
import com.nepxion.cots.twaver.element.TLink;
import com.nepxion.cots.twaver.element.TNode;
import com.nepxion.cots.twaver.graph.TGraphManager;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.ButtonManager;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.checkbox.JBasicCheckBox;
import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.dialog.JOptionDialog;
import com.nepxion.swing.element.ElementNode;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.icon.IconFactory;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.locale.SwingLocale;
import com.nepxion.swing.optionpane.JBasicOptionPane;
import com.nepxion.swing.renderer.combobox.ElementComboBoxCellRenderer;
import com.nepxion.swing.selector.dropdown.IDropDownPopupMenu;
import com.nepxion.swing.selector.dropdown.component.JTreeDropDownSelector;
import com.nepxion.swing.textfield.number.JNumberTextField;
import com.nepxion.swing.tip.bubble.JBubbleTip;
import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.common.entity.RegistryType;
import com.nepxion.thunder.console.controller.TopologyController;
import com.nepxion.thunder.console.icon.ConsoleIconFactory;
import com.nepxion.thunder.console.locale.ConsoleLocale;
import com.nepxion.thunder.console.workspace.common.UIUtil;
import com.nepxion.thunder.console.workspace.config.ConfigTree;

public class DeployTopology extends AbstractTopology {
    private static final long serialVersionUID = 1L;

    private int clusterGroupStartX = 200;
    private int clusterGroupStartY = 30;
    private int clusterGroupHorizontalGap = 300;
    private int clusterGroupVerticalGap = 0;

    private int groupStartX = 100;
    private int groupStartY = 250;
    private int groupHorizontalGap = 250;
    private int groupVerticalGap = 0;

    private int nodeStartX = 0;
    private int nodeStartY = 0;
    private int nodeHorizontalGap = 110;
    private int nodeVerticalGap = 60;

    private TopologyEntity registryGroupEntity = new TopologyEntity(TopologyEntityType.REGISTRY, true, true);
    private TopologyEntity mqGroupEntity = new TopologyEntity(TopologyEntityType.MQ, true, true);
    private TopologyEntity cacheGroupEntity = new TopologyEntity(TopologyEntityType.CACHE, true, true);
    private TopologyEntity loggerGroupEntity = new TopologyEntity(TopologyEntityType.LOGGER, true, true);
    private TopologyEntity serviceGroupEntity = new TopologyEntity(TopologyEntityType.SERVICE, true, true);

    private TopologyEntity registryNodeEntity = new TopologyEntity(TopologyEntityType.REGISTRY, false, false);
    private TopologyEntity mqNodeEntity = new TopologyEntity(TopologyEntityType.MQ, false, false);
    private TopologyEntity cacheNodeEntity = new TopologyEntity(TopologyEntityType.CACHE, false, false);
    private TopologyEntity loggerNodeEntity = new TopologyEntity(TopologyEntityType.LOGGER, false, false);
    private TopologyEntity serviceNodeEntity = new TopologyEntity(TopologyEntityType.SERVICE, false, true);

    private Map<String, Point> groupLocationMap = new HashMap<String, Point>();
    private Map<String, Point> clusterGroupLocationMap = new HashMap<String, Point>();

    private JBasicComboBox protocolComboBox;
    private JTreeDropDownSelector configDropDownSelector;
    private ConfigTree configTree;
    private JBasicCheckBox autoRefreshCheckBox;
    private JBasicComboBox autoRefreshComboBox;
    private AutoRefreshPane autoRefreshPane;
    private AutoRefreshTimer autoRefreshTimer;

    private LayoutDialog layoutDialog;
    private JBubbleTip bubbleTip;

    public DeployTopology() {
        initializeToolBar();
    }

    @SuppressWarnings("unchecked")
    private void initializeToolBar() {
        ElementNode[] nodes = TopologyController.getProtocolNodes();
        DefaultComboBoxModel<ElementNode> comboBoxModel = new DefaultComboBoxModel<ElementNode>(nodes);
        protocolComboBox = new JBasicComboBox();
        protocolComboBox.setRenderer(new ElementComboBoxCellRenderer());
        protocolComboBox.setPreferredSize(new Dimension(110, protocolComboBox.getPreferredSize().height));
        protocolComboBox.setModel(comboBoxModel);

        configTree = new ConfigTree(ThunderConstants.CONFIGURATION_ELEMENT_NAME);
        configDropDownSelector = new JTreeDropDownSelector(configTree, true) {
            private static final long serialVersionUID = 1L;

            public boolean confirm() {
                return configTree.getSelectionTreeNode().isLeaf();
            }

            public boolean cancel() {
                return true;
            }
        };
        configDropDownSelector.setShowRoot(false);
        configDropDownSelector.setDelimiter("/");
        configDropDownSelector.setPreferredSize(new Dimension(200, protocolComboBox.getPreferredSize().height));
        IDropDownPopupMenu configDropDownPopupMenu = configDropDownSelector.getDropDownPane().getPopupMenu();
        configDropDownPopupMenu.setPopupMenuWidth(300);
        configDropDownPopupMenu.setPopupMenuHeight(300);

        JToolBar toolBar = getGraph().getToolbar();
        toolBar.addSeparator();
        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(new JBasicLabel(ConsoleLocale.getString("application")));
        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(configDropDownSelector);
        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(new JBasicLabel(ConsoleLocale.getString("table_protocol")));
        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(protocolComboBox);
        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(new JClassicButton(createShowTopologyAction()));
        toolBar.add(new JClassicButton(createAutoRefreshTopologyAction()));
        toolBar.addSeparator();
        toolBar.add(createConfigButton(true));

        ButtonManager.updateUI(toolBar);

        setGroupAutoExpand(true);
        setLinkAutoHide(true);
    }

    private void addRegistries(List<String> registries) {
        int count = groupLocationMap.size();
        TGroup registryGroup = createGroup(ConsoleLocale.getString("registry_group_name"), registryGroupEntity, count, groupStartX, groupStartY, groupHorizontalGap, groupVerticalGap);
        registryGroup.setGroupType(TGroupType.ELLIPSE_GROUP_TYPE.getType());

        addGroup(registryGroup, registryGroupEntity, registryNodeEntity, registries, RegistryType.ZOOKEEPER.toString());
    }

    private void addMQs(List<String> mqs, ProtocolType protocolType) {
        int count = groupLocationMap.size();
        TGroup mqGroup = createGroup(ConsoleLocale.getString("mq_group_name"), mqGroupEntity, count, groupStartX, groupStartY, groupHorizontalGap, groupVerticalGap);
        mqGroup.setGroupType(TGroupType.ELLIPSE_GROUP_TYPE.getType());

        addGroup(mqGroup, mqGroupEntity, mqNodeEntity, mqs, protocolType.toString());
    }

    private void addCaches(List<String> caches) {
        int count = groupLocationMap.size();
        TGroup cacheGroup = createGroup(ConsoleLocale.getString("cache_group_name"), cacheGroupEntity, count, groupStartX, groupStartY, groupHorizontalGap, groupVerticalGap);
        cacheGroup.setGroupType(TGroupType.ELLIPSE_GROUP_TYPE.getType());

        addGroup(cacheGroup, cacheGroupEntity, cacheNodeEntity, caches, "redis");
    }

    private void addLoggers(List<String> loggers) {
        int count = groupLocationMap.size();
        TGroup loggerGroup = createGroup(ConsoleLocale.getString("logger_group_name"), loggerGroupEntity, count, groupStartX, groupStartY, groupHorizontalGap, groupVerticalGap);
        loggerGroup.setGroupType(TGroupType.ELLIPSE_GROUP_TYPE.getType());

        addGroup(loggerGroup, loggerGroupEntity, loggerNodeEntity, loggers, "splunk");
    }

    private void addGroup(TGroup group, TopologyEntity groupEntity, TopologyEntity nodeEntity, List<String> nodeNames, String protocol) {
        int count = 0;
        if (CollectionUtils.isNotEmpty(nodeNames)) {
            for (int i = 0; i < nodeNames.size(); i++) {
                group.addChild(createNode(nodeNames.get(i), nodeEntity, i, nodeStartX, nodeStartY, nodeHorizontalGap, nodeVerticalGap));
            }
            count = nodeNames.size();
        }

        String groupName = group.getName();
        group.setDisplayName(ButtonManager.getHtmlText(groupName + "\n" + protocol + " [" + count + "]"));
        groupLocationMap.put(groupName, group.getLocation());

        dataBox.addElement(group);
        TElementManager.addGroupChildren(dataBox, group);
    }

    @SuppressWarnings("unchecked")
    private void locateGroups() {
        List<TGroup> groups = TElementManager.getGroups(dataBox);
        for (TGroup group : groups) {
            String groupName = group.getName();
            if (groupLocationMap.containsKey(groupName)) {
                group.setLocation(groupLocationMap.get(groupName));
            }
        }
    }

    private void addClusters(List<ApplicationEntity> serviceInstances, List<ApplicationEntity> referenceInstances, ProtocolType protocolType) {
        if (CollectionUtils.isNotEmpty(serviceInstances)) {
            for (ApplicationEntity serviceInstance : serviceInstances) {
                addClusterInstance(serviceInstance, protocolType, true);
            }
        }

        if (CollectionUtils.isNotEmpty(referenceInstances)) {
            for (ApplicationEntity referenceInstance : referenceInstances) {
                addClusterInstance(referenceInstance, protocolType, false);
            }
        }
    }

    private void addClusterInstance(ApplicationEntity instance, ProtocolType protocolType, boolean service) {
        String cluster = instance.getCluster();
        String url = instance.toUrl();

        TGroup group = addClusterGroup(cluster, protocolType);
        addClusterNode(group, url, service, protocolType);
    }

    private TGroup addClusterGroup(String cluster, ProtocolType protocolType) {
        TGroup group = TElementManager.getGroup(dataBox, cluster);
        if (group == null) {
            int count = clusterGroupLocationMap.size();
            group = createGroup(cluster, serviceGroupEntity, count, clusterGroupStartX, clusterGroupStartY, clusterGroupHorizontalGap, clusterGroupVerticalGap);
            group.setDisplayName(ButtonManager.getHtmlText(ConsoleLocale.getString("application_group_name") + " - " + cluster + "\n" + protocolType + " [0]"));
            clusterGroupLocationMap.put(group.getName(), group.getLocation());
            dataBox.addElement(group);

            addClusterLinks(group);
        }

        return group;
    }

    private TNode addClusterNode(TGroup group, String url, boolean service, ProtocolType protocolType) {
        TNode node = TElementManager.getNode(dataBox, url);
        if (node == null) {
            int count = group.childrenSize();
            node = createNode(url, serviceNodeEntity, count, nodeStartX, nodeStartY, nodeHorizontalGap, nodeVerticalGap);
            group.addChild(node);
            group.setDisplayName(ButtonManager.getHtmlText(ConsoleLocale.getString("application_group_name") + " - " + group.getName() + "\n" + protocolType + " [" + (count + 1) + "]"));
            dataBox.addElement(node);
        }

        if (service) {
            node.putClientProperty("service", true);
        } else {
            node.putClientProperty("reference", true);
        }
        String type = (node.getClientProperty("service") != null ? ConsoleLocale.getString("service_group_name") : "") + " " + (node.getClientProperty("reference") != null ? ConsoleLocale.getString("reference_group_name") : "");
        String label = url + "\n" + type.trim();
        node.setDisplayName(ButtonManager.getHtmlText(label));

        return node;
    }

    @SuppressWarnings("unchecked")
    private void addClusterLinks(TGroup group) {
        List<TGroup> groups = TElementManager.getGroups(dataBox);
        for (TGroup g : groups) {
            String groupName = g.getName();
            if (groupLocationMap.containsKey(groupName)) {
                TLink link = createLink(g, group);
                link.putLinkWidth(3);
                dataBox.addElement(link);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void locateClusterGroups() {
        List<TGroup> groups = TElementManager.getGroups(dataBox);
        for (TGroup group : groups) {
            String groupName = group.getName();
            if (clusterGroupLocationMap.containsKey(groupName)) {
                group.setLocation(clusterGroupLocationMap.get(groupName));
            }
        }
    }

    private void showTopology(boolean hint) {
        TreeNode selectedTreeNode = configDropDownSelector.getSelectedTreeNode();
        if (selectedTreeNode == null) {
            if (hint) {
                JBasicOptionPane.showMessageDialog(HandleManager.getFrame(this), ConsoleLocale.getString("application_null"), SwingLocale.getString("error"), JBasicOptionPane.ERROR_MESSAGE);
            }

            return;
        }

        ElementNode selectedElementNode = (ElementNode) selectedTreeNode;
        String application = selectedElementNode.getText();

        ElementNode selectedParentElementNode = (ElementNode) selectedElementNode.getParent();
        String group = selectedParentElementNode.getText();

        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setApplication(application);
        applicationEntity.setGroup(group);

        ElementNode elementNode = (ElementNode) protocolComboBox.getSelectedItem();
        ProtocolType protocolType = (ProtocolType) elementNode.getUserObject();

        dataBox.clear();
        clusterGroupLocationMap.clear();
        groupLocationMap.clear();

        List<ApplicationEntity> serviceInstances = null;

        try {
            serviceInstances = TopologyController.getServiceInstances(protocolType, applicationEntity);
        } catch (Exception e) {

        }

        List<ApplicationEntity> referenceInstances = null;
        try {
            referenceInstances = TopologyController.getReferenceInstances(protocolType, applicationEntity);
        } catch (Exception e) {

        }

        List<String> registryUrls = null;
        try {
            registryUrls = TopologyController.getRegistryUrls();
        } catch (Exception e) {

        }

        List<String> mqUrls = null;
        try {
            mqUrls = TopologyController.getMQUrls(protocolType, applicationEntity);
        } catch (Exception e) {

        }

        List<String> cacheUrls = null;
        try {
            cacheUrls = TopologyController.getCacheUrls(applicationEntity);
        } catch (Exception e) {

        }

        List<String> loggerUrls = null;
        try {
            loggerUrls = TopologyController.getLoggerUrls(applicationEntity);
        } catch (Exception ex) {

        }

        addRegistries(registryUrls);
        if (!protocolType.isLoadBalanceSupported()) {
            addMQs(mqUrls, protocolType);
        }
        addCaches(cacheUrls);
        addLoggers(loggerUrls);
        addClusters(serviceInstances, referenceInstances, protocolType);

        locateGroups();
        locateClusterGroups();

        TGraphManager.setGroupExpand(graph, isGroupAutoExpand());
        TGraphManager.setLinkVisible(graph, !isLinkAutoHide());

        StringBuilder builder = new StringBuilder();
        if (registryUrls == null) {
            builder.append("[" + ConsoleLocale.getString("registry_group_name") + "] " + ConsoleLocale.getString("not_register") + "\n");
            TElementManager.getGroup(dataBox, ConsoleLocale.getString("registry_group_name")).getAlarmState().addAcknowledgedAlarm(AlarmSeverity.MINOR);
        }

        if (!protocolType.isLoadBalanceSupported() && mqUrls == null) {
            builder.append("[" + ConsoleLocale.getString("mq_group_name") + "] " + ConsoleLocale.getString("not_register") + "\n");
            TElementManager.getGroup(dataBox, ConsoleLocale.getString("mq_group_name")).getAlarmState().addAcknowledgedAlarm(AlarmSeverity.MINOR);
        }

        if (cacheUrls == null) {
            builder.append("[" + ConsoleLocale.getString("cache_group_name") + "] " + ConsoleLocale.getString("not_register") + "\n");
            TElementManager.getGroup(dataBox, ConsoleLocale.getString("cache_group_name")).getAlarmState().addAcknowledgedAlarm(AlarmSeverity.MINOR);
        }

        if (loggerUrls == null) {
            builder.append("[" + ConsoleLocale.getString("logger_group_name") + "] " + ConsoleLocale.getString("not_register") + "\n");
            TElementManager.getGroup(dataBox, ConsoleLocale.getString("logger_group_name")).getAlarmState().addAcknowledgedAlarm(AlarmSeverity.MINOR);
        }

        if (serviceInstances == null) {
            builder.append("[" + ConsoleLocale.getString("service_group_name") + "] " + ConsoleLocale.getString("not_register") + "\n");
        }

        if (referenceInstances == null) {
            builder.append("[" + ConsoleLocale.getString("reference_group_name") + "] " + ConsoleLocale.getString("not_register") + "\n");
        }
        
        if (!hint) {
            return;
        }

        String message = builder.toString();
        if (StringUtils.isNotEmpty(message)) {
            // message = ConsoleLocale.getString("get_data_failure") + "\n" + message;

            if (bubbleTip == null) {
                bubbleTip = new JBubbleTip() {
                    @Override
                    public void viewDetail(Object data) {

                    }
                };
                bubbleTip.setHeight(150);
            }
            
            bubbleTip.show(null, null, message.trim());
            
            // JBasicOptionPane.showMessageDialog(HandleManager.getFrame(this), message.trim(), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);
        }
    }
    
    @Override
    public void showLayout() {
        if (layoutDialog == null) {
            layoutDialog = new LayoutDialog();
        }

        layoutDialog.setToUI();
        layoutDialog.setVisible(true);
        boolean confirmed = layoutDialog.isConfirmed();
        if (confirmed && !dataBox.isEmpty()) {
            showTopology(false);
        }
    }

    private JSecurityAction createShowTopologyAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocale.getString("show_deploy_topology"), ConsoleIconFactory.getSwingIcon("component/ui_16.png"), ConsoleLocale.getString("show_deploy_topology")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                showTopology(true);
            }
        };

        return action;
    }

    private JSecurityAction createAutoRefreshTopologyAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocale.getString("auto_refresh_topology"), ConsoleIconFactory.getSwingIcon("netbean/rotate_16.png"), ConsoleLocale.getString("auto_refresh_topology")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                if (autoRefreshPane == null) {
                    autoRefreshPane = new AutoRefreshPane();
                }

                JBasicOptionPane.showOptionDialog(HandleManager.getFrame(DeployTopology.this), autoRefreshPane, ConsoleLocale.getString("auto_refresh_topology"), JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/date.png"), new Object[] { SwingLocale.getString("close") }, null, true);

                autoRefresh();
            }
        };

        return action;
    }

    private void autoRefresh() {
        if (autoRefreshCheckBox.isSelected()) {
            ElementNode elementNode = (ElementNode) autoRefreshComboBox.getSelectedItem();
            Integer interval = (Integer) elementNode.getUserObject();
            autoRefreshTimer.start(interval.intValue());
        } else {
            autoRefreshTimer.stop();
        }
    }

    private class AutoRefreshTimer extends Timer implements ActionListener {
        private static final long serialVersionUID = 1L;

        public AutoRefreshTimer() {
            super(0, null);

            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            showTopology(false);
        }

        public void start(int delay) {
            boolean isRunning = isRunning();
            if (getDelay() == delay && isRunning) {
                return;
            }

            if (isRunning) {
                stop();
            }

            setDelay(delay);
            start();
        }

        @Override
        public void stop() {
            boolean isRunning = isRunning();
            if (isRunning) {
                super.stop();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private class AutoRefreshPane extends JPanel {
        private static final long serialVersionUID = 1L;

        public AutoRefreshPane() {
            autoRefreshCheckBox = new JBasicCheckBox(ConsoleLocale.getString("auto_refresh_topology"), ConsoleLocale.getString("auto_refresh_topology"), false);

            ElementNode[] nodes = TopologyController.getTimerNodes();

            DefaultComboBoxModel<ElementNode> comboBoxModel = new DefaultComboBoxModel<ElementNode>(nodes);
            autoRefreshComboBox = new JBasicComboBox();
            autoRefreshComboBox.setRenderer(new ElementComboBoxCellRenderer());
            autoRefreshComboBox.setModel(comboBoxModel);
            autoRefreshComboBox.setSelectedIndex(1);

            double[][] size = {
                    { TableLayout.PREFERRED, TableLayout.FILL },
                    { TableLayout.PREFERRED, TableLayout.PREFERRED }
            };

            TableLayout tableLayout = new TableLayout(size);
            tableLayout.setHGap(5);
            tableLayout.setVGap(5);

            setPreferredSize(new Dimension(180, 60));
            setLayout(tableLayout);
            add(autoRefreshCheckBox, "0, 0, 1, 0");
            add(new JBasicLabel(ConsoleLocale.getString("auto_refresh_interval")), "0, 1");
            add(autoRefreshComboBox, "1, 1");

            autoRefreshTimer = new AutoRefreshTimer();

            autoRefresh();
        }
    }

    private class LayoutDialog extends JOptionDialog {
        private static final long serialVersionUID = 1L;

        private JNumberTextField clusterGroupStartXTextField;
        private JNumberTextField clusterGroupStartYTextField;
        private JNumberTextField clusterGroupHorizontalGapTextField;
        private JNumberTextField clusterGroupVerticalGapTextField;

        private JNumberTextField groupStartXTextField;
        private JNumberTextField groupStartYTextField;
        private JNumberTextField groupHorizontalGapTextField;
        private JNumberTextField groupVerticalGapTextField;

        private JNumberTextField nodeStartXTextField;
        private JNumberTextField nodeStartYTextField;
        private JNumberTextField nodeHorizontalGapTextField;
        private JNumberTextField nodeVerticalGapTextField;

        public LayoutDialog() {
            super(HandleManager.getFrame(DeployTopology.this), SwingLocale.getString("layout"), new Dimension(500, 430), true, false, true);

            clusterGroupStartXTextField = new JNumberTextField(4, 0, 0, 10000);
            clusterGroupStartYTextField = new JNumberTextField(4, 0, 0, 10000);
            clusterGroupHorizontalGapTextField = new JNumberTextField(4, 0, 0, 10000);
            clusterGroupVerticalGapTextField = new JNumberTextField(4, 0, 0, 10000);

            groupStartXTextField = new JNumberTextField(4, 0, 0, 10000);
            groupStartYTextField = new JNumberTextField(4, 0, 0, 10000);
            groupHorizontalGapTextField = new JNumberTextField(4, 0, 0, 10000);
            groupVerticalGapTextField = new JNumberTextField(4, 0, 0, 10000);

            nodeStartXTextField = new JNumberTextField(4, 0, 0, 10000);
            nodeStartYTextField = new JNumberTextField(4, 0, 0, 10000);
            nodeHorizontalGapTextField = new JNumberTextField(4, 0, 0, 10000);
            nodeVerticalGapTextField = new JNumberTextField(4, 0, 0, 10000);

            double[][] size = {
                    { 100, TableLayout.FILL, 100, TableLayout.FILL },
                    { TableLayout.PREFERRED, TableLayout.PREFERRED }
            };

            TableLayout tableLayout = new TableLayout(size);
            tableLayout.setHGap(5);
            tableLayout.setVGap(5);

            JPanel clusterGroupPanel = new JPanel();
            clusterGroupPanel.setLayout(tableLayout);
            clusterGroupPanel.setBorder(UIUtil.createTitledBorder(ConsoleLocale.getString("application_cluster") + " - " + ConsoleLocale.getString("group_layout")));
            clusterGroupPanel.add(new JBasicLabel(ConsoleLocale.getString("start_x")), "0, 0");
            clusterGroupPanel.add(clusterGroupStartXTextField, "1, 0");
            clusterGroupPanel.add(new JBasicLabel(ConsoleLocale.getString("start_y")), "2, 0");
            clusterGroupPanel.add(clusterGroupStartYTextField, "3, 0");
            clusterGroupPanel.add(new JBasicLabel(ConsoleLocale.getString("horizontal_gap")), "0, 1");
            clusterGroupPanel.add(clusterGroupHorizontalGapTextField, "1, 1");
            clusterGroupPanel.add(new JBasicLabel(ConsoleLocale.getString("vertical_gap")), "2, 1");
            clusterGroupPanel.add(clusterGroupVerticalGapTextField, "3, 1");

            JPanel groupPanel = new JPanel();
            groupPanel.setLayout(tableLayout);
            groupPanel.setBorder(UIUtil.createTitledBorder(ConsoleLocale.getString("third_party_cluster") + " - " + ConsoleLocale.getString("group_layout")));
            groupPanel.add(new JBasicLabel(ConsoleLocale.getString("start_x")), "0, 0");
            groupPanel.add(groupStartXTextField, "1, 0");
            groupPanel.add(new JBasicLabel(ConsoleLocale.getString("start_y")), "2, 0");
            groupPanel.add(groupStartYTextField, "3, 0");
            groupPanel.add(new JBasicLabel(ConsoleLocale.getString("horizontal_gap")), "0, 1");
            groupPanel.add(groupHorizontalGapTextField, "1, 1");
            groupPanel.add(new JBasicLabel(ConsoleLocale.getString("vertical_gap")), "2, 1");
            groupPanel.add(groupVerticalGapTextField, "3, 1");

            JPanel nodePanel = new JPanel();
            nodePanel.setLayout(tableLayout);
            nodePanel.setBorder(UIUtil.createTitledBorder(ConsoleLocale.getString("node_layout")));
            nodePanel.add(new JBasicLabel(ConsoleLocale.getString("start_x")), "0, 0");
            nodePanel.add(nodeStartXTextField, "1, 0");
            nodePanel.add(new JBasicLabel(ConsoleLocale.getString("start_y")), "2, 0");
            nodePanel.add(nodeStartYTextField, "3, 0");
            nodePanel.add(new JBasicLabel(ConsoleLocale.getString("horizontal_gap")), "0, 1");
            nodePanel.add(nodeHorizontalGapTextField, "1, 1");
            nodePanel.add(new JBasicLabel(ConsoleLocale.getString("vertical_gap")), "2, 1");
            nodePanel.add(nodeVerticalGapTextField, "3, 1");

            JPanel panel = new JPanel();
            panel.setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 5));
            panel.add(clusterGroupPanel);
            panel.add(groupPanel);
            panel.add(nodePanel);

            setOption(YES_NO_OPTION);
            setIcon(IconFactory.getSwingIcon("banner/navigator.png"));
            setContent(panel);
        }

        @Override
        public boolean confirm() {
            return setFromUI();
        }

        @Override
        public boolean cancel() {
            return true;
        }

        public void setToUI() {
            clusterGroupStartXTextField.setText(clusterGroupStartX + "");
            clusterGroupStartYTextField.setText(clusterGroupStartY + "");
            clusterGroupHorizontalGapTextField.setText(clusterGroupHorizontalGap + "");
            clusterGroupVerticalGapTextField.setText(clusterGroupVerticalGap + "");

            groupStartXTextField.setText(groupStartX + "");
            groupStartYTextField.setText(groupStartY + "");
            groupHorizontalGapTextField.setText(groupHorizontalGap + "");
            groupVerticalGapTextField.setText(groupVerticalGap + "");

            nodeStartXTextField.setText(nodeStartX + "");
            nodeStartYTextField.setText(nodeStartY + "");
            nodeHorizontalGapTextField.setText(nodeHorizontalGap + "");
            nodeVerticalGapTextField.setText(nodeVerticalGap + "");
        }

        public boolean setFromUI() {
            int clusterGroupStartX = 0;
            int clusterGroupStartY = 0;
            int clusterGroupHorizontalGap = 0;
            int clusterGroupVerticalGap = 0;
            int groupStartX = 0;
            int groupStartY = 0;
            int groupHorizontalGap = 0;
            int groupVerticalGap = 0;
            int nodeStartX = 0;
            int nodeStartY = 0;
            int nodeHorizontalGap = 0;
            int nodeVerticalGap = 0;

            try {
                clusterGroupStartX = Integer.parseInt(clusterGroupStartXTextField.getText());
                clusterGroupStartY = Integer.parseInt(clusterGroupStartYTextField.getText());
                clusterGroupHorizontalGap = Integer.parseInt(clusterGroupHorizontalGapTextField.getText());
                clusterGroupVerticalGap = Integer.parseInt(clusterGroupVerticalGapTextField.getText());

                groupStartX = Integer.parseInt(groupStartXTextField.getText());
                groupStartY = Integer.parseInt(groupStartYTextField.getText());
                groupHorizontalGap = Integer.parseInt(groupHorizontalGapTextField.getText());
                groupVerticalGap = Integer.parseInt(groupVerticalGapTextField.getText());

                nodeStartX = Integer.parseInt(nodeStartXTextField.getText());
                nodeStartY = Integer.parseInt(nodeStartYTextField.getText());
                nodeHorizontalGap = Integer.parseInt(nodeHorizontalGapTextField.getText());
                nodeVerticalGap = Integer.parseInt(nodeVerticalGapTextField.getText());
            } catch (NumberFormatException e) {
                return false;
            }

            DeployTopology.this.clusterGroupStartX = clusterGroupStartX;
            DeployTopology.this.clusterGroupStartY = clusterGroupStartY;
            DeployTopology.this.clusterGroupHorizontalGap = clusterGroupHorizontalGap;
            DeployTopology.this.clusterGroupVerticalGap = clusterGroupVerticalGap;
            DeployTopology.this.groupStartX = groupStartX;
            DeployTopology.this.groupStartY = groupStartY;
            DeployTopology.this.groupHorizontalGap = groupHorizontalGap;
            DeployTopology.this.groupVerticalGap = groupVerticalGap;
            DeployTopology.this.nodeStartX = nodeStartX;
            DeployTopology.this.nodeStartY = nodeStartY;
            DeployTopology.this.nodeHorizontalGap = nodeHorizontalGap;
            DeployTopology.this.nodeVerticalGap = nodeVerticalGap;

            return true;
        }
    }
}