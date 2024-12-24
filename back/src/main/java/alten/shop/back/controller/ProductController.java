package alten.shop.back.controller;

import alten.shop.back.dto.ProductDTO;
import alten.shop.back.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static alten.shop.back.util.Constantes.*;


@SecurityRequirement(name = BEARER_AUTHENTICATION)
@RestController
@RequestMapping("api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO, HttpServletRequest request) {
        productService.validateAdmin(request);
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO updatedProductDTO, HttpServletRequest request) {
        productService.validateAdmin(request);
        ProductDTO product = productService.updateProduct(id, updatedProductDTO);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id, HttpServletRequest request) {
        productService.validateAdmin(request);
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product with ID " + id + " has been successfully deleted.");
    }
}

