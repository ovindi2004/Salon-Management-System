package com.example.Salon_Management_System.repository;

import com.example.Salon_Management_System.entity.SalonService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<SalonService, Long> {
    boolean existsByServiceNameIgnoreCase(String serviceName);

    List<SalonService>
    findByServiceNameContainingIgnoreCase(String serviceName);

    List<SalonService>
    findByCategoryIgnoreCase(String category);

    List<SalonService>
    findByStatus(String status);
}
