package com.example.securityservice;

import com.example.securityservice.Domain.Employee;
import com.example.securityservice.Domain.Role;
import com.example.securityservice.Service.EmployeeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SecurityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityServiceApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(EmployeeService employeeService){
        return args -> {
            employeeService.saveRole(new Role(null,"ROLE_SUPER_ADMIN"));
            employeeService.saveRole(new Role(null,"ROLE_ADMIN"));
            employeeService.saveRole(new Role(null,"ROLE_MANAGER"));
            employeeService.saveRole(new Role(null,"ROLE_USER"));

            employeeService.saveEmployee(new Employee(null, "parves", "parves@gmail.com", "parves", new ArrayList<>()));
            employeeService.saveEmployee(new Employee(null, "ahmed", "ahmed@gmail.com", "ahmed", new ArrayList<>()));
            employeeService.saveEmployee(new Employee(null, "moon", "moon@gmail.com", "moon", new ArrayList<>()));
            employeeService.saveEmployee(new Employee(null, "arman", "arman@gmail.com", "arman", new ArrayList<>()));

            employeeService.addRoleToEmployee("parves", "ROLE_SUPER_ADMIN");
            employeeService.addRoleToEmployee("ahmed", "ROLE_ADMIN");
            employeeService.addRoleToEmployee("moon", "ROLE_MANAGER");
            employeeService.addRoleToEmployee("arman", "ROLE_USER");
        };
    }
}
