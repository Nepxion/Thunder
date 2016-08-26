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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import twaver.AlarmSeverity;
import twaver.BlinkingRule;
import twaver.Element;
import twaver.Generator;
import twaver.TWaverConst;

import com.nepxion.cots.twaver.element.TElement;
import com.nepxion.cots.twaver.element.TElementManager;
import com.nepxion.cots.twaver.element.TGroup;
import com.nepxion.cots.twaver.element.TGroupType;
import com.nepxion.cots.twaver.element.TLink;
import com.nepxion.cots.twaver.element.TNode;
import com.nepxion.cots.twaver.graph.TGraphBackground;
import com.nepxion.cots.twaver.graph.TGraphManager;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.ButtonManager;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.container.ContainerManager;
import com.nepxion.swing.dialog.JOptionDialog;
import com.nepxion.swing.exception.ExceptionTracer;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.icon.IconFactory;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.locale.SwingLocale;
import com.nepxion.swing.menuitem.JBasicMenuItem;
import com.nepxion.swing.optionpane.JBasicOptionPane;
import com.nepxion.swing.popupmenu.JBasicPopupMenu;
import com.nepxion.swing.radiobutton.JBasicRadioButton;
import com.nepxion.swing.renderer.table.DefaultTableCellRenderer;
import com.nepxion.swing.scrollpane.JBasicScrollPane;
import com.nepxion.swing.selector.calendar.JDateTimeSelector;
import com.nepxion.swing.selector.calendar.editor.JSpinnerDateTimeEditor;
import com.nepxion.swing.selector.file.JFileSelector;
import com.nepxion.swing.tabbedpane.JBasicTabbedPane;
import com.nepxion.swing.table.BasicTableModel;
import com.nepxion.swing.table.JBasicTable;
import com.nepxion.swing.textarea.JBasicTextArea;
import com.nepxion.swing.textfield.JBasicPasswordField;
import com.nepxion.swing.textfield.JBasicTextField;
import com.nepxion.swing.textfield.number.JNumberTextField;
import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.entity.MonitorStat;
import com.nepxion.thunder.common.entity.RedisType;
import com.nepxion.thunder.console.context.CacheContext;
import com.nepxion.thunder.console.context.PropertiesContext;
import com.nepxion.thunder.console.context.UIContext;
import com.nepxion.thunder.console.controller.MonitorController;
import com.nepxion.thunder.console.icon.ConsoleIconFactory;
import com.nepxion.thunder.console.locale.ConsoleLocale;
import com.nepxion.thunder.console.workspace.common.UIUtil;
import com.nepxion.thunder.monitor.SplunkLogServiceMonitorRetriever;
import com.nepxion.util.locale.LocaleContext;

public class TraceTopology extends AbstractTopology {
    private static final long serialVersionUID = 1L;
    private static final String[] TITLES = { 
        "messageId", "messageType", "fromCluster", "fromUrl", "toCluster", "toUrl", 
        "processStartTime", "processEndTime", "deliverStartTime", "deliverEndTime", "processedTime", "deliveredTime", "totalTime", 
        "interface", "method", "parameterTypes", "protocol", "application", "group",
        "async", "callback", "timeout", "broadcast", "loadBalance", "feedback", "exception"};
    
    private int groupStartX = 100;
    private int groupStartY = 150; // 200
    private int groupHorizontalGap = 300; // 250
    private int groupVerticalGap = 0;

    private int nodeStartX = 0;
    private int nodeStartY = 0;
    private int nodeHorizontalGap = 0;
    private int nodeVerticalGap = 100; // 60

    private TopologyEntity serviceGroupEntity = new TopologyEntity(TopologyEntityType.SERVICE, true, true);
    private TopologyEntity serviceNodeEntity = new TopologyEntity(TopologyEntityType.SERVICE, false, false);

    private Map<String, Point> groupLocationMap = new HashMap<String, Point>();

    private TGraphBackground background;
    
    private JPanel container;
    private JSplitPane splitPane;
    
    private JBasicTextField traceIdTextField;
    private JPanel infoPane;
    private JBasicTabbedPane infoTabbedPane;
    private JPanel infoHintPane;
    private JBasicMenuItem showInfoMenuItem;

    private DataSourcePane dataSourcePane;
    private LayoutDialog layoutDialog;

    private JBasicTable table;
    private JBasicScrollPane tableScrollPane;
    private boolean tableVisible;
    
    private JBasicTable schematicTable;
    private JBasicScrollPane schematicTableScrollPane;
    
    public TraceTopology() {
        initializeToolBar();
        initializeTopology();
        initializeTable();
    }
    
    @Override
    protected void initializePopupMenu() {
        super.initializePopupMenu();
        
        showInfoMenuItem = new JBasicMenuItem(createShowInfoAction());
        popupMenu.add(showInfoMenuItem, 0);
    }
    
    @Override
    protected JBasicPopupMenu popupMenuGenerate() {
        TGroup group = TElementManager.getSelectedGroup(dataBox);
        pinSelectedGroupMenuItem.setVisible(group != null);
        
        TNode node = TElementManager.getSelectedNode(dataBox);
        pinSelectedNodeMenuItem.setVisible(node != null);
        
        TElement element = TElementManager.getSelectedElement(dataBox);
        showInfoMenuItem.setVisible(element != null);
        
        if (group != null || node != null || element != null) {
            return popupMenu;
        }

        return null;
    }

    private void initializeToolBar() {
        traceIdTextField = new JBasicTextField();
        traceIdTextField.setPreferredSize(new Dimension(200, traceIdTextField.getPreferredSize().height));

        JToolBar toolBar = getGraph().getToolbar();
        toolBar.addSeparator();
        toolBar.add(new JClassicButton(createConfigDataSourceAction()));
        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(new JBasicLabel("Trace ID :"));
        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(traceIdTextField);
        toolBar.add(new JClassicButton(createShowTopologyAction()));
        toolBar.add(new JClassicButton(createShowTableAction()));
        toolBar.add(new JClassicButton(createShowInfoAction()));
        toolBar.add(new JClassicButton(createShowSchematicAction()));
        toolBar.addSeparator();
        toolBar.add(createConfigButton(false));

        ButtonManager.updateUI(toolBar);

        setGroupAutoExpand(true);
    }

