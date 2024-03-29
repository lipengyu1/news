package com.lpy.news.controller;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dto.NewsAndRecommendDto;
import com.lpy.news.dto.NewsDto;
import com.lpy.news.dto.NewsKeyQueryDto;
import com.lpy.news.dto.NewsUserRecommendDto;
import com.lpy.news.model.Response;
import com.lpy.news.service.impl.HistoryServiceImpl;
import com.lpy.news.service.impl.NewsServiceImpl;
import com.lpy.news.service.impl.RedisServiceImpl;
import com.lpy.news.service.impl.UserCFNewsRecommendServiceImpl;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/news")
@Api(tags = "文章相关接口")
public class NewsController {
    @Autowired
    private NewsServiceImpl newsService;
    @Autowired
    private RedisServiceImpl redisService;
    @Autowired
    private HistoryServiceImpl historyService;
    @Autowired
    private UserCFNewsRecommendServiceImpl userCFNewsRecommendSerevice;

    /**
     * 新增文章(待修改，当前只能添加一个分类)
     * @return
     */
    @CacheEvict(value = "newsCache",allEntries = true)
    @PostMapping
    @ApiOperation(value = "新增文章接口(后台)")
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
    @CacheEvict(value = "newsCache",allEntries = true)
    @PutMapping("/del")
    @ApiOperation(value = "删除文章接口(后台)")
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
    @ApiOperation(value = "分页查询文章接口(前后台)")
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
    @CacheEvict(value = "newsCache",allEntries = true)
    @PutMapping
    @ApiOperation(value = "修改文章接口(后台)")
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
    @ApiOperation(value = "查询文章接口(id)回显(后台)")
    public Response<NewsDto> getById(@PathVariable Long id){
        log.info("根据id查询文章...");
        NewsDto news = newsService.selectNewsById(id);
        if (news != null){
            return Response.success(news);
        }
        return Response.error("未查询到文章");
    }

    /**
     * id查询2
     * @param id
     * @return
     */
    @Cacheable(value = "newsCache",key = "#id+'_'+#request.getHeader('token')")
    @GetMapping("/a/{id}")
    @ApiOperation(value = "查询文章详细内容并保存浏览记录(前台)(可用于首页查看文章、历史记录中查看文章，同时返回推荐的新闻)")
    public Response<NewsAndRecommendDto> queryNewsById(@PathVariable Long id, HttpServletRequest request){
        log.info("根据id查询文章...");
        NewsDto news = newsService.selectNewsById(id);
        NewsAndRecommendDto newsAndRecommendDto = new NewsAndRecommendDto();
        if (news != null){
            Long userId = Long.valueOf(JwtUtils.getUserId(request.getHeader("token")));
            LocalDateTime date = LocalDateTime.now();
            //调用保存历史记录接口，将历史记录保存到redis
            historyService.saveHistory(userId,date,id);
            //获取推荐新闻
            ArrayList<NewsUserRecommendDto> arrayList = userCFNewsRecommendSerevice.RecommendTopic(userId);
            newsAndRecommendDto.setNewsDto(news);
            newsAndRecommendDto.setRecommendList(arrayList);
            return Response.success(newsAndRecommendDto);
        }
        return Response.error("未查询到文章");
    }

    /**
     * 查询热点文章
     * @return
     */
    @GetMapping("/hotNews")
    @ApiOperation(value = "查询热点文章(前十)(前台)")
    public Response<ArrayList<NewsDto>> getNewsLikeCount(){
        ArrayList<NewsDto> list = newsService.queryHotNews();
        return Response.success(list);
    }

    /**
     * 搜索框查询文章
     * 用户输入记录保存到redis
     * @return
     */
    @GetMapping("/querynews")
    @ApiOperation(value = "搜索框查询文章(前台)")
    public Response<ArrayList> queryNews(@RequestParam String keyWords,HttpServletRequest request){
        Long userId = Long.valueOf(JwtUtils.getUserId(request.getHeader("token")));
        ArrayList<NewsKeyQueryDto> list = newsService.queryNews(keyWords);
        //用户搜索记录保存
        redisService.saveUserQuery(keyWords,userId);
        return Response.success(list);
    }

    /**
     * 查询用户搜索历史关键字
     * @return
     */
    @GetMapping("/hiskeywds")
    @ApiOperation(value = "查询用户搜索历史关键字(前台)")
    public Response<List> queryHisKeyWds(HttpServletRequest request){
        Long userId = Long.valueOf(JwtUtils.getUserId(request.getHeader("token")));
        List list = redisService.getUserQuery(userId);
        return Response.success(list);
    }

    /**
     * 查询首页推荐文章
     * @return
     */
    @GetMapping("/likeNews")
    @ApiOperation(value = "查询首页推荐文章(前台)")
    public Response<ArrayList<NewsUserRecommendDto>> queryNewsLike(HttpServletRequest request){
        Long userId = null;
        try {
            userId = Long.valueOf(JwtUtils.getUserId(request.getHeader("token")));
        } catch (Exception e) {
            ArrayList<NewsUserRecommendDto> list = newsService.randomLikeNews();
            return Response.success(list);
        }
        ArrayList<NewsUserRecommendDto> list = newsService.queryLikeNews(userId);
        return Response.success(list);
    }
}
