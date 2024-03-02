package com.spring.shopappbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductDTO {
    @NotBlank(message = "name cannot be emty")
    @Size(min = 3,max = 200,message = "must between 3 and 200")
    private String name;

    @Min(value = 0,message = "price must greater than or equal 0")
    @Max(value = 10000000,message = "price must less than or equal 10,000,000")
    private Float price;

    private String thumbnail;
    private String des;

    @JsonProperty("category_id")
    private String categoryId;

    private List<MultipartFile> files;
}
