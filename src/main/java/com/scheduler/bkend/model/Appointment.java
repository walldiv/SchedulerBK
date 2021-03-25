package com.scheduler.bkend.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@DynamicUpdate
@Table(name = "appointments")
public class Appointment  implements MyClassUtils{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int appointmentid;
    @Column(name = "creationdate")
    private LocalDateTime creationdate;
    @Column(name = "createdby")
    private int createdby;
    @Column(name = "assignedto")
    private int assignedto;
    @Column(name = "scheduledate")
    private LocalDateTime scheduledate;
    @Column(name = "patientnotes")
    private String patientnotes;
    @Column(name = "employeenotes")
    private String employeenotes;
    @Column(name = "client")
    private int client;

    public Appointment() {
    }

    public Appointment(LocalDateTime creationdate, int createdby, int assignedto, LocalDateTime scheduledate,
                       String patientnotes, String employeenotes, int client) {
        this.creationdate = creationdate;
        this.createdby = createdby;
        this.assignedto = assignedto;
        this.scheduledate = scheduledate;
        this.patientnotes = patientnotes;
        this.employeenotes = employeenotes;
        this.client = client;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentid=" + appointmentid +
                ", creationdate=" + creationdate +
                ", createdby=" + createdby +
                ", assignedto=" + assignedto +
                ", scheduledate=" + scheduledate +
                ", patientnotes='" + patientnotes + '\'' +
                ", employeenotes='" + employeenotes + '\'' +
                ", client=" + client +
                '}';
    }

    public int getAppointmentid() {
        return appointmentid;
    }
    public void setAppointmentid(int appointmentid) {
        this.appointmentid = appointmentid;
    }
    public LocalDateTime getCreationdate() {
        return creationdate;
    }
    public void setCreationdate(LocalDateTime creationdate) {
        this.creationdate = creationdate;
    }
    public int getCreatedby() {
        return createdby;
    }
    public void setCreatedby(int createdby) {
        this.createdby = createdby;
    }
    public int getAssignedto() {
        return assignedto;
    }
    public void setAssignedto(int assignedto) {
        this.assignedto = assignedto;
    }
    public LocalDateTime getScheduledate() {
        return scheduledate;
    }
    public void setScheduledate(LocalDateTime scheduledate) {
        this.scheduledate = scheduledate;
    }
    public String getPatientnotes() {
        return patientnotes;
    }
    public void setPatientnotes(String patientnotes) {
        this.patientnotes = patientnotes;
    }
    public String getEmployeenotes() {
        return employeenotes;
    }
    public void setEmployeenotes(String employeenotes) {
        this.employeenotes = employeenotes;
    }
    public int getClient() {
        return client;
    }
    public void setClient(int client) {
        this.client = client;
    }
}
