package com.example.Salon_Management_System.repository;

import com.example.Salon_Management_System.entity.Product;
import com.example.Salon_Management_System.enumiration.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    List<Product> findByStatus(ProductStatus status);

    @Query("""
            SELECT p
            FROM Product p
            WHERE LOWER(p.productName)
                  LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(p.category)
                  LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(p.sku)
                  LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(COALESCE(p.brand, ''))
                  LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(COALESCE(p.supplier, ''))
                  LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<Product> searchProducts(@Param("keyword") String keyword);

    @Query(value = """
    SELECT *
    FROM product
    WHERE stock_quantity = 0
    """, nativeQuery = true)
    List<Product> findOutOfStockProducts();


    // ==========================================
    // GET LOW STOCK PRODUCTS
    //
    // stockQuantity > 0
    // stockQuantity <= reorderLevel
    // ==========================================
    @Query(value = """
        SELECT *
        FROM product
        WHERE stock_quantity > 0
          AND stock_quantity <= reorder_level
        """, nativeQuery = true)
    List<Product> findLowStockProducts();


    // ==========================================
    // GET TOTAL INVENTORY VALUE
    //
    // costPrice × stockQuantity
    // ==========================================
    @Query("""
            SELECT COALESCE(
                SUM(p.costPrice * p.stockQuantity),
                0
            )
            FROM Product p
            """)
    Double getInventoryValue();
}