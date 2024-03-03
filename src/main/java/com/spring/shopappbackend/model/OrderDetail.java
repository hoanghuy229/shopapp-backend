package com.spring.shopappbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "order_details")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "price",nullable = false)
    private Float price;

    @Column(name = "number_of_products",nullable = false)
    private int numberOfProducts;

    @Column(name = "total_price",nullable = false)
    private Float totalPrice;

    @Column(name = "color")
    private String color;
}
