package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.CommonResponse;
import com.example.Salon_Management_System.dto.PaymentDTO;
import com.example.Salon_Management_System.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse savePayment(@RequestBody PaymentDTO dto) {
        PaymentDTO savedPayment = paymentService.createPayment(dto);
        return new CommonResponse(0, savedPayment, "Payment created successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAllPayments();
        return new CommonResponse(0, payments, "All payments retrieved successfully");
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getPaymentById(@PathVariable Long id) {
        PaymentDTO payment = paymentService.getPaymentById(id);
        return new CommonResponse(0, payment, "Payment retrieved successfully");
    }

    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updatePayment(@PathVariable Long id, @RequestBody PaymentDTO dto) {
        PaymentDTO updatedPayment = paymentService.updatePayment(id, dto);
        return new CommonResponse(0, updatedPayment, "Payment updated successfully");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);return new CommonResponse(0, null, "Payment deleted successfully");
    }

    @PatchMapping(value = "/refund/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse refundPayment(@PathVariable Long id) {
        PaymentDTO refundedPayment = paymentService.refundPayment(id);
        return new CommonResponse(0, refundedPayment, "Payment refunded successfully");
    }

    @GetMapping(value = "/date-range", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getByDateRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        List<PaymentDTO> payments = paymentService.getPaymentsByDateRange(from, to);
        return new CommonResponse(0, payments, "Payments retrieved successfully for the selected date range");
    }

    @GetMapping(value = "/invoice/{invoiceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getPaymentsByInvoice(@PathVariable Long invoiceId) {
        List<PaymentDTO> payments = paymentService.getPaymentsByInvoice(invoiceId);
        return new CommonResponse(0, payments, "Invoice payments retrieved successfully");
    }

    @GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getPaymentsByStatus(@RequestParam String status) {
        List<PaymentDTO> payments = paymentService.getPaymentsByStatus(status);
        return new CommonResponse(0, payments, "Payments retrieved successfully by status");
    }

    @GetMapping(value = "/method", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getPaymentsByMethod(@RequestParam String method) {
        List<PaymentDTO> payments = paymentService.getPaymentsByMethod(method);
        return new CommonResponse(0, payments, "Payments retrieved successfully by method");
    }
}