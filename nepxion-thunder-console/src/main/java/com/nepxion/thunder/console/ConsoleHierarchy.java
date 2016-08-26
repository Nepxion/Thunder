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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.nepxion.swing.element.ElementNode;
import com.nepxion.swing.list.toggle.JToggleList;
import com.nepxion.swing.shrinkbar.JShrinkBar;
import com.nepxion.swing.shrinkbar.JShrinkOutlook;
import com.nepxion.swing.shrinkbar.JShrinkOutlookBar;
import com.nepxion.swing.shrinkbar.ShrinkListCellRenderer;
import com.nepxion.swing.shrinkbar.ShrinkOutlookSelectionListener;
import com.nepxion.swing.style.texture.shrink.IHeaderTextureStyle;
import com.nepxion.swing.style.texture.shrink.IOutlookTextureStyle;
import com.nepxion.thunder.common.entity.UserEntity;
import com.nepxion.thunder.common.entity.UserOperation;
import com.nepxion.thunder.console.context.UIContext;
import com.nepxion.thunder.console.context.UserContext;
import com.nepxion.thunder.console.icon.ConsoleIconFactory;
import com.nepxion.thunder.console.locale.ConsoleLocale;
import com.nepxion.thunder.console.toggle.ConsoleToggleConstants;
import com.nepxion.thunder.console.toggle.ConsoleToggleListener;
import com.nepxion.util.data.CollectionUtil;

public class ConsoleHierarchy extends JPanel {
    private static final long serialVersionUID = 1L;

    private JShrinkBar shrinkContentBar;
    private JShrinkOutlookBar shrinkOutlookBar;

    public ConsoleHierarchy(IHeaderTextureStyle headerTextureStyle, IOutlookTextureStyle outlookTextureStyle) {
        shrinkContentBar = new JShrinkBar(JShrinkBar.PLACEMENT_EAST, JShrinkBar.CONTENT_PANE_TYPE_LABEL, headerTextureStyle);
        shrinkContentBar.setShrinkable(false);
        shrinkContentBar.setTitle(ConsoleLocale.getString("content_bar"));
        shrinkContentBar.setToolTipText(ConsoleLocale.getString("content_bar"));
        shrinkContentBar.setIcon(ConsoleIconFactory.getSwingIcon("paste.png"));
        shrinkContentBar.setTitleFont(new Font(UIContext.getFontName(), Font.BOLD, UIContext.getLargeFontSize()));
        shrinkContentBar.getShrinkHeader().getLabel().addMouseListener(new ShrinkContentBarMouseListener());

        shrinkOutlookBar = new JShrinkOutlookBar(JShrinkBar.PLACEMENT_WEST, JShrinkBar.CONTENT_PANE_TYPE_LABEL, headerTextureStyle, outlookTextureStyle);
        shrinkOutlookBar.setTitle(ConsoleLocale.getString("navigator_bar"));
        shrinkOutlookBar.setToolTipText(ConsoleLocale.getString("navigator_bar"));
        shrinkOutlookBar.setIcon(ConsoleIconFactory.getSwingIcon("hierarchy.png"));
        shrinkOutlookBar.setTitleFont(new Font(UIContext.getFontName(), Font.BOLD, UIContext.getLargeFontSize()));
        shrinkOutlookBar.setPreferredSize(new Dimension(210, shrinkOutlookBar.getPreferredSize().height));

        UserEntity userEntity = UserContext.getUserEntity();
        if (userEntity.hasOperation(UserOperation.SERVICE_CONTROL)) {
            createServiceControlShrinkOutlook(shrinkOutlookBar);
        }

        if (userEntity.hasOperation(UserOperation.REMOTE_CONTROL)) {
            createRemoteControlShrinkOutlook(shrinkOutlookBar);
        }

        if (userEntity.hasOperation(UserOperation.SAFETY_CONTROL)) {
            createSafetyCotrolShrinkOutlook(shrinkOutlookBar);
        }

        createUserControlShrinkOutlook(shrinkOutlookBar);

        shrinkOutlookBar.getShrinkOutlook(0).setSelected(true);

        setLayout(new BorderLayout(5, 5));
        add(shrinkContentBar, BorderLayout.CENTER);
        add(shrinkOutlookBar, BorderLayout.WEST);
    }

