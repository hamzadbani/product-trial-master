package alten.shop.back.service;

import alten.shop.back.config.JwtUtil;
import alten.shop.back.dto.ProductDTO;
import alten.shop.back.entity.InventoryStatus;
import alten.shop.back.entity.Product;
import alten.shop.back.exception.ForbiddenException;
import alten.shop.back.exception.ResourceNotFoundException;
import alten.shop.back.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static alten.shop.back.util.Constantes.ADMIN_CAN_ADD_PRODUCTS;
import static alten.shop.back.util.Constantes.AUTHORIZATION;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final JwtUtil jwtUtil;

    public ProductService(ProductRepository productRepository, JwtUtil jwtUtil) {
        this.productRepository = productRepository;
        this.jwtUtil = jwtUtil;
    }

//    public List<Product> getAllProducts() {
//        return productRepository.findAll();
//    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToDTO)
                .toList();
    }

//    public Product getProductById(Long id) {
//        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
//    }
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return convertToDTO(product);
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = convertToProductEntity(productDTO);
        product.setCreatedAt(System.currentTimeMillis());
        product.setUpdatedAt(System.currentTimeMillis());
        Product createdProduct = productRepository.save(product);
        return convertToDTO(createdProduct);
    }

    public ProductDTO updateProduct(Long id, ProductDTO updatedProductDTO) {
        ProductDTO existingProductDTO = getProductById(id);
        Product product = convertToProductEntity(existingProductDTO);
        product.setCode(updatedProductDTO.getCode());
        product.setName(updatedProductDTO.getName());
        product.setDescription(updatedProductDTO.getDescription());
        product.setImage(updatedProductDTO.getImage());
        product.setCategory(updatedProductDTO.getCategory());
        product.setPrice(updatedProductDTO.getPrice());
        product.setQuantity(updatedProductDTO.getQuantity());
        product.setInventoryStatus(InventoryStatus.valueOf(updatedProductDTO.getInventoryStatus()));
        product.setRating(updatedProductDTO.getRating());
        product.setShellId(updatedProductDTO.getShellId());
        product.setUpdatedAt(System.currentTimeMillis());
        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product convertToProductEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setCode(productDTO.getCode());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setImage(productDTO.getImage());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setInternalReference(productDTO.getInternalReference());
        product.setShellId(productDTO.getShellId());
        product.setInventoryStatus(InventoryStatus.valueOf(productDTO.getInventoryStatus()));
        product.setRating(productDTO.getRating());
        return product;
    }

    public ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setCode(product.getCode());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setImage(product.getImage());
        productDTO.setCategory(product.getCategory());
        productDTO.setPrice(product.getPrice());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setInternalReference(product.getInternalReference());
        productDTO.setShellId(product.getShellId());
        productDTO.setInventoryStatus(product.getInventoryStatus().name());
        productDTO.setRating(product.getRating());
        return productDTO;
    }
    public void validateAdmin(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (!jwtUtil.isAdmin(token)) {
            throw new ForbiddenException(ADMIN_CAN_ADD_PRODUCTS);
        }
    }
}
