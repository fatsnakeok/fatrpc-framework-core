package org.fatsnske.fatrpc.framework.consumer.springboot.controller;

import org.fatsnake.fatrpc.framework.interfaces.OrderService;
import org.fatsnake.fatrpc.framework.interfaces.UserService;
import org.fatsnake.fatrpc.framework.spring.starter.common.FatRpcReference;
import org.fatsnake.fatrpc.framework.spring.starter.common.FatRpcService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/29 15:08
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @FatRpcReference
    private UserService userService;


    @FatRpcReference(group = "order-group", serviceToken = "order-token")
    private OrderService orderService;


     @GetMapping(value = "/test")
     public void test() {userService.test();}

    @GetMapping("/testMaxData")
    public String testMaxData(int i) {
         String result = orderService.testMaxData(i);
         System.out.println(result.length());
         return result;
    }

    @GetMapping(value = "/get-order-no")
    public List<String> getOrderNo() {
         List<String> result = orderService.getOrderNoList();
        System.out.println(result);
        return result;
    }

}
