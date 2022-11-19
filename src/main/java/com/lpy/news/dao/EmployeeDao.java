package com.lpy.news.dao;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.dto.EmployeeDto;
import com.lpy.news.entity.Employee;

import java.util.List;

public interface EmployeeDao {

    Employee selectEmpById(Long id);

    List<EmployeeDto> queryEmpPage(int pageNo, int pageSize, String name);

    int queryEmpCount(int pageNo, int pageSize, String name);

    void updateEmp(Employee employee);

    void removeEmp(Long ids);

    Employee empLogin(Employee employee);

    void setNewPasswd(String email, String newPasswd);

    void empRegister(Employee employee);

    Employee getEmpEmail(String email);
}
