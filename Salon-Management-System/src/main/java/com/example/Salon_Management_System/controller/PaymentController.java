package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.PaymentDTO;
import com.example.Salon_Management_System.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@CrossOrigin
public class PaymentController {

    private final PaymentService paymentService;


    // =========================================================
    // SAVE PAYMENT
    // =========================================================

    @PostMapping("/save")
    public ResponseEntity<PaymentDTO> savePayment(
            @RequestBody PaymentDTO dto
    ) {

        return new ResponseEntity<>(
                paymentService.createPayment(dto),
                HttpStatus.CREATED
        );
    }


    // =========================================================
    // GET ALL PAYMENTS
    // =========================================================

    @GetMapping("/all")
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {

        return ResponseEntity.ok(
                paymentService.getAllPayments()
        );
    }


    // =========================================================
    // GET PAYMENT BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(id)
        );
    }


    // =========================================================
    // UPDATE PAYMENT
    // =========================================================

    @PutMapping("/update/{id}")
    public ResponseEntity<PaymentDTO> updatePayment(
            @PathVariable Long id,
            @RequestBody PaymentDTO dto
    ) {

        return ResponseEntity.ok(
                paymentService.updatePayment(
                        id,
                        dto
                )
        );
    }


    // =========================================================
    // DELETE PAYMENT
    // =========================================================

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePayment(
            @PathVariable Long id
    ) {

        paymentService.deletePayment(id);

        return ResponseEntity.ok(
                "Payment deleted successfully"
        );
    }


    // =========================================================
    // REFUND PAYMENT
    // =========================================================

    @PatchMapping("/refund/{id}")
    public ResponseEntity<PaymentDTO> refundPayment(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                paymentService.refundPayment(id)
        );
    }


    // =========================================================
    // GET PAYMENTS BY DATE RANGE
    // =========================================================

    @GetMapping("/date-range")
    public ResponseEntity<List<PaymentDTO>> getByDateRange(
            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate from,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate to
    ) {

        return ResponseEntity.ok(
                paymentService.getPaymentsByDateRange(
                        from,
                        to
                )
        );
    }


    // =========================================================
    // GET PAYMENTS BY INVOICE
    // =========================================================

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByInvoice(
            @PathVariable Long invoiceId
    ) {

        return ResponseEntity.ok(
                paymentService.getPaymentsByInvoice(
                        invoiceId
                )
        );
    }


    // =========================================================
    // GET PAYMENTS BY STATUS
    // =========================================================

    @GetMapping("/status")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByStatus(
            @RequestParam String status
    ) {

        return ResponseEntity.ok(
                paymentService.getPaymentsByStatus(
                        status
                )
        );
    }


    // =========================================================
    // GET PAYMENTS BY METHOD
    // =========================================================

    @GetMapping("/method")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByMethod(
            @RequestParam String method
    ) {

        return ResponseEntity.ok(
                paymentService.getPaymentsByMethod(
                        method
                )
        );
    }
}