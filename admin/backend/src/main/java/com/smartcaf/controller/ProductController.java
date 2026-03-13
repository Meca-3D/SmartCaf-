package com.smartcaf.controller;

import com.smartcaf.model.Product;
import com.smartcaf.repository.ProductRepository;
import com.smartcaf.config.DatabaseConnectionChecker;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final DatabaseConnectionChecker databaseConnectionChecker;

    public ProductController(ProductRepository productRepository, DatabaseConnectionChecker databaseConnectionChecker) {
        this.productRepository = productRepository;
        this.databaseConnectionChecker = databaseConnectionChecker;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        if (!DatabaseConnectionChecker.databaseConnected) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(productRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        if (!DatabaseConnectionChecker.databaseConnected) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        if (!DatabaseConnectionChecker.databaseConnected) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(productRepository.findByCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
        if (!DatabaseConnectionChecker.databaseConnected) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(productRepository.findByNameContainingIgnoreCase(name));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        if (!DatabaseConnectionChecker.databaseConnected) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        if (!DatabaseConnectionChecker.databaseConnected) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return productRepository.findById(id)
                .map(existingProduct -> {
                    product.setId(id);
                    product.setCreatedAt(existingProduct.getCreatedAt());
                    return ResponseEntity.ok(productRepository.save(product));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!DatabaseConnectionChecker.databaseConnected) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
