package com.lpy.news.controller;


import com.lpy.news.dao.UserMessageDao;
import com.lpy.news.dto.UserMessageDto;
import com.lpy.news.entity.UserMessage;
import com.lpy.news.model.Response;
import com.lpy.news.service.impl.UserMessageServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/usermessage")
@Api(tags = "用户消息相关接口")
public class UserMessageController {

    @Autowired
    private UserMessageServiceImpl userMessageService;

    /**
     * 展示用户消息
     * @return
     */
    @GetMapping("/query")
    @ApiOperation(value = "展示用户消息接口")
    public Response<UserMessageDto> queryAllMessage(){
        UserMessageDto userMessageDto = userMessageService.queryAllMessage();
        return Response.success(userMessageDto);
    }

    /**
     * 删除用户消息（单选、多选）逻辑删除
     * @param ids
     * @return
     */
    @PutMapping("/del")
    @ApiOperation(value = "删除用户消息接口")
    public Response<String> delete(@RequestParam Long[] ids){
        log.info("ids:{}",ids);
        userMessageService.removeMessage(ids);
        return Response.success("删除用户消息成功");
    }

}
