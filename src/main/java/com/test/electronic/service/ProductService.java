package com.test.electronic.service;

import com.test.electronic.model.dto.request.ProductRequest;
import com.test.electronic.model.dto.response.ProductResponse;
import com.test.electronic.model.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findProductsByCategoryName(String categoryName);

    Product createProduct(ProductRequest request);

    ProductResponse getProductById(Long id);

    void updateProductById(Long id, ProductRequest request);

    void deleteProductById(Long id);

    List<ProductResponse> findProductsByCategory_Name(String categoryName);

    List<Product> findProductsByNames(List<String> productNames);


}




