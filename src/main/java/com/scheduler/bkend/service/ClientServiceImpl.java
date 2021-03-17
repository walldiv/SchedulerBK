package com.scheduler.bkend.service;

import com.scheduler.bkend.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ClientServiceImpl implements IClientService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ClientRepository clientRepo;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
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
    public List<Client> getClients(Client client) {
        List<Client> clients = new ArrayList<>();
        ExampleMatcher matchlist = ExampleMatcher.matchingAll()
                .withMatcher("fname", ExampleMatcher.GenericPropertyMatchers.ignoreCase().contains())
                .withMatcher("lname", ExampleMatcher.GenericPropertyMatchers.ignoreCase().contains())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withIgnorePaths("clientid", "phone","address", "allowsms", "emergencycontact");
        Example<Client> example = Example.of(client, matchlist);
        clients = clientRepo.findAll(example);
        return clients;
    }

    @Override
    public boolean updateClient(Client client) {
        try{
            Client tmp = this.clientRepo.getOne(client.getClientid());
            tmp.merge(client);
            clientRepo.save(tmp);
            return true;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
    }
}
