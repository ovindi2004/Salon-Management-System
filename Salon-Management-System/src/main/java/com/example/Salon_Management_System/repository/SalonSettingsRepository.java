package com.example.Salon_Management_System.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalonSettingsRepository
        extends JpaRepository<SalonSettings, Long> {

}