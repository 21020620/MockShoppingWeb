package com.example.demo.product_service.controller;

import com.example.demo.customer_service.repository.CustomerService;
import com.example.demo.product_service.entity.Product;
import com.example.demo.product_service.repository.ProductService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/products")
public class ProductController {
    private ProductService productService;
    private static final Logger logger = Logger.getLogger(ProductController.class.getName());

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //GET METHODS

    @GetMapping("{productId}")
    public ResponseEntity<?> getProduct(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping(value = {"getReviews/{productId}"})
    public ResponseEntity<?> getProductReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.showAllReviews(productId));
    }

    @GetMapping(value = {"getImage/{productId}"}, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getProductImage(@PathVariable Long productId) throws IOException {
        Resource resource = productService.getImage(productId);
        return ResponseEntity.ok().body(resource);
    }

    //POST METHODS
    @PostMapping("create")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        productService.addProduct(product);
        logger.info("Product created: " + product);
        return ResponseEntity.ok("Product created");
    }

    @PostMapping("addImage/{id}")
    public ResponseEntity<?> addImageToProduct(@PathVariable Long id, @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        productService.addImageToProduct(id, imageFile);
        logger.info("Image added to product");
        return ResponseEntity.ok("Image added to product");
    }

    //PUT METHODS
    @PutMapping("update/{id}")
    public ResponseEntity<?> changeProductDetail(@PathVariable Long id, @RequestBody Product inputProduct) {
        Product baseProduct = productService.getProductById(id);
        productService.updateProduct(baseProduct, inputProduct);
        logger.info("Product updated");
        return ResponseEntity.ok("Product updated");
    }

    //DELETE METHODS
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        logger.info("Product deleted");
        return ResponseEntity.ok("Product deleted");
    }
}
