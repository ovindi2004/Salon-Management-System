package com.example.Salon_Management_System.repository;

import com.example.Salon_Management_System.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // =========================================================
    // CUSTOMER APPOINTMENTS
    // =========================================================

    List<Appointment> findByCustomer_CustomerId(Long customerId);


    // =========================================================
    // STAFF APPOINTMENTS
    // =========================================================

    List<Appointment> findByStaff_StaffId(Long staffId);


    // =========================================================
    // APPOINTMENTS BY DATE
    // =========================================================

    List<Appointment> findByAppointmentDate(LocalDate date);


    // =========================================================
    // APPOINTMENTS BY STAFF + DATE
    // =========================================================

    List<Appointment> findByStaff_StaffIdAndAppointmentDate(
            Long staffId,
            LocalDate appointmentDate
    );


    // =========================================================
    // DASHBOARD / REPORT DATE RANGE
    // =========================================================

    List<Appointment> findByAppointmentDateBetween(
            LocalDate fromDate,
            LocalDate toDate
    );
}