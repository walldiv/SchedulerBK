package com.scheduler.bkend.service;

import com.scheduler.bkend.model.Address;
import com.scheduler.bkend.model.Appointment;
import com.scheduler.bkend.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ClientServiceImpl implements IClientService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ClientRepository clientRepo;
    private AddressRepository addRepo;
    private AppointmentRepository apptRepo;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepo, AddressRepository addRepo,
                             AppointmentRepository apptRepo) {
        this.clientRepo = clientRepo;
        this.addRepo = addRepo;
        this.apptRepo = apptRepo;
    }


    @Override
    public boolean createClient(Client client) {
        Client tmp = clientRepo.findByEmail(client.getEmail());
        if(tmp != null)
            return false;
        try{
            this.clientRepo.save(client);
            return true;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
    }

    @Override
    public Client getClient(int clientid) {
        return this.clientRepo.findById(clientid).get();
    }

    @Override
    public List<Client> getClients() {
//        List<Client> clients = new ArrayList<>();
//        ExampleMatcher matchlist = ExampleMatcher.matchingAll()
//                .withMatcher("fname", ExampleMatcher.GenericPropertyMatchers.ignoreCase().contains())
//                .withMatcher("lname", ExampleMatcher.GenericPropertyMatchers.ignoreCase().contains())
//                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
//                .withIgnorePaths("clientid", "phone","address", "allowsms", "emergencycontact");
//        Example<Client> example = Example.of(client, matchlist);
//        clients = clientRepo.findAll();
        return this.clientRepo.findAll();
    }

    @Override
    public boolean updateClient(Client client) {
        Client tmp = this.clientRepo.getOne(client.getClientid());
        Address tmpAddress = this.addRepo.getOne(client.getAddress().getAddressid());
        try{
            tmp = tmp.merge(client);
            tmpAddress = tmpAddress.merge(client.getAddress());
            addRepo.save(tmpAddress);
            clientRepo.save(tmp);
            return true;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
    }

    @Override
    public Appointment getAppointmentById(int apptId) {
        Appointment tmp = this.apptRepo.findById(apptId).get();
        logger.info("ClientServiceImpl::getAppointmentById()   APPOINTMENT => {}", tmp);
        return tmp;
    }

    @Override
    public List<Appointment> getAppointments(Client client) {
//        Client tmp = this.clientRepo.getOne(client.getClientid());
//        System.out.printf("CLIENT SERVICE::getAppointments() => %s", tmp.toString());
        return this.apptRepo.findAllByClientId(client.getClientid());
    }

    @Override
    public boolean setAppointment(Appointment appointment) {
//        Client tmp = this.clientRepo.getOne(client.getClientid());
        appointment.setClient(appointment.getClient());
        appointment.setCreationdate(LocalDateTime.now());
        try{
            Appointment tmpAppt = this.apptRepo.save(appointment);
            return true;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
    }

    @Override
    public boolean changeAppointment(Appointment appointment) {
        Appointment tmp = this.apptRepo.getOne(appointment.getAppointmentid());
        appointment.setCreationdate(LocalDateTime.now());
        try {
            tmp = tmp.merge(appointment);
            this.apptRepo.save(tmp);
            return true;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
    }
}
