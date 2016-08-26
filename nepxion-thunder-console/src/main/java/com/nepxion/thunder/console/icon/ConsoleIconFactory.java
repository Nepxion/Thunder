package com.nepxion.thunder.console.icon;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import javax.swing.ImageIcon;

import com.nepxion.swing.icon.IconFactory;

public class ConsoleIconFactory extends IconFactory {
    public static final String ICON_FOLDER = "com/nepxion/thunder/console/icon/";

    public static ImageIcon getContextIcon(String iconName) {
        return getIcon(ICON_FOLDER + iconName);
    }
}