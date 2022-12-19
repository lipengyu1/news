package com.lpy.news.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 员工姓名
     */
    private String name;
    /**
     * 性别
     */
    private String sex;
    /**
     * 身份证
     */
    private String idNumber;
    /**
     * 电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 用户名，用于登录
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 状态1正常0删除
     */
    private Integer status;
    /**
     * token
     */
    private String token;
}
