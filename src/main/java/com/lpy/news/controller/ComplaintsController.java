package com.lpy.news.controller;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dto.ComplaintsDto;
import com.lpy.news.dto.NoticeDto;
import com.lpy.news.entity.Notice;
import com.lpy.news.model.Response;
import com.lpy.news.service.impl.ComplaintsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/complaints")
@Api(tags = "投诉相关接口")
public class ComplaintsController {

    @Autowired
    private ComplaintsServiceImpl  complaintsService;
    /**
     * 新增投诉
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增投诉接口")
    public Response<String> save(@RequestBody ComplaintsDto complaintsDto){
        log.info(complaintsDto.toString());
        complaintsService.saveComplaints(complaintsDto);
        return Response.success("新增投诉成功");
    }

    /**
     * 删除投诉（单选、多选）逻辑删除
     * @param ids
     * @return
     */
    @PutMapping("/del")
    @ApiOperation(value = "删除投诉接口")
    public Response<String> delete(@RequestParam Long[] ids){
        log.info("ids:{}",ids);
        complaintsService.removeComplaints(ids);
        return Response.success("投诉删除成功");
    }

    /**
     * 投诉分页查询
     * @param pageNo
     * @param pageSize
     * @param complaintsCategory
     * @param complaintsTime
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询投诉接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo",value = "页码",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页记录数",required = true),
            @ApiImplicitParam(name = "complaintsCategory",value = "投诉类别",required = false),
            @ApiImplicitParam(name = "complaintsTime",value = "投诉时间",required = false)
    })
    public Response<BasePageResponse<ComplaintsDto>> page(int pageNo, int pageSize, String complaintsCategory, LocalDateTime complaintsTime){
        log.info("pageNo={},pageSize={},complaintsCategory={},complaintsTime={}",pageNo,pageSize,complaintsCategory,complaintsTime);
        BasePageResponse<ComplaintsDto> response = complaintsService.queryComplaintsPage(pageNo,pageSize,complaintsCategory,complaintsTime);
        return Response.success(response);
    }
}
