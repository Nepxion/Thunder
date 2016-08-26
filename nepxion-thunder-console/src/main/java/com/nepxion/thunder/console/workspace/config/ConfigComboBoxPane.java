package com.nepxion.thunder.console.workspace.config;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

import org.apache.commons.lang3.ArrayUtils;

import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.element.ElementNode;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.renderer.combobox.ElementComboBoxCellRenderer;

public abstract class ConfigComboBoxPane extends JPanel {
    private static final long serialVersionUID = 1L;

    private JBasicLabel label;
    private JBasicComboBox comboBox;

    @SuppressWarnings("unchecked")
    public ConfigComboBoxPane(String labelText) {
        label = new JBasicLabel(labelText);

        comboBox = new JBasicComboBox();
        comboBox.setRenderer(new ElementComboBoxCellRenderer());
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ElementNode elementNode = (ElementNode) comboBox.getSelectedItem();
                itemSelection(elementNode);
            }
        });

        double[][] size = {
                { 100, TableLayout.FILL },
                { TableLayout.PREFERRED }
        };

        TableLayout tableLayout = new TableLayout(size);
        tableLayout.setHGap(0);
        tableLayout.setVGap(5);

        setLayout(tableLayout);
        add(label, "0, 0");
        add(comboBox, "1, 0");
    }

    @SuppressWarnings("unchecked")
    public void setData(ElementNode[] nodes) {
        if (ArrayUtils.isNotEmpty(nodes)) {
            DefaultComboBoxModel<ElementNode> comboBoxModel = new DefaultComboBoxModel<ElementNode>(nodes);
            comboBox.setModel(comboBoxModel);
            comboBox.setSelectedIndex(0);
        } else {
            DefaultComboBoxModel<ElementNode> comboBoxModel = new DefaultComboBoxModel<ElementNode>();
            comboBox.setModel(comboBoxModel);
            itemSelection(null);
        }
    }

    protected abstract void itemSelection(ElementNode elementNode);
}