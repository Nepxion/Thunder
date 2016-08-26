package com.nepxion.thunder.testcase.algorithm;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;

import com.nepxion.thunder.common.entity.ApplicationEntity;

public class HashAlgorithmFactory {
    public static List<ApplicationEntity> getNodeList(int count, int index) {
        List<ApplicationEntity> nodeList = new ArrayList<ApplicationEntity>();
        for (int i = index; i < index + count; i++) {
            ApplicationEntity node = new ApplicationEntity();
            node.setApplication("application");
            node.setGroup("group");
            node.setHost(i + "." + i + "." + i + "." + i);
            node.setPort(i);
            nodeList.add(node);
        }

        return nodeList;
    }
}