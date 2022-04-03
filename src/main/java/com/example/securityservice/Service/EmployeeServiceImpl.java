package com.example.securityservice.Service;

import com.example.securityservice.Domain.Employee;
import com.example.securityservice.Domain.Role;
import com.example.securityservice.Repository.EmployeeRepository;
import com.example.securityservice.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EmployeeServiceImpl implements EmployeeService, UserDetailsService {
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String employeeName) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findEmployeesByEmployeeName(employeeName);
        if(employee == null){
            log.error("Employee not found in the database");
            throw new UsernameNotFoundException("Employee not found in the database");
        }
        else {
            log.info("Employee found in database: {}", employeeName);
        }
        Collection<SimpleGrantedAuthority>authorities = new ArrayList<>();
        employee.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));
        return new org.springframework.security.core.userdetails.User(employee.getEmployeeName(), employee.getPassword(), authorities);
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return employeeRepository.save(employee);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToEmployee(String employeeName, String roleName) {
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
