package com.nepxion.thunder.monitor;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.List;

import com.nepxion.thunder.common.entity.MonitorStat;

public interface MonitorRetriever {

    // 解析Json成MonitorStat对象
    MonitorStat create(String monitorStatJson);

    // 根据时间排序
    void sort(List<MonitorStat> monitorStatList);
}