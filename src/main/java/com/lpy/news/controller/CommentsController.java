package com.lpy.news.controller;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dto.CommentsDto;
import com.lpy.news.model.Response;
import com.lpy.news.service.impl.CommentsServiceImpl;
import com.lpy.news.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/comments")
@Api(tags = "评论相关接口")
public class CommentsController {
    @Autowired
    private CommentsServiceImpl commentsService;

    /**
     * 新增评论(用户调用)
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增评论接口")
    public Response<String> save(@RequestBody CommentsDto commentsDto, HttpServletRequest request){
        log.info(commentsDto.toString());
        commentsDto.setUserId(Long.valueOf(JwtUtils.getUserId(request.getHeader("token"))));
        commentsService.saveComments(commentsDto);
        return Response.success("新增评论成功");
    }

    /**
     * 评论分页查询
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询评论接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo",value = "页码",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页记录数",required = true),
            @ApiImplicitParam(name = "state",value = "每页记录数",required = true)
    })
    public Response<BasePageResponse<CommentsDto>> page(int pageNo, int pageSize,String state){
        log.info("pageNo={},pageSize={},state={}",pageNo,pageSize,state);
        BasePageResponse<CommentsDto> response = commentsService.queryCommentsPage(pageNo,pageSize,state);
        return Response.success(response);
    }

    /**
     * 删除评论（单选、多选）逻辑删除
     * @param ids
     * @return
     */
    @PutMapping("/del")
    @ApiOperation(value = "删除评论接口")
    public Response<String> delete(@RequestParam Long[] ids){
        log.info("ids:{}",ids);
        commentsService.removeComments(ids);
        return Response.success("评论删除成功");
    }

    /**
     * 评论审核
     * @param commentsDto
     * @return
     */
    @PutMapping
    @ApiOperation(value = "评论审核接口")
    public Response<String> update(@RequestBody CommentsDto commentsDto){
        log.info(commentsDto.toString());
        commentsService.updateComments(commentsDto);
        return Response.success("评论审核成功");
    }

}
