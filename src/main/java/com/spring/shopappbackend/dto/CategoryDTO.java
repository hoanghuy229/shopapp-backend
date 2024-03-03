package com.spring.shopappbackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data //create toString/getter/setter/constructor
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    @NotBlank(message = "Category's name cannot be emty")
    private String name;
}
