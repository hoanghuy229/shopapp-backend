package com.spring.shopappbackend.repository;

import com.spring.shopappbackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR u.fullName LIKE %:keyword% OR u.phoneNumber LIKE %:keyword% " +
            "OR u.address LIKE %:keyword% )")
    Page<User> findByKeyword(String keyword, Pageable pageable);

    User findByGoogleAccountId(String googleAccount);

}