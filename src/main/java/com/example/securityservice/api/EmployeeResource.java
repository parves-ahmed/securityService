package com.example.securityservice.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.securityservice.Domain.Employee;
import com.example.securityservice.Domain.Role;
import com.example.securityservice.Service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeResource {
    private final EmployeeService employeeService;

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>>getEmployees(){
        return ResponseEntity.ok().body(employeeService.getEmployees());
    }

    @PostMapping("/employee/save")
    public ResponseEntity<Employee>saveEmployee(@RequestBody Employee employee){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/employee/save").toUriString());
        return ResponseEntity.created(uri).body(employeeService.saveEmployee(employee));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role>saveRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/save/role").toUriString());
        return ResponseEntity.created(uri).body(employeeService.saveRole(role));
    }

    @PostMapping("/role/addToEmployee")
    public ResponseEntity<?>addRoleToUser(@RequestBody RoleToEmployeeForm form){
        employeeService.addRoleToEmployee(form.getEmployeeName(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/auth/refresh/token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {

                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                Employee employee = employeeService.getEmployee(username);
                String access_token = JWT.create()
                        .withSubject(employee.getEmployeeName())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 40 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", employee.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
        else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}

@Data
class RoleToEmployeeForm{
    private String employeeName;
    private String roleName;
}
