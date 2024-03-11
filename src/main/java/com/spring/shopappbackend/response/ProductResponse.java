package com.spring.shopappbackend.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.shopappbackend.model.Product;
import com.spring.shopappbackend.model.ProductImage;
import com.spring.shopappbackend.repository.ProductImageResponse;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductResponse extends BaseResponse{
    private Long id;
    private String name;
    private Float price;
    private String thumbnail;
    private String des;

    @JsonProperty("category_id")
    private Long categoryId;

    @JsonProperty("product_images")
    private List<ProductImageResponse> productImageResponses = new ArrayList<>();
//    public static ProductResponse fromProduct(Product product){
//        ProductResponse productResponse = ProductResponse.builder()
//                .name(product.getName())
//                .des(product.getDes())
//                .price(product.getPrice())
//                .thumbnail(product.getThumbnail())
//                .categoryId(product.getCategory().getId())
//                .build();
//        productResponse.setCreatedAt(product.getCreatedAt());
//        productResponse.setUpdatedAt(product.getUpdatedAt());
//        return productResponse;
//
//    }
}
