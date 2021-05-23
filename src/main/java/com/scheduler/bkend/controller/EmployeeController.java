package com.scheduler.bkend.controller;

import com.scheduler.bkend.model.*;
import com.scheduler.bkend.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
public class EmployeeController {
    static class EmployeeAndAddress {
        public Employee employee;
        public Address address;
    }

    static class EmployeeWorkSchedule {
        public Employee employee;
        public WorkSchedule schedule;
    }

    static class EmployeeOOO{
        public Employee employee;
        public OutOfOffice outOfOffice;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private IEmployeeService empService;
    private EmployeeRepository empRepo;
    private AddressRepository addressRepo;
    private AppointmentRepository apptRepo;
    private WorkScheduleRepository workRepo;


    @Autowired
    public EmployeeController(IEmployeeService empService, EmployeeRepository empRepo, AddressRepository addressRepo,
                              AppointmentRepository apptRepo, WorkScheduleRepository workRepo) {
        this.empService = empService;
        this.empRepo = empRepo;
        this.addressRepo = addressRepo;
        this.apptRepo = apptRepo;
        this.workRepo = workRepo;
    }

    @ResponseBody
    @PostMapping("/employee/create")
    public ResponseEntity createEmployee(@RequestBody Employee inObject) {
        logger.info("ClientController::createClient => {}", inObject.toString());
        //Add the address if it doesnt already exist - else use existing address in DB
        ExampleMatcher matchlist = ExampleMatcher.matchingAll()
                .withMatcher("street", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withMatcher("zipcode", ExampleMatcher.GenericPropertyMatchers.ignoreCase().contains())
                .withIgnorePaths("addressid", "street2", "city", "state", "country");
        Example<Address> example = Example.of(inObject.getAddress(), matchlist);
        Optional<Address> thisAddress = this.addressRepo.findOne(example);
        if(thisAddress.isPresent())
            inObject.setAddress(thisAddress.get());
        else {
//            inObject.employee.setAddress(inObject.address);
            this.addressRepo.save(inObject.getAddress());
        }
        this.workRepo.save(inObject.getWorkschedule());
        if (this.empService.createEmployee(inObject))
            return new ResponseEntity("EMPLOYEE CREATED SUCCESSFULLY", HttpStatus.OK);
        else return new ResponseEntity("EMPLOYEE ALREADY EXISTS IN SYSTEM", HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @GetMapping("/employee/get")
    public ResponseEntity<List<Employee>> getEmployee() {
        List<Employee> list = this.empService.getAllEmployees();
        return new ResponseEntity<List<Employee>>(list, HttpStatus.OK);
    }
    @ResponseBody
    @PostMapping("/employee/getsingle")
    public ResponseEntity<Employee> getEmployeeSingle(@RequestBody Employee employee) {
        logger.info("EmployeeController::getEmployeeSingle => {}", employee.toString());
//        Employee ret = this.empService.findOne(employee);
        ExampleMatcher matchlist = ExampleMatcher.matchingAny()
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withMatcher("phone", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withIgnorePaths("fname", "lname", "orgunit", "workschedule", "outofoffices");
        Example<Employee> example = Example.of(employee, matchlist);
        Optional<Employee> thisEmp = this.empRepo.findOne(example);
        logger.info("EMPLOYEE RETURNED => {}", thisEmp.get().toString());
        return new ResponseEntity<Employee>(thisEmp.get(), HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/employee/getappointments")
    public ResponseEntity<List<Appointment>> getEmployeeAppointments(@RequestParam("empid") int empid) {
        List<Appointment> list = this.apptRepo.findAllByEmployeetId(empid);
        return new ResponseEntity<List<Appointment>>(list, HttpStatus.OK);
    }


    @ResponseBody
    @PostMapping("/employee/update")
    public ResponseEntity updateEmployee(@RequestBody Employee inObject) {
        logger.info("EmployeeController::updateEmployee => {}", inObject.toString());
//        if (this.empService.updateEmployee(inObject.employee, inObject.address))
        if (this.empService.updateEmployee(inObject))
            return new ResponseEntity("EMPLOYEE UPATED SUCCESSFULLY", HttpStatus.OK);
        else return new ResponseEntity("ERROR IN UPATED", HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @PostMapping("/employee/delete")
    public ResponseEntity deleteEmployee(@RequestBody Employee employee) {
        logger.info("EmployeeController::deleteEmployee => {}", employee.toString());
        try {
            Employee tmp = this.empRepo.getOne(employee.getEmpid());
            System.out.printf("FOUND EMPLOYEE => %s", tmp.toString());
            this.empRepo.delete(tmp);
            return new ResponseEntity("EMPLOYEE DELETED SUCCESSFULLY", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity("ERROR DELETING", HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @PostMapping("/employee/setworkschedule")
    public ResponseEntity setWorkSchedule(@RequestBody EmployeeWorkSchedule inObject) {
        logger.info("EmployeeController::setWorkSchedule => {}", inObject.schedule.toString());
        if (this.empService.setEmployeeSchedule(inObject.employee, inObject.schedule)) {
            return new ResponseEntity("SCHEDULE SET", HttpStatus.OK);
        } else
            return new ResponseEntity("PROBLEM SETTING SCHEDULE", HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @GetMapping("/employee/getworkschedule")
    public ResponseEntity<WorkSchedule> setWorkSchedule(@RequestBody Employee employee) {
        WorkSchedule tmp = this.empService.getEmployeeSchedule(employee);
        return new ResponseEntity<WorkSchedule>(tmp, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/employee/setoutofoffice")
    public ResponseEntity setOutOfOffice(@RequestBody EmployeeOOO inObject){
        if(this.empService.setOutOfOffice(inObject.employee, inObject.outOfOffice))
            return new ResponseEntity("OOO SET PROPERLY", HttpStatus.OK);
        else
            return new ResponseEntity("FAILED TO SET OOO", HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @GetMapping("/employee/getoutofoffice")
    public ResponseEntity<List<OutOfOffice>> getOutOfOffice(@RequestBody Employee employee) {
        List<OutOfOffice> tmp = this.empService.getOutOfOffices(employee);
        return new ResponseEntity<List<OutOfOffice>>(tmp, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/employee/deleteoutofoffice")
    public ResponseEntity deleteOutOfOffice(@RequestBody EmployeeOOO inObject) {
        if(this.empService.deleteOutOfOffice(inObject.outOfOffice))
            return new ResponseEntity("OUT OF OFFICE REMOVED SUCCESS", HttpStatus.OK);
        else
            return new ResponseEntity("ERROR REMOVING OUT OF OFFICE", HttpStatus.BAD_REQUEST);
    }
}
