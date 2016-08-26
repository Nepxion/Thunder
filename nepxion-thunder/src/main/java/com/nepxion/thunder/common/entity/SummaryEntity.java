package com.nepxion.thunder.common.entity;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class SummaryEntity implements Serializable {
    private static final long serialVersionUID = -3843119621145926019L;
    
    private static final String NOT_STARTED = "Not Started";
    private static final String NOT_CONNECTED = "Not Connected";

    private ProtocolType protocolType;
    private ApplicationType applicationType;
    private String application;
    private String group;
    private String interfaze;
    private List<String> methods;
    private List<String> addresses;
    private String addressString;
    private List<String> times;
    private String timeString;

    private boolean htmlStyle = true;
    private String notStartedDescription;
    private String notConnectedDescription;

    public ProtocolType getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getInterface() {
        return interfaze;
    }

    public void setInterface(String interfaze) {
        this.interfaze = interfaze;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
        if (CollectionUtils.isNotEmpty(addresses)) {
            StringBuilder builder = new StringBuilder();
            if (htmlStyle) {
                builder.append("<html>");
                for (String address : addresses) {
                    builder.append("<p align=left>" + address + "</p>");
                }
                builder.append("</html>");
                addressString = builder.toString();
            } else {
                for (String address : addresses) {
                    builder.append(address + ",");
                }
                addressString = builder.toString();
                addressString = addressString.substring(0, addressString.lastIndexOf(","));
            }
        } else {
            addressString = getOfflineDescription();
        }
    }

    public String getAddressString() {
        return addressString;
    }
    
    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
        if (CollectionUtils.isNotEmpty(times)) {
            StringBuilder builder = new StringBuilder();
            if (htmlStyle) {
                builder.append("<html>");
                for (String time : times) {
                    builder.append("<p align=left>" + time + "</p>");
                }
                builder.append("</html>");
                timeString = builder.toString();
            } else {
                for (String time : times) {
                    builder.append(time + ",");
                }
                timeString = builder.toString();
                timeString = timeString.substring(0, timeString.lastIndexOf(","));
            }
        } else {
            timeString = getOfflineDescription();
        }
    }
    
    private String getOfflineDescription() {
        switch (applicationType) {
            case SERVICE:
                return StringUtils.isNotEmpty(notStartedDescription) ? notStartedDescription : NOT_STARTED;
            case REFERENCE:
                return StringUtils.isNotEmpty(notConnectedDescription) ? notConnectedDescription : NOT_CONNECTED;
        }
        
        return null;
    }

    public String getTimeString() {
        return timeString;
    }

    public boolean isHtmlStyle() {
        return htmlStyle;
    }

    public void setHtmlStyle(boolean htmlStyle) {
        this.htmlStyle = htmlStyle;
    }

    public String getNotStartedDescription() {
        return notStartedDescription;
    }

    public void setNotStartedDescription(String notStartedDescription) {
        this.notStartedDescription = notStartedDescription;
    }

    public String getNotConnectedDescription() {
        return notConnectedDescription;
    }

    public void setNotConnectedDescription(String notConnectedDescription) {
        this.notConnectedDescription = notConnectedDescription;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("protocolType=");
        builder.append(protocolType);
        builder.append(", applicationType=");
        builder.append(applicationType);
        builder.append(", application=");
        builder.append(application);
        builder.append(", group=");
        builder.append(group);
        builder.append(", interface=");
        builder.append(interfaze);
        builder.append(", methods=");
        builder.append(methods);
        builder.append(", addresses=");
        builder.append(addresses);

        return builder.toString();
    }
}