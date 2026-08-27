package com.example.Salon_Management_System.service;

import com.example.Salon_Management_System.dto.StaffDTO;
import com.example.Salon_Management_System.dto.StaffLeaveDTO;
import com.example.Salon_Management_System.dto.StaffWorkingHourDTO;

import java.util.List;

public interface StaffService {
    StaffDTO createStaff(StaffDTO staffDTO);
    StaffDTO getStaffById(Long staffId);
    List<StaffDTO> getAllStaff();
    List<StaffDTO>searchStaff(String keyword);
    StaffDTO updateStaff(Long staffId, StaffDTO staffDTO);
    void deleteStaff(Long staffId);
    void updateStatus(Long staffId);
    void updateWorkingHours(Long staffId, List<StaffWorkingHourDTO> dto);
    List<StaffWorkingHourDTO> getWorkingHours(Long staffId);
    StaffLeaveDTO addLeave(Long staffId, StaffLeaveDTO dto);
    List<StaffLeaveDTO> getLeaves(Long staffId);
    void updateLeave(Long leaveId, String status);
}
