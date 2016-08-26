package com.nepxion.thunder.monitor;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.entity.MonitorStat;

public class LogServiceMonitorRetriever extends AbstractMonitorRetriever {
    private static final Logger LOG = LoggerFactory.getLogger(LogServiceMonitorRetriever.class);

    public List<MonitorStat> retrieve(String traceId, String filePath) throws Exception {
        return retrieve(traceId, filePath, ThunderConstants.ENCODING_FORMAT);
    }

    public List<MonitorStat> retrieve(String traceId, String filePath, String encoding) throws Exception {
        if (StringUtils.isEmpty(traceId)) {
            throw new MonitorException("Trace ID is null");
        }
        
        if (StringUtils.isEmpty(filePath)) {
            throw new MonitorException("File path is null");
        }
        
        List<MonitorStat> monitorStatList = new ArrayList<MonitorStat>();

        String key = "\"" + ThunderConstants.TRACE_ID + "\":\"" + traceId + "\"";

        InputStream inputStream = new FileInputStream(filePath);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charsets.toCharset(encoding));
        BufferedReader bufferedReader = IOUtils.toBufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();
        while (line != null) {
            if (line.contains(key)) {
                line = line.substring(line.indexOf("{"));
                try {
                    MonitorStat monitorStat = create(line);
                    monitorStatList.add(monitorStat);
                } catch (Exception e) {
                    LOG.error("Create MonitorStat failed", e);
                }
            }
            line = bufferedReader.readLine();
        }

        sort(monitorStatList);

        IOUtils.closeQuietly(bufferedReader);
        IOUtils.closeQuietly(inputStreamReader);
        IOUtils.closeQuietly(inputStream);

        return monitorStatList;
    }
}