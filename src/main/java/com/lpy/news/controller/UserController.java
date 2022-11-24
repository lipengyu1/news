package com.lpy.news.controller;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dto.UserDto;
import com.lpy.news.entity.User;
import com.lpy.news.model.Response;
import com.lpy.news.service.impl.UserServiceImpl;
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
@RequestMapping("/user")
@Api(tags = "用户信息相关接口")
public class UserController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserServiceImpl userService;
    /**
     * 用户登录
     * @param user
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录接口")
    public Response<User> login(@RequestBody User user, HttpServletRequest httpServletRequest) {
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        User user1 = userService.userLogin(user);
        if (user1 == null) {
            return Response.error("登录失败，该用户不存在");
        }
        if (!(user1.getPassword().equals(password))) {
            return Response.error("登录失败，密码错误");
        }
        if (user1.getStatus() == 0) {
            return Response.error("账号已禁用");
        }
        httpServletRequest.getSession().setAttribute("user", user1.getName());
        return Response.success(user1);
    }

    /**
     * 用户注册
     * @return
     */
    @PutMapping("/register")
    @ApiOperation(value = "用户注册接口")
    public Response<String> register(@RequestBody User user, HttpServletRequest httpServletRequest){
        log.info("用户注册");
        User user1 =  userService.userLogin(user);
        if (user1 == null){
            userService.userRegister(user);
            return Response.success("注册成功");
        }else {
            return Response.error("该邮箱已注册");
        }
    }
    /**
     * 发送验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    @ApiOperation(value = "验证码接口")
    public Response<String> sendMsg(@RequestBody User user){
        //获取邮箱号
        String email = user.getEmail();
        String subject = "新闻系统【找回密码】";
        //StringUtils.isNotEmpty字符串非空判断
        if (StringUtils.isNotEmpty(email)) {
            //发送一个四位数的验证码,把验证码变成String类型
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            String text = "【新闻系统】您好，您的验证码为：" + code + "，有效期5分钟";
            log.info("验证码为：" + code);
            //发送短信
            try {
                userService.sendMsg(email,subject,text);
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
    @ApiOperation(value = "用户找回密码接口")
    public Response<String> find(@RequestBody Map map) {
        log.info("找回密码", map);
        String email = map.get("email").toString();
        String code = map.get("code").toString();
        String newPasswd = map.get("password").toString();
        newPasswd =  DigestUtils.md5DigestAsHex(newPasswd.getBytes());
        Object sessionCode = redisTemplate.opsForValue().get(email);
        if (sessionCode != null && sessionCode.equals(code)) {
            userService.setNewPasswd(email,newPasswd);
            redisTemplate.delete(email);
            return Response.success("找回密码成功");
        }
        return Response.error("验证码错误");
    }

    /**
     * 用户退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "用户退出接口")
    public Response<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return Response.success("退出成功");
    }

    /**
     * id查询用户信息（回显）
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "用户查询接口(id)")
    public Response<User> getById(@PathVariable Long id) {
        log.info("根据id查询用户信息...");
        User user = userService.selectUserById(id);
        if (user != null) {
            return Response.success(user);
        }
        return Response.error("未查询到用户信息");
    }

    /**
     * 用户信息分页查询与条件查询
     * @param pageNo
     * @param pageSize
     * @param name
     * @param email
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询用户接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo",value = "页码",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页记录数",required = true),
            @ApiImplicitParam(name = "name",value = "用户姓名",required = false),
            @ApiImplicitParam(name = "email",value = "用户邮箱",required = false)
    })
    public Response<BasePageResponse<UserDto>> page(int pageNo, int pageSize, String name,String email) {
        log.info("pageNo={},pageSize={},name={},email={}", pageNo, pageSize, name,email);
        BasePageResponse<UserDto> response = userService.queryUserPage(pageNo, pageSize, name,email);
        return Response.success(response);
    }

    /**
     * 删除用户（单选、多选）逻辑删除
     * @param ids
     * @return
     */
    @PutMapping("/del")
    @ApiOperation(value = "删除接口")
    public Response<String> delete(@RequestParam Long[] ids) {
        log.info("ids:{}", ids);
        userService.removeUser(ids);
        return Response.success("用户删除成功");
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @PutMapping
    @ApiOperation(value = "用户信息修改接口")
    public Response<String> update(@RequestBody User user) {
        log.info(user.toString());
        userService.updateUser(user);
        return Response.success("用户信息修改成功");
    }
}
