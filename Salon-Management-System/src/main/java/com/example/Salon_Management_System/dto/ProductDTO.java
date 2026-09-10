package com.example.Salon_Management_System.dto;

import com.example.Salon_Management_System.enumiration.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long productId;
    private String productName;
    private String sku;
    private String category;
    private String brand;
    private Double costPrice;
    private Double sellingPrice;
    private Integer stockQuantity;
    private Integer reorderLevel;
    private String supplier;
    private ProductStatus status;
    private String description;
    private String image;
}