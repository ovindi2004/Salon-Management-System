package com.example.Salon_Management_System.repository;

import com.example.Salon_Management_System.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

    List<InvoiceItem> findByInvoiceInvoiceId(Long invoiceId);

    void deleteByInvoiceInvoiceId(Long invoiceId);
}