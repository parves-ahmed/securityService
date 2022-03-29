package com.example.securityservice.api;

import com.example.securityservice.Domain.Employee;
import com.example.securityservice.Domain.Role;
import com.example.securityservice.Service.EmployeeService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeResource {
    private final EmployeeService employeeService;

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>>getEmployees(){
        return ResponseEntity.ok().body(employeeService.getEmployees());
    }

    @PostMapping("/save/employee")
    public ResponseEntity<Employee>saveEmployee(@RequestBody Employee employee){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/save/employee").toUriString());
        return ResponseEntity.created(uri).body(employeeService.saveEmployee(employee));
    }

    @PostMapping("/save/role")
    public ResponseEntity<Role>saveRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/save/role").toUriString());
        return ResponseEntity.created(null).body(employeeService.saveRole(role));
    }

    @PostMapping("/role/addToEmployee")
    public ResponseEntity<?>addRoleToUser(@RequestBody RoleToEmployeeForm form){
        employeeService.addRoleToEmployee(form.getEmployeeName(), form.getRoleName());
        return ResponseEntity.ok().build();
    }
}

@Data
class RoleToEmployeeForm{
    private String employeeName;
    private String roleName;
}
