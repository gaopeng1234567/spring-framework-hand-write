package com.patrick.spring.serivce;

import com.patrick.spring.annotation.Autowired;
import com.patrick.spring.annotation.Component;
import com.patrick.spring.annotation.Scope;

import java.lang.management.OperatingSystemMXBean;

/**
 * @author patrick
 * @date 2021/2/28 3:30 下午
 * @Des 用户service
 * 最簡單的事是堅持，最難的事還是堅持
 */
@Component("userService")
@Scope("prototype")
public class UserService {
    @Autowired
    private OrderService orderService;

    public void test() {
        System.out.println(orderService);
    }
}
