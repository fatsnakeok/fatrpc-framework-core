package org.fatsnake.fatrpc.framework.core.serialize;

import java.io.Serializable;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/13 10:08 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class User implements Serializable {

    private static final long serialVersionUID = -1728196331321496561L;

    private Integer id;

    private Long tel;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getTel() {
        return tel;
    }

    public void setTel(Long tel) {
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", tel=" + tel +
                '}';
    }
}
