package com.nepxion.thunder.monitor;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.entity.MonitorStat;
import com.nepxion.thunder.serialization.SerializerExecutor;

public class LogServiceMonitorExecutor extends AbstractMonitorExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(LogServiceMonitorExecutor.class);

    @Override
    public void execute(MonitorStat monitorStat) throws Exception {
        executeJson(monitorStat);
    }

    protected void executeString(MonitorStat monitorStat) {
        String value = monitorStat.toString();
        String exception = monitorStat.getException();
        if (exception != null) {
            LOG.error(value + "\r\n{}", exception);
        } else {
            LOG.info(value);
        }
    }

    protected void executeJson(MonitorStat monitorStat) {
        String value = SerializerExecutor.toJson(monitorStat);
        String exception = monitorStat.getException();
        if (exception != null) {
            LOG.error(value);
        } else {
            LOG.info(value);
        }
    }
}