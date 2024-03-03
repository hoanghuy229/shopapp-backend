package com.spring.shopappbackend.repository;

import com.spring.shopappbackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.*;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByName(String name);
    Page<Product> findAll(Pageable pageable);
}
