package org.fatsnake.fatrpc.framework.core.server;


import org.fatsnake.fatrpc.framework.interfaces.UserService;

/**
 * @Author linhao
 * @Date created in 7:45 下午 2022/1/8
 */
public class UserServiceImpl implements UserService {

    @Override
    public void test() {
        System.out.println("test");
    }
}
