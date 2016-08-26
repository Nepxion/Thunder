package com.nepxion.thunder.console.workspace.common;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.separator.JBasicSeparator;

public class Workspace extends JPanel {
    private static final long serialVersionUID = 1L;

    public Workspace() {
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 0));
    }

    protected JBasicSeparator createSeparator() {
        return UIUtil.createSeparator();
    }

    protected Border createTitledBorder(String title) {
        return UIUtil.createTitledBorder(title);
    }
}