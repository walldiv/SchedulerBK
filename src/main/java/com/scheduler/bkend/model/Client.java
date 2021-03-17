package com.scheduler.bkend.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int clientid;
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
    @Column(name = "dateofbirth")
    private LocalDate dateofbirth;
    @Column(name = "emergencycontact")
    private String emergencycontact;
    @Column(name = "allowsms")
    private boolean allowsms;

    public Client() {
    }

    public Client(String fname, String lname, String phone, int address, String email, LocalDate dateofbirth, String emergencycontact, boolean allowsms) {
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.dateofbirth = dateofbirth;
        this.emergencycontact = emergencycontact;
        this.allowsms = allowsms;
    }

    public Client merge(Client inClient){
        this.setFname(inClient.getFname());
        this.setLname(inClient.getLname());
        this.setPhone(inClient.getPhone());
        this.setAddress(inClient.getAddress());
        this.setEmail(inClient.getEmail());
        this.setDateofbirth(inClient.getDateofbirth());
        this.setEmergencycontact(inClient.getEmergencycontact());
        this.setAllowsms(inClient.isAllowsms());
        return this;
    }
    @Override
    public String toString() {
        return "Client{" +
                "clientid=" + clientid +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", phone='" + phone + '\'' +
                ", address=" + address +
                ", email='" + email + '\'' +
                ", dateofbirth=" + dateofbirth +
                ", emergencycontact='" + emergencycontact + '\'' +
                ", allowsms=" + allowsms +
                '}';
    }

    public int getClientid() {
        return clientid;
    }
    public void setClientid(int clientid) {
        this.clientid = clientid;
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
    public LocalDate getDateofbirth() {
        return dateofbirth;
    }
    public void setDateofbirth(LocalDate dateofbirth) {
        this.dateofbirth = dateofbirth;
    }
    public String getEmergencycontact() {
        return emergencycontact;
    }
    public void setEmergencycontact(String emergencycontact) {
        this.emergencycontact = emergencycontact;
    }
    public boolean isAllowsms() {
        return allowsms;
    }
    public void setAllowsms(boolean allowsms) {
        this.allowsms = allowsms;
    }
}
