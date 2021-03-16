package com.scheduler.bkend.service;

import com.scheduler.bkend.model.Employee;

import java.util.List;

public interface IEmployeeService {
    /**
     * Employee service is for CRUD operations to the employee table
     */

    boolean createEmployee(Employee employee);

    Employee findOne(Employee employee);
    List<Employee> getEmployees(Employee employee);
    boolean updateEmployee(Employee employee);


}
