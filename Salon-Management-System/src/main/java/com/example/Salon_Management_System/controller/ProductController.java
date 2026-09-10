package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.ProductDTO;
import com.example.Salon_Management_System.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.PATCH,
                RequestMethod.DELETE,
                RequestMethod.OPTIONS
        }
)
public class ProductController {

    private final ProductService productService;


    // ==========================================
    // SAVE PRODUCT
    // POST /api/v1/product/save
    // ==========================================
    @PostMapping("/save")
    public ResponseEntity<?> saveProduct(
            @RequestBody ProductDTO dto
    ) {

        try {

            ProductDTO savedProduct =
                    productService.saveProduct(dto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(savedProduct);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(errorResponse(e.getMessage()));
        }
    }


    // ==========================================
    // GET ALL PRODUCTS
    // GET /api/v1/product/all
    // ==========================================
    @GetMapping("/all")
    public ResponseEntity<?> getAllProducts() {

        try {

            List<ProductDTO> products =
                    productService.getAllProducts();

            return ResponseEntity.ok(products);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(errorResponse(e.getMessage()));
        }
    }


    // ==========================================
    // GET PRODUCT BY ID
    // GET /api/v1/product/{productId}
    // ==========================================
    @GetMapping("/{productId:\\d+}")
    public ResponseEntity<?> getProductById(
            @PathVariable Long productId
    ) {

        try {

            ProductDTO product =
                    productService.getProductById(productId);

            return ResponseEntity.ok(product);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(errorResponse(e.getMessage()));
        }
    }


    // ==========================================
    // SEARCH PRODUCTS
    // GET /api/v1/product/search?keyword=shampoo
    // ==========================================
    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(
            @RequestParam String keyword
    ) {

        try {

            List<ProductDTO> products =
                    productService.searchProducts(keyword);

            return ResponseEntity.ok(products);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(errorResponse(e.getMessage()));
        }
    }


    // ==========================================
    // UPDATE PRODUCT
    // PUT /api/v1/product/update/{productId}
    // ==========================================
    @PutMapping("/update/{productId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductDTO dto
    ) {

        try {

            ProductDTO updatedProduct =
                    productService.updateProduct(
                            productId,
                            dto
                    );

            return ResponseEntity.ok(updatedProduct);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(errorResponse(e.getMessage()));
        }
    }


    // ==========================================
    // DELETE PRODUCT
    // DELETE /api/v1/product/delete/{productId}
    // ==========================================
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long productId
    ) {

        try {

            productService.deleteProduct(productId);

            Map<String, Object> response =
                    new HashMap<>();

            response.put("status", 1);
            response.put(
                    "message",
                    "Product deleted successfully"
            );
            response.put(
                    "productId",
                    productId
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(errorResponse(e.getMessage()));
        }
    }


    // ==========================================
    // UPDATE STOCK
    // PATCH /api/v1/product/{productId}/stock
    //
    // adjustment = add / remove / set
    // quantity = number
    // ==========================================
    @PatchMapping("/{productId}/stock")
    public ResponseEntity<?> updateStock(
            @PathVariable Long productId,
            @RequestParam String adjustment,
            @RequestParam Integer quantity
    ) {

        try {

            ProductDTO updatedProduct =
                    productService.updateStock(
                            productId,
                            adjustment,
                            quantity
                    );

            return ResponseEntity.ok(updatedProduct);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(errorResponse(e.getMessage()));
        }
    }


    // ==========================================
    // UPDATE STATUS
    // PATCH /api/v1/product/{productId}/status
    //
    // status = ACTIVE / INACTIVE
    // ==========================================
    @PatchMapping("/{productId}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long productId,
            @RequestParam String status
    ) {

        try {

            ProductDTO updatedProduct =
                    productService.updateStatus(
                            productId,
                            status
                    );

            return ResponseEntity.ok(updatedProduct);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(errorResponse(e.getMessage()));
        }
    }


    // ==========================================
    // PRODUCT STATISTICS
    // GET /api/v1/product/stats
    // ==========================================
    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {

        try {

            Map<String, Object> stats =
                    new HashMap<>();

            stats.put(
                    "totalProducts",
                    productService.getTotalProducts()
            );

            stats.put(
                    "lowStock",
                    productService.getLowStockCount()
            );

            stats.put(
                    "outOfStock",
                    productService.getOutOfStockCount()
            );

            stats.put(
                    "inventoryValue",
                    productService.getInventoryValue()
            );

            return ResponseEntity.ok(stats);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(errorResponse(e.getMessage()));
        }
    }


    // ==========================================
    // ERROR RESPONSE
    // ==========================================
    private Map<String, Object> errorResponse(
            String message
    ) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "status",
                0
        );

        response.put(
                "message",
                message == null
                        ? "Something went wrong"
                        : message
        );

        return response;
    }

    // ==========================================
// GET LOW STOCK PRODUCTS
// GET /api/v1/product/low-stock
// ==========================================
    @GetMapping("/low-stock")
    public ResponseEntity<?> getLowStockProducts() {

        try {

            List<ProductDTO> products =
                    productService.getLowStockProducts();

            return ResponseEntity.ok(products);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(errorResponse(e.getMessage()));
        }
    }


    // ==========================================
// GET OUT OF STOCK PRODUCTS
// GET /api/v1/product/out-of-stock
// ==========================================
    @GetMapping("/out-of-stock")
    public ResponseEntity<?> getOutOfStockProducts() {

        try {

            List<ProductDTO> products =
                    productService.getOutOfStockProducts();

            return ResponseEntity.ok(products);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(errorResponse(e.getMessage()));
        }
    }
}