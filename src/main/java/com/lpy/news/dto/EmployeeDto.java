package com.lpy.news.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class EmployeeDto implements Serializable {
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
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * token
     */
    private String token;
}

