package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.CommonResponse;
import com.example.Salon_Management_System.dto.StaffDTO;
import com.example.Salon_Management_System.dto.StaffLeaveDTO;
import com.example.Salon_Management_System.dto.StaffSaveDTO;
import com.example.Salon_Management_System.dto.StaffWorkingHourDTO;
import com.example.Salon_Management_System.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/staff")
@CrossOrigin
public class StaffController {

    private final StaffService staffService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse createStaff(@RequestBody StaffSaveDTO dto) {
        StaffDTO response = staffService.createStaff(dto);
        return new CommonResponse(0, response, "Staff created successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllStaff() {
        List<StaffDTO> staffList = staffService.getAllStaff();
        return new CommonResponse(0, staffList, "All Staff successfully retrieved");
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getStaffById(@PathVariable Long id) {
        StaffDTO staff = staffService.getStaffById(id);
        return new CommonResponse(0, staff, "Staff retrieved successfully"
        );
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse searchStaff(@RequestParam String keyword) {
        List<StaffDTO> staffList = staffService.searchStaff(keyword);
        return new CommonResponse(0, staffList, "Staff search successfully");
    }


    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateStaff(@PathVariable Long id, @RequestBody StaffSaveDTO dto) {
        StaffDTO response = staffService.updateStaff(id, dto);
        return new CommonResponse(0, response, "Staff updated successfully");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return new CommonResponse(0, "Staff deleted successfully");
    }


    @PatchMapping(value = "/status/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateStatus(@PathVariable Long id) {
        staffService.updateStatus(id);
        return new CommonResponse(0, "Staff status updated successfully"
        );
    }

    @GetMapping(value = "/{id}/working-hours", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getWorkingHours(@PathVariable Long id) {
        List<StaffWorkingHourDTO> workingHours = staffService.getWorkingHours(id);
        return new CommonResponse(0, workingHours, "Staff working hours retrieved successfully");
    }

    @PutMapping(value = "/{id}/working-hours", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateWorkingHours(@PathVariable Long id, @RequestBody List<StaffWorkingHourDTO> dto) {
        staffService.updateWorkingHours(id, dto);
        return new CommonResponse(0, "Staff working hours updated successfully");
    }


    @PostMapping(value = "/{id}/leave", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse addLeave(@PathVariable Long id, @RequestBody StaffLeaveDTO dto) {
        StaffLeaveDTO response = staffService.addLeave(id, dto);
        return new CommonResponse(0, response, "Staff leave added successfully");
    }


    @GetMapping(value = "/{id}/leave", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getLeaves(@PathVariable Long id) {
        List<StaffLeaveDTO> leaves = staffService.getStaffLeaves(id);
        return new CommonResponse(0, leaves, "Staff leaves retrieved successfully");
    }

    @PatchMapping(value = "/leave/{leaveId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateLeaveStatus(@PathVariable Long leaveId, @RequestParam String status) {
        staffService.updateLeaveStatus(leaveId, status);
        return new CommonResponse(0, "Staff leave status updated successfully");
    }
}