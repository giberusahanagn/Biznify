package com.biznify.warehouse.serviceImplimentation;

import com.biznify.warehouse.dto.EmployeeDTO;
import com.biznify.warehouse.entity.Employee;
import com.biznify.warehouse.repository.EmployeeRepository;
import com.biznify.warehouse.service.EmployeeService;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImplimentation implements EmployeeService {

	@Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employeeRepository.save(employee);
    }
}