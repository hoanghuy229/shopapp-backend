package com.spring.shopappbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "coupons")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code",nullable = false,unique = true)
    private String code;

    @Column(name = "discount",nullable = false)
    private BigDecimal discount;

    @Column(name = "start_time",nullable = false)
    private LocalDate startTime;

    @Column(name = "end_time",nullable = false)
    private LocalDate endTime;

    @Column(name = "min_price",nullable = false)
    private BigDecimal minPrice;

    @Column(name = "active",nullable = false)
    private boolean active;

}
