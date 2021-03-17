package com.scheduler.bkend.model;

import javax.persistence.*;

@Entity
@Table(name = "orgunits")
public class OrgUnit {
    @Id
    private String orgname;
    @Column(name = "description")
    private String description;
    @Column(name = "dephead")
    private int dephead;

    public OrgUnit(String orgname, String description, int dephead) {
        this.orgname = orgname;
        this.description = description;
        this.dephead = dephead;
    }

    @Override
    public String toString() {
        return "OrgUnit{" +
                "orgname='" + orgname + '\'' +
                ", description='" + description + '\'' +
                ", dephead=" + dephead +
                '}';
    }

    public String getOrgname() {
        return orgname;
    }
    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getDephead() {
        return dephead;
    }
    public void setDephead(int dephead) {
        this.dephead = dephead;
    }
}
