package com.lpy.news.service.impl;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dao.EmployeeDao;
import com.lpy.news.dto.EmployeeDto;
import com.lpy.news.entity.Employee;
import com.lpy.news.service.EmployeeService;
import com.lpy.news.service.SnowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeDao employeeDao;
    //把yml配置的邮箱号赋值到from
    @Value("${spring.mail.username}")
    private String from;
    //发送邮件需要的对象
    @Autowired
    private JavaMailSender javaMailSender;

    SnowService snowService = new SnowService(1, 1);


    @Override
    public Employee selectEmpById(Long id) {
        return employeeDao.selectEmpById(id);
    }

    @Override
    public BasePageResponse<EmployeeDto> queryEmpPage(int pageNo, int pageSize, String name) {
        int pageNo1 = pageSize * (pageNo - 1);
        //分页查询，将结果存入List
        List<EmployeeDto> queryList = employeeDao.queryEmpPage(pageNo1, pageSize, name);
        ArrayList<EmployeeDto> arrayList = new ArrayList<>(queryList);
        //总条数
        int totalCount = employeeDao.queryEmpCount(pageNo1, pageSize, name);
        //分页信息
        BasePageResponse<EmployeeDto> basePageResponse = new BasePageResponse<>();
        basePageResponse.setPageNo(pageNo);
        basePageResponse.setPageSize(pageSize);
        basePageResponse.setTotalPage((int) Math.ceil((float) totalCount / pageSize));//总页数
        basePageResponse.setResultList(arrayList);
        basePageResponse.setTotalCount(totalCount);//总条数
        return basePageResponse;
    }

    @Override
    public void updateEmp(Employee employee) {
        employee.setUpdateTime(LocalDateTime.now());
        employeeDao.updateEmp(employee);
    }

    @Override
    public void removeEmp(Long[] ids) {
        for (int i = 0; i < ids.length; i++) {
            Long id = ids[i];
            employeeDao.removeEmp(id);
        }
    }

    @Override
    public Employee empLogin(Employee employee) {
        return employeeDao.empLogin(employee);
    }

    @Override
    public void setNewPasswd(String email,String newPasswd) {
        employeeDao.setNewPasswd(email,newPasswd);
    }

    @Override
    public void sendMsg(String to, String subject, String text) {
        //发送简单邮件，简单邮件不包括附件等别的
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        //发送邮件
        javaMailSender.send(message);
    }

    @Override
    public void empRegister(Employee employee) {
        String password = employee.getPassword();
        //密码加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        employee.setPassword(password);
        employee.setId(snowService.getId());
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        employeeDao.empRegister(employee);
    }

    @Override
    public Employee getEmpEmail(String email) {
        return employeeDao.getEmpEmail(email);
    }
}
