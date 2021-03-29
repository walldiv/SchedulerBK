package com.scheduler.bkend.model;


import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "workschedules")
@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
public class WorkSchedule implements MyClassUtils{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int workscheduleid;
    @Type(type = "list-array")
    @Column(name = "day", columnDefinition = "text[]")
    private List<String> day;
    @Type(type = "list-array")
    @Column(name = "timein", columnDefinition = "text[]")
    private List<String> timein;
    @Type(type = "list-array")
    @Column(name = "timeout", columnDefinition = "text[]")
    private List<String> timeout;

    public WorkSchedule() {
    }

    public WorkSchedule(List<String> day, List<String> timein, List<String> timeout) {
        this.day = day;
        this.timein = timein;
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "{" +
                "day:'" + day + '\'' +
                ", timeIn:" + timein +
                ", timeOut:" + timeout +
                '}';
    }

    public int getWorkscheduleid() {
        return workscheduleid;
    }
    public void setWorkscheduleid(int workscheduleid) {
        this.workscheduleid = workscheduleid;
    }
    public List<String> getDay() {
        return day;
    }
    public void setDay(List<String> day) {
        this.day = day;
    }
    public List<String> getTimein() {
        return timein;
    }
    public void setTimein(List<String> timein) {
        this.timein = timein;
    }
    public List<String> getTimeout() {
        return timeout;
    }
    public void setTimeout(List<String> timeout) {
        this.timeout = timeout;
    }
}
