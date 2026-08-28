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
    @Transactional(readOnly = true)
    public List<StaffDTO> getAllStaff() {

        return staffRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StaffDTO getStaffById(Long id) {

        Staff staff = findStaff(id);

        return convertToDTO(staff);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffDTO> searchStaff(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return getAllStaff();
        }

        String search = keyword.trim().toLowerCase();

        return staffRepository.findAll()
                .stream()
                .filter(staff ->
                        contains(staff.getStaffName(), search)
                                || contains(staff.getStaffEmail(), search)
                                || contains(staff.getStaffPhone(), search)
                                || contains(staff.getPosition(), search)
                )
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StaffDTO updateStaff(Long id, StaffSaveDTO dto) {

        Staff staff = findStaff(id);

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
        }

        if (staff.getUser() != null) {

            User user = staff.getUser();

            if (dto.getUsername() != null &&
                    !dto.getUsername().isBlank()) {

                user.setUserName(dto.getUsername());
            }

            user.setUserEmail(dto.getStaffEmail());

            if (dto.getPassword() != null &&
                    !dto.getPassword().isBlank()) {

                user.setUserPassword(
                        passwordEncoder.encode(dto.getPassword())
                );
            }
        }

        Staff updatedStaff = staffRepository.save(staff);

        return convertToDTO(updatedStaff);
    }

    @Override
    public void deleteStaff(Long id) {

        Staff staff = findStaff(id);

        staffRepository.delete(staff);
    }


    @Override
    public void updateStatus(Long id) {

        Staff staff = findStaff(id);

        if (staff.getStatus() == StaffStatus.ACTIVE) {

            staff.setStatus(StaffStatus.INACTIVE);

        } else {

            staff.setStatus(StaffStatus.ACTIVE);
        }

        staffRepository.save(staff);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffWorkingHourDTO> getWorkingHours(Long staffId) {

        findStaff(staffId);

        return workingHourRepository
                .findByStaffStaffId(staffId)
                .stream()
                .map(this::convertWorkingHourToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateWorkingHours(
            Long staffId,
            List<StaffWorkingHourDTO> dtoList
    ) {

        Staff staff = findStaff(staffId);

        List<StaffWorkingHour> existingHours =
                workingHourRepository.findByStaffStaffId(staffId);


        workingHourRepository.deleteAll(existingHours);

        if (dtoList == null) {
            return;
        }

        for (StaffWorkingHourDTO dto : dtoList) {

            StaffWorkingHour workingHour =
                    new StaffWorkingHour();

            workingHour.setDay(dto.getDay());
            workingHour.setStartTime(dto.getStartTime());
            workingHour.setEndTime(dto.getEndTime());
            workingHour.setDayOff(dto.isDayOff());
            workingHour.setStaff(staff);

            workingHourRepository.save(workingHour);
        }
    }

    @Override
    public StaffLeaveDTO addLeave(
            Long staffId,
            StaffLeaveDTO dto
    ) {

        Staff staff = findStaff(staffId);

        if (dto.getStartDate() == null ||
                dto.getEndDate() == null) {

            throw new RuntimeException(
                    "Leave start date and end date are required"
            );
        }

        if (dto.getEndDate().isBefore(dto.getStartDate())) {

            throw new RuntimeException(
                    "Leave end date cannot be before start date"
            );
        }

        StaffLeave leave = new StaffLeave();

        leave.setStaff(staff);
        leave.setLeaveType(dto.getLeaveType());
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());

        if (dto.getStatus() != null) {
            leave.setStatus(dto.getStatus());
        } else {
            leave.setStatus(LeaveStatus.PENDING);
        }

        StaffLeave savedLeave =
                leaveRepository.save(leave);

        return convertLeaveToDTO(savedLeave);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffLeaveDTO> getStaffLeaves(Long staffId) {

        findStaff(staffId);

        return leaveRepository
                .findByStaffStaffId(staffId)
                .stream()
                .map(this::convertLeaveToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateLeaveStatus(
            Long leaveId,
            String status
    ) {

        StaffLeave leave =
                leaveRepository.findById(leaveId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Leave request not found"
                                )
                        );

        LeaveStatus leaveStatus;

        try {

            leaveStatus =
                    LeaveStatus.valueOf(
                            status.toUpperCase()
                    );

        } catch (IllegalArgumentException e) {

            throw new RuntimeException(
                    "Invalid leave status: " + status
            );
        }

        leave.setStatus(leaveStatus);

        leaveRepository.save(leave);

        if (leaveStatus == LeaveStatus.APPROVED) {

            Staff staff = leave.getStaff();

            staff.setAvailability(
                    StaffAvailability.ON_LEAVE
            );

            staffRepository.save(staff);
        }
    }

    private Staff findStaff(Long id) {

        return staffRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Staff not found with ID: " + id
                        )
                );
    }

    private String generateStaffCode() {

        long count = staffRepository.count() + 1;

        return String.format(
                "PBS-%03d",
                count
        );
    }

    private void createDefaultWorkingHours(
            Staff staff
    ) {

        String[] days = {
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday",
                "Sunday"
        };

        for (String day : days) {

            StaffWorkingHour workingHour =
                    new StaffWorkingHour();

            workingHour.setDay(day);

            workingHour.setStartTime(
                    java.time.LocalTime.of(9, 0)
            );

            workingHour.setEndTime(
                    java.time.LocalTime.of(17, 0)
            );

            // Sunday = day off
            workingHour.setDayOff(
                    day.equals("Sunday")
            );

            workingHour.setStaff(staff);

            workingHourRepository.save(workingHour);
        }
    }

    private StaffDTO convertToDTO(Staff staff) {

        StaffDTO dto = new StaffDTO();

        dto.setStaffId(staff.getStaffId());
        dto.setStaffCode(staff.getStaffCode());
        dto.setStaffName(staff.getStaffName());
        dto.setStaffEmail(staff.getStaffEmail());
        dto.setStaffPhone(staff.getStaffPhone());

        dto.setDateOfBirth(staff.getDateOfBirth());
        dto.setGender(staff.getGender());
        dto.setAddress(staff.getAddress());
        dto.setPosition(staff.getPosition());
        dto.setHireDate(staff.getHireDate());
        dto.setSalary(staff.getSalary());
        dto.setStatus(staff.getStatus());
        dto.setAvailability(staff.getAvailability());

        if (staff.getUser() != null) {
            dto.setUserId(staff.getUser().getUserId());
        }

        dto.setServices(new ArrayList<>());
        return dto;
    }

    private StaffWorkingHourDTO
    convertWorkingHourToDTO(StaffWorkingHour entity) {

        StaffWorkingHourDTO dto = new StaffWorkingHourDTO();

        dto.setWorkingHourId(entity.getWorkingHourId());
        dto.setDay(entity.getDay());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setDayOff(entity.isDayOff());

        return dto;
    }



    private StaffLeaveDTO
    convertLeaveToDTO(StaffLeave entity) {

        StaffLeaveDTO dto = new StaffLeaveDTO();

        dto.setLeaveId(entity.getLeaveId());
        dto.setStaffId(entity.getStaff().getStaffId());
        dto.setLeaveType(entity.getLeaveType());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setReason(entity.getReason());
        dto.setStatus(entity.getStatus());

        return dto;
    }

    private boolean contains(String value, String search) {

        return value != null && value.toLowerCase().contains(search);
    }
}