package com.example.Salon_Management_System.service.impl;

import com.example.Salon_Management_System.dto.AppointmentDTO;
import com.example.Salon_Management_System.entity.Appointment;
import com.example.Salon_Management_System.entity.Customer;
import com.example.Salon_Management_System.entity.SalonService;
import com.example.Salon_Management_System.entity.Staff;
import com.example.Salon_Management_System.enumiration.AppointmentStatus;
import com.example.Salon_Management_System.repository.AppointmentRepository;
import com.example.Salon_Management_System.repository.CustomerRepository;
import com.example.Salon_Management_System.repository.ServiceRepository;
import com.example.Salon_Management_System.repository.StaffRepository;
import com.example.Salon_Management_System.service.AppointmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final ServiceRepository salonServiceRepository;
    private final StaffRepository staffRepository;


    @Override
    public AppointmentDTO createAppointment(AppointmentDTO dto) {

        log.info("Creating appointment: {}", dto);

        validateBasicAppointmentData(dto);

        try {

            Customer customer =
                    customerRepository.findById(dto.getCustomerId())
                            .orElseThrow(() ->
                                    new EntityNotFoundException(
                                            "Customer not found with id: "
                                                    + dto.getCustomerId()
                                    )
                            );

            SalonService service =
                    salonServiceRepository.findById(dto.getServiceId())
                            .orElseThrow(() ->
                                    new EntityNotFoundException(
                                            "Service not found with id: "
                                                    + dto.getServiceId()
                                    )
                            );

            Staff staff =
                    staffRepository.findById(dto.getStaffId())
                            .orElseThrow(() ->
                                    new EntityNotFoundException(
                                            "Staff not found with id: "
                                                    + dto.getStaffId()
                                    )
                            );

            Integer duration = service.getDuration();

            if (duration == null || duration <= 0) {
                throw new RuntimeException(
                        "Service duration is not available"
                );
            }


            checkStaffAvailability(
                    staff.getStaffId(),
                    dto.getAppointmentDate(),
                    dto.getStartTime(),
                    duration,
                    null
            );

            Appointment appointment = new Appointment();

            appointment.setCustomer(customer);
            appointment.setService(service);
            appointment.setStaff(staff);

            appointment.setAppointmentDate(
                    dto.getAppointmentDate()
            );

            appointment.setStartTime(
                    dto.getStartTime()
            );

            appointment.setDuration(duration);

            appointment.setStatus(
                    dto.getStatus() != null
                            ? dto.getStatus()
                            : AppointmentStatus.SCHEDULED
            );

            appointment.setNotes(dto.getNotes());

            Appointment savedAppointment =
                    appointmentRepository.save(appointment);

            log.info(
                    "Appointment created successfully. ID: {}",
                    savedAppointment.getAppointmentId()
            );

            return toDTO(savedAppointment);

        } catch (EntityNotFoundException e) {

            throw e;

        } catch (RuntimeException e) {

            throw e;

        } catch (Exception e) {

            log.error(
                    "Error creating appointment",
                    e
            );

            throw new RuntimeException(
                    "Failed to create appointment: "
                            + e.getMessage()
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentDTO getAppointmentById(Long appointmentId) {

        log.info("Fetching appointment by ID: {}", appointmentId);

        try {

            if (appointmentId == null) {
                throw new RuntimeException(
                        "Appointment ID is required"
                );
            }

            Appointment appointment =
                    appointmentRepository.findById(appointmentId)
                            .orElseThrow(() ->
                                    new EntityNotFoundException(
                                            "Appointment not found with id: "
                                                    + appointmentId
                                    )
                            );

            return toDTO(appointment);

        } catch (Exception e) {
           log.error("Error fetching appointment by ID: {}", appointmentId, e);
           throw new RuntimeException("Failed to fetch appointment by ID: " + e.getMessage());
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAllAppointments() {
        log.info("Fetching all appointments");

        try{

        return appointmentRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
        } catch (Exception e) {
            log.error("Error fetching all appointments", e);
            throw new RuntimeException("Failed to fetch all appointments: " + e.getMessage());
        }
    }

    @Override
    public AppointmentDTO updateAppointment(Long appointmentId, AppointmentDTO dto) {

        log.info("Updating appointment with ID: {}", appointmentId);

        try{

        if (appointmentId == null) {
            throw new RuntimeException(
                    "Appointment ID is required"
            );
        }

        if (dto == null) {
            throw new RuntimeException(
                    "Appointment data is required"
            );
        }

        Appointment existing = appointmentRepository.findById(appointmentId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Appointment not found with id: "
                                                + appointmentId
                                )
                        );

        Customer customer = existing.getCustomer();

        if (dto.getCustomerId() != null) {

            customer =
                    customerRepository.findById(
                                    dto.getCustomerId()
                            )
                            .orElseThrow(() ->
                                    new EntityNotFoundException(
                                            "Customer not found with id: "
                                                    + dto.getCustomerId()
                                    )
                            );

            existing.setCustomer(customer);
        }

        SalonService service = existing.getService();

        if (dto.getServiceId() != null) {

            service =
                    salonServiceRepository.findById(
                                    dto.getServiceId()
                            )
                            .orElseThrow(() ->
                                    new EntityNotFoundException(
                                            "Service not found with id: "
                                                    + dto.getServiceId()
                                    )
                            );

            existing.setService(service);
        }

        Staff staff = existing.getStaff();

        if (dto.getStaffId() != null) {

            staff =
                    staffRepository.findById(
                                    dto.getStaffId()
                            )
                            .orElseThrow(() ->
                                    new EntityNotFoundException(
                                            "Staff not found with id: "
                                                    + dto.getStaffId()
                                    )
                            );

            existing.setStaff(staff);
        }


        LocalDate appointmentDate =
                dto.getAppointmentDate() != null
                        ? dto.getAppointmentDate()
                        : existing.getAppointmentDate();


        LocalTime startTime =
                dto.getStartTime() != null
                        ? dto.getStartTime()
                        : existing.getStartTime();

        Integer duration;

        if (dto.getServiceId() != null) {

            duration = service.getDuration();

        } else {

            duration = existing.getDuration();
        }

        if (duration == null || duration <= 0) {

            throw new RuntimeException(
                    "Duration must be greater than zero"
            );
        }

        validateAppointmentDate(appointmentDate);

        checkStaffAvailability(
                staff.getStaffId(),
                appointmentDate,
                startTime,
                duration,
                appointmentId
        );

        existing.setAppointmentDate(appointmentDate);

        existing.setStartTime(startTime);

        existing.setDuration(duration);

        if (dto.getStatus() != null) {

            existing.setStatus(dto.getStatus());
        }

        if (dto.getNotes() != null) {

            existing.setNotes(dto.getNotes());
        }

        Appointment updatedAppointment = appointmentRepository.save(existing);

        log.info(
                "Appointment updated successfully. ID: {}",
                appointmentId
        );

        return toDTO(updatedAppointment);
    }catch (Exception e) {
        log.error("Error updating appointment with ID: {}", appointmentId, e);
        throw new RuntimeException("Failed to update appointment with ID: " + e.getMessage());
    }
    }

    @Override
    public void deleteAppointment(Long appointmentId) {
        log.info("Deleting appointment with ID: {}", appointmentId);

        try{

        if (appointmentId == null) {
            throw new RuntimeException(
                    "Appointment ID is required"
            );
        }

        Appointment appointment =
                appointmentRepository.findById(appointmentId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Appointment not found with id: "
                                                + appointmentId
                                )
                        );

        if (appointment.getInvoice() != null) {

            throw new RuntimeException(
                    "Cannot delete appointment because an invoice already exists"
            );
        }

        appointmentRepository.delete(appointment);

        log.info(
                "Appointment deleted successfully. ID: {}",
                appointmentId
        );
        }catch (Exception e) {
            log.error("Error deleting appointment with ID: {}", appointmentId, e);
            throw new RuntimeException("Failed to delete appointment with ID: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByCustomer(Long customerId) {

        log.info("Fetching appointments by customer ID: {}", customerId);

        try{

        if (customerId == null) {
            throw new RuntimeException(
                    "Customer ID is required"
            );
        }

        return appointmentRepository
                .findByCustomer_CustomerId(customerId)
                .stream()
                .map(this::toDTO)
                .toList();
        }catch (Exception e) {
            log.error("Error fetching appointments by customer ID: {}", customerId, e);
            throw new RuntimeException("Failed to fetch appointments by customer ID: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByStaff(Long staffId) {

        log.info("Fetching appointments by staff ID: {}", staffId);

        try{

        if (staffId == null) {
            throw new RuntimeException(
                    "Staff ID is required"
            );
        }

        return appointmentRepository
                .findByStaff_StaffId(staffId)
                .stream()
                .map(this::toDTO)
                .toList();
        }catch (Exception e) {
            log.error("Error fetching appointments by staff ID: {}", staffId, e);
            throw new RuntimeException("Failed to fetch appointments by staff ID: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByDate(LocalDate date) {

        log.info("Fetching appointments by date: {}", date);

        try{

        if (date == null) {
            throw new RuntimeException(
                    "Appointment date is required"
            );
        }

        return appointmentRepository
                .findByAppointmentDate(date)
                .stream()
                .map(this::toDTO)
                .toList();
        }catch (Exception e) {
            log.error("Error fetching appointments by date: {}", date, e);
            throw new RuntimeException("Failed to fetch appointments by date: " + e.getMessage());
        }
    }


    private void validateBasicAppointmentData(AppointmentDTO dto) {


        if (dto == null) {
            throw new RuntimeException(
                    "Appointment data is required"
            );
        }

        if (dto.getCustomerId() == null) {
            throw new RuntimeException(
                    "Customer ID is required"
            );
        }

        if (dto.getServiceId() == null) {
            throw new RuntimeException(
                    "Service ID is required"
            );
        }

        if (dto.getStaffId() == null) {
            throw new RuntimeException(
                    "Staff ID is required"
            );
        }

        if (dto.getAppointmentDate() == null) {
            throw new RuntimeException(
                    "Appointment date is required"
            );
        }

        if (dto.getStartTime() == null) {
            throw new RuntimeException(
                    "Appointment start time is required"
            );
        }

        validateAppointmentDate(
                dto.getAppointmentDate()
        );
    }

    private void validateAppointmentDate(LocalDate appointmentDate) {

        log.info("Validating appointment date");

        try{

        if (appointmentDate == null) {

            throw new RuntimeException(
                    "Appointment date is required"
            );
        }

        if (appointmentDate.isBefore(LocalDate.now())) {

            throw new RuntimeException(
                    "Appointment date cannot be in the past"
            );
        }
        }catch (Exception e) {
            log.error("Error validating appointment date", e);
            throw new RuntimeException("Failed to validate appointment date: " + e.getMessage());
        }
    }

    private void checkStaffAvailability(
            Long staffId,
            LocalDate appointmentDate,
            LocalTime newStartTime,
            Integer newDuration,
            Long currentAppointmentId
    ) {

        log.info("Checking staff availability");

        try{

        List<Appointment> appointments = appointmentRepository
                        .findByStaff_StaffIdAndAppointmentDate(
                                staffId,
                                appointmentDate
                        );

        LocalTime newEndTime =
                newStartTime.plusMinutes(newDuration);


        for (Appointment existing : appointments) {


            if (currentAppointmentId != null &&
                    existing.getAppointmentId()
                            .equals(currentAppointmentId)) {

                continue;
            }

            if (existing.getStatus() ==
                    AppointmentStatus.CANCELLED) {

                continue;
            }

            if (existing.getStatus() ==
                    AppointmentStatus.NO_SHOW) {

                continue;
            }


            if (existing.getStartTime() == null ||
                    existing.getDuration() == null ||
                    existing.getDuration() <= 0) {

                continue;
            }


            LocalTime existingEndTime =
                    existing.getStartTime()
                            .plusMinutes(
                                    existing.getDuration()
                            );


            boolean overlaps = existing.getStartTime()
                            .isBefore(newEndTime)
                            &&
                            existingEndTime
                                    .isAfter(newStartTime);


            if (overlaps) {

                throw new RuntimeException(
                        "Staff is already booked from "
                                + existing.getStartTime()
                                + " to "
                                + existingEndTime
                );
            }
        }
        }catch (Exception e) {
            log.error("Error checking staff availability", e);
            throw new RuntimeException("Failed to check staff availability: " + e.getMessage());
        }

    }

    private AppointmentDTO toDTO(Appointment appointment) {

        log.info("Converting appointment to DTO");

        try{


        return new AppointmentDTO(appointment.getAppointmentId(),
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

                appointment.getNotes()
        );
        }catch (Exception e) {
            log.error("Error converting appointment to DTO", e);
            throw new RuntimeException("Failed to convert appointment to DTO: " + e.getMessage());
        }
    }

}