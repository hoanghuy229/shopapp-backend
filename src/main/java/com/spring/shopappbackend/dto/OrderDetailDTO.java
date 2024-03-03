package com.spring.shopappbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "order id must > 0")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "product id must > 0")
    private Long productId;

    @Min(value = 0, message = "price must >= 0")
    private Float price;

    @JsonProperty("number_of_products")
    @Min(value = 1, message = "number of product must > 0")
    private int numberOfProducts;

    @JsonProperty("total_price")
    @Min(value = 0, message = "total Price must >= 0")
    private Float totalPrice;

    private String color;
}
