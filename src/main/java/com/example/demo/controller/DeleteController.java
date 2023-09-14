package com.example.demo.controller;

import com.example.demo.entities.ApplicationLogger;
import com.example.demo.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class DeleteController {
    @Autowired
    private IProductService productService;

    private static final Logger logger = ApplicationLogger.getLogger();

    @DeleteMapping("api/delete/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        logger.info("Product deleted");
        return ResponseEntity.ok("Product deleted");
    }
}
