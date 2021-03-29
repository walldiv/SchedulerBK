package com.scheduler.bkend.controller;

import com.scheduler.bkend.model.Address;
import com.scheduler.bkend.model.Employee;
import com.scheduler.bkend.model.OutOfOffice;
import com.scheduler.bkend.model.WorkSchedule;
import com.scheduler.bkend.service.AddressRepository;
import com.scheduler.bkend.service.EmployeeRepository;
import com.scheduler.bkend.service.IEmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

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


    @Autowired
    public EmployeeController(IEmployeeService empService, EmployeeRepository empRepo, AddressRepository addressRepo) {
        this.empService = empService;
        this.empRepo = empRepo;
        this.addressRepo = addressRepo;
    }

    @ResponseBody
    @PostMapping("/employee/create")
    public ResponseEntity createEmployee(@RequestBody EmployeeAndAddress inObject) {
        logger.info("ClientController::createClient => {}", inObject.employee.toString());
        logger.info("ClientController::createClient => {}", inObject.address.toString());
        //Add the address if it doesnt already exist - else use existing address in DB
        ExampleMatcher matchlist = ExampleMatcher.matchingAll()
                .withMatcher("street", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withMatcher("zipcode", ExampleMatcher.GenericPropertyMatchers.ignoreCase().contains())
                .withIgnorePaths("addressid", "street2", "city", "state", "country");
        Example<Address> example = Example.of(inObject.address, matchlist);
        Optional<Address> thisAddress = this.addressRepo.findOne(example);
        int addressid = -1;
        if (thisAddress.isPresent())
            addressid = thisAddress.get().getAddressid();
        else {
            Address tmp = this.addressRepo.save(inObject.address);
            addressid = tmp.getAddressid();
        }
        inObject.employee.setAddress(addressid);
        if (this.empService.createEmployee(inObject.employee))
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
    public ResponseEntity updateEmployee(@RequestBody EmployeeAndAddress inObject) {
        logger.info("EmployeeController::updateEmployee => {}", inObject.employee.toString());
        if (this.empService.updateEmployee(inObject.employee, inObject.address))
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
