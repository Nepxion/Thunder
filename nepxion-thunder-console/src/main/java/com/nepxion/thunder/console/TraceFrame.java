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
import com.nepxion.thunder.console.context.CacheContext;
import com.nepxion.thunder.console.icon.ConsoleIconFactory;
import com.nepxion.thunder.console.locale.ConsoleLocale;
import com.nepxion.thunder.console.workspace.TraceControlWorkspace;

public class TraceFrame extends JBasicFrame {
    private static final long serialVersionUID = 1L;

    public TraceFrame() {
        super(ConsoleLocale.getString("trace_title"), ConsoleIconFactory.getSwingIcon("ribbon/navigator_nepxion.png"), new Dimension(1280, 700));
    }
        
    public void launch() {
        getContentPane().add(createWorkspace());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CacheContext.stop();
            }
        });

        setExtendedState(TraceFrame.MAXIMIZED_BOTH);
        setVisible(true);
        toFront();
    }
    
    protected TraceControlWorkspace createWorkspace() {
        return new TraceControlWorkspace();
    }
}