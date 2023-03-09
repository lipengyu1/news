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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @CacheEvict(value = "commentsCache",allEntries = true)
    @PostMapping
    @ApiOperation(value = "新增评论接口(前台)")
    public Response<String> save(@RequestBody CommentsDto commentsDto, HttpServletRequest request){
        log.info(commentsDto.toString());
        commentsDto.setUserId(Long.valueOf(JwtUtils.getUserId(request.getHeader("token"))));
        commentsService.saveComments(commentsDto);
        return Response.success("新增评论成功,请等待审核");
    }

    /**
     * 评论分页查询
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询评论接口(后台)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo",value = "页码",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页记录数",required = true),
            @ApiImplicitParam(name = "state",value = "状态(通过;拒绝)",required = false)
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
    @CacheEvict(value = "commentsCache",allEntries = true)
    @PutMapping("/del")
    @ApiOperation(value = "删除评论接口(前后台)")
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
    @CacheEvict(value = "commentsCache",allEntries = true)
    @PutMapping
    @ApiOperation(value = "评论审核接口(后台)")
    public Response<String> update(@RequestBody CommentsDto commentsDto){
        log.info(commentsDto.toString());
        commentsService.updateComments(commentsDto);
        return Response.success("评论审核成功");
    }

    /**
     * 新闻评论分页查询
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Cacheable(value = "commentsCache",key = "#newsId+'_'+'comment'")
    @GetMapping("/getcomment")
    @ApiOperation(value = "用户分页查询文章相关评论接口(前台)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo",value = "页码",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页记录数",required = true),
            @ApiImplicitParam(name = "newsId",value = "新闻id",required = true),
    })
    public Response<BasePageResponse<CommentsDto>> getcomment(int pageNo, int pageSize,Long newsId){
        log.info("pageNo={},pageSize={},newsId={}", pageNo, pageSize, newsId);
        BasePageResponse<CommentsDto> response = commentsService.queryCommentsPageByNewsId(pageNo,pageSize,newsId);
        return Response.success(response);
    }
}
