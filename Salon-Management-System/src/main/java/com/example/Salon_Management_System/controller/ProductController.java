package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.CommonResponse;
import com.example.Salon_Management_System.dto.ProductDTO;
import com.example.Salon_Management_System.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveProduct(@RequestBody ProductDTO dto) {
        ProductDTO savedProduct = productService.saveProduct(dto);
        return new CommonResponse(0, savedProduct, "Product saved successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return new CommonResponse(0, products, "All products retrieved successfully");
    }

    @GetMapping(value = "/{productId:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getProductById(@PathVariable Long productId) {
        ProductDTO product = productService.getProductById(productId);
        return new CommonResponse(0, product, "Product retrieved successfully");
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse searchProducts(@RequestParam String keyword) {
        List<ProductDTO> products = productService.searchProducts(keyword);
        return new CommonResponse(0, products, "Product search completed successfully");
    }

    @PutMapping(value = "/update/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateProduct(@PathVariable Long productId, @RequestBody ProductDTO dto) {
        ProductDTO updatedProduct = productService.updateProduct(productId, dto);
        return new CommonResponse(0, updatedProduct, "Product updated successfully");
    }

    @DeleteMapping(value = "/delete/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        Map<String, Object> response = new HashMap<>();
        response.put("productId", productId);
        return new CommonResponse(0, response, "Product deleted successfully");
    }

    @PatchMapping(value = "/{productId}/stock", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateStock(@PathVariable Long productId, @RequestParam String adjustment, @RequestParam Integer quantity) {
        ProductDTO updatedProduct = productService.updateStock(productId, adjustment, quantity);
        return new CommonResponse(0, updatedProduct, "Product stock updated successfully");
    }

    @PatchMapping(value = "/{productId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateStatus(@PathVariable Long productId, @RequestParam String status) {
        ProductDTO updatedProduct = productService.updateStatus(productId, status);

        return new CommonResponse(0, updatedProduct, "Product status updated successfully"
        );
    }

    @GetMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProducts", productService.getTotalProducts());
        stats.put("lowStock", productService.getLowStockCount());
        stats.put("outOfStock", productService.getOutOfStockCount());
        stats.put("inventoryValue", productService.getInventoryValue());
        return new CommonResponse(0, stats, "Product statistics retrieved successfully");
    }
    @GetMapping(value = "/low-stock", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getLowStockProducts() {
        List<ProductDTO> products = productService.getLowStockProducts();
        return new CommonResponse(0, products, "Low stock products retrieved successfully");
    }

    @GetMapping(value = "/out-of-stock", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getOutOfStockProducts() {
        List<ProductDTO> products = productService.getOutOfStockProducts();
        return new CommonResponse(0, products, "Out of stock products retrieved successfully");
    }
}