    private void initializeTopology() {
        background = graph.getGraphBackground();
        background.setTitle(ConsoleLocale.getString("trace_view_title"));
        graph.setBlinkingRule(new BlinkingRule() {
            public boolean isBodyBlinking(Element element) {
                return element.getAlarmState().getHighestNativeAlarmSeverity() != null || element.getClientProperty(TWaverConst.PROPERTYNAME_RENDER_COLOR) != null;
            }

            public boolean isOutlineBlinking(Element element) {
                return element.getAlarmState().getPropagateSeverity() != null || element.getClientProperty(TWaverConst.PROPERTYNAME_STATE_OUTLINE_COLOR) != null;
            }
        });
        graph.setElementStateOutlineColorGenerator(new Generator() {
            public Object generate(Object object) {
                return null;
            }
        });
    }
    
    private void initializeTable() {
        table = new JBasicTable() {
            private static final long serialVersionUID = 1L;

            @Override
            public void executeSelection(int selectedRow) {
                select(selectedRow);
            }
        };
        table.setAutoResizeMode(JBasicTable.AUTO_RESIZE_OFF);
        table.setRowHeightGap(3);
        table.setColumnWidthGap(3);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font(UIContext.getFontName(), Font.PLAIN, UIContext.getSmallFontSize()));
        
        tableScrollPane = new JBasicScrollPane(table);
        
