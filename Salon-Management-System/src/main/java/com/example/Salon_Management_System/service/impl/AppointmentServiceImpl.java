package com.example.Salon_Management_System.service.impl;

import com.example.Salon_Management_System.dto.AppointmentDTO;
import com.example.Salon_Management_System.entity.Appointment;
import com.example.Salon_Management_System.repository.AppointmentRepository;
import com.example.Salon_Management_System.repository.CustomerRepository;
import com.example.Salon_Management_System.repository.ServiceRepository;
import com.example.Salon_Management_System.repository.StaffRepository;
import com.example.Salon_Management_System.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final ServiceRepository salonServiceRepository;
    private final StaffRepository staffRepository;

    @Override
    public AppointmentDTO createAppointment(AppointmentDTO  dto) {
        log.info("Creating appointment: {}", dto);
        try {

            Appointment appointment = toEntity(dto);
            Appointment saved = appointmentRepository.save(appointment);
            return toDTO(saved);

        } catch (Exception e) {
            log.error("Error occurred while creating appointment", e);
            throw new RuntimeException("Error occurred while creating appointment", e);
        }
    }

    @Override
    public AppointmentDTO getAppointmentById(Long appointmentId) {
        log.info("Fetching appointment with ID: {}", appointmentId);
        try{

            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + appointmentId));
            return toDTO(appointment);

        }catch (Exception e){
            log.error("Error occurred while fetching appointment", e);
            throw new RuntimeException("Error occurred while fetching appointment", e);
        }
    }

    @Override
    public List<AppointmentDTO> getAllAppointments() {
        log.info("Fetching all appointments");
        try{
            return appointmentRepository.findAll()
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        }catch (Exception e){
            log.error("Error occurred while fetching all appointments", e);
            throw new RuntimeException("Error occurred while fetching all appointments", e);
        }
    }

    @Override
    public AppointmentDTO updateAppointment(Long appointmentId, AppointmentDTO appointmentDTO) {
        return null;
    }

    @Override
    public void deleteAppointment(Long appointmentId) {

    }

    @Override
    public List<AppointmentDTO> getAppointmentsByCustomer(Long customerId) {
        return List.of();
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByStaff(Long staffId) {
        return List.of();
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByDate(LocalDate date) {
        return List.of();
    }
}
