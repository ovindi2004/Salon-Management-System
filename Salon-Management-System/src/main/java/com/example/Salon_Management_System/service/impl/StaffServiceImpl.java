package com.example.Salon_Management_System.service.impl;

import com.example.Salon_Management_System.dto.StaffDTO;
import com.example.Salon_Management_System.dto.StaffLeaveDTO;
import com.example.Salon_Management_System.dto.StaffWorkingHourDTO;
import com.example.Salon_Management_System.service.StaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaffServiceImpl implements StaffService {
    @Override
    public StaffDTO createStaff(StaffDTO staffDTO) {
        return null;
    }

    @Override
    public StaffDTO getStaffById(Long staffId) {
        return null;
    }

    @Override
    public List<StaffDTO> getAllStaff() {
        return List.of();
    }

    @Override
    public List<StaffDTO> searchStaff(String keyword) {
        return List.of();
    }

    @Override
    public StaffDTO updateStaff(Long staffId, StaffDTO staffDTO) {
        return null;
    }

    @Override
    public void deleteStaff(Long staffId) {

    }

    @Override
    public void updateStatus(Long staffId) {

    }

    @Override
    public void updateWorkingHours(Long staffId, List<StaffWorkingHourDTO> dto) {

    }

    @Override
    public List<StaffWorkingHourDTO> getWorkingHours(Long staffId) {
        return List.of();
    }

    @Override
    public StaffLeaveDTO addLeave(Long staffId, StaffLeaveDTO dto) {
        return null;
    }

    @Override
    public List<StaffLeaveDTO> getLeaves(Long staffId) {
        return List.of();
    }

    @Override
    public void updateLeave(Long leaveId, String status) {

    }
}
