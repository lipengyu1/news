package com.lpy.news.common;

import com.lpy.news.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public Response<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2]+"已存在";
            return Response.error(msg);
        }
        return Response.error("未知错误");
    }

    @ExceptionHandler({com.lpy.news.common.CustomException.class})
    public Response<String> exceptionHandler(com.lpy.news.common.CustomException ex){
        log.error(ex.getMessage());
        return Response.error(ex.getMessage());
    }
}
