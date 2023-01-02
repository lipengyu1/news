package com.lpy.news.news;

import com.lpy.news.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class NewsApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    void getEmpTokenTest(){
        Map<String, Object> info = new HashMap<>();
        info.put("username", "admin");
        info.put("pass", "123456");
        //生成token
        String id = "185988114257413121";
        Long userId = Long.valueOf(id);
        String token = JwtUtils.sign(userId, info);
        System.out.println(token);;
    }
    @Test
    void getUserTokenTest(){
        Map<String, Object> info = new HashMap<>();
        info.put("email", "1169424910@qq.com");
        info.put("pass", "123456");
        //生成token
        String id = "202505201737925633";
        Long userId = Long.valueOf(id);
        String token = JwtUtils.sign(userId, info);
        System.out.println(token);;
    }

}
