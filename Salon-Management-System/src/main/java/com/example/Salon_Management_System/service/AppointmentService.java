package com.example.Salon_Management_System.service;

import com.example.Salon_Management_System.dto.AppointmentDTO;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    AppointmentDTO createAppointment(AppointmentDTO appointmentDTO);

    AppointmentDTO getAppointmentById(Long appointmentId);

    List<AppointmentDTO> getAllAppointments();

    AppointmentDTO updateAppointment(Long appointmentId, AppointmentDTO appointmentDTO);

    void deleteAppointment(Long appointmentId);

    List<AppointmentDTO> getAppointmentsByCustomer(Long customerId);

    List<AppointmentDTO> getAppointmentsByStaff(Long staffId);

    List<AppointmentDTO> getAppointmentsByDate(LocalDate date);
}