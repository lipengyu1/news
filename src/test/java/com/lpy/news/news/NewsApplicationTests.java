package com.lpy.news.news;

import com.lpy.news.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.HashMap;
import java.util.Map;


@SpringBootTest
public class NewsApplicationTests {

    @Test
    void getEmpTokenTest() {
        Map<String, Object> info = new HashMap<>();
        info.put("username", "admin");
        info.put("pass", "123456");
        //生成token
        String id = "188057040541844480";
        Long userId = Long.valueOf(id);
        String token = JwtUtils.sign(userId, info);
        System.out.println(token);
    }

    @Test
    void getUserTokenTest() {
        Map<String, Object> info = new HashMap<>();
        info.put("email", "1169424910@qq.com");
        info.put("pass", "123456");
        //生成token
        String id = "216328933296178177";
        Long userId = Long.valueOf(id);
        String token = JwtUtils.sign(userId, info);
        System.out.println(token);
    }

}
