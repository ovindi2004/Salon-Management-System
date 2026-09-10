package com.example.Salon_Management_System.entity;

import com.example.Salon_Management_System.enumiration.InvoiceItemType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoice_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private SalonService service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InvoiceItemType itemType;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(nullable = false)
    private Double unitPrice = 0.0;

    @Column(nullable = false)
    private Double subtotal = 0.0;
}