package com.test.electronic.controller;

import com.test.electronic.model.dto.request.ProductRequest;
import com.test.electronic.model.dto.response.ProductResponse;
import com.test.electronic.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void add(@Valid  @RequestBody ProductRequest request) {
         productService.createProduct(request);
    }


    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {
        return productService.getProductById(id);
    }


    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<ProductResponse>> findProductsByCategory_Name(@PathVariable String categoryName) {
        List<ProductResponse> products = productService.findProductsByCategory_Name(categoryName);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody ProductRequest request) {
         productService.updateProductById(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.deleteProductById(id);
    }


}
