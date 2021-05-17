package com.scheduler.bkend.controller;

import com.scheduler.bkend.model.Address;
import com.scheduler.bkend.model.Appointment;
import com.scheduler.bkend.model.Client;
import com.scheduler.bkend.service.AddressRepository;
import com.scheduler.bkend.service.ClientRepository;
import com.scheduler.bkend.service.IClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
public class ClientController {
    static class ClientAndAddress {
        public Client client;
        public Address address;
    }
    static class ClientAppointment {
        public Client client;
        public Appointment appointment;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private IClientService clientService;
    private ClientRepository clientRepo;
    private AddressRepository addressRepo;

    @Autowired
    public ClientController(IClientService clientService, ClientRepository clientRepo, AddressRepository addressRepo) {
        this.clientService = clientService;
        this.clientRepo = clientRepo;
        this.addressRepo = addressRepo;
    }

    @ResponseBody
    @PostMapping("/client/create")
    public ResponseEntity createClient(@RequestBody ClientAndAddress inObject){
        logger.info("ClientController::createClient => {}", inObject.client.toString());
        logger.info("ClientController::createClient => {}", inObject.address.toString());
        //Add the address if it doesnt already exist - else use existing address in DB
        ExampleMatcher matchlist = ExampleMatcher.matchingAll()
                .withMatcher("street", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withMatcher("zipcode", ExampleMatcher.GenericPropertyMatchers.ignoreCase().contains())
                .withIgnorePaths("addressid", "street2","city", "state", "country");
        Example<Address> example = Example.of(inObject.address, matchlist);
        Optional<Address> thisAddress = this.addressRepo.findOne(example);
        int addressid = -1;
        if(thisAddress.isPresent())
                addressid = thisAddress.get().getAddressid();
        else{
            Address tmp = this.addressRepo.save(inObject.address);
            addressid = tmp.getAddressid();
        }
        inObject.client.setAddress(addressid);
        if(this.clientService.createClient(inObject.client))
            return new ResponseEntity("CLIENT CREATED SUCCESSFULLY", HttpStatus.OK);
        else return new ResponseEntity("ERROR CREATING CLIENT", HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @GetMapping("/client/get")
    public ResponseEntity<Client> getClient(@RequestParam("clientid") int clientid) {
        logger.info("ClientController::getClient => {}", clientid);
        Client client = this.clientService.getClient(clientid);
        return new ResponseEntity<Client>(client, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/client/update")
    public ResponseEntity updateClient(@RequestBody ClientAndAddress inObject){
        logger.info("ClientController::updateClient => {}", inObject.client.toString());
        if(this.clientService.updateClient(inObject.client, inObject.address))
            return new ResponseEntity("CLIENT UPDATED SUCCESSFULLY", HttpStatus.OK);
        else return new ResponseEntity("ERROR UPDATING CLIENT", HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @PostMapping("/client/delete")
    public ResponseEntity deleteClient(@RequestBody Client client){
        logger.info("ClientController::deleteClient => {}", client.toString());
        try{
            Client tmp = this.clientRepo.getOne(client.getClientid());
            this.clientRepo.delete(tmp);
            return new ResponseEntity("CLIENT DELETED SUCCESSFULLY", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("ERROR DELETING CLIENT", HttpStatus.BAD_REQUEST);
        }
    }

    /*******    APPOINTMENTS CONTROL   *********/
    @ResponseBody
    @GetMapping("/client/getappointment")
    public ResponseEntity<List<Appointment>> getAppointments(@RequestBody Client client){
        logger.info("ClientController::getAppointments => {}", client.toString());
        List<Appointment> appointments = this.clientService.getAppointments(client);
        return new ResponseEntity<List<Appointment>>(appointments, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/client/getsingleappointment")
    public ResponseEntity<Appointment> getSingleAppointment(@RequestParam("appt") int apptId){
        logger.info("ClientController::getSingleAppointment => {}", apptId);
        Appointment tmp= this.clientService.getAppointmentById(apptId);
        if(tmp == null)
            return new ResponseEntity<Appointment>(tmp, HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<Appointment>(tmp, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/client/setappointment")
    public ResponseEntity setAppointment(@RequestBody ClientAppointment inObject) {
        if(this.clientService.setAppointment(inObject.client, inObject.appointment)){
            return new ResponseEntity("APPOINTMENT SET SUCCESSFULLY", HttpStatus.OK);
        }
        else
            return new ResponseEntity("ERROR CREATING APPOINTMENT", HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @PostMapping("/client/changeappointment")
    public ResponseEntity changeAppointment(@RequestBody Appointment appointment) {
        if(this.clientService.changeAppointment(appointment)){
            return new ResponseEntity("APPOINTMENT CHANGED SUCCESSFULLY", HttpStatus.OK);
        }
        else
            return new ResponseEntity("ERROR CHANGING APPOINTMENT", HttpStatus.BAD_REQUEST);
    }

}
