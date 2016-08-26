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

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.nepxion.swing.separator.JBasicSeparator;
import com.nepxion.thunder.console.context.UIContext;

public class UIUtil {
    public static JBasicSeparator createSeparator() {
        JBasicSeparator separator = new JBasicSeparator(JBasicSeparator.HORIZONTAL, JBasicSeparator.LOWERED_STYLE, -1);
        separator.setBrightColor(new Color(197, 196, 198));
        separator.setDarkColor(new Color(153, 152, 154));

        return separator;
    }

    public static Border createTitledBorder(String title) {
        return BorderFactory.createTitledBorder(null, title, TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, new Font(UIContext.getFontName(), Font.PLAIN, UIContext.getLargeFontSize()), new Color(64, 0, 0));
    }
}
