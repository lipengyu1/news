package com.lpy.news.controller;

import com.lpy.news.dto.NewsUserHistoryDto;
import com.lpy.news.model.Response;
import com.lpy.news.service.impl.HistoryServiceImpl;
import com.lpy.news.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;


@Slf4j
@RestController
@RequestMapping("/history")
@Api(tags = "历史记录接口")
public class HistoryController {
    @Autowired
    private HistoryServiceImpl historyService;

//    删除历史浏览记录
    @DeleteMapping("/delhistory")
    @ApiOperation(value = "用户删除历史记录(前台)")
    public Response<String> delHistory(HttpServletRequest request, @RequestParam Long newsId){
        Long userId = Long.valueOf(JwtUtils.getUserId(request.getHeader("token")));
        historyService.delHistory(userId,newsId);
        return Response.success("删除成功");
    }

//    查询历史浏览记录（根据redis中的newsId查询详细文章信息）
    @GetMapping("/gethistory")
    @ApiOperation(value = "用户查询历史记录(前台)")
    public Response<ArrayList<NewsUserHistoryDto>> queryHistory(HttpServletRequest request, @RequestParam Long newsId){
    Long userId = Long.valueOf(JwtUtils.getUserId(request.getHeader("token")));
    ArrayList<NewsUserHistoryDto> arrayList = historyService.queryHistory(userId,newsId);
    return Response.success(arrayList);
    }
}
