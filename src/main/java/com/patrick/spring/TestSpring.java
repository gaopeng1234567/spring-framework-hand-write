package com.patrick.spring;

import com.patrick.spring.config.AppConfig;
import com.patrick.spring.context.PatrickApplicationContext;
import com.patrick.spring.serivce.UserService;

/**
 * @author patrick
 * @date 2021/2/28 3:24 下午
 * @Des 测试类
 * 最簡單的事是堅持，最難的事還是堅持
 */
public class TestSpring {
    public static void main(String[] args) {
        //1、启动 2、扫描 3、创建非懒加载的单例bean
        PatrickApplicationContext patrickApplicationContext =
                new PatrickApplicationContext(AppConfig.class);
        //测试单例
        System.out.println(patrickApplicationContext.getBean("orderService"));
        System.out.println(patrickApplicationContext.getBean("orderService"));
        System.out.println(patrickApplicationContext.getBean("orderService"));
        //测试原型
        System.out.println(patrickApplicationContext.getBean("userService"));
        System.out.println(patrickApplicationContext.getBean("userService"));
        //测试注入
        UserService userService = (UserService) patrickApplicationContext.getBean("userService");
        userService.test();

    }
}
