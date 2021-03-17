package com.scheduler.bkend.controller;

import com.scheduler.bkend.model.Address;
import com.scheduler.bkend.model.Client;
import com.scheduler.bkend.model.Employee;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
public class ClientController {
    static class ClientAndAddress {
        public Client client;
        public Address address;
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
    public ResponseEntity<List<Client>> getClient(@RequestBody Client client) {
        logger.info("ClientController::getClient => {}", client.toString());
        List<Client> clients = this.clientService.getClients(client);
        return new ResponseEntity<List<Client>>(clients, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/client/update")
    public ResponseEntity updateClient(@RequestBody Client client){
        logger.info("ClientController::updateClient => {}", client.toString());
        if(this.clientService.updateClient(client))
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
}
