package com.lpy.news.service;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dto.EmployeeDto;
import com.lpy.news.entity.Employee;


public interface EmployeeService {
    Employee selectEmpById(Long id);

    BasePageResponse<EmployeeDto> queryEmpPage(int page, int pageSize, String name);

    void updateEmp(Employee employee);

    void removeEmp(Long[] ids);

    Employee empLogin(Employee employee);

    void setNewPasswd(String email,String newPasswd);

    //发送邮件
    void sendMsg(String to,String subject,String text);

    void empRegister(Employee employee);

    Employee getEmpEmail(String email);
}
