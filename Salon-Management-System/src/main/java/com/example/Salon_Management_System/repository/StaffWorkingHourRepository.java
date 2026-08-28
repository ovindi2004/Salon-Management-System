package com.example.Salon_Management_System.repository;

import com.example.Salon_Management_System.entity.StaffWorkingHour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffWorkingHourRepository extends JpaRepository<StaffWorkingHour, Long> {

    List<StaffWorkingHour> findByStaffStaffId(Long staffId);

}