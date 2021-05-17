package com.scheduler.bkend.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orgunits")
public class OrgUnit implements MyClassUtils{
    @Id
    private String orgname;
    @Type(type = "list-array")
    @Column(name = "duties", columnDefinition = "text[]")
    private List<String> duties;
//    @Column(name = "dephead")
//    private int dephead;

    @OneToOne
    @JoinColumn(name = "dephead")
    private Employee dephead;


    public OrgUnit() {
    }
    public OrgUnit(String orgname, List<String> duties, Employee dephead) {
        this.orgname = orgname;
        this.duties = duties;
        this.dephead = dephead;
    }

    @Override
    public String toString() {
        return "OrgUnit{" +
                "orgname='" + orgname + '\'' +
                ", duties=" + duties +
                ", dephead=" + dephead +
                '}';
    }

    public String getOrgname() {
        return orgname;
    }
    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }
    public List<String> getDuties() {
        return duties;
    }
    public void setDuties(List<String> duties) {
        this.duties = duties;
    }
    public Employee getDephead() {
        return dephead;
    }
    public void setDephead(Employee dephead) {
        this.dephead = dephead;
    }
}
