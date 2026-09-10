package com.example.Salon_Management_System.dto;

import com.example.Salon_Management_System.enumiration.InvoiceItemType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceItemDTO {

    private Long invoiceItemId;

    private Long serviceId;
    private Long productId;

    private InvoiceItemType itemType;

    private String itemName;

    private Integer quantity;

    private Double unitPrice;

    private Double subtotal;
}