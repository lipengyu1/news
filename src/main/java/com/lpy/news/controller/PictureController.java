package com.lpy.news.controller;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.entity.Picture;
import com.lpy.news.model.Response;
import com.lpy.news.service.impl.PictureServiceImpl;
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
import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/pictureshow")
@Api(tags = "轮播图相关接口")
public class PictureController {

    @Autowired
    private PictureServiceImpl pictureService;

    /**
     * 新增轮播图
     * @return
     */
    @CacheEvict(value = "showNewsPicture",allEntries = true)
    @PostMapping
    @ApiOperation(value = "新增轮播图接口(后台)")
    public Response<String> save(HttpServletRequest request, @RequestBody Picture picture){
        picture.setEmpId(Long.valueOf(JwtUtils.getUserId(request.getHeader("token"))));
        pictureService.savePicture(picture);
        return Response.success("新增轮播图成功");
    }

    /**
     * 删除轮播图
     */
    @CacheEvict(value = "showNewsPicture",allEntries = true)
    @PutMapping("/del")
    @ApiOperation(value = "删除轮播图接口(后台)")
    public Response<String> delete(@RequestParam Long[] ids){
        log.info("ids:{}",ids);
        pictureService.removePicture(ids);
        return Response.success("轮播图删除成功");
    }

    /**
     * 修改轮播图
     * @param picture
     * @return
     */
    @CacheEvict(value = "showNewsPicture",allEntries = true)
    @PutMapping
    @ApiOperation(value = "轮播图文章接口(后台)")
    public Response<String> update(@RequestBody Picture picture){
        log.info(picture.toString());
        pictureService.updateNews(picture);
        return Response.success("轮播图修改成功");
    }

    /**
     * 轮播图分页查询
     * @param pageNo
     * @param pageSize
     * @param divideName
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "轮播图查询公告接口(后台)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo",value = "页码",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页记录数",required = true),
            @ApiImplicitParam(name = "divideName",value = "分类名",required = false)
    })
    public Response<BasePageResponse<Picture>> page(int pageNo, int pageSize, String divideName){
        log.info("pageNo={},pageSize={},createPerson={}",pageNo,pageSize,divideName);
        BasePageResponse<Picture> response = pictureService.queryPicturePage(pageNo,pageSize,divideName);
        return Response.success(response);
    }

    /**
     * 轮播图分页查询
     * @param divideName
     * @return
     */
    @Cacheable(value = "showNewsPicture",key = "#divideName+'_'+'NewsPicture'")
    @GetMapping("/show")
    @ApiOperation(value = "轮播图查询公告接口(前台)")
    public Response<ArrayList<Picture>> page2(@RequestParam String divideName){
        ArrayList<Picture> arrayList = pictureService.showPicturePage(divideName);
        return Response.success(arrayList);
    }

    /**
     * id查询轮播图（回显）
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询查询轮播图（接口(id)(后台)")
    public Response<Picture> getById(@PathVariable Long id){
        log.info("根据id查询轮播图（...");
        Picture picture = pictureService.selectPictureById(id);
        if (picture != null){
            return Response.success(picture);
        }
        return Response.error("未查询到轮播图（");
    }
}
