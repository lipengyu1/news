package com.lpy.news.controller;

import com.lpy.news.model.Response;
import com.lpy.news.service.impl.NewsServiceImpl;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/news")
@Api(tags = "文章相关接口")
public class NewsController {
    @Autowired
    private NewsServiceImpl newsService;

//    /**
//     * 新增分类
//     * @return
//     */
//    @PostMapping
//    @ApiOperation(value = "新增分类接口")
//    public Response<String> save(@RequestBody Divide divide){
//        log.info(divide.toString());
//        divideService.saveDivide(divide);
//        return Response.success("新增分类成功");
//    }
}
