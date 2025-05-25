package com.biznify.warehouse.controller;

import com.biznify.warehouse.dto.EmployeeDTO;
import com.biznify.warehouse.entity.Employee;
import com.biznify.warehouse.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee savedEmployee = employeeService.addEmployee(employeeDTO);
        return ResponseEntity.ok(savedEmployee);
    }
}
