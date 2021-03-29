package com.scheduler.bkend.service;

import com.scheduler.bkend.model.Address;
import com.scheduler.bkend.model.Employee;
import com.scheduler.bkend.model.OutOfOffice;
import com.scheduler.bkend.model.WorkSchedule;

import java.util.List;

public interface IEmployeeService {
    /**
     * Employee service is for CRUD operations to the employee table
     */

    boolean createEmployee(Employee employee);

    Employee findOne(Employee employee);
    List<Employee> getEmployees(Employee employee);
    boolean updateEmployee(Employee employee, Address address);

    boolean setEmployeeSchedule(Employee employee, WorkSchedule schedule);
    WorkSchedule getEmployeeSchedule(Employee employee);

    boolean setOutOfOffice(Employee employee, OutOfOffice outOfOffice);
    List<OutOfOffice> getOutOfOffices(Employee employee);
    boolean deleteOutOfOffice(OutOfOffice outOfOffice);
}
