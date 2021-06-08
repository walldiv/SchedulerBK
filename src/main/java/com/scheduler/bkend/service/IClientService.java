package com.scheduler.bkend.service;

import com.scheduler.bkend.model.Address;
import com.scheduler.bkend.model.Appointment;
import com.scheduler.bkend.model.Client;

import java.util.List;

public interface IClientService {

    boolean createClient(Client client);
    Client getClient(int clientid);
    List<Client> getClients();
    boolean updateClient(Client client);

    Appointment getAppointmentById(int apptId);
    List<Appointment> getAppointments(Client client);
    boolean setAppointment(Appointment appointment);
    boolean changeAppointment(Appointment appointment);
}
