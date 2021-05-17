package com.scheduler.bkend.controller;

import com.scheduler.bkend.model.OrgUnit;
import com.scheduler.bkend.service.IOrgUnitService;
import com.sun.mail.iap.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
public class OrgUnitController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private IOrgUnitService orgUnitService;

    @Autowired
    public OrgUnitController(IOrgUnitService orgUnitService){
        this.orgUnitService = orgUnitService;
    }

    @ResponseBody
    @GetMapping("/orgunit/getall")
    public ResponseEntity<List<OrgUnit>> getAllOrgUnits(){
        List<OrgUnit> units = this.orgUnitService.getOrgUnits();
        return new ResponseEntity<List<OrgUnit>>(units, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/orgunit/create")
    public ResponseEntity createOrgUnit(@RequestBody OrgUnit inObject){
        logger.info("OrgUnitController::createOrgUnit => {}", inObject.toString());
        if(this.orgUnitService.createOrgUnit(inObject))
            return new ResponseEntity("ORG UNIT CREATED SUCCESSFULLY", HttpStatus.OK);
        else return new ResponseEntity("ERROR CREATING ORG UNIT", HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @PostMapping("/orgunit/update")
    public ResponseEntity updateOrgUnit(@RequestBody OrgUnit inObject){
        logger.info("OrgUnitController::updateOrgUnit => {}", inObject.toString());
        if(this.orgUnitService.updateOrgUnit(inObject))
            return new ResponseEntity("ORG UNIT UPDATED SUCCESSFULLY", HttpStatus.OK);
        else return new ResponseEntity("ERROR UPDATE ORG UNIT", HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @PostMapping("/orgunit/delete")
    public ResponseEntity deleteOrgUnit(@RequestBody OrgUnit inObject){
        logger.info("OrgUnitController::deleteOrgUnit => {}", inObject.toString());
        if(this.orgUnitService.deleteOrgUnit(inObject))
            return new ResponseEntity("REMOVED ORG UNIT - SUCCESS!", HttpStatus.OK);
        else return new ResponseEntity("ERROR REMOVING ORG UNIT", HttpStatus.BAD_REQUEST);
    }
}
