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
    public StaffDTO getStaffById(Long staffId) {
        Staff staff = findStaff(staffId);
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
                .map(this::convertTODTO)
                .collect(Collectors.toList());
    }

    @Override
    public StaffDTO updateStaff(Long id, StaffDTO dto) {
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
    return

    convertToDTO(updatedStaff);
}
@Override
public void deleteStaff(Long id){
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
}
@Override
@Transactional(readOnly = true)
public List<StaffWorkingHourDTO>getWorkingHours(Long staffId){
    Staff staff = findStaff(staffId);
    return staff.getStaffWorkingHours()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
}

@Override
public void updateWorkingHours(Long staffId, List<StaffWorkingHourDTO> dtoList) {
    Staff staff = findStaff(staffId);
    List<StaffWorkingHour>existingWorkingHours = workingHourRepository.findByStaffStaffId(staffId);

    workingHourRepository.deleteAll(existingWorkingHours);

    if(dtoList == null){
        return;
    }
    for (StaffWorkingHourDTO staffWorkingHourDTO : dtoList) {
        StaffWorkingHour staffWorkingHour = new StaffWorkingHour();

        staffWorkingHour.setDay(staffWorkingHourDTO.getDay());
        staffWorkingHour.setStartTime(staffWorkingHourDTO.getStartTime());
        staffWorkingHour.setEndTime(staffWorkingHourDTO.getEndTime());
        staffWorkingHour.setStaff(staff);

        workingHourRepository.save(staffWorkingHour);
    }


}

@Override
public void updateLeave(Long staffId, List<StaffLeaveDTO> dto) {

    Staff staff =findStaff(staffId);

    if (dto.getStartDate()== null ||
    dto.getEndDate() == null){

        throw new RuntimeException("" +
                "Levre Start")

    }
    if(dto.getEndDate().isBefore(dto.getStartDate())){
        throw new RuntimeException("Leave End Date");
    }
     StaffLeave staffLeave = new StaffLeave();

    staffLeave.setStartDate(dto.getStartDate());
    staffLeave.setEndDate(dto.getEndDate());
    staffLeave.setStaff(staff);


    if (dto.getStatus() != null) {
        leave.setStatus(dto.getStatus());
    } else {
        leave.setStatus(LeaveStatus.PENDING);
    }

    StaffLeave savedLeave =
            leaveRepository.save(leave);

    return convertLeaveToDTO(savedLeave);

    staffLeaveRepository.save(staffLeave);
}

