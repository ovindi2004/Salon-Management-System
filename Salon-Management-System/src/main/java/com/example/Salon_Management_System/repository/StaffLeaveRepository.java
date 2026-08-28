package com.example.Salon_Management_System.repository;

import com.example.Salon_Management_System.entity.StaffLeave;
import com.example.Salon_Management_System.enumiration.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffLeaveRepository extends JpaRepository<StaffLeave, Long> {

    List<StaffLeave> findByStaffStaffId(Long staffId);

    List<StaffLeave> findByStatus(LeaveStatus status);
}