    private JShrinkOutlook createServiceControlShrinkOutlook(JShrinkOutlookBar shrinkOutlookBar) {
        List<ElementNode> elementNodes = new ArrayList<ElementNode>();
        elementNodes.add(new ElementNode(ConsoleToggleConstants.DEPLOY_SUMMARY, ConsoleLocale.getString(ConsoleToggleConstants.DEPLOY_SUMMARY), ConsoleIconFactory.getSwingIcon("component/ui_16.png"), ConsoleLocale.getString(ConsoleToggleConstants.DEPLOY_SUMMARY)));
        elementNodes.add(new ElementNode(ConsoleToggleConstants.INVOKE_TRACE, ConsoleLocale.getString(ConsoleToggleConstants.INVOKE_TRACE), ConsoleIconFactory.getSwingIcon("netbean/close_path_16.png"), ConsoleLocale.getString(ConsoleToggleConstants.INVOKE_TRACE)));

        JToggleList list = createList(elementNodes);
        list.setSelectedIndex(0);

        JShrinkOutlook shrinkOutlook = shrinkOutlookBar.addShrinkOutlook(ConsoleLocale.getString("service_control"), ConsoleIconFactory.getSwingIcon("stereo/favorite_16.png"), ConsoleIconFactory.getSwingIcon("stereo/favorite_add_16.png"), ConsoleLocale.getString("service_control"), new Font(UIContext.getFontName(), Font.BOLD, UIContext.getMiddleFontSize()));
        shrinkOutlook.setContentPane(list);
        shrinkOutlook.addPropertyChangeListener(new OutlookSelectionListener());

        return shrinkOutlook;
    }

    private JShrinkOutlook createRemoteControlShrinkOutlook(JShrinkOutlookBar shrinkOutlookBar) {
        List<ElementNode> elementNodes = new ArrayList<ElementNode>();
        elementNodes.add(new ElementNode(ConsoleToggleConstants.REMOTE_CONFIG, ConsoleLocale.getString(ConsoleToggleConstants.REMOTE_CONFIG), ConsoleIconFactory.getSwingIcon("netbean/canvas_16.png"), ConsoleLocale.getString(ConsoleToggleConstants.REMOTE_CONFIG)));

        JToggleList list = createList(elementNodes);
        list.setSelectedIndex(0);

        JShrinkOutlook shrinkOutlook = shrinkOutlookBar.addShrinkOutlook(ConsoleLocale.getString("remote_control"), ConsoleIconFactory.getSwingIcon("stereo/favorite_16.png"), ConsoleIconFactory.getSwingIcon("stereo/favorite_add_16.png"), ConsoleLocale.getString("remote_control"), new Font(UIContext.getFontName(), Font.BOLD, UIContext.getMiddleFontSize()));
        shrinkOutlook.setContentPane(list);
        shrinkOutlook.addPropertyChangeListener(new OutlookSelectionListener());

        return shrinkOutlook;
    }

