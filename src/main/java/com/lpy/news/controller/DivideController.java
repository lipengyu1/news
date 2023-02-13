package com.lpy.news.controller;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.entity.Divide;
import com.lpy.news.model.Response;
import com.lpy.news.service.impl.DivideServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/divide")
@Api(tags = "分类相关接口")
public class DivideController {
    @Autowired
    private DivideServiceImpl divideService;

    /**
     * 新增分类
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增分类接口(后台)")
    public Response<String> save(@RequestBody Divide divide){
        log.info(divide.toString());
        divideService.saveDivide(divide);
        return Response.success("新增分类成功");
    }

    /**
     * 删除分类（单选、多选）逻辑删除
     * @param ids
     * @return
     */
    @PutMapping("/del")
    @ApiOperation(value = "删除分类接口(后台)")
    public Response<String> delete(@RequestParam Long[] ids){
        log.info("ids:{}",ids);
        divideService.removeDivide(ids);
        return Response.success("分类删除成功");
    }

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询分类接口(后台)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo",value = "页码",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页记录数",required = true),
            @ApiImplicitParam(name = "name",value = "分类名称",required = false)
    })
    public Response<BasePageResponse<Divide>> page(int pageNo, int pageSize, String name){
        log.info("pageNo={},pageSize={},name={}",pageNo,pageSize,name);
        BasePageResponse<Divide> response = divideService.queryDividePage(pageNo,pageSize,name);
        return Response.success(response);
    }

    /**
     * 修改分类
     * @param divide
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改分类接口(后台)")
    public Response<String> update(@RequestBody Divide divide){
        log.info(divide.toString());
        divideService.updateDivide(divide);
        return Response.success("分类修改成功");
    }

    /**
     * id查询（回显）
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询分类接口(id)(后台)")
    public Response<Divide> getById(@PathVariable Long id){
        log.info("根据id查询分类...");
        Divide divide = divideService.selectDivideById(id);
        if (divide != null){
            return Response.success(divide);
        }
        return Response.error("未查询到分类");
    }
}
