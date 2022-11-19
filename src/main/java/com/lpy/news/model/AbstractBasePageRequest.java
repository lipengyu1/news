package com.lpy.news.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public abstract class AbstractBasePageRequest implements java.io.Serializable{
    private static final long serialVersionUID = 2608062026598967425L;
    /**
     * 页数
     */
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message="最小值为1")
    private int pageNo;
    /**
     * 每页显示记录数
     */
    @NotNull(message = "每页显示记录数不能为空")
    @Min(value = 1, message="最小值为1")
    @Max(value = 1000, message = "最大值为1000")
    private int pageSize;
}