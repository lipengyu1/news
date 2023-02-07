package com.lpy.news.controller;

import com.lpy.news.model.Response;
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
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/newslike")
@Api(tags = "文章点赞接口")
public class NewsLikeController {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RedisServiceImpl redisService;
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
        return Response.success("点赞失败，已点赞");
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
        System.out.println(liked);
        System.out.println(redisTemplate.opsForHash().get(RedisKeyUtils.MAP_USER_LIKED,id));
        if ((liked==true)&&(redisTemplate.opsForHash().get(RedisKeyUtils.MAP_USER_LIKED,id).equals(1))){
            redisService.unlikeFromRedis(newsId,userId);
            redisService.decrementLikedCount(newsId);
            return Response.success("取消点赞成功");
        }
        return Response.success("取消失败");
    }

    /**
     * 获取所有文章点赞数据
     * @return
     */
    @GetMapping("getlike")
    public Response<List> getAllNewsLike(){
        List num = redisService.getLikedDataFromRedis();
        return Response.success(num);
    }

    /**
     * 获取所有文章点赞数量
     * @return
     */
    @GetMapping("getlikenum")
    public Response<List> getAllNewsLikeNum(){
        List num = redisService.getLikedCountFromRedis();
        return Response.success(num);
    }
}
