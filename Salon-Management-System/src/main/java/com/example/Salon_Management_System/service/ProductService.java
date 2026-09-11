package com.example.Salon_Management_System.service;

import com.example.Salon_Management_System.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    ProductDTO saveProduct(ProductDTO dto);

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(Long productId);

    ProductDTO updateProduct(Long productId, ProductDTO dto);

    void deleteProduct(Long productId);

    List<ProductDTO> searchProducts(String keyword);

    ProductDTO updateStock(Long productId, String adjustment, Integer quantity);

    ProductDTO updateStatus(Long productId, String status);

    long getTotalProducts();

    long getLowStockCount();

    long getOutOfStockCount();

    Double getInventoryValue();

    // ==========================================
    // LOW STOCK PRODUCTS
    // ==========================================
    List<ProductDTO> getLowStockProducts();

    // ==========================================
    // OUT OF STOCK PRODUCTS
    // ==========================================
    List<ProductDTO> getOutOfStockProducts();
}