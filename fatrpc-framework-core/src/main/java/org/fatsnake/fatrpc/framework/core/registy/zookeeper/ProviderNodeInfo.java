package org.fatsnake.fatrpc.framework.core.registy.zookeeper;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/8 10:40
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class ProviderNodeInfo {

    private String  serviceName;

    private String address;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "ProviderNodeInfo{" +
                "serviceName='" + serviceName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
