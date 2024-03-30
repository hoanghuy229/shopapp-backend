package com.spring.shopappbackend.service;

import com.spring.shopappbackend.dto.ProductDTO;
import com.spring.shopappbackend.dto.ProductImageDTO;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.model.Product;
import com.spring.shopappbackend.model.ProductImage;
import com.spring.shopappbackend.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;
    ProductResponse getProductById(long id) throws DataNotFoundException;
    Page<ProductResponse> getAllProducts(String keyword,Long categoryId,PageRequest pageRequest);
    Product updateProduct(long id,ProductDTO productDTO) throws DataNotFoundException;
    void deleteProduct(long id) throws DataNotFoundException;
    boolean existsByName(String name);
    List<ProductResponse> findProductByIds(List<Long> productIds);
    Product findById(long id) throws Exception;
    ProductImage createProductImages(long id, ProductImageDTO productImageDTO) throws Exception;

    Page<ProductResponse> getCarousel(PageRequest pageRequest);

    String deleteProductImage(String imageName) throws DataNotFoundException;
}
