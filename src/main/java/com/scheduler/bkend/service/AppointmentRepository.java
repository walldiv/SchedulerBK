package com.scheduler.bkend.service;

import com.scheduler.bkend.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Query("SELECT a from Appointment a WHERE a.client = ?1")
    List<Appointment> findAllByClientId(int clientid);

    @Query("SELECT a from Appointment a WHERE a.assignedto = ?1")
    List<Appointment> findAllByEmployeetId(int employeeid);
}