    private JShrinkOutlook createSafetyCotrolShrinkOutlook(JShrinkOutlookBar shrinkOutlookBar) {
        List<ElementNode> elementNodes = new ArrayList<ElementNode>();
        elementNodes.add(new ElementNode(ConsoleToggleConstants.FREQUENCY_CONTROL, ConsoleLocale.getString(ConsoleToggleConstants.FREQUENCY_CONTROL), ConsoleIconFactory.getSwingIcon("netbean/action_16.png"), ConsoleLocale.getString(ConsoleToggleConstants.FREQUENCY_CONTROL)));
        elementNodes.add(new ElementNode(ConsoleToggleConstants.SECRET_KEY_CONTROL, ConsoleLocale.getString(ConsoleToggleConstants.SECRET_KEY_CONTROL), ConsoleIconFactory.getSwingIcon("netbean/key_frame_16.png"), ConsoleLocale.getString(ConsoleToggleConstants.SECRET_KEY_CONTROL)));
        elementNodes.add(new ElementNode(ConsoleToggleConstants.VERSION_CONTROL, ConsoleLocale.getString(ConsoleToggleConstants.VERSION_CONTROL), ConsoleIconFactory.getSwingIcon("netbean/custom_node_16.png"), ConsoleLocale.getString(ConsoleToggleConstants.VERSION_CONTROL)));
        elementNodes.add(new ElementNode(ConsoleToggleConstants.RESET_CONTROL, ConsoleLocale.getString(ConsoleToggleConstants.RESET_CONTROL), ConsoleIconFactory.getSwingIcon("netbean/rotate_16.png"), ConsoleLocale.getString(ConsoleToggleConstants.RESET_CONTROL)));

        JToggleList list = createList(elementNodes);
        list.setSelectedIndex(0);

        JShrinkOutlook shrinkOutlook = shrinkOutlookBar.addShrinkOutlook(ConsoleLocale.getString("safety_control"), ConsoleIconFactory.getSwingIcon("stereo/favorite_16.png"), ConsoleIconFactory.getSwingIcon("stereo/favorite_add_16.png"), ConsoleLocale.getString("safety_control"), new Font(UIContext.getFontName(), Font.BOLD, UIContext.getMiddleFontSize()));
        shrinkOutlook.setContentPane(list);
        shrinkOutlook.addPropertyChangeListener(new OutlookSelectionListener());

        return shrinkOutlook;
    }

    private JShrinkOutlook createUserControlShrinkOutlook(JShrinkOutlookBar shrinkOutlookBar) {
        List<ElementNode> elementNodes = new ArrayList<ElementNode>();
        UserEntity userEntity = UserContext.getUserEntity();
        if (userEntity.hasOperation(UserOperation.USER_CONTROL)) {
            elementNodes.add(new ElementNode(ConsoleToggleConstants.USER_CONTROL, ConsoleLocale.getString(ConsoleToggleConstants.USER_CONTROL), ConsoleIconFactory.getSwingIcon("user.png"), ConsoleLocale.getString(ConsoleToggleConstants.USER_CONTROL)));
        }
        elementNodes.add(new ElementNode(ConsoleToggleConstants.PASSWORD_CONTROL, ConsoleLocale.getString(ConsoleToggleConstants.PASSWORD_CONTROL), ConsoleIconFactory.getSwingIcon("user_password.png"), ConsoleLocale.getString(ConsoleToggleConstants.PASSWORD_CONTROL)));

        JToggleList list = createList(elementNodes);
        list.setSelectedIndex(0);

        JShrinkOutlook shrinkOutlook = shrinkOutlookBar.addShrinkOutlook(ConsoleLocale.getString("user_control"), ConsoleIconFactory.getSwingIcon("stereo/favorite_16.png"), ConsoleIconFactory.getSwingIcon("stereo/favorite_add_16.png"), ConsoleLocale.getString("user_control"), new Font(UIContext.getFontName(), Font.BOLD, UIContext.getMiddleFontSize()));
        shrinkOutlook.setContentPane(list);
        shrinkOutlook.addPropertyChangeListener(new OutlookSelectionListener());

        return shrinkOutlook;
    }

    @SuppressWarnings("unchecked")
    private JToggleList createList(List<ElementNode> elementNodes) {
        JToggleList list = new JToggleList(CollectionUtil.parseVector(elementNodes));
        list.setSelectionMode(JToggleList.SINGLE_SELECTION);
        list.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        list.setCellRenderer(new ShrinkListCellRenderer(list, BorderFactory.createEmptyBorder(0, 10, 0, 0), 22));
        list.setToggleContentPanel(shrinkContentBar);
        list.setToggleAdapter(new ConsoleToggleListener(list));

        return list;
    }

    private class ShrinkContentBarMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() > 1) {
                boolean isShrinked = !shrinkOutlookBar.isShrinked();
                shrinkOutlookBar.setShrinked(isShrinked);
            }
        }
    }

    private class OutlookSelectionListener extends ShrinkOutlookSelectionListener {
        public void selectionStateChanged(JShrinkOutlook shrinkOutlook) {
            JToggleList toggleList = (JToggleList) shrinkOutlook.getContentPane();
            toggleList.executeSelection(-1, toggleList.getSelectedIndex());
        }
    }
}