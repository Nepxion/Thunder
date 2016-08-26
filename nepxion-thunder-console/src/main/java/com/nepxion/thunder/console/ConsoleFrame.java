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

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.nepxion.swing.frame.JBasicFrame;
import com.nepxion.swing.framework.reflection.JReflectionHierarchy;
import com.nepxion.swing.style.texture.shrink.JBlackHeaderTextureStyle;
import com.nepxion.swing.style.texture.shrink.JGreenOutlookTextureStyle;
import com.nepxion.thunder.console.context.CacheContext;
import com.nepxion.thunder.console.context.RegistryContext;
import com.nepxion.thunder.console.context.UserContext;
import com.nepxion.thunder.console.icon.ConsoleIconFactory;
import com.nepxion.thunder.console.locale.ConsoleLocale;

public class ConsoleFrame extends JBasicFrame {
    private static final long serialVersionUID = 1L;

    public ConsoleFrame() {
        super(ConsoleLocale.getString("title"), ConsoleIconFactory.getSwingIcon("ribbon/navigator_nepxion.png"), new Dimension(1280, 700));
    }

    public void launch() {
        ConsoleHierarchy deployHierarchy = new ConsoleHierarchy(new JBlackHeaderTextureStyle(), new JGreenOutlookTextureStyle());

        JReflectionHierarchy reflectionHierarchy = new JReflectionHierarchy(20, 20);
        reflectionHierarchy.setContentPane(deployHierarchy);

        getContentPane().add(reflectionHierarchy);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                RegistryContext.stop();
                CacheContext.stop();
            }
        });

        setTitle(ConsoleLocale.getString("title") + " [" + UserContext.getUserEntity().getName() + "]");
        setExtendedState(ConsoleFrame.MAXIMIZED_BOTH);
        setVisible(true);
        toFront();
    }
}