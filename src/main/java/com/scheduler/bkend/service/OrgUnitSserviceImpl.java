package com.scheduler.bkend.service;

import com.scheduler.bkend.model.OrgUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class OrgUnitSserviceImpl implements IOrgUnitService{
    private Logger logger = LoggerFactory.getLogger(getClass());
    private OrgUnitRepository orgRepo;

    @Autowired
    public OrgUnitSserviceImpl(OrgUnitRepository orgRepo){
        this.orgRepo = orgRepo;
    }

    @Override
    public OrgUnit getOrgByName(String orgname) {
        return this.orgRepo.findOrgByName(orgname);
    }

    @Override
    public List<OrgUnit> getOrgUnits() {
        List<OrgUnit> orgUnits = this.orgRepo.findAll();
        return orgUnits;
    }

    @Override
    public boolean createOrgUnit(OrgUnit orgUnit) {
        OrgUnit tmp = this.orgRepo.findOrgByName(orgUnit.getOrgname());
        if(tmp != null)
            return false;
        try{
            this.orgRepo.save(orgUnit);
            return true;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
    }

    @Override
    public boolean updateOrgUnit(OrgUnit orgUnit) {
        OrgUnit tmp = this.orgRepo.getOne(orgUnit.getOrgname());
        try{
            tmp = tmp.merge(orgUnit);
            this.orgRepo.save(tmp);
            return true;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
    }

    @Override
    public boolean deleteOrgUnit(OrgUnit orgUnit) {
        try{
            this.orgRepo.deleteById(orgUnit.getOrgname());
            return true;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
    }
}
