package com.biznify.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biznify.warehouse.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
