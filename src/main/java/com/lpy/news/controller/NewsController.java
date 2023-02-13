package com.lpy.news.controller;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dto.NewsDto;
import com.lpy.news.entity.News;
import com.lpy.news.model.Response;
import com.lpy.news.service.impl.NewsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/news")
@Api(tags = "文章相关接口")
public class NewsController {
    @Autowired
    private NewsServiceImpl newsService;

    /**
     * 新增文章(待修改，当前只能添加一个分类)
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增分类接口")
    public Response<String> save(@RequestBody NewsDto newsDto){
        log.info("新增文章");
        newsService.saveNews(newsDto);
        return Response.success("新增文章成功");
    }

    /**
     * 删除文章（单选、多选）逻辑删除
     * @param ids
     * @return
     */
    @PutMapping("/del")
    @ApiOperation(value = "删除文章接口")
    public Response<String> delete(@RequestParam Long[] ids){
        log.info("ids:{}",ids);
        newsService.removeNews(ids);
        return Response.success("文章删除成功");
    }

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @param divideName
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询文章接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo",value = "页码",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页记录数",required = true),
            @ApiImplicitParam(name = "divideName",value = "分类名称",required = false),
            @ApiImplicitParam(name = "author",value = "作者名称",required = false)
    })
    public Response<BasePageResponse<NewsDto>> page(int pageNo, int pageSize, String divideName,String author){
        log.info("pageNo={},pageSize={},divideName={},author={}",pageNo,pageSize,divideName,author);
        BasePageResponse<NewsDto> response = newsService.queryNewsPage(pageNo,pageSize,divideName,author);
        return Response.success(response);
    }

    /**
     * 修改文章
     * @param newsDto
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改文章接口")
    public Response<String> update(@RequestBody NewsDto newsDto){
        log.info(newsDto.toString());
        newsService.updateNews(newsDto);
        return Response.success("文章修改成功");
    }

    /**
     * id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询文章接口(id)回显，或用户前台查看详细内容")
    public Response<NewsDto> getById(@PathVariable Long id){
        log.info("根据id查询文章...");
        NewsDto news = newsService.selectNewsById(id);
        if (news != null){
            return Response.success(news);
        }
        return Response.error("未查询到文章");
    }

    /**
     * 查询热点文章
     * @return
     */
    @GetMapping("/hotNews")
    @ApiOperation(value = "查询热点文章(前十)")
    public Response<ArrayList<NewsDto>> getNewsLikeCount(){
        ArrayList<NewsDto> list = newsService.queryHotNews();
        return Response.success(list);
    }
}
