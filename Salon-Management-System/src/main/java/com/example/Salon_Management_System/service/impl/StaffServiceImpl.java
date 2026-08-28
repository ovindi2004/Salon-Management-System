package com.example.Salon_Management_System.service.impl;

import com.example.Salon_Management_System.dto.StaffDTO;
import com.example.Salon_Management_System.dto.StaffLeaveDTO;
import com.example.Salon_Management_System.dto.StaffSaveDTO;
import com.example.Salon_Management_System.dto.StaffWorkingHourDTO;
import com.example.Salon_Management_System.entity.Staff;
import com.example.Salon_Management_System.entity.StaffLeave;
import com.example.Salon_Management_System.entity.StaffWorkingHour;
import com.example.Salon_Management_System.entity.User;
import com.example.Salon_Management_System.enumiration.LeaveStatus;
import com.example.Salon_Management_System.enumiration.StaffAvailability;
import com.example.Salon_Management_System.enumiration.StaffStatus;
import com.example.Salon_Management_System.repository.StaffLeaveRepository;
import com.example.Salon_Management_System.repository.StaffRepository;
import com.example.Salon_Management_System.repository.StaffWorkingHourRepository;
import com.example.Salon_Management_System.service.StaffService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final StaffWorkingHourRepository workingHourRepository;
    private final StaffLeaveRepository leaveRepository;
    private final PasswordEncoder passwordEncoder;



    @Override
    public StaffDTO createStaff(StaffSaveDTO dto) {

        if (dto.getStaffName() == null || dto.getStaffName().isBlank()) {
            throw new RuntimeException("Staff name is required");
        }

        if (dto.getStaffEmail() == null || dto.getStaffEmail().isBlank()) {
            throw new RuntimeException("Staff email is required");
        }

        if (dto.getStaffPhone() == null || dto.getStaffPhone().isBlank()) {
            throw new RuntimeException("Staff phone is required");
        }

        if (staffRepository.existsByStaffEmail(dto.getStaffEmail())) {
            throw new RuntimeException("Staff email already exists");
        }

        if (staffRepository.existsByStaffPhone(dto.getStaffPhone())) {
            throw new RuntimeException("Staff phone already exists");
        }

        Staff staff = new Staff();

        staff.setStaffCode(generateStaffCode());
        staff.setStaffName(dto.getStaffName());
        staff.setStaffEmail(dto.getStaffEmail());
        staff.setStaffPhone(dto.getStaffPhone());
        staff.setDateOfBirth(dto.getDateOfBirth());
        staff.setGender(dto.getGender());
        staff.setAddress(dto.getAddress());
        staff.setPosition(dto.getPosition());
        staff.setHireDate(dto.getHireDate());
        staff.setSalary(dto.getSalary());

        if (dto.getStatus() != null) {
            staff.setStatus(dto.getStatus());
        } else {
            staff.setStatus(StaffStatus.ACTIVE);
        }

        staff.setAvailability(StaffAvailability.AVAILABLE);


        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {

            User user = new User();

            user.setUserName(dto.getUsername());
            user.setUserEmail(dto.getStaffEmail());

            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                user.setUserPassword(
                        passwordEncoder.encode(dto.getPassword())
                );
            }

            staff.setUser(user);
        }

        Staff savedStaff = staffRepository.save(staff);


        createDefaultWorkingHours(savedStaff);

        return convertToDTO(savedStaff);
    }

    @Override
    public StaffDTO getStaffById(Long id) {
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
    public StaffDTO updateStaff(Long id, StaffSaveDTO dto) {
        return null;
    }

    @Override
    public void deleteStaff(Long id) {

    }

    @Override
    public void updateStatus(Long id) {

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
    public List<StaffLeaveDTO> getStaffLeaves(Long staffId) {
        return List.of();
    }

    @Override
    public void updateLeaveStatus(Long leaveId, String status) {

    }

    private StaffDTO convertToDTO(Staff savedStaff) {
        return savedStaff;
    }

    private void createDefaultWorkingHours(Staff savedStaff) {

    }


}
