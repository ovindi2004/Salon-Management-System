package com.example.Salon_Management_System.service;

import com.example.Salon_Management_System.dto.ProductDTO;
import com.example.Salon_Management_System.entity.Product;
import com.example.Salon_Management_System.enumiration.ProductStatus;
import com.example.Salon_Management_System.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    // ==========================================
    // SAVE PRODUCT
    // ==========================================
    @Override
    public ProductDTO saveProduct(ProductDTO dto) {

        Product product = new Product();

        product.setProductName(dto.getProductName());

        // AUTO GENERATE SKU
        product.setSku(generateNextSku());

        product.setCategory(dto.getCategory());
        product.setBrand(dto.getBrand());
        product.setCostPrice(dto.getCostPrice());
        product.setSellingPrice(dto.getSellingPrice());

        product.setStockQuantity(
                dto.getStockQuantity() == null
                        ? 0
                        : dto.getStockQuantity()
        );

        product.setReorderLevel(
                dto.getReorderLevel() == null
                        ? 10
                        : dto.getReorderLevel()
        );

        product.setSupplier(dto.getSupplier());

        product.setStatus(
                dto.getStatus() == null
                        ? ProductStatus.ACTIVE
                        : dto.getStatus()
        );

        product.setDescription(dto.getDescription());
        product.setImage(dto.getImage());

        Product savedProduct =
                productRepository.save(product);

        return convertToDTO(savedProduct);
    }


    // ==========================================
    // GET ALL PRODUCTS
    // ==========================================
    @Override
    public List<ProductDTO> getAllProducts() {

        return productRepository
                .findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // ==========================================
    // GET PRODUCT BY ID
    // ==========================================
    @Override
    public ProductDTO getProductById(Long productId) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found with ID: "
                                                + productId
                                )
                        );

        return convertToDTO(product);
    }


    // ==========================================
    // UPDATE PRODUCT
    // ==========================================
    @Override
    public ProductDTO updateProduct(
            Long productId,
            ProductDTO dto
    ) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found with ID: "
                                                + productId
                                )
                        );

        product.setProductName(dto.getProductName());

        // SKU is NOT changed during update
        // Existing SKU remains unchanged

        product.setCategory(dto.getCategory());
        product.setBrand(dto.getBrand());
        product.setCostPrice(dto.getCostPrice());
        product.setSellingPrice(dto.getSellingPrice());

        if (dto.getStockQuantity() != null) {
            product.setStockQuantity(
                    dto.getStockQuantity()
            );
        }

        if (dto.getReorderLevel() != null) {
            product.setReorderLevel(
                    dto.getReorderLevel()
            );
        }

        product.setSupplier(dto.getSupplier());

        if (dto.getStatus() != null) {
            product.setStatus(dto.getStatus());
        }

        product.setDescription(dto.getDescription());
        product.setImage(dto.getImage());

        Product updatedProduct =
                productRepository.save(product);

        return convertToDTO(updatedProduct);
    }


    // ==========================================
    // DELETE PRODUCT
    // ==========================================
    @Override
    public void deleteProduct(Long productId) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found with ID: "
                                                + productId
                                )
                        );

        productRepository.delete(product);
    }


    // ==========================================
    // SEARCH PRODUCTS
    // ==========================================
    @Override
    public List<ProductDTO> searchProducts(
            String keyword
    ) {

        if (keyword == null ||
                keyword.trim().isEmpty()) {

            return getAllProducts();
        }

        return productRepository
                .searchProducts(keyword.trim())
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // ==========================================
    // UPDATE STOCK
    //
    // adjustment = add / remove / set
    // ==========================================
    @Override
    public ProductDTO updateStock(
            Long productId,
            String adjustment,
            Integer quantity
    ) {

        if (quantity == null || quantity < 0) {

            throw new RuntimeException(
                    "Quantity must be zero or greater"
            );
        }

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found with ID: "
                                                + productId
                                )
                        );

        int currentStock =
                product.getStockQuantity();

        switch (adjustment.toLowerCase()) {

            case "add" -> {

                product.setStockQuantity(
                        currentStock + quantity
                );
            }

            case "remove" -> {

                if (quantity > currentStock) {

                    throw new RuntimeException(
                            "Cannot remove more than current stock"
                    );
                }

                product.setStockQuantity(
                        currentStock - quantity
                );
            }

            case "set" -> {

                product.setStockQuantity(
                        quantity
                );
            }

            default -> throw new RuntimeException(
                    "Invalid adjustment type"
            );
        }

        Product updatedProduct =
                productRepository.save(product);

        return convertToDTO(updatedProduct);
    }


    // ==========================================
    // UPDATE PRODUCT STATUS
    //
    // status = ACTIVE / INACTIVE
    // ==========================================
    @Override
    public ProductDTO updateStatus(
            Long productId,
            String status
    ) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found with ID: "
                                                + productId
                                )
                        );

        if (status == null ||
                status.trim().isEmpty()) {

            throw new RuntimeException(
                    "Status is required"
            );
        }

        try {

            ProductStatus productStatus =
                    ProductStatus.valueOf(
                            status.trim().toUpperCase()
                    );

            product.setStatus(productStatus);

            Product updatedProduct =
                    productRepository.save(product);

            return convertToDTO(updatedProduct);

        } catch (IllegalArgumentException e) {

            throw new RuntimeException(
                    "Invalid status. Use ACTIVE or INACTIVE"
            );
        }
    }


    // ==========================================
    // TOTAL PRODUCT COUNT
    // ==========================================
    @Override
    public long getTotalProducts() {

        return productRepository.count();
    }


    // ==========================================
    // LOW STOCK COUNT
    // ==========================================
    @Override
    public long getLowStockCount() {

        return productRepository
                .findLowStockProducts()
                .size();
    }


    // ==========================================
    // OUT OF STOCK COUNT
    // ==========================================
    @Override
    public long getOutOfStockCount() {

        return productRepository
                .findOutOfStockProducts()
                .size();
    }


    // ==========================================
    // INVENTORY VALUE
    // ==========================================
    @Override
    public Double getInventoryValue() {

        Double value =
                productRepository.getInventoryValue();

        return value == null
                ? 0.0
                : value;
    }


    // ==========================================
    // GET LOW STOCK PRODUCTS
    //
    // stock > 0
    // stock <= reorder level
    // ==========================================
    @Override
    public List<ProductDTO> getLowStockProducts() {

        return productRepository
                .findLowStockProducts()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // ==========================================
    // GET OUT OF STOCK PRODUCTS
    //
    // stock = 0
    // ==========================================
    @Override
    public List<ProductDTO> getOutOfStockProducts() {

        return productRepository
                .findOutOfStockProducts()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // ==========================================
    // GENERATE NEXT SKU
    // ==========================================
    private String generateNextSku() {

        String prefix = "PBS-PRD-";

        List<Product> products =
                productRepository.findAll();

        int maxNumber = 0;

        for (Product product : products) {

            String sku = product.getSku();

            if (sku == null ||
                    !sku.startsWith(prefix)) {

                continue;
            }

            try {

                String numberPart =
                        sku.substring(prefix.length());

                int number =
                        Integer.parseInt(numberPart);

                if (number > maxNumber) {
                    maxNumber = number;
                }

            } catch (NumberFormatException ignored) {

                // Ignore invalid SKU format
            }
        }

        int nextNumber =
                maxNumber + 1;

        return String.format(
                "%s%03d",
                prefix,
                nextNumber
        );
    }



    // ==========================================
    // CONVERT ENTITY → DTO
    // ==========================================
    private ProductDTO convertToDTO(
            Product product
    ) {

        ProductDTO dto =
                new ProductDTO();

        dto.setProductId(
                product.getProductId()
        );

        dto.setProductName(
                product.getProductName()
        );

        dto.setSku(
                product.getSku()
        );

        dto.setCategory(
                product.getCategory()
        );

        dto.setBrand(
                product.getBrand()
        );

        dto.setCostPrice(
                product.getCostPrice()
        );

        dto.setSellingPrice(
                product.getSellingPrice()
        );

        dto.setStockQuantity(
                product.getStockQuantity()
        );

        dto.setReorderLevel(
                product.getReorderLevel()
        );

        dto.setSupplier(
                product.getSupplier()
        );

        dto.setStatus(
                product.getStatus()
        );

        dto.setDescription(
                product.getDescription()
        );

        dto.setImage(
                product.getImage()
        );

        return dto;
    }
}