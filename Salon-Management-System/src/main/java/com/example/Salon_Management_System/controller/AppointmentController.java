package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.AppointmentDTO;
import com.example.Salon_Management_System.dto.CommonResponse;
import com.example.Salon_Management_System.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO response = appointmentService.createAppointment(appointmentDTO);
        return new CommonResponse(0, response, "Appointment created successfully");
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAppointmentById(@PathVariable Long id) {
        return new CommonResponse(0, appointmentService.getAppointmentById(id), "Appointment found successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllAppointments() {
        return new CommonResponse(0, appointmentService.getAllAppointments(), "All appointments successfully retrieved");
    }


    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateAppointment(@PathVariable Long id, @RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO response = appointmentService.updateAppointment(id, appointmentDTO);
        return new CommonResponse(0, response, "Appointment updated successfully");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return new CommonResponse(0, "Appointment deleted successfully");
    }

    @GetMapping(value = "/customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAppointmentsByCustomer(@PathVariable Long customerId) {
        return new CommonResponse(0, appointmentService.getAppointmentsByCustomer(customerId), "Customer appointments retrieved successfully");
    }

    @GetMapping(value = "/staff/{staffId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAppointmentsByStaff(@PathVariable Long staffId) {
        return new CommonResponse(0, appointmentService.getAppointmentsByStaff(staffId), "Staff appointments retrieved successfully");
    }


    @GetMapping(value = "/date/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAppointmentsByDate(@PathVariable @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date) {
        return new CommonResponse(0, appointmentService.getAppointmentsByDate(date), "Appointments for selected date retrieved successfully");
    }
}