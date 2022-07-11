package org.fatsnake.fatrpc.framework.core.router;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/11 11:12
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public enum SelectorEnum {

    RANDOM_SELECTOR(0, "random");

    int code;
    String  desc;

    SelectorEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
