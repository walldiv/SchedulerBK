package com.scheduler.bkend.service;

import com.scheduler.bkend.model.Client;

import java.util.List;

public interface IClientService {

    boolean createClient(Client client);
    List<Client> getClients(Client client);
    boolean updateClient(Client client);
}
