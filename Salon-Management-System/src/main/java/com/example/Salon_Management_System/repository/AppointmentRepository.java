package com.example.Salon_Management_System.repository;

import com.example.Salon_Management_System.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByCustomer_CustomerId(Long customerId);

    List<Appointment> findByStaff_StaffId(Long staffId);

    List<Appointment> findByAppointmentDate(LocalDate date);

    List<Appointment> findByStaff_StaffIdAndAppointmentDate(Long staffId, LocalDate appointmentDate);

    List<Appointment> findByAppointmentDateBetween(LocalDate fromDate, LocalDate toDate);
}