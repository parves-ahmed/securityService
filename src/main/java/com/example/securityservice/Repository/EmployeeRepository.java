package com.example.securityservice.Repository;

import com.example.securityservice.Domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findEmployeesByEmployeeName(String employeeName);
}
