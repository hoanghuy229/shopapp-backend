package com.spring.shopappbackend.repository;

import com.spring.shopappbackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.*;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByName(String name);

    @Query("SELECT p FROM Product p WHERE " +
           "(:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId)" +
           "AND (:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword% OR p.des LIKE %:keyword%)")
    Page<Product> searchProducts(String keyword,Long categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p ORDER BY p.id DESC")
    Page<Product> getCarousel(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    List<Product> findProductsByIds(List<Long> productIds);
}
