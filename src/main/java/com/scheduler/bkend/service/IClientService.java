package com.scheduler.bkend.service;

import com.scheduler.bkend.model.Address;
import com.scheduler.bkend.model.Appointment;
import com.scheduler.bkend.model.Client;

import java.util.List;

public interface IClientService {

    boolean createClient(Client client);
    List<Client> getClients(Client client);
    boolean updateClient(Client client, Address address);

    List<Appointment> getAppointments(Client client);
    boolean setAppointment(Client client, Appointment appointment);
    boolean changeAppointment(Appointment appointment);
}
