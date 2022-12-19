package com.lpy.news.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    private Long id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String sex;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 密码
     */
    private String password;
    /**
     * 身份证
     */
    private String idNumber;
    /**
     * 头像
     */
    private String avatar;
    /**
     * token
     */
    private String token;
}
