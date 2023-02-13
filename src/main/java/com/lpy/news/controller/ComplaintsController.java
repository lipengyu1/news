package com.lpy.news.controller;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dto.ComplaintsDto;
import com.lpy.news.model.Response;
import com.lpy.news.service.impl.ComplaintsServiceImpl;
import com.lpy.news.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


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
    @ApiOperation(value = "新增投诉接口(前台)")
    public Response<String> save(@RequestBody ComplaintsDto complaintsDto, HttpServletRequest request){
        Long userId = Long.valueOf(JwtUtils.getUserId(request.getHeader("token")));
        log.info(complaintsDto.toString());
        complaintsDto.setUserId(userId);
        complaintsService.saveComplaints(complaintsDto);
        return Response.success("新增投诉成功");
    }

    /**
     * 删除投诉（单选、多选）逻辑删除
     * @param ids
     * @return
     */
    @PutMapping("/del")
    @ApiOperation(value = "删除投诉接口(后台)")
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
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询投诉接口(后台)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo",value = "页码",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页记录数",required = true),
            @ApiImplicitParam(name = "complaintsCategory",value = "投诉类别",required = false),
    })
    public Response<BasePageResponse<ComplaintsDto>> page(int pageNo, int pageSize, String complaintsCategory){
        log.info("pageNo={},pageSize={},complaintsCategory={}",pageNo,pageSize,complaintsCategory);
        BasePageResponse<ComplaintsDto> response = complaintsService.queryComplaintsPage(pageNo,pageSize,complaintsCategory);
        return Response.success(response);
    }
}
