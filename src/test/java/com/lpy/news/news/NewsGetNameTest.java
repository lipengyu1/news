package com.lpy.news.news;

import com.lpy.news.entity.User;
import com.lpy.news.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;


@SpringBootTest
class NewsGetNameTest {
    @Autowired
    UserServiceImpl userService;

    @Test
    void contextLoads() {
    }

    /**
     * 测试数据添加
     * @throws Exception
     */
    @Test
    void registerTest() throws Exception {

        for (int i = 10; i <= 1000; i++) {
            User user = new User();
            user.setPassword("123456");
            user.setSex(getSex());
            user.setName(createName());
            user.setPhone(getTel());
            user.setIdNumber("142226222222222222");
            String email = i + "@qq.com";
            user.setEmail(email);
            requestPostMethod(user);
        }
    }

    //    随机姓名
    private void requestPostMethod(User requestBody) throws Exception {
        userService.userRegister(requestBody);
    }

    public String createName() {
        String line = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦" +
                "章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞仁袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅" +
                "皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜" +
                "阮蓝闵席季麻强贾路娄危江童颜郭梅盛林刁钟徐邱骆高夏蔡田樊胡";
        Random random = new Random();
        String name = line.charAt(random.nextInt(line.length())) + "";
        for (int i = 1 + random.nextInt(2); i > 0; i--) {
            name += line.charAt(random.nextInt(line.length())) + "";
        }
        return name;
    }
    //    随机性别
    public String getSex() {
        int randNum = new Random().nextInt(2) + 1;
        return randNum == 1 ? "男" : "女";
    }
    //    随件电话号
    public int getNum(int start,int end) {
        return (int)(Math.random()*(end-start+1)+start);
    }
    private String[] telFirst="158,138".split(",");
    private String getTel() {
        int index=getNum(0,telFirst.length-1);
        //手机号前三位
        String first="135";
        //手机号中间四位
        //String[] siwei = {"1532","5328","5329"};
        Random r3 = new Random();
        //String second1= siwei[r3.nextInt(3)];
        String second1= "5301";
        //手机号最后四位
        String thrid=String.valueOf(getNum(1,9100)+10000).substring(1);
        return first+second1+thrid;
    }
}

