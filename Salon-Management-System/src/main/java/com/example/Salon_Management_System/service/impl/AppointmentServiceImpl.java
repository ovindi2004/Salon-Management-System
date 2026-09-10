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


    // =========================================================
    // CREATE APPOINTMENT
    // =========================================================
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

            // -------------------------------------------------
            // USE SERVICE DURATION
            // -------------------------------------------------
            Integer duration = service.getDuration();

            if (duration == null || duration <= 0) {
                throw new RuntimeException(
                        "Service duration is not available"
                );
            }

            // -------------------------------------------------
            // CHECK STAFF DOUBLE BOOKING
            // -------------------------------------------------
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


    // =========================================================
    // GET APPOINTMENT BY ID
    // =========================================================
    @Override
    @Transactional(readOnly = true)
    public AppointmentDTO getAppointmentById(Long appointmentId) {

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
    }


    // =========================================================
    // GET ALL APPOINTMENTS
    // =========================================================
    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAllAppointments() {

        return appointmentRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }


    // =========================================================
    // UPDATE APPOINTMENT
    // =========================================================
    @Override
    public AppointmentDTO updateAppointment(
            Long appointmentId,
            AppointmentDTO dto
    ) {

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

        Appointment existing =
                appointmentRepository.findById(appointmentId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Appointment not found with id: "
                                                + appointmentId
                                )
                        );


        // =====================================================
        // CUSTOMER
        // =====================================================
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


        // =====================================================
        // SERVICE
        // =====================================================
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


        // =====================================================
        // STAFF
        // =====================================================
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


        // =====================================================
        // DATE
        // =====================================================
        LocalDate appointmentDate =
                dto.getAppointmentDate() != null
                        ? dto.getAppointmentDate()
                        : existing.getAppointmentDate();


        // =====================================================
        // START TIME
        // =====================================================
        LocalTime startTime =
                dto.getStartTime() != null
                        ? dto.getStartTime()
                        : existing.getStartTime();


        // =====================================================
        // DURATION
        // =====================================================
        Integer duration;

        /*
         * Service duration is the source of truth.
         *
         * If service is changed, duration changes automatically.
         */
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


        // =====================================================
        // DATE VALIDATION
        // =====================================================
        validateAppointmentDate(appointmentDate);


        // =====================================================
        // DOUBLE BOOKING CHECK
        // =====================================================
        checkStaffAvailability(
                staff.getStaffId(),
                appointmentDate,
                startTime,
                duration,
                appointmentId
        );


        // =====================================================
        // SET VALUES
        // =====================================================
        existing.setAppointmentDate(appointmentDate);

        existing.setStartTime(startTime);

        existing.setDuration(duration);


        // =====================================================
        // STATUS
        // =====================================================
        if (dto.getStatus() != null) {

            existing.setStatus(dto.getStatus());
        }


        // =====================================================
        // NOTES
        // =====================================================
        if (dto.getNotes() != null) {

            existing.setNotes(dto.getNotes());
        }


        Appointment updatedAppointment =
                appointmentRepository.save(existing);

        log.info(
                "Appointment updated successfully. ID: {}",
                appointmentId
        );

        return toDTO(updatedAppointment);
    }


    // =========================================================
    // DELETE APPOINTMENT
    // =========================================================
    @Override
    public void deleteAppointment(Long appointmentId) {

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

        /*
         * Invoice has a OneToOne relationship with Appointment.
         *
         * If an invoice exists, deleting the appointment can
         * break the invoice relationship.
         */
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
    }


    // =========================================================
    // GET BY CUSTOMER
    // =========================================================
    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByCustomer(
            Long customerId
    ) {

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
    }


    // =========================================================
    // GET BY STAFF
    // =========================================================
    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByStaff(
            Long staffId
    ) {

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
    }


    // =========================================================
    // GET BY DATE
    // =========================================================
    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByDate(
            LocalDate date
    ) {

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
    }


    // =========================================================
    // BASIC VALIDATION
    // =========================================================
    private void validateBasicAppointmentData(
            AppointmentDTO dto
    ) {

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


    // =========================================================
    // DATE VALIDATION
    // =========================================================
    private void validateAppointmentDate(
            LocalDate appointmentDate
    ) {

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
    }


    // =========================================================
    // DOUBLE BOOKING CHECK
    // =========================================================
    private void checkStaffAvailability(
            Long staffId,
            LocalDate appointmentDate,
            LocalTime newStartTime,
            Integer newDuration,
            Long currentAppointmentId
    ) {

        List<Appointment> appointments =
                appointmentRepository
                        .findByStaff_StaffIdAndAppointmentDate(
                                staffId,
                                appointmentDate
                        );

        LocalTime newEndTime =
                newStartTime.plusMinutes(newDuration);


        for (Appointment existing : appointments) {

            // Ignore current appointment during update
            if (currentAppointmentId != null &&
                    existing.getAppointmentId()
                            .equals(currentAppointmentId)) {

                continue;
            }


            // Cancelled appointments do not block the time
            if (existing.getStatus() ==
                    AppointmentStatus.CANCELLED) {

                continue;
            }


            // No-show appointments do not block the time
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


            /*
             * Overlap formula:
             *
             * Existing Start < New End
             * AND
             * Existing End > New Start
             */
            boolean overlaps =
                    existing.getStartTime()
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
    }


    // =========================================================
    // ENTITY → DTO
    // =========================================================
    private AppointmentDTO toDTO(
            Appointment appointment
    ) {

        return new AppointmentDTO(

                appointment.getAppointmentId(),

                appointment.getCustomer() != null
                        ? appointment.getCustomer().getCustomerId()
                        : null,

                appointment.getCustomer() != null
                        ? appointment.getCustomer().getCustomerName()
                        : null,

                appointment.getService() != null
                        ? appointment.getService().getServiceId()
                        : null,

                appointment.getService() != null
                        ? appointment.getService().getServiceName()
                        : null,

                appointment.getStaff() != null
                        ? appointment.getStaff().getStaffId()
                        : null,

                appointment.getStaff() != null
                        ? appointment.getStaff().getStaffName()
                        : null,

                appointment.getAppointmentDate(),

                appointment.getStartTime(),

                appointment.getDuration(),

                appointment.getStatus(),

                appointment.getNotes()
        );
    }
}