        setTableModel(table, new ArrayList<MonitorStat>(), TITLES);
    }
    
    public JPanel createContainer() {
        if (container == null) {
            splitPane = new JSplitPane();
            splitPane.setBorder(BorderFactory.createEmptyBorder());
            splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            
            container = new JPanel();
            container.setLayout(new BorderLayout());
            
            showTable(tableVisible);
        }
        
        return container;
    }
    
    private boolean isMessageIdEquals(MonitorStat monitorStat, String messageId) {
        return StringUtils.equals(monitorStat.getMessageId(), messageId);
    }
    
    private boolean isMessageTypeEquals(MonitorStat monitorStat, String messageId) {
        return StringUtils.equals(monitorStat.getMessageType(), messageId);
    }
    
    private boolean isInternal(MonitorStat monitorStat) {
        return StringUtils.equals(monitorStat.getFromUrl(), monitorStat.getToUrl());
    }
    
    private boolean hasMessageId(List<MonitorStat> monitorStatList, String messageId) {
        if (CollectionUtils.isEmpty(monitorStatList)) {
            return false;
        }

        // MonitorStat列表里是否含有至少一个该MessageId
        for (MonitorStat monitorStat : monitorStatList) {
            if (isMessageIdEquals(monitorStat, messageId)) {
                return true;
            }
        }

        return false;
    }
    
    private boolean hasExceptionMessageId(List<MonitorStat> monitorStatList, String messageId) {
        if (CollectionUtils.isEmpty(monitorStatList)) {
            return false;
        }

        // MonitorStat列表里是否含有至少一个该MessageId，且对应的MonitorStat必须含有异常
        for (MonitorStat monitorStat : monitorStatList) {
            if (isMessageIdEquals(monitorStat, messageId) && StringUtils.isNotEmpty(monitorStat.getException())) {
                return true;
            }
        }

        return false;
    }
    
    private boolean hasException(MonitorStat monitorStat) {
        // MonitorStat里是否含有异常
        String exception = monitorStat.getException();
        if (StringUtils.isNotEmpty(exception)) {
            return true;
        }
        
        return false;
    }

    private boolean hasException(List<MonitorStat> monitorStatList) {
        // MonitorStat列表里是否含有至少一个异常
        if (CollectionUtils.isEmpty(monitorStatList)) {
            return false;
        }

        for (MonitorStat monitorStat : monitorStatList) {
            boolean hasException = hasException(monitorStat);
            if (hasException) {
                return true;
            }
        }

        return false;
    }
    
    @SuppressWarnings("unchecked")
    private boolean hasException(TNode node) {
        List<MonitorStat> monitorStatList = (List<MonitorStat>) node.getUserObject();
        // 该Node本身拥有的List里面有异常
        if (hasException(monitorStatList)) {
            return true;
        }
        
        // 跟该Node关联的Link有异常
        List<TLink> links = node.getAllLinks();
        if (CollectionUtils.isEmpty(links)) {
            return false;
        }
        
        for (TLink link : links) {
            // 该Node是Link的From，表示该Node是异常的触发源
            if (link.getFrom() == node) {
                MonitorStat monitorStat = (MonitorStat) link.getUserObject();
                if (hasException(monitorStat)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean hasException(TLink link) {
        MonitorStat monitorStat = (MonitorStat) link.getUserObject();
        // 该Link本身拥有的Object里面有异常
        if (hasException(monitorStat)) {
            return true;
        }
        
        return false;
    }
    
    @SuppressWarnings("unchecked")
    private boolean hasRelevantException(TLink link) {
        MonitorStat monitorStat = (MonitorStat) link.getUserObject();
        String messageId = monitorStat.getMessageId();
        
        // 取该Link的To，表示该Link是跟异常Node具有关联性，或者说，正因为有该Link(调用)，才使Node产生异常
        List<MonitorStat> monitorStatList = (List<MonitorStat>) link.getTo().getUserObject();
        if (hasException(monitorStatList) && hasExceptionMessageId(monitorStatList, messageId)) {
            return true;
        }
        
        return false;
    }

    private TGroup addGroup(String cluster) {
        TGroup group = TElementManager.getGroup(dataBox, cluster);
        if (group == null) {
            int count = groupLocationMap.size();
            group = createGroup(cluster, serviceGroupEntity, count, groupStartX, groupStartY, groupHorizontalGap, groupVerticalGap);
            group.setGroupType(TGroupType.ELLIPSE_GROUP_TYPE.getType());
            group.setDisplayName(cluster + " [0]");
            groupLocationMap.put(group.getName(), group.getLocation());
            dataBox.addElement(group);
        }

        return group;
    }

    private TNode addNode(TGroup group, String url) {
        TNode node = TElementManager.getNode(dataBox, url);
        if (node == null) {
            int count = group.childrenSize();
            node = createNode(url, serviceNodeEntity, count, nodeStartX, nodeStartY, nodeHorizontalGap, nodeVerticalGap);
            group.addChild(node);
            group.setDisplayName(group.getName() + " [" + (count + 1) + "]");
            dataBox.addElement(node);
        }

        return node;
    }
    
    @SuppressWarnings("unchecked")
    private void addNodeUserObject(TElement element, MonitorStat monitorStat) {
        // Group和Node里面维护一个MonitorStat列表，当FromUrl==ToUrl时，该列表不为空
        List<MonitorStat> monitorStatList = (List<MonitorStat>) element.getUserObject();
        if (CollectionUtils.isEmpty(monitorStatList)) {
            monitorStatList = new ArrayList<MonitorStat>();
            element.setUserObject(monitorStatList);
        }
        monitorStatList.add(monitorStat);
    }

    private void addLink(TNode fromNode, TNode toNode, MonitorStat monitorStat) {
        TLink link = createLink(fromNode, toNode, true);
        link.setName(monitorStat.getInterfaze().substring(monitorStat.getInterfaze().lastIndexOf(".") + 1) + ":" + monitorStat.getMethod());
        link.setUserObject(monitorStat);
        if (monitorStat.isFeedback()) {
            link.putLinkToArrowColor(Color.yellow);
        } else {
            link.putLinkToArrowColor(new Color(0, 255, 255));
        }
        
        dataBox.addElement(link);
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

    @SuppressWarnings("unchecked")
    private void alarmNodes() {        
        List<TNode> nodes = TElementManager.getNodes(dataBox);
        for (TNode node : nodes) {
            if (hasException(node)) {
                node.getAlarmState().addAcknowledgedAlarm(AlarmSeverity.CRITICAL);
                // node.getAlarmState().setNewAlarmCount(AlarmSeverity.CRITICAL, 1);
                
                TGroup group = (TGroup) node.getParent();
                if (group != null) {
                    group.getAlarmState().addAcknowledgedAlarm(AlarmSeverity.CRITICAL);
                    // group.getAlarmState().setNewAlarmCount(AlarmSeverity.CRITICAL, 1);
                }
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private void alarmLinks() {
        List<TLink> links = TElementManager.getLinks(dataBox);
        for (TLink link : links) {
            if (hasException(link)) {
                // Link本身有异常
                link.putLinkFlowing(true);
                link.putLinkFlowingColor(Color.red);
                link.putLinkFlowingWidth(3);
                // link.getAlarmState().addAcknowledgedAlarm(AlarmSeverity.CRITICAL);
                // link.getAlarmState().setNewAlarmCount(AlarmSeverity.CRITICAL, 1);
            } else if (hasRelevantException(link)) {
                // 关联的Link有异常
                link.putLinkFlowing(true);
                link.putLinkFlowingColor(new Color(255, 155, 85));
                link.putLinkFlowingWidth(3);
                // link.getAlarmState().addAcknowledgedAlarm(AlarmSeverity.CRITICAL);
                // link.getAlarmState().setNewAlarmCount(AlarmSeverity.CRITICAL, 1);
            }
        }
    }
    
    private void select(int selectedRow) {
        if (selectedRow > -1) {
            BasicTableModel tableModel = (BasicTableModel) table.getModel();
            MonitorStat monitorStat = (MonitorStat) tableModel.getRow(table.convertRowIndexToModel(selectedRow));

            dataBox.getSelectionModel().clearSelection();
            if (isInternal(monitorStat)) {
                selectNodes(monitorStat);
            } else {
                selectLinks(monitorStat);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private void selectNodes(MonitorStat monitorStat) {
        String messageId = monitorStat.getMessageId();
        
        List<TNode> nodes = TElementManager.getNodes(dataBox);
        for (TNode node : nodes) {
            List<MonitorStat> monitorStatList = (List<MonitorStat>) node.getUserObject();
            boolean selected = hasMessageId(monitorStatList, messageId);
            node.setSelected(selected);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void selectLinks(MonitorStat monitorStat) {
        String messageId = monitorStat.getMessageId();
        String messageType = monitorStat.getMessageType();
        
        List<TLink> links = TElementManager.getLinks(dataBox);
        for (TLink link : links) {
            MonitorStat stat = (MonitorStat) link.getUserObject();
            boolean selected = isMessageIdEquals(stat, messageId) && isMessageTypeEquals(stat, messageType);
            link.setSelected(selected);
        }    
    }
    
    @SuppressWarnings("unchecked")
    private void highlightLinks(String messageId) {
        List<TLink> links = TElementManager.getLinks(dataBox);
        for (TLink link : links) {
            MonitorStat monitorStat = (MonitorStat) link.getUserObject();
            boolean highlighted = isMessageIdEquals(monitorStat, messageId);
            link.setHighLightable(highlighted);
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

    private void showTopology(boolean hint) {
        String traceId = traceIdTextField.getText();
        if (StringUtils.isEmpty(traceId)) {
            if (hint) {
                JBasicOptionPane.showMessageDialog(HandleManager.getFrame(TraceTopology.this), ConsoleLocale.getString("trace_id_null"), SwingLocale.getString("error"), JBasicOptionPane.ERROR_MESSAGE);
            }
            
            return;
        }

        String title = ConsoleLocale.getString("trace_view_title") + " [" + traceId + "]";
        background.setTitle(title);
        
        dataBox.clear();
        groupLocationMap.clear();
        
        BasicTableModel tableModel = (BasicTableModel) table.getModel();
        tableModel.clearRows();

        List<MonitorStat> monitorStatList = null;
        try {
            monitorStatList = retrieve(traceId);
        } catch (Exception e) {
            ExceptionTracer.traceException(HandleManager.getFrame(TraceTopology.this), ConsoleLocale.getString("data_source_error"), e);

            return;
        }

        if (CollectionUtils.isEmpty(monitorStatList)) {
            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(TraceTopology.this), ConsoleLocale.getString("get_trace_error"), SwingLocale.getString("error"), JBasicOptionPane.ERROR_MESSAGE);

            return;
        }

        for (MonitorStat monitorStat : monitorStatList) {
            String fromCluster = monitorStat.getFromCluster();
            String fromUrl = monitorStat.getFromUrl();
            String toCluster = monitorStat.getToCluster();
            String toUrl = monitorStat.getToUrl();

            TGroup fromGroup = addGroup(fromCluster);
            TGroup toGroup = addGroup(toCluster);

            TNode fromNode = addNode(fromGroup, fromUrl);
            TNode toNode = addNode(toGroup, toUrl);

            // 当调用发生在内部(FromUrl==ToUrl)，不添加Link，只往Node的List里面塞
            if (isInternal(monitorStat)) {
                addNodeUserObject(fromGroup, monitorStat);
                addNodeUserObject(fromNode, monitorStat);
            } else {
                addLink(fromNode, toNode, monitorStat);
            }
        }

        locateGroups();
        TGraphManager.setGroupExpand(graph, isGroupAutoExpand());
        
        alarmNodes();
        alarmLinks();
        
        setTableModel(table, monitorStatList, TITLES);
    }

    private void showTable(boolean visible) {
        container.removeAll();
        if (visible) {
            splitPane.setDividerLocation(380);
            splitPane.setLeftComponent(this);
            splitPane.setRightComponent(tableScrollPane);
            container.add(splitPane, BorderLayout.CENTER);
        } else {
            container.add(this, BorderLayout.CENTER);
        }
        
        ContainerManager.update(container);
    }
    
    private void setTableModel(final JBasicTable table, final List<MonitorStat> rows, final String[] titles) {
        final BasicTableModel tableModel = new BasicTableModel(rows, titles) {
            private static final long serialVersionUID = 1L;

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                MonitorStat monitorStat = rows.get(rowIndex);
                switch (columnIndex) {
                    case 0:
                        return monitorStat.getMessageId();
                    case 1:
                        return monitorStat.getMessageType();
                    case 2:
                        return monitorStat.getFromCluster();
                    case 3:
                        return monitorStat.getFromUrl();
                    case 4:
                        return monitorStat.getToCluster();
                    case 5:
                        return monitorStat.getToUrl();
                    case 6:
                        return new SimpleDateFormat(MonitorStat.DATE_FORMAT).format(new Date(monitorStat.getProcessStartTime()));
                    case 7:
                        return new SimpleDateFormat(MonitorStat.DATE_FORMAT).format(new Date(monitorStat.getProcessEndTime()));
                    case 8:
                        return new SimpleDateFormat(MonitorStat.DATE_FORMAT).format(new Date(monitorStat.getDeliverStartTime()));
                    case 9:
                        return new SimpleDateFormat(MonitorStat.DATE_FORMAT).format(new Date(monitorStat.getDeliverEndTime()));
                    case 10:
                        return monitorStat.getProcessEndTime() - monitorStat.getProcessStartTime();
                    case 11:
                        return monitorStat.getDeliverEndTime() - monitorStat.getDeliverStartTime();
                    case 12:
                        return monitorStat.getProcessEndTime() - monitorStat.getProcessStartTime() + monitorStat.getDeliverEndTime() - monitorStat.getDeliverStartTime();
                    case 13:
                        return monitorStat.getInterfaze();
                    case 14:
                        return monitorStat.getMethod();
                    case 15:
                        return monitorStat.getParameterTypes();
                    case 16:
                        return monitorStat.getProtocol();
                    case 17:
                        return monitorStat.getApplication();
                    case 18:
                        return monitorStat.getGroup();
                    case 19:
                        return monitorStat.isAsync();
                    case 20:
                        return monitorStat.getCallback();
                    case 21:
                        return monitorStat.getTimeout();
                    case 22:
                        return monitorStat.isBroadcast();
                    case 23:
                        return monitorStat.getLoadBalance();
                    case 24:
                        return monitorStat.isFeedback();
                    case 25:
                        return monitorStat.getException();
                }

                return null;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 10 || columnIndex == 11 || columnIndex == 12 || columnIndex == 20) {
                    return Long.class;
                }
                
                return super.getColumnClass(columnIndex);
            }
        };
        table.setModel(tableModel);
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            table.getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
                private static final long serialVersionUID = 1L;

                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    
                    MonitorStat monitorStat = (MonitorStat) tableModel.getRow(table.convertRowIndexToModel(row));                    
                    
                    if (hasException(monitorStat)) {
                        // super.setBackground(Color.red);
                        super.setForeground(Color.red);
                    }
                    
                    return this;
                }
            });
        }
        table.adaptLayout(JBasicTable.ROW_COLUMN_LAYOUT_MODE);
    }

    @SuppressWarnings("unchecked")
    private void showInfo() {
        if (dataBox.isEmpty()) {
            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(TraceTopology.this), ConsoleLocale.getString("get_trace_first"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

            return;
        }
        
        TElement element = TElementManager.getSelectedElement(dataBox);
        if (element == null) {
            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(TraceTopology.this), ConsoleLocale.getString("select_one_element"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

            return;
        }
        
        Object userObject = element.getUserObject();
        if (userObject == null) {
            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(TraceTopology.this), ConsoleLocale.getString("invoke_info_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

            return;
        }
        
        List<MonitorStat> monitorStatList = null;
        boolean highlightable = false;
        if (userObject instanceof List) {
            monitorStatList = (List<MonitorStat>) userObject;
            highlightable = true;
        } else if (userObject instanceof MonitorStat) {
            MonitorStat monitorStat = (MonitorStat) userObject;
            monitorStatList = new ArrayList<MonitorStat>();
            monitorStatList.add(monitorStat);
            highlightable = false;
        }

        showTabbedPane(ConsoleLocale.getString("show_info"), "net.png", monitorStatList, highlightable);
    }

    private void showTabbedPane(String title, String icon, List<MonitorStat> monitorStatList, final boolean highlightable) {
        if (infoTabbedPane == null) {
            infoTabbedPane = new JBasicTabbedPane();
            infoTabbedPane.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    boolean value = ((Boolean) infoTabbedPane.getClientProperty("highlightable")).booleanValue();
                    // 当从Node触发选项卡显示的时候，切换事件会触发相关Link定位
                    // 当从Link触发选项卡显示的时候，则忽略
                    if (value) {
                        Component selectedComponent = infoTabbedPane.getSelectedComponent();
                        if (selectedComponent != null) {
                            String messageId = selectedComponent.getName();
                            highlightLinks(messageId);
                        }
                    }
                }
            });

            JBasicLabel infoHintLabel = new JBasicLabel();
            infoHintLabel.setText("* " + ConsoleLocale.getString("invoke_hint"));
            infoHintPane = new JPanel();
            infoHintPane.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.LEFT, 0));
            infoHintPane.add(infoHintLabel);
            
            infoPane = new JPanel();
            infoPane.setLayout(new BoxLayout(infoPane, BoxLayout.Y_AXIS));
            infoPane.setPreferredSize(new Dimension(520, 520));
            infoPane.add(infoTabbedPane);
            infoPane.add(Box.createVerticalStrut(5));
            infoPane.add(infoHintPane);
        }
        
        infoTabbedPane.putClientProperty("highlightable", highlightable);
        infoTabbedPane.removeAll();
        infoHintPane.setVisible(highlightable);

        for (MonitorStat monitorStat : monitorStatList) {
            String messageId = monitorStat.getMessageId();
            String info = monitorStat.toString().replace(", ", "\n");
            String exception = monitorStat.getException();
            if (hasException(monitorStat)) {
                info += "\nexception=\n" + exception;
            }
            
            JBasicTextArea textArea = new JBasicTextArea();
            textArea.setEditable(false);
            textArea.setText(info);
            textArea.setCaretPosition(0);

            JBasicScrollPane scrollPane = new JBasicScrollPane(textArea);
            scrollPane.setName(messageId);

            String tabTitle = ConsoleLocale.getString("invoke") + (monitorStatList.size() == 1 ? "" : " - " + (infoTabbedPane.getTabCount() + 1));
            infoTabbedPane.addTab(tabTitle, scrollPane, tabTitle);
        }
        
        JBasicOptionPane.showOptionDialog(HandleManager.getFrame(this), infoPane, title, JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/" + icon), new Object[] { SwingLocale.getString("close") }, null, true);

        highlightLinks(null);
        select(table.getSelectedRow());
    }
    
    private void showSchematic() {
        if (schematicTable == null) {
            String[][] rowDatas = new String[][] {{"schematic_node_alarm_exception.png", "schematic_node_alarm_exception"}, 
                                                  {"schematic_node_select.png", "schematic_node_select"}, 
                                                  {"schematic_link_alarm_exception.png", "schematic_link_alarm_exception"}, 
                                                  {"schematic_link_alarm_relevant_exception.png", "schematic_link_alarm_relevant_exception"},
                                                  {"schematic_link_select.png", "schematic_link_select"}, 
                                                  {"schematic_link_highlight.png", "schematic_link_highlight"},
                                                  {"schematic_link_feedback.png", "schematic_link_feedback"}, 
                                                  {"schematic_link_nofeedback.png", "schematic_link_nofeedback"}};                     
            String[] columnTitles = new String[] {ConsoleLocale.getString("schematic"), ConsoleLocale.getString("schematic_indicator")};
            
            schematicTable = new JBasicTable();
            DefaultTableModel schematicTableModel = new DefaultTableModel(rowDatas, columnTitles){
                private static final long serialVersionUID = 1L;
                
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            schematicTable.setModel(schematicTableModel);
            for (int i = 0; i < schematicTableModel.getColumnCount(); i++) {
                DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                        if (column == 0) {
                            setText(null);
                            ImageIcon icon = ConsoleIconFactory.getContextIcon(value.toString());
                            setIcon(icon);
                            setPreferredSize(new Dimension(this.getPreferredSize().width, icon.getIconHeight()));
                        } else if (column == 1) {
                            setText(ConsoleLocale.getString(value.toString() + "_hint"));
                        }

                        return this;
                    }
                }; 
                if (i == 1 && LocaleContext.getLocale() != Locale.SIMPLIFIED_CHINESE) {
                    tableCellRenderer.setHorizontalTextPosition(SwingConstants.CENTER);
                    tableCellRenderer.setVerticalTextPosition(SwingConstants.BOTTOM);
                    tableCellRenderer.setVerticalAlignment(SwingConstants.VERTICAL);
                }
                schematicTable.getColumn(i).setCellRenderer(tableCellRenderer);
            }
            schematicTable.setRowHeightGap(10);
            schematicTable.setColumnWidthGap(0);
            schematicTable.adaptLayout(JBasicTable.ROW_COLUMN_LAYOUT_MODE);

            schematicTableScrollPane = new JBasicScrollPane(schematicTable);
            schematicTableScrollPane.setPreferredSize(new Dimension(schematicTableScrollPane.getPreferredSize().width + 40, 615));
        }
        
        JBasicOptionPane.showOptionDialog(HandleManager.getFrame(this), schematicTableScrollPane, ConsoleLocale.getString("show_schematic"), JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/mark.png"), new Object[] { SwingLocale.getString("close") }, null, true);
    }

    protected List<MonitorStat> retrieve(String traceId) throws Exception {
        int selectedValue = dataSourcePane.getSelectedValue();
        switch (selectedValue) {
            case 0:
                return MonitorController.retrieveFromSplunk(traceId, dataSourcePane.getConditions());
            case 1:
                return MonitorController.retrieveFromLog(traceId, dataSourcePane.getFilePath());
            case 2:
                return MonitorController.retrieveFromRedis(traceId, RedisType.REDIS_SENTINEL);
            case 3:
                return MonitorController.retrieveFromRedis(traceId, RedisType.REDIS_CLUSTER);
        }

        return null;
    }

    private JSecurityAction createConfigDataSourceAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocale.getString("config_data_source"), ConsoleIconFactory.getSwingIcon("netbean/action_16.png"), ConsoleLocale.getString("config_data_source")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                if (dataSourcePane == null) {
                    dataSourcePane = new DataSourcePane();
                }

                JBasicOptionPane.showOptionDialog(HandleManager.getFrame(TraceTopology.this), dataSourcePane, ConsoleLocale.getString("config_data_source"), JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/config.png"), new Object[] { SwingLocale.getString("close") }, null, true);
            }
        };

        return action;
    }

    private JSecurityAction createShowTopologyAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocale.getString("show_trace_topology"), ConsoleIconFactory.getSwingIcon("netbean/close_path_16.png"), ConsoleLocale.getString("show_trace_topology")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                showTopology(true);
            }
        };

        return action;
    }
    
    private JSecurityAction createShowTableAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocale.getString("show_trace_table"), ConsoleIconFactory.getSwingIcon("component/table_16.png"), ConsoleLocale.getString("show_trace_table")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                tableVisible = !tableVisible;
                
                showTable(tableVisible);
            }
        };

        return action;
    }

    private JSecurityAction createShowInfoAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocale.getString("show_info"), ConsoleIconFactory.getSwingIcon("netbean/hyperlink_16.png"), ConsoleLocale.getString("show_info")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                showInfo();
            }
        };

        return action;
    }
    
    private JSecurityAction createShowSchematicAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocale.getString("show_schematic"), ConsoleIconFactory.getSwingIcon("netbean/color_adjust_16.png"), ConsoleLocale.getString("show_schematic")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                showSchematic();
            }
        };

        return action;
    }

    private class DataSourcePane extends JPanel {
        private static final long serialVersionUID = 1L;

        private JPanel loggerRadioButtonPane;
        private JBasicRadioButton loggerServerRadioButton;
        private JBasicRadioButton loggerFileRadioButton;
        private JBasicTabbedPane loggerTabbedPane;
        private JPanel loggerServerPane;
        private JPanel loggerFilePane;

        private JPanel cacheRadioButtonPane;
        private JBasicRadioButton redisSentinelServerRadioButton;
        private JBasicRadioButton redisClusterServerRadioButton;
        private JBasicTabbedPane cacheTabbedPane;
        private JPanel redisSentinelServerPane;
        private JPanel redisClusterServerPane;

        private JBasicTextField sourceTypeTextField;
        private JDateTimeSelector earliestDateTimeSelector;
        private JDateTimeSelector latestDateTimeSelector;

        private JBasicTextField filePathTextField;

        private Map<String, Object> conditions = new HashMap<String, Object>();

        public DataSourcePane() {
            loggerServerRadioButton = new JBasicRadioButton(ConsoleLocale.getString("from_logger_server") + " (Splunk)", true);
            loggerServerRadioButton.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        loggerTabbedPane.setSelectedIndex(0);
                    }
                }
            });
            loggerFileRadioButton = new JBasicRadioButton(ConsoleLocale.getString("from_logger_file"), false);
            loggerFileRadioButton.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        loggerTabbedPane.setSelectedIndex(1);
                    }
                }
            });
            loggerRadioButtonPane = new JPanel();
            loggerRadioButtonPane.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.LEFT, 10));
            loggerRadioButtonPane.add(loggerServerRadioButton);
            loggerRadioButtonPane.add(loggerFileRadioButton);

            loggerServerPane = createLoggerServerPane();
            loggerFilePane = createLoggerFilePane();
            loggerTabbedPane = new JBasicTabbedPane();
            loggerTabbedPane.addTab("Splunk", loggerServerPane, "Splunk");
            loggerTabbedPane.addTab(ConsoleLocale.getString("from_logger_file"), loggerFilePane, ConsoleLocale.getString("from_logger_file"));

            redisSentinelServerRadioButton = new JBasicRadioButton(ConsoleLocale.getString("from_cache_server") + " (Redis Sentinel)", false);
            redisSentinelServerRadioButton.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        cacheTabbedPane.setSelectedIndex(0);
                    }
                }
            });
            redisClusterServerRadioButton = new JBasicRadioButton(ConsoleLocale.getString("from_cache_server") + " (Redis Cluster)", false);
            redisClusterServerRadioButton.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        cacheTabbedPane.setSelectedIndex(1);
                    }
                }
            });
            cacheRadioButtonPane = new JPanel();
            cacheRadioButtonPane.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.LEFT, 10));
            cacheRadioButtonPane.add(redisSentinelServerRadioButton);
            cacheRadioButtonPane.add(redisClusterServerRadioButton);

            redisSentinelServerPane = createCacheSentinelServerPane();
            redisClusterServerPane = createCacheClusterServerPane();
            cacheTabbedPane = new JBasicTabbedPane();
            cacheTabbedPane.addTab("Redis Sentinel", redisSentinelServerPane, "Redis Sentinel");
            cacheTabbedPane.addTab("Redis Cluster", redisClusterServerPane, "Redis Cluster");

            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(loggerServerRadioButton);
            buttonGroup.add(loggerFileRadioButton);
            buttonGroup.add(redisSentinelServerRadioButton);
            buttonGroup.add(redisClusterServerRadioButton);

            setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 5));
            add(loggerRadioButtonPane);
            add(loggerTabbedPane);
            add(Box.createVerticalStrut(5));
            add(cacheRadioButtonPane);
            add(cacheTabbedPane);
            setPreferredSize(new Dimension(400, getPreferredSize().height));
        }

        public int getSelectedValue() {
            if (loggerServerRadioButton.isSelected()) {
                return 0;
            }

            if (loggerFileRadioButton.isSelected()) {
                return 1;
            }

            if (redisSentinelServerRadioButton.isSelected()) {
                return 2;
            }

            if (redisClusterServerRadioButton.isSelected()) {
                return 3;
            }

            return -1;
        }

        public Map<String, Object> getConditions() {
            conditions.clear();
            conditions.put(ThunderConstants.SPLUNK_SOURCE_TYPE_ATTRIBUTE_NAME, sourceTypeTextField.getText());
            conditions.put(ThunderConstants.SPLUNK_EARLIEST_TIME_ATTRIBUTE_NAME, earliestDateTimeSelector.getDate());
            conditions.put(ThunderConstants.SPLUNK_LATEST_TIME_ATTRIBUTE_NAME, latestDateTimeSelector.getDate());

            return conditions;
        }

        public String getFilePath() {
            return filePathTextField.getText().trim();
        }

        @SuppressWarnings("unchecked")
        private JPanel createLoggerServerPane() {
            if (loggerServerPane == null) {
                String host = null;
                try {
                    host = PropertiesContext.getLoggerHost();
                } catch (Exception e) {

                }

                Vector<String> hosts = new Vector<String>();
                if (StringUtils.isNotEmpty(host)) {
                    hosts.add(host);
                }

                final JBasicComboBox hostComboBox = new JBasicComboBox(hosts);
                hostComboBox.setEditable(true);
                hostComboBox.setRenderer(new DefaultListCellRenderer() {
                    private static final long serialVersionUID = 1L;

                    @SuppressWarnings("rawtypes")
                    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                        setToolTipText(value.toString());

                        return this;
                    }
                });

                int port = PropertiesContext.getLoggerPort();
                final JBasicTextField portTextField = new JBasicTextField();
                if (port != -1) {
                    portTextField.setText(port + "");
                }

                String userName = PropertiesContext.getLoggerUserName();
                final JBasicTextField userNameTextField = new JBasicTextField();
                if (StringUtils.isNotEmpty(userName)) {
                    userNameTextField.setText(userName);
                }

                String password = PropertiesContext.getLoggerPassword();
                final JBasicPasswordField passwordField = new JBasicPasswordField();
                if (StringUtils.isNotEmpty(password)) {
                    passwordField.setText(password);
                }

                sourceTypeTextField = new JBasicTextField();
                sourceTypeTextField.setText(ThunderConstants.NAMESPACE_ELEMENT_NAME);
                earliestDateTimeSelector = new JDateTimeSelector(new Date(), SplunkLogServiceMonitorRetriever.DATE_FORMAT, new JSpinnerDateTimeEditor());
                latestDateTimeSelector = new JDateTimeSelector(new Date(), SplunkLogServiceMonitorRetriever.DATE_FORMAT, new JSpinnerDateTimeEditor());

                JClassicButton startButton = new JClassicButton(ConsoleLocale.getString("start"), ConsoleIconFactory.getSwingIcon("stereo/control_play_16.png"), ConsoleLocale.getString("start"));
                startButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Map<String, Object> splunkValue = new HashMap<String, Object>();
                        splunkValue.put(ThunderConstants.SPLUNK_HOST_ATTRIBUTE_NAME, hostComboBox.getSelectedItem().toString());
                        splunkValue.put(ThunderConstants.SPLUNK_PORT_ATTRIBUTE_NAME, Integer.valueOf(portTextField.getText().trim()));
                        splunkValue.put(ThunderConstants.SPLUNK_USER_NAME_ATTRIBUTE_NAME, userNameTextField.getText().trim());
                        splunkValue.put(ThunderConstants.SPLUNK_PASSWORD_ATTRIBUTE_NAME, passwordField.getPasswordText().trim());
                        PropertiesContext.addPropertiesMap(splunkValue);

                        try {
                            MonitorController.getSplunkLogServiceMonitorRetriever().dispose();
                            MonitorController.getSplunkLogServiceMonitorRetriever().initialize(PropertiesContext.getProperties());
                        } catch (Exception ex) {

                        }

                        if (MonitorController.getSplunkLogServiceMonitorRetriever().enabled()) {
                            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(TraceTopology.this), ConsoleLocale.getString("start_success"), SwingLocale.getString("information"), JBasicOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(TraceTopology.this), ConsoleLocale.getString("start_failure"), SwingLocale.getString("error"), JBasicOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.LEFT, 5));
                buttonPanel.add(startButton);

                double[][] size = {
                        { 120, TableLayout.FILL },
                        { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }
                };

                TableLayout tableLayout = new TableLayout(size);
                tableLayout.setVGap(5);

                loggerServerPane = new JPanel();
                loggerServerPane.setLayout(tableLayout);
                loggerServerPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0), UIUtil.createTitledBorder("")));
                loggerServerPane.add(new JBasicLabel("Splunk Host"), "0, 0");
                loggerServerPane.add(hostComboBox, "1, 0");
                loggerServerPane.add(new JBasicLabel("Splunk Port"), "0, 1");
                loggerServerPane.add(portTextField, "1, 1");
                loggerServerPane.add(new JBasicLabel("Splunk User Name"), "0, 2");
                loggerServerPane.add(userNameTextField, "1, 2");
                loggerServerPane.add(new JBasicLabel("Splunk Password"), "0, 3");
                loggerServerPane.add(passwordField, "1, 3");
                loggerServerPane.add(new JBasicLabel("Filter Source Type"), "0, 4");
                loggerServerPane.add(sourceTypeTextField, "1, 4");
                loggerServerPane.add(new JBasicLabel("Filter Earliest Time"), "0, 5");
                loggerServerPane.add(earliestDateTimeSelector, "1, 5");
                loggerServerPane.add(new JBasicLabel("Filter Latest Time"), "0, 6");
                loggerServerPane.add(latestDateTimeSelector, "1, 6");
                loggerServerPane.add(buttonPanel, "1, 7");
            }

            return loggerServerPane;
        }

        private JPanel createLoggerFilePane() {
            if (loggerFilePane == null) {
                filePathTextField = new JBasicTextField();
                filePathTextField.setText(PropertiesContext.getLoggerFilePath());

                JClassicButton selectorButton = new JClassicButton(ConsoleIconFactory.getSwingIcon("stereo/import_16.png"), ConsoleLocale.getString("open_logger_file"));
                selectorButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JFileSelector fileSelector = new JFileSelector(HandleManager.getFrame(TraceTopology.this), ConsoleLocale.getString("open_logger_file"));
                        File file = fileSelector.openFile();
                        if (file != null) {
                            filePathTextField.setText(file.getAbsolutePath());
                        }
                    }
                });

                double[][] size = {
                        { 120, TableLayout.FILL, TableLayout.PREFERRED },
                        { TableLayout.PREFERRED }
                };

                TableLayout tableLayout = new TableLayout(size);
                tableLayout.setVGap(5);

                loggerFilePane = new JPanel();
                loggerFilePane.setLayout(tableLayout);
                loggerFilePane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0), UIUtil.createTitledBorder("")));
                loggerFilePane.add(new JBasicLabel(ConsoleLocale.getString("logger_file_path")), "0, 0");
                loggerFilePane.add(filePathTextField, "1, 0");
                loggerFilePane.add(selectorButton, "2, 0");
            }

            return loggerFilePane;
        }

        @SuppressWarnings("unchecked")
        private JPanel createCacheSentinelServerPane() {
            if (redisSentinelServerPane == null) {
                String sentinel = null;
                try {
                    sentinel = PropertiesContext.getRedisSentinel();
                } catch (Exception e) {

                }

                Vector<String> sentinels = new Vector<String>();
                if (StringUtils.isNotEmpty(sentinel)) {
                    sentinels.add(sentinel);
                }

                final JBasicComboBox sentinelComboBox = new JBasicComboBox(sentinels);
                sentinelComboBox.setEditable(true);
                sentinelComboBox.setRenderer(new DefaultListCellRenderer() {
                    private static final long serialVersionUID = 1L;

                    @SuppressWarnings("rawtypes")
                    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                        setToolTipText(value.toString());

                        return this;
                    }
                });

                String masterName = PropertiesContext.getRedisMasterName();
                final JBasicTextField masterNameTextField = new JBasicTextField();
                if (StringUtils.isNotEmpty(masterName)) {
                    masterNameTextField.setText(masterName);
                }

                String clientName = PropertiesContext.getRedisClientName();
                final JBasicTextField clientNameTextField = new JBasicTextField();
                if (StringUtils.isNotEmpty(clientName)) {
                    clientNameTextField.setText(clientName);
                }

                String password = PropertiesContext.getRedisPassword();
                final JBasicPasswordField passwordField = new JBasicPasswordField();
                if (StringUtils.isNotEmpty(password)) {
                    passwordField.setText(password);
                }

                final JClassicButton startButton = new JClassicButton(ConsoleLocale.getString("start"), ConsoleIconFactory.getSwingIcon("stereo/control_play_16.png"), ConsoleLocale.getString("start"));
                final JClassicButton stopButton = new JClassicButton(ConsoleLocale.getString("stop"), ConsoleIconFactory.getSwingIcon("stereo/control_stop_16.png"), ConsoleLocale.getString("stop"));

                startButton.setEnabled(true);
                stopButton.setEnabled(false);

                startButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Map<String, Object> sentinelValue = new HashMap<String, Object>();
                        sentinelValue.put(ThunderConstants.REDIS_SENTINEL_ATTRIBUTE_NAME, sentinelComboBox.getSelectedItem().toString());
                        sentinelValue.put(ThunderConstants.REDIS_MASTER_NAME_ATTRIBUTE_NAME, masterNameTextField.getText().trim());
                        sentinelValue.put(ThunderConstants.REDIS_CLIENT_NAME_ATTRIBUTE_NAME, clientNameTextField.getText().trim());
                        sentinelValue.put(ThunderConstants.REDIS_PASSWORD_ATTRIBUTE_NAME, passwordField.getPasswordText().trim());

                        PropertiesContext.addPropertiesMap(sentinelValue);
                        CacheContext.startSentinel();

                        if (CacheContext.sentinelEnabled()) {
                            startButton.setEnabled(false);
                            stopButton.setEnabled(true);

                            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(TraceTopology.this), ConsoleLocale.getString("start_success"), SwingLocale.getString("information"), JBasicOptionPane.INFORMATION_MESSAGE);
                        } else {
                            startButton.setEnabled(true);
                            stopButton.setEnabled(false);

                            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(TraceTopology.this), ConsoleLocale.getString("start_failure"), SwingLocale.getString("error"), JBasicOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                stopButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        CacheContext.stopSentinel();

                        startButton.setEnabled(true);
                        stopButton.setEnabled(false);
                    }
                });

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.LEFT, 5));
                buttonPanel.add(startButton);
                buttonPanel.add(stopButton);

                double[][] size = {
                        { 120, TableLayout.FILL },
                        { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }
                };

                TableLayout tableLayout = new TableLayout(size);
                tableLayout.setVGap(5);

                redisSentinelServerPane = new JPanel();
                redisSentinelServerPane.setLayout(tableLayout);
                redisSentinelServerPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0), UIUtil.createTitledBorder("")));
                redisSentinelServerPane.add(new JBasicLabel("Redis Sentinel"), "0, 0");
                redisSentinelServerPane.add(sentinelComboBox, "1, 0");
                redisSentinelServerPane.add(new JBasicLabel("Redis Master Name"), "0, 1");
                redisSentinelServerPane.add(masterNameTextField, "1, 1");
                redisSentinelServerPane.add(new JBasicLabel("Redis Client Name"), "0, 2");
                redisSentinelServerPane.add(clientNameTextField, "1, 2");
                redisSentinelServerPane.add(new JBasicLabel("Redis Password"), "0, 3");
                redisSentinelServerPane.add(passwordField, "1, 3");
                redisSentinelServerPane.add(buttonPanel, "1, 4");
            }

            return redisSentinelServerPane;
        }

        @SuppressWarnings("unchecked")
        private JPanel createCacheClusterServerPane() {
            if (redisClusterServerPane == null) {
                String cluster = null;
                try {
                    cluster = PropertiesContext.getRedisCluster();
                } catch (Exception e) {

                }

                Vector<String> clusters = new Vector<String>();
                if (StringUtils.isNotEmpty(cluster)) {
                    clusters.add(cluster);
                }

                final JBasicComboBox clusterComboBox = new JBasicComboBox(clusters);
                clusterComboBox.setEditable(true);
                clusterComboBox.setRenderer(new DefaultListCellRenderer() {
                    private static final long serialVersionUID = 1L;

                    @SuppressWarnings("rawtypes")
                    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                        setToolTipText(value.toString());

                        return this;
                    }
                });

                final JClassicButton startButton = new JClassicButton(ConsoleLocale.getString("start"), ConsoleIconFactory.getSwingIcon("stereo/control_play_16.png"), ConsoleLocale.getString("start"));
                final JClassicButton stopButton = new JClassicButton(ConsoleLocale.getString("stop"), ConsoleIconFactory.getSwingIcon("stereo/control_stop_16.png"), ConsoleLocale.getString("stop"));

                startButton.setEnabled(true);
                stopButton.setEnabled(false);

                startButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Map<String, Object> sentinelValue = new HashMap<String, Object>();
                        sentinelValue.put(ThunderConstants.REDIS_CLUSTER_ATTRIBUTE_NAME, clusterComboBox.getSelectedItem().toString());

                        PropertiesContext.addPropertiesMap(sentinelValue);
                        CacheContext.startCluster();

                        if (CacheContext.clusterEnabled()) {
                            startButton.setEnabled(false);
                            stopButton.setEnabled(true);

                            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(TraceTopology.this), ConsoleLocale.getString("start_success"), SwingLocale.getString("information"), JBasicOptionPane.INFORMATION_MESSAGE);
                        } else {
                            startButton.setEnabled(true);
                            stopButton.setEnabled(false);

                            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(TraceTopology.this), ConsoleLocale.getString("start_failure"), SwingLocale.getString("error"), JBasicOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                stopButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        CacheContext.stopCluster();

                        startButton.setEnabled(true);
                        stopButton.setEnabled(false);
                    }
                });

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.LEFT, 5));
                buttonPanel.add(startButton);
                buttonPanel.add(stopButton);

                double[][] size = {
                        { 120, TableLayout.FILL },
                        { TableLayout.PREFERRED, TableLayout.PREFERRED }
                };

                TableLayout tableLayout = new TableLayout(size);
                tableLayout.setVGap(5);

                redisClusterServerPane = new JPanel();
                redisClusterServerPane.setLayout(tableLayout);
                redisClusterServerPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0), UIUtil.createTitledBorder("")));
                redisClusterServerPane.add(new JBasicLabel("Redis Cluster"), "0, 0");
                redisClusterServerPane.add(clusterComboBox, "1, 0");
                redisClusterServerPane.add(buttonPanel, "1, 1");
            }

            return redisClusterServerPane;
        }
    }

    private class LayoutDialog extends JOptionDialog {
        private static final long serialVersionUID = 1L;

        private JNumberTextField groupStartXTextField;
        private JNumberTextField groupStartYTextField;
        private JNumberTextField groupHorizontalGapTextField;
        private JNumberTextField groupVerticalGapTextField;

        private JNumberTextField nodeStartXTextField;
        private JNumberTextField nodeStartYTextField;
        private JNumberTextField nodeHorizontalGapTextField;
        private JNumberTextField nodeVerticalGapTextField;

        public LayoutDialog() {
            super(HandleManager.getFrame(TraceTopology.this), SwingLocale.getString("layout"), new Dimension(500, 320), true, false, true);

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

            JPanel groupPanel = new JPanel();
            groupPanel.setLayout(tableLayout);
            groupPanel.setBorder(UIUtil.createTitledBorder(ConsoleLocale.getString("group_layout")));
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
            int groupStartX = 0;
            int groupStartY = 0;
            int groupHorizontalGap = 0;
            int groupVerticalGap = 0;
            int nodeStartX = 0;
            int nodeStartY = 0;
            int nodeHorizontalGap = 0;
            int nodeVerticalGap = 0;

            try {
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

            TraceTopology.this.groupStartX = groupStartX;
            TraceTopology.this.groupStartY = groupStartY;
            TraceTopology.this.groupHorizontalGap = groupHorizontalGap;
            TraceTopology.this.groupVerticalGap = groupVerticalGap;
            TraceTopology.this.nodeStartX = nodeStartX;
            TraceTopology.this.nodeStartY = nodeStartY;
            TraceTopology.this.nodeHorizontalGap = nodeHorizontalGap;
            TraceTopology.this.nodeVerticalGap = nodeVerticalGap;

            return true;
        }
    }
}