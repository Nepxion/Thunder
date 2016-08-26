package com.nepxion.thunder.console.toggle;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.swing.element.IElementNode;
import com.nepxion.swing.list.toggle.AbstractToggleAdapter;
import com.nepxion.swing.list.toggle.JToggleList;
import com.nepxion.swing.toggle.ITogglePanel;

public class ConsoleToggleListener extends AbstractToggleAdapter {
    public ConsoleToggleListener(JToggleList list) {
        super(list);
    }

    public ITogglePanel getTogglePanel(IElementNode elementNode) {
        Object userObject = elementNode.getUserObject();
        if (userObject == null) {
            userObject = new ConsoleTogglePanel(elementNode);
            elementNode.setUserObject(userObject);
        }

        return (ITogglePanel) userObject;
    }
}