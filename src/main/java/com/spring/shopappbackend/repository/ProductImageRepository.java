package com.spring.shopappbackend.repository;

import com.spring.shopappbackend.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {

    List<ProductImage> findByProductId(long productId);

    @Query("SELECT p FROM ProductImage p WHERE p.imageUrl = ?1")
    ProductImage getProductImageByImageUrl(String imageUrl);
}
