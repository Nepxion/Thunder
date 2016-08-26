package com.nepxion.thunder.protocol.hessian;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.entity.ServiceEntity;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.util.ClassUtil;
import com.nepxion.thunder.common.util.FileUtil;

public class HessianServletGenerator {
    private static final String CONTEXT_CLASSES_PATH = "WEB-INF/classes";

    private Map<String, ServiceEntity> serviceEntityMap;

    private String hessianServiceExporter;
    private String hessianServletFile;

    private File file;

    public HessianServletGenerator(Map<String, ServiceEntity> serviceEntityMap, ThunderProperties properties, String path) {
        this.serviceEntityMap = serviceEntityMap;

        hessianServiceExporter = properties.getString(ThunderConstants.HESSIAN_SERVICE_EXPORTER_ID);
        hessianServletFile = properties.getString(ThunderConstants.HESSIAN_SERVLET_FILE_ATTRIBUTE_NAME);

        path = FileUtil.validatePath(path);

        File directory = new File(path + CONTEXT_CLASSES_PATH);
        if (!directory.exists() || !directory.isDirectory()) {
            directory.mkdir();
        }

        path += CONTEXT_CLASSES_PATH + "/" + hessianServletFile;
        file = new File(path);
    }

    public void generate() {
        forceDelete(5);

        StringBuilder builder = generateContent();

        forceCreate(5, builder);
    }

    private StringBuilder generateContent() {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"" + ThunderConstants.ENCODING_FORMAT + "\"?>\n");
        builder.append("<beans xmlns=\"http://www.springframework.org/schema/beans\"\n");
        builder.append("    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
        builder.append("    xsi:schemaLocation=\"http://www.springframework.org/schema/beans\n");
        builder.append("    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd\">\n");
        for (Map.Entry<String, ServiceEntity> entry : serviceEntityMap.entrySet()) {
            String serviceClassName = entry.getKey();
            Object serviceImpl = entry.getValue().getService();
            String serviceImplClassName = serviceImpl.getClass().getName();

            String serviceName = ClassUtil.convertBeanName(serviceClassName);
            String refName = "_" + serviceName + "Impl";

            builder.append("    <bean name=\"/" + serviceClassName + "\" class=\"" + hessianServiceExporter + "\">\n");
            builder.append("        <property name=\"serviceInterface\" value=\"" + serviceClassName + "\"/>\n");
            builder.append("        <property name=\"service\" ref=\"" + refName + "\"/>\n");
            builder.append("    </bean>\n");
            builder.append("    <bean name=\"" + refName + "\" class=\"" + serviceImplClassName + "\"/>\n");
        }
        builder.append("</beans>");

        return builder;
    }

    private void forceCreate(int times, StringBuilder builder) {
        if (times == 0) {
            return;
        }
        if (!file.exists()) {
            try {
                FileUtils.writeStringToFile(file, builder.toString());
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    TimeUnit.MILLISECONDS.sleep(1500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                times--;
                forceCreate(times, builder);
            }
        }
    }

    private void forceDelete(int times) {
        if (times == 0) {
            return;
        }
        if (file.exists()) {
            try {
                FileDeleteStrategy.FORCE.delete(file);
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    TimeUnit.MILLISECONDS.sleep(1500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                times--;
                forceDelete(times);
            }
        }
    }
}