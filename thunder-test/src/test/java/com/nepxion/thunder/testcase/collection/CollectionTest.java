package com.nepxion.thunder.testcase.collection;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.util.RandomUtil;

public class CollectionTest {
    private static final Logger LOG = LoggerFactory.getLogger(CollectionTest.class);

    @Test
    public void testIntersection() throws Exception {
        List<ApplicationEntity> localList = new ArrayList<ApplicationEntity>();
        for (int i = 1; i < 5; i++) {
            ApplicationEntity applicationEntity = new ApplicationEntity();
            applicationEntity.setApplication(i + "");
            applicationEntity.setGroup(i + "");

            localList.add(applicationEntity);
        }

        List<ApplicationEntity> remoteList = new ArrayList<ApplicationEntity>();
        for (int i = 3; i < 7; i++) {
            ApplicationEntity applicationEntity = new ApplicationEntity();
            applicationEntity.setApplication(i + "");
            applicationEntity.setGroup(i + "");

            remoteList.add(applicationEntity);
        }

        List<ApplicationEntity> intersectedList = (List<ApplicationEntity>) CollectionUtils.intersection(localList, remoteList);
        List<ApplicationEntity> onlineList = (List<ApplicationEntity>) CollectionUtils.subtract(remoteList, intersectedList);
        List<ApplicationEntity> offlineList = (List<ApplicationEntity>) CollectionUtils.subtract(localList, intersectedList);

        LOG.info("Online list:{}", onlineList);
        LOG.info("Offline list:{}", offlineList);
    }

    @Test
    public void testEquals() throws Exception {
        List<ApplicationEntity> localList = new ArrayList<ApplicationEntity>();
        for (int i = 0; i < 5; i++) {
            ApplicationEntity applicationEntity = new ApplicationEntity();
            applicationEntity.setApplication(i + "");
            applicationEntity.setGroup(i + "");
            applicationEntity.setId(RandomUtil.uuidRandom());

            localList.add(applicationEntity);
        }

        List<ApplicationEntity> remoteList = new ArrayList<ApplicationEntity>();
        for (int i = 4; i >= 0; i--) {
            ApplicationEntity applicationEntity = new ApplicationEntity();
            applicationEntity.setApplication(i + "");
            applicationEntity.setGroup(i + "");
            applicationEntity.setId(RandomUtil.uuidRandom());

            remoteList.add(applicationEntity);
        }

        boolean isEqual = CollectionUtils.isEqualCollection(localList, remoteList);
        LOG.info("Is equal:{}", isEqual);

        ApplicationEntity applicationEntity = localList.get(0);
        LOG.info("Remote list:{}", remoteList);
        if (remoteList.contains(applicationEntity)) {
            remoteList.remove(applicationEntity);
            LOG.info("Remote list:{}", remoteList);
        }
    }
}