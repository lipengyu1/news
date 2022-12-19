package com.lpy.news.controller;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dto.EmployeeDto;
import com.lpy.news.model.Response;
import com.lpy.news.entity.Employee;
import com.lpy.news.service.impl.EmployeeServiceImpl;
import com.lpy.news.utils.ValidateCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
@RestController
@RequestMapping("/employee")
@Api(tags = "员工相关接口")
public class EmployeeController {
    @Autowired
    private EmployeeServiceImpl employeeService;
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 员工注册
     * @return
     */
    @PutMapping("/register")
    @ApiOperation(value = "员工注册接口")
    public Response<String> register(@RequestBody Employee employee, HttpServletRequest httpServletRequest){
        log.info("员工注册");
        //调用登录查询，判断用户是否存在
        Employee emp =  employeeService.empLogin(employee);
        if (emp == null){
            Employee email = employeeService.getEmpEmail(employee.getEmail());
            if (email == null){
                employeeService.empRegister(employee);
                return Response.success("注册成功");
            }else {
                return Response.error("该邮箱已注册");
            }
        }else {
            return Response.error("该账号已注册");
        }
    }

    /**
     * 员工登录
     * @param employee
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录接口")
    public Response<Employee> login(@RequestBody Employee employee, HttpServletRequest httpServletRequest) {
        String password = employee.getPassword();
        //密码加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        Employee emp = employeeService.empLogin(employee);

        if (emp == null) {
            return Response.error("登录失败，该用户不存在");
        }
        if (!(emp.getPassword().equals(password))) {
            return Response.error("登录失败，密码错误");
        }
        if (emp.getStatus() == 0) {
            return Response.error("账号已禁用");
        }
        httpServletRequest.getSession().setAttribute("employee", emp.getId());
        return Response.success(emp);
    }

    /**
     * 发送验证码
     * @param employee
     * @return
     */
    @PostMapping("/sendMsg")
    @ApiOperation(value = "验证码接口")
    public Response<String> sendMsg(@RequestBody Employee employee){
        //获取邮箱号
        String email = employee.getEmail();
        String subject = "新闻系统";
        //StringUtils.isNotEmpty字符串非空判断
        if (StringUtils.isNotEmpty(email)) {
            //发送一个四位数的验证码,把验证码变成String类型
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            String text = "【新闻系统】您好，您的验证码为：" + code + "，有效期5分钟，请尽快登录";
            log.info("验证码为：" + code);
            //发送短信
            try {
                employeeService.sendMsg(email,subject,text);
            } catch (Exception e) {
                //捕获邮箱错误异常
                return Response.error("邮箱错误，请重新输入");
            }
            //将验证码保存到session当中
            //将生成的验证码到redis中，并设置有效期为5分钟
            redisTemplate.opsForValue().set(email,code,5, TimeUnit.MINUTES);
            return Response.success("验证码发送成功");
        }
        return Response.error("验证码发送异常，请重新发送");
    }

    /**
     * 找回密码
     * @param map
     * @return
     */
    @PutMapping("/find")
    @ApiOperation(value = "员工找回密码接口")
    public Response<String> find(@RequestBody Map map) {
        log.info("找回密码", map);
        //获取邮箱，用户输入的
        String email = map.get("email").toString();
        //获取验证码，用户输入的
        String code = map.get("code").toString();
        //获取新密码
        String newPasswd = map.get("password").toString();
        newPasswd =  DigestUtils.md5DigestAsHex(newPasswd.getBytes());
        //从redis中获取验证码
        Object sessionCode = redisTemplate.opsForValue().get(email);
        //如果session的验证码和用户输入的验证码进行比对,&&同时
        if (sessionCode != null && sessionCode.equals(code)) {
            employeeService.setNewPasswd(email,newPasswd);
            redisTemplate.delete(email);
            return Response.success("找回密码成功");
        }
        return Response.error("验证码错误");
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "员工退出接口")
    public Response<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return Response.success("退出成功");
    }

    /**
     * id查询员工信息（回显）
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "员工查询接口(id)")
    public Response<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.selectEmpById(id);
        if (employee != null) {
            return Response.success(employee);
        }
        return Response.error("未查询到员工信息");
    }

    /**
     * 员工信息分页查询与条件（name）查询
     *
     * @param pageNo
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询员工接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo",value = "页码",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页记录数",required = true),
            @ApiImplicitParam(name = "name",value = "员工名称",required = false)
    })
    public Response<BasePageResponse<EmployeeDto>> page(int pageNo, int pageSize, String name) {
        log.info("pageNo={},pageSize={},name={}", pageNo, pageSize, name);
        BasePageResponse<EmployeeDto> response = employeeService.queryEmpPage(pageNo, pageSize, name);
        return Response.success(response);
    }

    /**
     * 修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    @ApiOperation(value = "员工信息修改接口")
    public Response<String> update(@RequestBody Employee employee) {
        log.info(employee.toString());
        employeeService.updateEmp(employee);
        return Response.success("员工信息修改成功");
    }

    /**
     * 删除员工（单选、多选）逻辑删除
     * /employee/del?ids=1,2
     *
     * @param ids
     * @return
     */
    @PutMapping("/del")
    @ApiOperation(value = "删除接口")
    public Response<String> delete(@RequestParam Long[] ids) {
        log.info("ids:{}", ids);
        employeeService.removeEmp(ids);
        return Response.success("员工删除成功");
    }
}
