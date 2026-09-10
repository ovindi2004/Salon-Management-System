package com.example.Salon_Management_System.repository;

import com.example.Salon_Management_System.entity.Invoice;
import com.example.Salon_Management_System.enumiration.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    boolean existsByInvoiceNumber(String invoiceNumber);

    Optional<Invoice> findByAppointmentAppointmentId(Long appointmentId);

    List<Invoice> findByStatus(InvoiceStatus status);

    List<Invoice> findByCustomerCustomerId(Long customerId);

    List<Invoice> findByInvoiceDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );

    @Query("""
            SELECT i FROM Invoice i
            JOIN i.customer c
            WHERE LOWER(i.invoiceNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(c.customerName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(c.customerEmail) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<Invoice> searchInvoices(@Param("keyword") String keyword);

    @Query("""
            SELECT COUNT(i)
            FROM Invoice i
            WHERE i.status = :status
            """)
    Long countByInvoiceStatus(@Param("status") InvoiceStatus status);

    @Query("""
            SELECT COALESCE(SUM(i.totalAmount), 0)
            FROM Invoice i
            """)
    Double getTotalInvoiced();

    @Query("""
            SELECT COALESCE(SUM(i.amountPaid), 0)
            FROM Invoice i
            """)
    Double getTotalPaid();

    @Query("""
            SELECT COALESCE(SUM(i.balanceDue), 0)
            FROM Invoice i
            """)
    Double getTotalOutstanding();

    @Query("""
            SELECT COUNT(i)
            FROM Invoice i
            WHERE i.balanceDue > 0
              AND i.dueDate < CURRENT_DATE
            """)
    Long countOverdueInvoices();


    
}