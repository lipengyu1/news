package com.lpy.news.controller;


import com.lpy.news.dto.UserMessageDto;
import com.lpy.news.model.Response;
import com.lpy.news.service.impl.UserMessageServiceImpl;
import com.lpy.news.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/usermessage")
@Api(tags = "用户消息相关接口")
public class UserMessageController {

    @Autowired
    private UserMessageServiceImpl userMessageService;

    /**
     * 用户消息展示
     * @param request
     * @return
     */
    @Cacheable(value = "usermessageCache",key = "#request.getHeader('token')+'_'+'usermessage'")
    @GetMapping("/query")
    @ApiOperation(value = "展示用户消息接口(前台)")
    public Response<ArrayList<UserMessageDto>> queryAllMessage(HttpServletRequest request){
        Long userId = Long.valueOf(JwtUtils.getUserId(request.getHeader("token")));
        ArrayList<UserMessageDto> userMessageDtoArrayList = userMessageService.queryAllMessage(userId);
        return Response.success(userMessageDtoArrayList);
    }

    /**
     * 删除用户消息（单选、多选）逻辑删除
     * @param ids
     * @return
     */
    @CacheEvict(value = "usermessageCache",allEntries = true)
    @PutMapping("/del")
    @ApiOperation(value = "删除用户消息接口(前台)")
    public Response<String> delete(@RequestParam Long[] ids){
        log.info("ids:{}",ids);
        userMessageService.removeMessage(ids);
        return Response.success("删除用户消息成功");
    }

}
