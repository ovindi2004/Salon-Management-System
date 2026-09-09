package com.example.Salon_Management_System.service.impl;

import com.example.Salon_Management_System.dto.AppointmentDTO;
import com.example.Salon_Management_System.entity.Appointment;
import com.example.Salon_Management_System.entity.Customer;
import com.example.Salon_Management_System.entity.SalonService;
import com.example.Salon_Management_System.entity.Staff;
import com.example.Salon_Management_System.repository.AppointmentRepository;
import com.example.Salon_Management_System.repository.CustomerRepository;
import com.example.Salon_Management_System.repository.ServiceRepository;
import com.example.Salon_Management_System.repository.StaffRepository;
import com.example.Salon_Management_System.service.AppointmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final ServiceRepository salonServiceRepository;
    private final StaffRepository staffRepository;

    @Override
    public AppointmentDTO createAppointment(AppointmentDTO dto) {
        log.info("Creating appointment: " + dto);
        try {
            Appointment appointment = toEntity(dto);
            Appointment saved = appointmentRepository.save(appointment);
            return toDTO(saved);
        }catch (Exception e) {
            log.error("Error creating appointment: " + e.getMessage());
            throw new RuntimeException("Failed to create appointment");
        }
    }

    @Override
    public AppointmentDTO getAppointmentById(Long appointmentId) {
        log.info("Getting appointment by id: " + appointmentId);
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id: " + appointmentId));
            return toDTO(appointment);
        }catch (Exception e) {
            log.error("Error getting appointment by id: " + e.getMessage());
            throw new RuntimeException("Failed to get appointment by id");
        }
    }

    @Override
    public List<AppointmentDTO> getAllAppointments() {
        log.info("Getting all appointments");
        try {
            return appointmentRepository.findAll()
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        }catch (Exception e) {
            log.error("Error getting all appointments: " + e.getMessage());
            throw new RuntimeException("Failed to get all appointments");
        }
    }

    @Override
    public AppointmentDTO updateAppointment(Long appointmentId, AppointmentDTO dto) {
        log.info("Updating appointment by id: " + appointmentId);
        try {
        Appointment existing = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id: " + appointmentId));

        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + dto.getCustomerId()));
            existing.setCustomer(customer);
        }
        if (dto.getServiceId() != null) {
            SalonService service = salonServiceRepository.findById(dto.getServiceId())
                    .orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + dto.getServiceId()));
            existing.setService(service);
        }
        if (dto.getStaffId() != null) {
            Staff staff = staffRepository.findById(dto.getStaffId())
                    .orElseThrow(() -> new EntityNotFoundException("Staff not found with id: " + dto.getStaffId()));
            existing.setStaff(staff);
        }
        if (dto.getAppointmentDate() != null) existing.setAppointmentDate(dto.getAppointmentDate());
        if (dto.getStartTime() != null) existing.setStartTime(dto.getStartTime());
        if (dto.getDuration() != null) existing.setDuration(dto.getDuration());
        if (dto.getStatus() != null) existing.setStatus(dto.getStatus());
        if (dto.getPaymentStatus() != null) existing.setPaymentStatus(dto.getPaymentStatus());
        if (dto.getNotes() != null) existing.setNotes(dto.getNotes());

        Appointment updated = appointmentRepository.save(existing);
        return toDTO(updated);
    }catch (Exception e) {
        log.error("Error updating appointment by id: " + e.getMessage());
        throw new RuntimeException("Failed to update appointment by id");
    }
    }

    @Override
    public void deleteAppointment(Long appointmentId) {
        log.info("Deleting appointment by id: " + appointmentId);
        try {
            if (!appointmentRepository.existsById(appointmentId)) {
                throw new EntityNotFoundException("Appointment not found with id: " + appointmentId);
            }
            appointmentRepository.deleteById(appointmentId);

        }catch (Exception e) {
            log.error("Error deleting appointment by id: " + e.getMessage());
            throw new RuntimeException("Failed to delete appointment by id");
        }
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByCustomer(Long customerId) {
        log.info("Getting appointments by customer id: " + customerId);
        try {
            return appointmentRepository.findByCustomer_CustomerId(customerId)
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());

        }catch (Exception e) {
            log.error("Error getting appointments by customer id: " + e.getMessage());
            throw new RuntimeException("Failed to get appointments by customer id");
        }

    }

    @Override
    public List<AppointmentDTO> getAppointmentsByStaff(Long staffId) {
        log.info("Getting appointments by staff id: " + staffId);
        try {
            return appointmentRepository.findByStaff_StaffId(staffId)
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        }catch (Exception e) {
            log.error("Error getting appointments by staff id: " + e.getMessage());
            throw new RuntimeException("Failed to get appointments by staff id");
        }
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByDate(LocalDate date) {
        log.info("Getting appointments by date: " + date);
        try {
            return appointmentRepository.findByAppointmentDate(date)
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        }catch (Exception e) {
            log.error("Error getting appointments by date: " + e.getMessage());
            throw new RuntimeException("Failed to get appointments by date");
        }
    }

    private Appointment toEntity(AppointmentDTO dto) {
        log.info("Converting appointment dto to entity");
        try {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + dto.getCustomerId()));
            SalonService service = salonServiceRepository.findById(dto.getServiceId())
                    .orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + dto.getServiceId()));
            Staff staff = staffRepository.findById(dto.getStaffId())
                    .orElseThrow(() -> new EntityNotFoundException("Staff not found with id: " + dto.getStaffId()));

            Appointment appointment = new Appointment();
            appointment.setCustomer(customer);
            appointment.setService(service);
            appointment.setStaff(staff);
            appointment.setAppointmentDate(dto.getAppointmentDate());
            appointment.setStartTime(dto.getStartTime());
            appointment.setDuration(dto.getDuration());
            if (dto.getStatus() != null) appointment.setStatus(dto.getStatus());
            if (dto.getPaymentStatus() != null) appointment.setPaymentStatus(dto.getPaymentStatus());
            appointment.setNotes(dto.getNotes());
            return appointment;
        }catch (Exception e) {
            log.error("Error converting appointment dto to entity: " + e.getMessage());
            throw new RuntimeException("Failed to convert appointment dto to entity");
        }
    }

    private AppointmentDTO toDTO(Appointment appointment) {
        log.info("Converting appointment entity to dto");
        try {
        return new AppointmentDTO(
                appointment.getAppointmentId(),
                appointment.getCustomer() != null ? appointment.getCustomer().getCustomerId() : null,
                appointment.getCustomer() != null ? appointment.getCustomer().getCustomerName() : null,
                appointment.getService() != null ? appointment.getService().getServiceId() : null,
                appointment.getService() != null ? appointment.getService().getServiceName() : null,
                appointment.getStaff() != null ? appointment.getStaff().getStaffId() : null,
                appointment.getStaff() != null ? appointment.getStaff().getStaffName() : null,
                appointment.getAppointmentDate(),
                appointment.getStartTime(),
                appointment.getDuration(),
                appointment.getStatus(),
                appointment.getPaymentStatus(),
                appointment.getNotes());
        }catch (Exception e) {
            log.error("Error converting appointment entity to dto: " + e.getMessage());
            throw new RuntimeException("Failed to convert appointment entity to dto");
        }
    }
}