package com.example.Salon_Management_System.service.impl;

import com.example.Salon_Management_System.dto.PaymentDTO;
import com.example.Salon_Management_System.entity.Appointment;
import com.example.Salon_Management_System.entity.Payment;
import com.example.Salon_Management_System.enumiration.PaymentStatus;
import com.example.Salon_Management_System.repository.AppointmentRepository;
import com.example.Salon_Management_System.repository.PaymentRepository;
import com.example.Salon_Management_System.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public PaymentDTO createPayment(PaymentDTO dto) {

        if (dto.getAppointmentId() == null) {
            throw new RuntimeException("Appointment is required");
        }

        Appointment appointment = appointmentRepository
                .findById(dto.getAppointmentId())
                .orElseThrow(() ->
                        new RuntimeException("Appointment not found")
                );

        if (paymentRepository.existsByAppointmentAppointmentId(
                dto.getAppointmentId())) {

            throw new RuntimeException(
                    "A payment already exists for this appointment"
            );
        }

        if (dto.getAmount() == null ||
                dto.getAmount().signum() < 0) {

            throw new RuntimeException("Invalid payment amount");
        }

        Payment payment = new Payment();

        payment.setAppointment(appointment);
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());

        if (dto.getPaymentStatus() == null) {
            payment.setPaymentStatus(PaymentStatus.PENDING);
        } else {
            payment.setPaymentStatus(dto.getPaymentStatus());
        }

        payment.setPaymentDate(
                dto.getPaymentDate() != null
                        ? dto.getPaymentDate()
                        : LocalDate.now()
        );

        payment.setTransactionReference(
                dto.getTransactionReference()
        );

        payment.setNotes(dto.getNotes());

        Payment saved = paymentRepository.save(payment);

        /*
         * Keep Appointment.paymentStatus synchronized.
         */
        appointment.setPaymentStatus(
                payment.getPaymentStatus()
        );

        appointmentRepository.save(appointment);

        return convertToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDTO getPaymentById(Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found")
                );

        return convertToDTO(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTO> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDTO updatePayment(
            Long id,
            PaymentDTO dto
    ) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found")
                );

        if (dto.getAmount() != null) {
            if (dto.getAmount().signum() < 0) {
                throw new RuntimeException(
                        "Invalid payment amount"
                );
            }

            payment.setAmount(dto.getAmount());
        }

        if (dto.getPaymentMethod() != null) {
            payment.setPaymentMethod(
                    dto.getPaymentMethod()
            );
        }

        if (dto.getPaymentStatus() != null) {
            payment.setPaymentStatus(
                    dto.getPaymentStatus()
            );

            payment.getAppointment().setPaymentStatus(
                    dto.getPaymentStatus()
            );

            appointmentRepository.save(
                    payment.getAppointment()
            );
        }

        if (dto.getPaymentDate() != null) {
            payment.setPaymentDate(
                    dto.getPaymentDate()
            );
        }

        payment.setTransactionReference(
                dto.getTransactionReference()
        );

        payment.setNotes(dto.getNotes());

        Payment updated = paymentRepository.save(payment);

        return convertToDTO(updated);
    }

    @Override
    public void deletePayment(Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found")
                );

        Appointment appointment = payment.getAppointment();

        /*
         * When payment is deleted, appointment becomes pending.
         */
        if (appointment != null) {
            appointment.setPaymentStatus(
                    PaymentStatus.PENDING
            );

            appointmentRepository.save(appointment);
        }

        paymentRepository.delete(payment);
    }

    @Override
    public PaymentDTO refundPayment(Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found")
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

        payment.getAppointment().setPaymentStatus(
                PaymentStatus.REFUNDED
        );

        appointmentRepository.save(
                payment.getAppointment()
        );

        Payment saved = paymentRepository.save(payment);

        return convertToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTO> getPaymentsByDateRange(
            LocalDate from,
            LocalDate to
    ) {

        return paymentRepository
                .findByPaymentDateBetween(from, to)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PaymentDTO convertToDTO(Payment payment) {

        Appointment appointment = payment.getAppointment();

        PaymentDTO dto = new PaymentDTO();

        dto.setPaymentId(payment.getPaymentId());

        dto.setAppointmentId(
                appointment.getAppointmentId()
        );

        if (appointment.getCustomer() != null) {

            dto.setCustomerId(
                    appointment.getCustomer().getCustomerId()
            );

            dto.setCustomerName(
                    appointment.getCustomer().getCustomerName()
            );

            dto.setCustomerPhone(
                    appointment.getCustomer().getCustomerPhone()
            );
        }

        if (appointment.getService() != null) {

            dto.setServiceId(
                    appointment.getService().getServiceId()
            );

            dto.setServiceName(
                    appointment.getService().getServiceName()
            );

            dto.setServicePrice(
                    appointment.getService().getPrice()
            );
        }

        if (appointment.getStaff() != null) {

            dto.setStaffId(
                    appointment.getStaff().getStaffId()
            );

            dto.setStaffName(
                    appointment.getStaff().getStaffName()
            );
        }

        dto.setAppointmentDate(
                appointment.getAppointmentDate()
        );

        dto.setAppointmentTime(
                appointment.getStartTime() != null
                        ? appointment.getStartTime().toString()
                        : null
        );

        dto.setAmount(payment.getAmount());

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

        dto.setNotes(payment.getNotes());

        return dto;
    }
}