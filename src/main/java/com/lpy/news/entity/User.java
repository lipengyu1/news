package com.lpy.news.entity;

import lombok.Data;

@Data
public class User {
    private static final long serialVersionUID = 1L;

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
     * 身份证
     */
    private String idNumber;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 1正常0删除
     */
    private Integer status;

}
