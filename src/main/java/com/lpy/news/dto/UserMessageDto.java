package com.lpy.news.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserMessageDto  implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 消息内容
     */
    private String message;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
