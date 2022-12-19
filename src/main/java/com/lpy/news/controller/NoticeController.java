package com.lpy.news.controller;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dto.NoticeDto;
import com.lpy.news.entity.Notice;
import com.lpy.news.model.Response;
import com.lpy.news.service.impl.NoticeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/notice")
@Api(tags = "公告相关接口")
public class NoticeController {

    @Autowired
    private NoticeServiceImpl noticeService;

    /**
     * 新增公告
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增公告接口")
    public Response<String> save(@RequestBody NoticeDto noticeDto){
        log.info(noticeDto.toString());
        noticeService.saveNotice(noticeDto);
        return Response.success("新增公告成功");
    }

    /**
     * 删除公告（单选、多选）逻辑删除
     * @param ids
     * @return
     */
    @PutMapping("/del")
    @ApiOperation(value = "删除公告接口")
    public Response<String> delete(@RequestParam Long[] ids){
        log.info("ids:{}",ids);
        noticeService.removeNotice(ids);
        return Response.success("公告删除成功");
    }

    /**
     * 公告分页查询
     * @param pageNo
     * @param pageSize
     * @param createPerson
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询公告接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo",value = "页码",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页记录数",required = true),
            @ApiImplicitParam(name = "createPerson",value = "创建者姓名",required = false)
    })
    public Response<BasePageResponse<NoticeDto>> page(int pageNo, int pageSize, String createPerson){
        log.info("pageNo={},pageSize={},createPerson={}",pageNo,pageSize,createPerson);
        BasePageResponse<NoticeDto> response = noticeService.queryNoticePage(pageNo,pageSize,createPerson);
        return Response.success(response);
    }

    /**
     * 修改公告
     * @param notice
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改公告接口")
    public Response<String> update(@RequestBody Notice notice){
        log.info(notice.toString());
        noticeService.updateNotice(notice);
        return Response.success("公告修改成功");
    }

    /**
     * id查询公告（回显）
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询公告接口(id)")
    public Response<NoticeDto> getById(@PathVariable Long id){
        log.info("根据id查询公告...");
        NoticeDto notice = noticeService.selectNoticeById(id);
        if (notice != null){
            return Response.success(notice);
        }
        return Response.error("未查询到公告");
    }
}
