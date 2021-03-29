package com.scheduler.bkend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;


/**
 *  EXAMPLE API - SpringData API that allows dynamic queries with JPA
 */

@Entity
@DynamicUpdate
@Table(name="employees")
public class Employee implements MyClassUtils {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int empid;
    @Column(name = "fname")
    private String fname;
    @Column(name = "lname")
    private String lname;
    @Column(name = "phone")
    private String phone;
    @Column(name = "address")
    private int address;
    @Column(name = "email")
    private String email;
    @Column(name = "orgunit")
    private int orgunit;

    @OneToOne
    @JoinColumn(name = "workschedule")
    private WorkSchedule workschedule;

    @OneToMany(mappedBy = "employee")
    private List<OutOfOffice> outofoffices;

    public Employee() {
    }

    public Employee(String fname, String lname, String phone, int address, String email, int orgunit) {
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.orgunit = orgunit;
    }


    @Override
    public String toString() {
        return "Employee{" +
                "empid=" + empid +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", phone='" + phone + '\'' +
                ", address=" + address +
                ", email='" + email + '\'' +
                ", orgunit=" + orgunit +
                ", workschedule=" + workschedule +
                '}';
    }

    public int getEmpid() {
        return empid;
    }
    public void setEmpid(int empid) {
        this.empid = empid;
    }
    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }
    public String getLname() {
        return lname;
    }
    public void setLname(String lname) {
        this.lname = lname;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public int getAddress() {
        return address;
    }
    public void setAddress(int address) {
        this.address = address;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getOrgunit() {
        return orgunit;
    }
    public void setOrgunit(int orgunit) {
        this.orgunit = orgunit;
    }

    public WorkSchedule getWorkschedule() {
        return workschedule;
    }
    public void setWorkschedule(WorkSchedule workschedule) {
        this.workschedule = workschedule;
    }

    @JsonIgnore
    public List<OutOfOffice> getOutofoffices() {
        return outofoffices;
    }
    public void setOutofoffices(List<OutOfOffice> outofoffices) {
        this.outofoffices = outofoffices;
    }
}
