package com.nepxion.thunder.testcase.algorithm;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.cluster.loadbalance.consistenthash.ketama.DefaultHashAlgorithm;
import com.nepxion.thunder.cluster.loadbalance.consistenthash.ketama.KetamaNodeLocator;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.util.RandomUtil;

public class HashAlgorithmPercentTest {
    private static final Logger LOG = LoggerFactory.getLogger(HashAlgorithmPercentTest.class);
    public static final Integer NODE_COUNT = 50;
    public static final Integer NODE_ADDED_COUNT = 8;
    public static final Integer NODE_REDUCED_COUNT = 10;
    public static final Integer LAUNCH_TIMES = 100000;

    // 节点增删命中率测试：在环上插入N个节点，每个节点nCopies个虚拟结点。随机生成众多key，在增删节点时，测试同一个key选择相同节点的概率
    @Test
    public void test() throws Exception {
        Map<String, List<ApplicationEntity>> nodeMap = new HashMap<String, List<ApplicationEntity>>(LAUNCH_TIMES);
        for (int i = 0; i < LAUNCH_TIMES; i++) {
            List<ApplicationEntity> list = new ArrayList<ApplicationEntity>();
            nodeMap.put(RandomUtil.uuidRandom(), list);
        }

        List<ApplicationEntity> nodeList = HashAlgorithmFactory.getNodeList(NODE_COUNT, 1);

        // 正常情况情况下的节点数目
        LOG.info("Normal case - Nodes count : {}", nodeList.size());
        select(nodeList, nodeMap);

        // 节点增加情况下的节点数目
        nodeList = HashAlgorithmFactory.getNodeList(NODE_COUNT + NODE_ADDED_COUNT, 1);
        LOG.info("Added case - Nodes count : {}", nodeList.size());
        select(nodeList, nodeMap);

        // 节点删除情况下的节点数目
        nodeList = HashAlgorithmFactory.getNodeList(NODE_COUNT - NODE_REDUCED_COUNT, 1);
        LOG.info("Reduced case - Nodes count : {}", nodeList.size());
        select(nodeList, nodeMap);

        int addCount = 0;
        int reduceCount = 0;
        for (Map.Entry<String, List<ApplicationEntity>> entry : nodeMap.entrySet()) {
            List<ApplicationEntity> list = entry.getValue();
            // 选择次数为3
            if (list.size() == 3) {
                // 节点命中
                if (list.get(0).equals(list.get(1))) {
                    addCount++;
                }

                // 节点命中
                if (list.get(0).equals(list.get(2))) {
                    reduceCount++;
                }
            } else {
                LOG.error("It's wrong size of list, key is {}, size is {}", entry.getKey(), list.size());
            }
        }
        
        // 在节点增加和删除情况下，同一个key分配在相同节点上的比例(命中率)
        // 命中率与节点数目和增减的节点数量有关
        // 同样增删节点数目情况下，节点多时命中率高
        // 同样节点数目，增删结点越少，命中率越高
        LOG.info("Added select times : {}", addCount);
        LOG.info("Same percent in added case : {}%", (float) addCount * 100 / LAUNCH_TIMES);
        
        LOG.info("Reduced select times : {}", reduceCount);
        LOG.info("Same percent in reduced case : {}%", (float) reduceCount * 100 / LAUNCH_TIMES);
        
        System.in.read();
    }

    // 被命中的节点Map， 存放在List<ApplicationEntity>里
    private void select(List<ApplicationEntity> nodeList, Map<String, List<ApplicationEntity>> nodeMap) {
        KetamaNodeLocator<ApplicationEntity> locator = new KetamaNodeLocator<ApplicationEntity>(nodeList, DefaultHashAlgorithm.KETAMA_HASH);
        for (Map.Entry<String, List<ApplicationEntity>> entry : nodeMap.entrySet()) {
            ApplicationEntity node = locator.getPrimary(entry.getKey());
            if (node != null) {
                List<ApplicationEntity> list = entry.getValue();
                list.add(node);
            }
        }
    }
}