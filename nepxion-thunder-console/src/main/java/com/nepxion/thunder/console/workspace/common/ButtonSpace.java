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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.nepxion.swing.button.ButtonManager;
import com.nepxion.swing.dimension.DimensionManager;

public class ButtonSpace extends JPanel {
    private static final long serialVersionUID = 1L;

    public ButtonSpace() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    }

    public void updateLayout() {
        int width = 0;
        int height = 0;
        for (int i = 0; i < getComponentCount(); i++) {
            Component component = getComponent(i);
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setMargin(new Insets(2, 0, 2, 0));
                button.setIconTextGap(0);
                ButtonManager.setButtonLayout(button, new int[] { SwingConstants.VERTICAL, SwingConstants.CENTER });
            }

            Dimension preferredSize = component.getPreferredSize();
            if (preferredSize.width > width) {
                width = preferredSize.width;
            }

            if (preferredSize.height > height) {
                height = preferredSize.height;
            }
        }

        Dimension preferredSize = new Dimension(width, height);

        for (int i = 0; i < getComponentCount(); i++) {
            Component component = getComponent(i);
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                DimensionManager.setDimension(button, preferredSize);
            }
        }
    }
}