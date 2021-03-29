package com.scheduler.bkend.service;

import com.scheduler.bkend.model.Address;
import com.scheduler.bkend.model.Employee;
import com.scheduler.bkend.model.OutOfOffice;
import com.scheduler.bkend.model.WorkSchedule;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class EmployeeServiceImpl implements IEmployeeService{
    private Logger logger = LoggerFactory.getLogger(getClass());
    private EmployeeRepository empRepo;
    private AddressRepository addRepo;
    private WorkScheduleRepository workSchedRepo;
    private OutOfOfficeRepository oooRepo;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository empRepo, AddressRepository addRepo,
                               WorkScheduleRepository workSchedRepo, OutOfOfficeRepository oooRepo) {
        this.empRepo = empRepo;
        this.addRepo = addRepo;
        this.workSchedRepo = workSchedRepo;
        this.oooRepo = oooRepo;
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
    public boolean updateEmployee(Employee employee, Address address) {
        Employee tmp = this.empRepo.getOne(employee.getEmpid());
        Address tmpAddress = this.addRepo.getOne(address.getAddressid());
        try{
//            logger.info("DB FOUND EMPLOYEE => {}", tmp.toString());
            tmp = tmp.merge(employee);
            tmpAddress = tmpAddress.merge(address);
//            empRepo.save(tmp);
//            addRepo.save(tmpAddress);
            //            logger.info("AFTER MERGE EMPLOYEE => {}", tmp.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean setEmployeeSchedule(Employee employee, WorkSchedule schedule) {
        Employee tmp = this.empRepo.getOne(employee.getEmpid());
        WorkSchedule tmpSched = tmp.getWorkschedule();
        try{
            if(tmp.getWorkschedule() == null){
                tmpSched = this.workSchedRepo.save(schedule);
            }
            else {
                schedule.setWorkscheduleid(tmpSched.getWorkscheduleid());
                tmpSched = schedule;
            }
            this.workSchedRepo.save(tmpSched);
            System.out.printf("NEW SCHEDULE: %d %s", tmpSched.getWorkscheduleid(), tmpSched.toString());
            tmp.setWorkschedule(schedule);
            this.empRepo.save(tmp);
            return true;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
    }

    @Override
    public WorkSchedule getEmployeeSchedule(Employee employee) {
        Employee tmp = this.empRepo.getOne(employee.getEmpid());
//        return this.workSchedRepo.findById(tmp.getWorkschedule().getWorkscheduleid()).get();
        return tmp.getWorkschedule();
    }

    @Override
    public boolean setOutOfOffice(Employee employee, OutOfOffice outOfOffice) {
        Employee tmp = this.empRepo.getOne(employee.getEmpid());
        OutOfOffice tmpOoo = outOfOffice;
        try{
            if(outOfOffice.getOooid() != 0){
                tmpOoo = this.oooRepo.getOne(outOfOffice.getOooid());
                List<String> newTimeIn = Stream.concat(tmpOoo.getTimein().stream(),
                        outOfOffice.getTimein().stream()).collect(Collectors.toList());
                List<String> newTimeOut = Stream.concat(tmpOoo.getTimeout().stream(),
                        outOfOffice.getTimeout().stream()).collect(Collectors.toList());
                tmpOoo.setTimein(newTimeIn);
                tmpOoo.setTimeout(newTimeOut);
            }
            tmpOoo.setEmployee(tmp);
            tmpOoo = this.oooRepo.save(tmpOoo);
            return true;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
    }

    @Override
    public List<OutOfOffice> getOutOfOffices(Employee employee) {
        Employee tmp = this.empRepo.getOne(employee.getEmpid());
        return tmp.getOutofoffices();
    }

    @Override
    public boolean deleteOutOfOffice(OutOfOffice outOfOffice) {
        OutOfOffice tmpOoo = this.oooRepo.getOne(outOfOffice.getOooid());
        try{
            tmpOoo.getTimein().removeIf(x -> outOfOffice.getTimein().contains(x));
            tmpOoo.getTimeout().removeIf(x -> outOfOffice.getTimeout().contains(x));
            tmpOoo = this.oooRepo.save(tmpOoo);
            return true;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
    }
}
