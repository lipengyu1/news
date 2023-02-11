package com.lpy.news.controller;

import com.lpy.news.model.Response;
import com.lpy.news.service.impl.LikedServiceImpl;
import com.lpy.news.service.impl.RedisServiceImpl;
import com.lpy.news.utils.JwtUtils;
import com.lpy.news.utils.RedisKeyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/newslike")
@Api(tags = "文章点赞接口")
public class NewsLikeController {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RedisServiceImpl redisService;
    @Autowired
    LikedServiceImpl likedService;
    /**
     * 点赞
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "点赞接口")
    public Response<String> like(HttpServletRequest request,@RequestParam Long newsId){
        Long userId = Long.valueOf(JwtUtils.getUserId(request.getHeader("token")));
        //判断是否是第一次点赞
        String id = newsId+"::"+userId;
        boolean liked = redisTemplate.opsForHash().hasKey(RedisKeyUtils.MAP_USER_LIKED,id);
        if ((liked==false)||(redisTemplate.opsForHash().get(RedisKeyUtils.MAP_USER_LIKED,id).equals(0))){
            redisService.saveLiked2Redis(newsId,userId);
            redisService.incrementLikedCount(newsId);
            return Response.success("点赞成功");
        }
        return Response.success("点赞失败，已点赞,请24小时后再来");
    }
    /**
     * 取消点赞
     * @return
     */
    @PostMapping("/sub")
    @ApiOperation(value = "取消点赞接口")
    public Response<String> nolike(HttpServletRequest request,@RequestParam Long newsId){
        Long userId = Long.valueOf(JwtUtils.getUserId(request.getHeader("token")));
        String id = newsId+"::"+userId;
        boolean liked = redisTemplate.opsForHash().hasKey(RedisKeyUtils.MAP_USER_LIKED,id);
        if ((liked==true)&&(redisTemplate.opsForHash().get(RedisKeyUtils.MAP_USER_LIKED,id).equals(1))){
            redisService.unlikeFromRedis(newsId,userId);
            redisService.decrementLikedCount(newsId);
            return Response.success("取消点赞成功");
        }
        return Response.success("取消失败");
    }

}
