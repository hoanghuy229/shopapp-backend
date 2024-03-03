package com.spring.shopappbackend.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDTO {

    @JsonProperty("product_id")
    @Min(value = 1,message = "product id must > 0")
    private Long productId;

    @JsonProperty("image_url")
    @Size(min = 3,max = 200,message = "image's name")
    private String imageUrl;
}
