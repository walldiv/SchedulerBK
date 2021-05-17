package com.scheduler.bkend.service;

import com.scheduler.bkend.model.OrgUnit;

import java.util.List;

public interface IOrgUnitService {
    OrgUnit getOrgByName(String orgname);
    List<OrgUnit> getOrgUnits();

    boolean createOrgUnit(OrgUnit orgUnit);
    boolean updateOrgUnit(OrgUnit orgUnit);
    boolean deleteOrgUnit(OrgUnit orgUnit);
}
