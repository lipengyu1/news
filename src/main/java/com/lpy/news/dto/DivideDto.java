package com.lpy.news.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DivideDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 类名
     */
    private String name;

    /**
     * 链接名
     */
    private String link;
}
