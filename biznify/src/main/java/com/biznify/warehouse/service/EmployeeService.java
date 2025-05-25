package com.biznify.warehouse.service;

import com.biznify.warehouse.dto.EmployeeDTO;
import com.biznify.warehouse.entity.Employee;

public interface EmployeeService {
    Employee addEmployee(EmployeeDTO employeeDTO);
}
