package com.example.Salon_Management_System.service;

import com.example.Salon_Management_System.dto.InvoiceDTO;
import com.example.Salon_Management_System.enumiration.InvoiceStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface InvoiceService {

    InvoiceDTO saveInvoice(InvoiceDTO dto);

    List<InvoiceDTO> getAllInvoices();

    InvoiceDTO getInvoiceById(Long invoiceId);

    InvoiceDTO updateInvoice(Long invoiceId, InvoiceDTO dto);

    void deleteInvoice(Long invoiceId);

    List<InvoiceDTO> searchInvoices(String keyword);

    List<InvoiceDTO> getInvoicesByStatus(InvoiceStatus status);

    List<InvoiceDTO> getInvoicesByDateRange(
            LocalDate startDate,
            LocalDate endDate
    );

    Map<String, Object> getInvoiceStats();
}