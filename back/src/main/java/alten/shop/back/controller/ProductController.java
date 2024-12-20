package alten.shop.back.controller;

import alten.shop.back.config.JwtUtil;
import alten.shop.back.entity.Product;
import alten.shop.back.exception.ForbiddenException;
import alten.shop.back.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;
    private final JwtUtil jwtUtil;

    public ProductController(ProductService productService, JwtUtil jwtUtil) {
        this.productService = productService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product, @RequestHeader("Authorization") String token) {
        if (!jwtUtil.isAdmin(token)) {
            throw new ForbiddenException("Only admin can add products");
        }
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct, @RequestHeader("Authorization") String token) {
        if (!jwtUtil.isAdmin(token)) {
            throw new ForbiddenException("Only admin can update products");
        }
        Product product = productService.updateProduct(id, updatedProduct);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        if (!jwtUtil.isAdmin(token)) {
            throw new ForbiddenException("Only admin can delete products");
        }
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}

