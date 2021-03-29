package com.scheduler.bkend.service;

import com.scheduler.bkend.model.OutOfOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutOfOfficeRepository extends JpaRepository<OutOfOffice, Integer> {
}
