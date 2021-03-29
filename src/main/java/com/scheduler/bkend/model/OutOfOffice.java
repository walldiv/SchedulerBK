package com.scheduler.bkend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "outofoffices")
@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
public class OutOfOffice {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int oooid;
    @Column(name = "day")
    private String day;
    @Type(type = "list-array")
    @Column(name = "timein", columnDefinition = "text[]")
    private List<String> timein;
    @Type(type = "list-array")
    @Column(name = "timeout", columnDefinition = "text[]")
    private List<String> timeout;

    @ManyToOne
    @JoinColumn(name = "employee", nullable = false)
    private Employee employee;

    public OutOfOffice() {
    }
    public OutOfOffice(String day, List<String> timein, List<String> timeout) {
        this.day = day;
        this.timein = timein;
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "OutOfOffices{" +
                "oooid=" + oooid +
                ", day=" + day +
                ", timein=" + timein +
                ", timeout=" + timeout +
                '}';
    }

    public int getOooid() {
        return oooid;
    }
    public void setOooid(int oooid) {
        this.oooid = oooid;
    }
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
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

    @JsonIgnore
    public Employee getEmployee() {
        return employee;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
