package com.lpy.news.controller;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dto.UserCollectionDto;
import com.lpy.news.entity.UserCollection;
import com.lpy.news.model.Response;
import com.lpy.news.service.impl.UserCollectionServiceImpl;
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
@RequestMapping("/collection")
@Api(tags = "用户收藏接口")
public class UserCollectionController {
    @Autowired
    private UserCollectionServiceImpl userCollectionService;

    /**
     * 添加收藏
     * @return
     */
    @CacheEvict(value = "newsCollectionCache",allEntries = true)
    @PostMapping("/add")
    @ApiOperation(value = "用户添加收藏接口(前台)请求示例{\n" +
            "    \"newsId\":\"215577361272407041\"\n" +
            "}")
    public Response<String> add(@RequestBody UserCollection userCollection, HttpServletRequest request){
        Long userId = Long.valueOf(JwtUtils.getUserId(request.getHeader("token")));
        userCollection.setUserId(userId);
        userCollectionService.addCollection(userCollection);
        return Response.success("收藏成功");
    }

    /**
     * 用户删除收藏
     * @param ids
     * @param request
     * @return
     */
    @CacheEvict(value = "newsCollectionCache",allEntries = true)
    @PostMapping("/del")
    @ApiOperation(value = "用户删除收藏接口(前台)")
    public Response<String> del(@RequestParam Long[] ids, HttpServletRequest request){
        Long userId = Long.valueOf(JwtUtils.getUserId(request.getHeader("token")));
        userCollectionService.delCollection(ids,userId);
        return Response.success("删除成功");
    }

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Cacheable(value = "newsCollectionCache",key = "#request.getHeader('token')+'_'+'collection'")
    @GetMapping("/page")
    @ApiOperation(value = "分页查询收藏接口(前台)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo",value = "页码",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页记录数",required = true),
    })
    public Response<BasePageResponse<UserCollectionDto>> page(int pageNo, int pageSize,HttpServletRequest request){
        log.info("pageNo={},pageSize={},divideName={}",pageNo,pageSize);
        Long userId = Long.valueOf(JwtUtils.getUserId(request.getHeader("token")));
        BasePageResponse<UserCollectionDto> response = userCollectionService.queryCollectionPage(pageNo,pageSize,userId);
        return Response.success(response);
    }

}
