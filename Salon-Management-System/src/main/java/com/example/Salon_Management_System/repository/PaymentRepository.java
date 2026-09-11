package com.example.Salon_Management_System.repository;

import com.example.Salon_Management_System.entity.Payment;
import com.example.Salon_Management_System.enumiration.PaymentMethod;
import com.example.Salon_Management_System.enumiration.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByInvoiceInvoiceId(Long invoiceId);

    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);

    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);

    List<Payment> findByPaymentDateBetween(LocalDate from, LocalDate to);

    List<Payment> findByPaymentStatusAndPaymentDateBetween(PaymentStatus status, LocalDate from, LocalDate to);
}