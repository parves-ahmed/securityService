package com.example.securityservice.Service;

import com.example.securityservice.Domain.Employee;
import com.example.securityservice.Domain.Role;
import com.example.securityservice.Repository.EmployeeRepository;
import com.example.securityservice.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;

    @Override
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String employeeName, String roleName) {
        Employee employee = employeeRepository.findEmployeesByEmployeeName(employeeName);
        Role role = roleRepository.findRoleByRoleName(roleName);
        employee.getRoles().add(role);
    }

    @Override
    public Employee getEmployee(String employeeName) {
        return employeeRepository.findEmployeesByEmployeeName(employeeName);
    }

    @Override
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }
}
