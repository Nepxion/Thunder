package com.nepxion.thunder.testcase.algorithm;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.cluster.loadbalance.consistenthash.ketama.DefaultHashAlgorithm;
import com.nepxion.thunder.cluster.loadbalance.consistenthash.ketama.KetamaNodeLocator;
import com.nepxion.thunder.cluster.loadbalance.consistenthash.ketama.NodeLocator;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.util.RandomUtil;

public class HashAlgorithmStatisticsTest {
    private static final Logger LOG = LoggerFactory.getLogger(HashAlgorithmStatisticsTest.class);
    public static final Integer NODE_COUNT = 5;
    public static final Integer NODE_ADDED_COUNT = 3;
    public static final Integer LAUNCH_TIMES = 100000;

    // 分布平均性测试：测试随机生成的众多key是否会平均分布到各个节点上
    @Test
    public void test() throws Exception {
        // 用以统计执行次数的分布情况
        Map<ApplicationEntity, Integer> nodeMap = new HashMap<ApplicationEntity, Integer>();

        List<ApplicationEntity> nodeList = HashAlgorithmFactory.getNodeList(NODE_COUNT, 1);
        NodeLocator<ApplicationEntity> locator = new KetamaNodeLocator<ApplicationEntity>(nodeList, DefaultHashAlgorithm.KETAMA_HASH);

        for (int i = 0; i < LAUNCH_TIMES; i++) {
            ApplicationEntity node = locator.getPrimary(RandomUtil.uuidRandom());
            Integer times = nodeMap.get(node);
            /*if (i == LAUNCH_TIMES / 2) {
                LOG.info("Added node count : {}, when Launch times is {}", NODE_ADDED_COUNT, LAUNCH_TIMES / 2);
                nodeAdded(locator, nodeMap);
            }*/
            if (times == null) {
                nodeMap.put(node, 1);
            } else {
                nodeMap.put(node, times + 1);
            }
        }

        // 节点数目，总共有多少key，完美情况情况下，每个节点应该分配key的比例是多少
        LOG.info("Node count : {}, Launch times : {}, Accurate percent : {}%", NODE_COUNT, LAUNCH_TIMES, (float) 100 / NODE_COUNT);
        for (Map.Entry<ApplicationEntity, Integer> entry : nodeMap.entrySet()) {
            // 每个结点分配到key的数目和比例。多次测试后发现，这个Hash算法的节点分布还是不错的，都在标准比例左右徘徊，是个合适的负载均衡算法
            LOG.info("Node name : {}, Launch times : {}, Statistics percent : {}%", entry.getKey(), entry.getValue(), (float) entry.getValue() / LAUNCH_TIMES * 100);
        }

        System.in.read();
    }

    protected void nodeAdded(NodeLocator<ApplicationEntity> locator, Map<ApplicationEntity, Integer> nodeMap) {
        List<ApplicationEntity> nodeList = locator.getAll();

        List<ApplicationEntity> nodeAddedList = HashAlgorithmFactory.getNodeList(NODE_ADDED_COUNT, NODE_COUNT + 1);
        nodeList.addAll(nodeAddedList);

        locator.updateLocator(nodeList);
    }
}