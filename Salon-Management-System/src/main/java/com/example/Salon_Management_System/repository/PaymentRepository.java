package com.example.Salon_Management_System.repository;

import com.example.Salon_Management_System.entity.Payment;
import com.example.Salon_Management_System.enumiration.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByAppointmentAppointmentId(Long appointmentId);

    boolean existsByAppointmentAppointmentId(Long appointmentId);

    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);

    List<Payment> findByPaymentDateBetween(
            LocalDate from,
            LocalDate to
    );

    List<Payment> findByPaymentStatusAndPaymentDateBetween(
            PaymentStatus status,
            LocalDate from,
            LocalDate to
    );

    
}