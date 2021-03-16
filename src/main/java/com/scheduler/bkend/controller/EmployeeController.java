package com.scheduler.bkend.controller;

import com.scheduler.bkend.model.Employee;
import com.scheduler.bkend.service.EmployeeRepository;
import com.scheduler.bkend.service.IEmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Controller
public class EmployeeController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private IEmployeeService empService;
    private EmployeeRepository empRepo;


    @Autowired
    public EmployeeController(IEmployeeService empService, EmployeeRepository empRepo) {
        this.empService = empService;
        this.empRepo = empRepo;
    }

    @ResponseBody
    @PostMapping("/employee/create")
    public ResponseEntity createEmployee(@RequestBody Employee employee) {
//        logger.info("EmployeeController::createEmployee => {}", employee.toString());
        if(this.empService.createEmployee(employee))
            return new ResponseEntity("EMPLOYEE CREATED SUCCESSFULLY", HttpStatus.OK);
        else return new ResponseEntity("EMPLOYEE ALREADY EXISTS IN SYSTEM", HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @GetMapping("/employee/get")
    public ResponseEntity<List<Employee>> getEmployee(@RequestBody Employee employee) {
        logger.info("EmployeeController::getEmployee => {}", employee.toString());
        List<Employee> list = this.empService.getEmployees(employee);
        return new ResponseEntity<List<Employee>>(list, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/employee/update")
    public ResponseEntity updateEmployee(@RequestBody Employee employee) {
        logger.info("EmployeeController::updateEmployee => {}", employee.toString());
        if(this.empService.updateEmployee(employee))
            return new ResponseEntity("EMPLOYEE UPATED SUCCESSFULLY", HttpStatus.OK);
        else return new ResponseEntity("ERROR IN UPATED", HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @PostMapping("/employee/delete")
    public ResponseEntity deleteEmployee(@RequestBody Employee employee) {
        logger.info("EmployeeController::deleteEmployee => {}", employee.toString());
        try{
            Employee tmp = this.empRepo.getOne(employee.getEmpid());
            System.out.printf("FOUND EMPLOYEE => %s", tmp.toString());
            this.empRepo.delete(tmp);
            return new ResponseEntity("EMPLOYEE DELETED SUCCESSFULLY", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity("ERROR DELETING", HttpStatus.BAD_REQUEST);
        }
    }
}
