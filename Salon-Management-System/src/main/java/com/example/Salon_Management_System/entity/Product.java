package com.example.Salon_Management_System.entity;
import com.example.Salon_Management_System.enumiration.ProductStatus;
import jakarta.persistence.*;
        import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "product",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_product_sku", columnNames = "sku")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false, length = 150)
    private String productName;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(nullable = false, length = 80)
    private String category;

    @Column(length = 100)
    private String brand;

    @Column(nullable = false)
    private Double costPrice;

    @Column(nullable = false)
    private Double sellingPrice;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false)
    private Integer reorderLevel = 10;

    @Column(length = 150)
    private String supplier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status = ProductStatus.ACTIVE;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "LONGTEXT")
    private String image;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == null) {
            status = ProductStatus.ACTIVE;
        }

        if (reorderLevel == null) {
            reorderLevel = 10;
        }

        if (stockQuantity == null) {
            stockQuantity = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "product")
    private List<InvoiceItem> invoiceItems = new ArrayList<>();


}