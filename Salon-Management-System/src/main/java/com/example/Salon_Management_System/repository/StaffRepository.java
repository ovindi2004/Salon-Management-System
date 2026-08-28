package com.example.Salon_Management_System.repository;

import com.example.Salon_Management_System.entity.Staff;
import com.example.Salon_Management_System.enumiration.StaffStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    List<Staff> findByStatus(StaffStatus status);

    List<Staff> findByStaffNameContainingIgnoreCase(String name);

    boolean existsByStaffEmail(String email);

    boolean existsByStaffPhone(String phone);
}