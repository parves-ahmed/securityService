package com.example.securityservice.Service;

import com.example.securityservice.Domain.Employee;
import com.example.securityservice.Domain.Role;

import java.util.List;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    Role saveRole(Role role);
    void addRoleToEmployee(String employeeName, String roleName);
    Employee getEmployee(String employeeName);
    List<Employee> getEmployees();
}
