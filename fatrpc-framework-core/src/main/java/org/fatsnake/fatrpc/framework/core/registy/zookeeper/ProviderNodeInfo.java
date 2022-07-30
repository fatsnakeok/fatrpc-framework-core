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

    private Integer weight;

    private String registryTime;

    private String group;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getRegistryTime() {
        return registryTime;
    }

    public void setRegistryTime(String registryTime) {
        this.registryTime = registryTime;
    }

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
