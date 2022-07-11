package org.fatsnake.fatrpc.framework.core.router;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/11 11:10
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class Selector {

    /**
     * 服务名
     * eg: com.fat.test.DataService
     */
    private String providerServiceName;

    public String getProviderServiceName() {
        return providerServiceName;
    }

    public void setProviderServiceName(String providerServiceName) {
        this.providerServiceName = providerServiceName;
    }
}
