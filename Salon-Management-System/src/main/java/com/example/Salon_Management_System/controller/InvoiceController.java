package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.CommonResponse;
import com.example.Salon_Management_System.dto.InvoiceDTO;
import com.example.Salon_Management_System.enumiration.InvoiceStatus;
import com.example.Salon_Management_System.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/invoices")
@CrossOrigin
public class InvoiceController {

    private final InvoiceService invoiceService;


    // =========================================================
    // SAVE INVOICE
    // =========================================================

    @PostMapping(
            value = "/save",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse saveInvoice(
            @RequestBody InvoiceDTO invoiceDTO
    ) {

        InvoiceDTO response =
                invoiceService.saveInvoice(invoiceDTO);

        return new CommonResponse(
                0,
                response,
                "Invoice created successfully"
        );
    }


    // =========================================================
    // GET ALL INVOICES
    // =========================================================

    @GetMapping(
            value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse getAllInvoices() {

        List<InvoiceDTO> invoiceList =
                invoiceService.getAllInvoices();

        return new CommonResponse(
                0,
                invoiceList,
                "All invoices successfully retrieved"
        );
    }


    // =========================================================
    // GET INVOICE BY ID
    // =========================================================

    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse getInvoiceById(
            @PathVariable Long id
    ) {

        InvoiceDTO invoice =
                invoiceService.getInvoiceById(id);

        return new CommonResponse(
                0,
                invoice,
                "Invoice retrieved successfully"
        );
    }


    // =========================================================
    // UPDATE INVOICE
    // =========================================================

    @PutMapping(
            value = "/update/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse updateInvoice(
            @PathVariable Long id,
            @RequestBody InvoiceDTO invoiceDTO
    ) {

        InvoiceDTO response =
                invoiceService.updateInvoice(
                        id,
                        invoiceDTO
                );

        return new CommonResponse(
                0,
                response,
                "Invoice updated successfully"
        );
    }


    // =========================================================
    // DELETE INVOICE
    // =========================================================

    @DeleteMapping(
            value = "/delete/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse deleteInvoice(
            @PathVariable Long id
    ) {

        invoiceService.deleteInvoice(id);

        return new CommonResponse(
                0,
                "Invoice deleted successfully"
        );
    }


    // =========================================================
    // SEARCH INVOICES
    // =========================================================

    @GetMapping(
            value = "/search",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse searchInvoices(
            @RequestParam String keyword
    ) {

        List<InvoiceDTO> invoiceList =
                invoiceService.searchInvoices(keyword);

        return new CommonResponse(
                0,
                invoiceList,
                "Invoice search successfully"
        );
    }


    // =========================================================
    // GET INVOICES BY STATUS
    // =========================================================

    @GetMapping(
            value = "/status",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse getInvoicesByStatus(
            @RequestParam InvoiceStatus status
    ) {

        List<InvoiceDTO> invoiceList =
                invoiceService.getInvoicesByStatus(status);

        return new CommonResponse(
                0,
                invoiceList,
                "Invoices filtered by status successfully"
        );
    }


    // =========================================================
    // GET INVOICES BY DATE RANGE
    // =========================================================

    @GetMapping(
            value = "/date-range",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse getInvoicesByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {

        List<InvoiceDTO> invoiceList =
                invoiceService.getInvoicesByDateRange(
                        startDate,
                        endDate
                );

        return new CommonResponse(
                0,
                invoiceList,
                "Invoices retrieved by date range successfully"
        );
    }


    // =========================================================
    // INVOICE STATISTICS
    // =========================================================

    @GetMapping(
            value = "/stats",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse getInvoiceStats() {

        Map<String, Object> stats =
                invoiceService.getInvoiceStats();

        return new CommonResponse(
                0,
                stats,
                "Invoice statistics retrieved successfully"
        );
    }
}