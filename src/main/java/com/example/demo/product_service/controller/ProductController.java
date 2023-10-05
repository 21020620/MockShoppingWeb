package com.example.demo.product_service.controller;

import com.example.demo.customer_service.repository.CustomerService;
import com.example.demo.product_service.entity.Product;
import com.example.demo.product_service.repository.ProductService;
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
    @GetMapping(value = {"getImage/{productId}"})
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        if (product == null || product.getImage() == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return ResponseEntity.ok()
                .headers(headers)
                .body(product.getImage());
    }

    @GetMapping("{productId}")
    public ResponseEntity<?> getProduct(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping(value = {"getReviews/{productId}"})
    public ResponseEntity<?> getProductReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.showAllReviews(productId));
    }

    //POST METHODS
    @PostMapping("create")
    public ResponseEntity<?> createProduct(@RequestPart("imageFile") MultipartFile imageFile,
                                           @RequestPart("productData") Product product) {
        try {
            byte[] imageBytes = imageFile.getBytes();
            product.setImage(imageBytes);
            System.out.println(product);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().body("Error when uploading image");
        }
        productService.addProduct(product);
        logger.info("Product created: " + product);
        return ResponseEntity.ok("File received successful\nProduct created");
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
