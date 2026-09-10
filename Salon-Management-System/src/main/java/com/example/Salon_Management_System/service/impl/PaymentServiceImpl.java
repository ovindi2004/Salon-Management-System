package com.example.Salon_Management_System.service.impl;

import com.example.Salon_Management_System.dto.PaymentDTO;
import com.example.Salon_Management_System.entity.Appointment;
import com.example.Salon_Management_System.entity.Customer;
import com.example.Salon_Management_System.entity.Invoice;
import com.example.Salon_Management_System.entity.Payment;
import com.example.Salon_Management_System.enumiration.InvoiceStatus;
import com.example.Salon_Management_System.enumiration.PaymentMethod;
import com.example.Salon_Management_System.enumiration.PaymentStatus;
import com.example.Salon_Management_System.repository.InvoiceRepository;
import com.example.Salon_Management_System.repository.PaymentRepository;
import com.example.Salon_Management_System.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;


    // =========================================================
    // CREATE PAYMENT
    // =========================================================

    @Override
    public PaymentDTO createPayment(PaymentDTO dto) {

        if (dto == null) {
            throw new RuntimeException(
                    "Payment data is required"
            );
        }

        if (dto.getInvoiceId() == null) {
            throw new RuntimeException(
                    "Invoice is required"
            );
        }

        Invoice invoice = invoiceRepository
                .findById(dto.getInvoiceId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invoice not found"
                        )
                );


        // =====================================================
        // AMOUNT VALIDATION
        // =====================================================

        if (dto.getAmount() == null) {

            throw new RuntimeException(
                    "Payment amount is required"
            );
        }

        if (dto.getAmount().signum() <= 0) {

            throw new RuntimeException(
                    "Payment amount must be greater than zero"
            );
        }


        // =====================================================
        // CHECK BALANCE
        // =====================================================

        BigDecimal currentBalance =
                BigDecimal.valueOf(
                        invoice.getBalanceDue() != null
                                ? invoice.getBalanceDue()
                                : 0.0
                );

        if (dto.getAmount().compareTo(currentBalance) > 0) {

            throw new RuntimeException(
                    "Payment amount cannot be greater than invoice balance"
            );
        }


        // =====================================================
        // PAYMENT METHOD
        // =====================================================

        if (dto.getPaymentMethod() == null) {

            throw new RuntimeException(
                    "Payment method is required"
            );
        }


        // =====================================================
        // CREATE PAYMENT
        // =====================================================

        Payment payment = new Payment();

        payment.setInvoice(invoice);

        payment.setAmount(
                dto.getAmount()
        );

        payment.setPaymentMethod(
                dto.getPaymentMethod()
        );


        if (dto.getPaymentStatus() == null) {

            payment.setPaymentStatus(
                    PaymentStatus.PENDING
            );

        } else {

            payment.setPaymentStatus(
                    dto.getPaymentStatus()
            );
        }


        payment.setPaymentDate(
                dto.getPaymentDate() != null
                        ? dto.getPaymentDate()
                        : LocalDate.now()
        );

        payment.setTransactionReference(
                dto.getTransactionReference()
        );

        payment.setNotes(
                dto.getNotes()
        );


        Payment savedPayment =
                paymentRepository.save(payment);


        // =====================================================
        // UPDATE INVOICE PAYMENT SUMMARY
        // =====================================================

        updateInvoicePaymentSummary(invoice);


        return convertToDTO(savedPayment);
    }


    // =========================================================
    // GET PAYMENT BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public PaymentDTO getPaymentById(Long id) {

        if (id == null) {
            throw new RuntimeException(
                    "Payment ID is required"
            );
        }

        Payment payment =
                paymentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found"
                                )
                        );

        return convertToDTO(payment);
    }


    // =========================================================
    // GET ALL PAYMENTS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTO> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // =========================================================
    // UPDATE PAYMENT
    // =========================================================

    @Override
    public PaymentDTO updatePayment(
            Long id,
            PaymentDTO dto
    ) {

        if (id == null) {
            throw new RuntimeException(
                    "Payment ID is required"
            );
        }

        if (dto == null) {
            throw new RuntimeException(
                    "Payment data is required"
            );
        }


        Payment payment =
                paymentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found"
                                )
                        );


        Invoice invoice =
                payment.getInvoice();


        // =====================================================
        // AMOUNT
        // =====================================================

        if (dto.getAmount() != null) {

            if (dto.getAmount().signum() <= 0) {

                throw new RuntimeException(
                        "Payment amount must be greater than zero"
                );
            }

            payment.setAmount(
                    dto.getAmount()
            );
        }


        // =====================================================
        // PAYMENT METHOD
        // =====================================================

        if (dto.getPaymentMethod() != null) {

            payment.setPaymentMethod(
                    dto.getPaymentMethod()
            );
        }


        // =====================================================
        // PAYMENT STATUS
        // =====================================================

        if (dto.getPaymentStatus() != null) {

            payment.setPaymentStatus(
                    dto.getPaymentStatus()
            );
        }


        // =====================================================
        // PAYMENT DATE
        // =====================================================

        if (dto.getPaymentDate() != null) {

            payment.setPaymentDate(
                    dto.getPaymentDate()
            );
        }


        // =====================================================
        // OTHER DETAILS
        // =====================================================

        payment.setTransactionReference(
                dto.getTransactionReference()
        );

        payment.setNotes(
                dto.getNotes()
        );


        // =====================================================
        // SAVE
        // =====================================================

        Payment updatedPayment =
                paymentRepository.save(payment);


        // =====================================================
        // UPDATE INVOICE
        // =====================================================

        updateInvoicePaymentSummary(invoice);


        return convertToDTO(updatedPayment);
    }


    // =========================================================
    // DELETE PAYMENT
    // =========================================================

    @Override
    public void deletePayment(Long id) {

        if (id == null) {
            throw new RuntimeException(
                    "Payment ID is required"
            );
        }

        Payment payment =
                paymentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found"
                                )
                        );

        Invoice invoice =
                payment.getInvoice();

        paymentRepository.delete(payment);


        // Update invoice after payment deletion
        updateInvoicePaymentSummary(invoice);
    }


    // =========================================================
    // REFUND PAYMENT
    // =========================================================

    @Override
    public PaymentDTO refundPayment(Long id) {

        if (id == null) {
            throw new RuntimeException(
                    "Payment ID is required"
            );
        }

        Payment payment =
                paymentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found"
                                )
                        );


        if (payment.getPaymentStatus()
                != PaymentStatus.PAID) {

            throw new RuntimeException(
                    "Only paid payments can be refunded"
            );
        }


        payment.setPaymentStatus(
                PaymentStatus.REFUNDED
        );


        Payment savedPayment =
                paymentRepository.save(payment);


        // Update invoice after refund
        updateInvoicePaymentSummary(
                payment.getInvoice()
        );


        return convertToDTO(savedPayment);
    }


    // =========================================================
    // GET PAYMENTS BY DATE RANGE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTO> getPaymentsByDateRange(
            LocalDate from,
            LocalDate to
    ) {

        if (from == null || to == null) {

            throw new RuntimeException(
                    "From date and to date are required"
            );
        }

        if (from.isAfter(to)) {

            throw new RuntimeException(
                    "From date cannot be after to date"
            );
        }

        return paymentRepository
                .findByPaymentDateBetween(from, to)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // =========================================================
    // GET PAYMENTS BY INVOICE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTO> getPaymentsByInvoice(
            Long invoiceId
    ) {

        if (invoiceId == null) {

            throw new RuntimeException(
                    "Invoice ID is required"
            );
        }

        return paymentRepository
                .findByInvoiceInvoiceId(invoiceId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // =========================================================
    // GET PAYMENTS BY STATUS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTO> getPaymentsByStatus(
            String status
    ) {

        if (status == null || status.trim().isEmpty()) {

            throw new RuntimeException(
                    "Payment status is required"
            );
        }

        PaymentStatus paymentStatus;

        try {

            paymentStatus =
                    PaymentStatus.valueOf(
                            status.trim().toUpperCase()
                    );

        } catch (IllegalArgumentException e) {

            throw new RuntimeException(
                    "Invalid payment status"
            );
        }


        return paymentRepository
                .findByPaymentStatus(paymentStatus)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // =========================================================
    // GET PAYMENTS BY METHOD
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTO> getPaymentsByMethod(
            String method
    ) {

        if (method == null || method.trim().isEmpty()) {

            throw new RuntimeException(
                    "Payment method is required"
            );
        }

        PaymentMethod paymentMethod;

        try {

            paymentMethod =
                    PaymentMethod.valueOf(
                            method.trim().toUpperCase()
                    );

        } catch (IllegalArgumentException e) {

            throw new RuntimeException(
                    "Invalid payment method"
            );
        }


        return paymentRepository
                .findByPaymentMethod(paymentMethod)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // =========================================================
    // UPDATE INVOICE PAYMENT SUMMARY
    // =========================================================

    private void updateInvoicePaymentSummary(
            Invoice invoice
    ) {

        if (invoice == null) {
            return;
        }


        List<Payment> payments =
                paymentRepository
                        .findByInvoiceInvoiceId(
                                invoice.getInvoiceId()
                        );


        double totalPaid = 0.0;


        for (Payment payment : payments) {

            if (payment.getPaymentStatus()
                    == PaymentStatus.PAID) {

                if (payment.getAmount() != null) {

                    totalPaid +=
                            payment.getAmount()
                                    .doubleValue();
                }
            }
        }


        double totalAmount =
                invoice.getTotalAmount() != null
                        ? invoice.getTotalAmount()
                        : 0.0;


        double balance =
                totalAmount - totalPaid;


        if (balance < 0) {
            balance = 0;
        }


        invoice.setAmountPaid(
                totalPaid
        );

        invoice.setBalanceDue(
                balance
        );


        // =====================================================
        // UPDATE INVOICE STATUS
        // =====================================================

        if (invoice.getStatus() != InvoiceStatus.DRAFT) {

            if (totalPaid <= 0) {

                if (invoice.getDueDate() != null
                        && invoice.getDueDate()
                        .isBefore(LocalDate.now())) {

                    invoice.setStatus(
                            InvoiceStatus.OVERDUE
                    );

                } else {

                    invoice.setStatus(
                            InvoiceStatus.UNPAID
                    );
                }

            } else if (totalPaid < totalAmount) {

                invoice.setStatus(
                        InvoiceStatus.PARTIAL
                );

            } else {

                invoice.setStatus(
                        InvoiceStatus.PAID
                );
            }
        }


        invoiceRepository.save(invoice);
    }


    // =========================================================
    // CONVERT PAYMENT ENTITY → DTO
    // =========================================================

    private PaymentDTO convertToDTO(
            Payment payment
    ) {

        PaymentDTO dto =
                new PaymentDTO();


        // =====================================================
        // PAYMENT
        // =====================================================

        dto.setPaymentId(
                payment.getPaymentId()
        );

        dto.setAmount(
                payment.getAmount()
        );

        dto.setPaymentMethod(
                payment.getPaymentMethod()
        );

        dto.setPaymentStatus(
                payment.getPaymentStatus()
        );

        dto.setPaymentDate(
                payment.getPaymentDate()
        );

        dto.setTransactionReference(
                payment.getTransactionReference()
        );

        dto.setNotes(
                payment.getNotes()
        );


        // =====================================================
        // INVOICE
        // =====================================================

        Invoice invoice =
                payment.getInvoice();

        if (invoice == null) {
            return dto;
        }


        dto.setInvoiceId(
                invoice.getInvoiceId()
        );

        dto.setInvoiceNumber(
                invoice.getInvoiceNumber()
        );


        // =====================================================
        // APPOINTMENT
        // =====================================================

        Appointment appointment =
                invoice.getAppointment();

        if (appointment == null) {
            return dto;
        }


        dto.setAppointmentId(
                appointment.getAppointmentId()
        );

        dto.setAppointmentDate(
                appointment.getAppointmentDate()
        );

        dto.setAppointmentTime(
                appointment.getStartTime() != null
                        ? appointment.getStartTime().toString()
                        : null
        );


        // =====================================================
        // CUSTOMER
        // =====================================================

        Customer customer =
                appointment.getCustomer();

        if (customer != null) {

            dto.setCustomerId(
                    customer.getCustomerId()
            );

            dto.setCustomerName(
                    customer.getCustomerName()
            );

            dto.setCustomerPhone(
                    customer.getCustomerPhone()
            );
        }


        // =====================================================
        // SERVICE
        // =====================================================

        if (appointment.getService() != null) {

            dto.setServiceId(
                    appointment.getService()
                            .getServiceId()
            );

            dto.setServiceName(
                    appointment.getService()
                            .getServiceName()
            );

            dto.setServicePrice(
                    appointment.getService()
                            .getPrice()
            );
        }


        // =====================================================
        // STAFF
        // =====================================================

        if (appointment.getStaff() != null) {

            dto.setStaffId(
                    appointment.getStaff()
                            .getStaffId()
            );

            dto.setStaffName(
                    appointment.getStaff()
                            .getStaffName()
            );
        }


        return dto;
    }
}