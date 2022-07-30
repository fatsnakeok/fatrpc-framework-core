package org.fatsnake.fatrpc.framework.provider.springboot.service.impl;

import org.fatsnake.fatrpc.framework.interfaces.UserService;
import org.fatsnake.fatrpc.framework.spring.starter.common.FatRpcService;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/29 13:39
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
@FatRpcService
public class UserServiceImpl implements UserService {
    @Override
    public void test() {
        System.out.println("test");
    }
}
