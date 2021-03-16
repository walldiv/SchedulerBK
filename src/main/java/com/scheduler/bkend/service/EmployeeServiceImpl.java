package com.scheduler.bkend.service;

import com.scheduler.bkend.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeServiceImpl implements IEmployeeService{
    private Logger logger = LoggerFactory.getLogger(getClass());
    private EmployeeRepository empRepo;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository empRepo) {
        this.empRepo = empRepo;
    }


    @Override
    public boolean createEmployee(Employee employee) {
        Employee tmp = empRepo.findByEmail(employee.getEmail());
        if(tmp != null)
            return false;
        try{
            empRepo.save(employee);
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
        return true;
    }

    @Override
    public Employee findOne(Employee employee) {
        Optional<Employee> tmp = this.empRepo.findById(employee.getEmpid());
        Employee ret = tmp.isPresent() ? tmp.get() : new Employee();
        return ret;
    }

    @Override
    public List<Employee> getEmployees(Employee employee) {
        List<Employee> tmp = new ArrayList<>();
        ExampleMatcher matchlist = ExampleMatcher.matchingAll()
                .withMatcher("fname", ExampleMatcher.GenericPropertyMatchers.ignoreCase().contains())
                .withMatcher("lname", ExampleMatcher.GenericPropertyMatchers.ignoreCase().contains())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withIgnorePaths("empid", "phone","address", "orgunit");
        Example<Employee> example = Example.of(employee, matchlist);
        tmp = empRepo.findAll(example);
        return tmp;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        try{
            Employee tmp = this.empRepo.getOne(employee.getEmpid());
//            System.out.printf("FOUND EMPLOYEE => %s \n", tmp.toString());
            tmp.merge(employee);
//            System.out.printf("AFTER UPDATE => %s\n", tmp.toString());
            empRepo.save(tmp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
