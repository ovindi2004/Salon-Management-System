package com.example.Salon_Management_System.service;

import com.example.Salon_Management_System.dto.StaffDTO;
import com.example.Salon_Management_System.dto.StaffLeaveDTO;
import com.example.Salon_Management_System.dto.StaffSaveDTO;
import com.example.Salon_Management_System.dto.StaffWorkingHourDTO;

import java.util.List;

public interface StaffService {

    StaffDTO createStaff(StaffSaveDTO dto);

    StaffDTO getStaffById(Long id);

    List<StaffDTO> getAllStaff();

    List<StaffDTO> searchStaff(String keyword);

    StaffDTO updateStaff(Long id, StaffSaveDTO dto);

    void deleteStaff(Long id);

    void updateStatus(Long id);

    void updateWorkingHours(
            Long staffId,
            List<StaffWorkingHourDTO> dto
    );

    List<StaffWorkingHourDTO> getWorkingHours(Long staffId);

    StaffLeaveDTO addLeave(
            Long staffId,
            StaffLeaveDTO dto
    );

    List<StaffLeaveDTO> getStaffLeaves(Long staffId);

    void updateLeaveStatus(
            Long leaveId,
            String status
    );
}