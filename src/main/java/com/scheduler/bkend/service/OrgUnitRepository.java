package com.scheduler.bkend.service;

import com.scheduler.bkend.model.OrgUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgUnitRepository extends JpaRepository<OrgUnit, String> {
    @Query("SELECT o from OrgUnit o WHERE o.orgname = ?1")
    OrgUnit findOrgByName(String orgname);
}
