package com.spring.shopappbackend.controller;

import com.spring.shopappbackend.dto.ProductDTO;
import com.spring.shopappbackend.dto.ProductImageDTO;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.model.Product;
import com.spring.shopappbackend.model.ProductImage;
import com.spring.shopappbackend.service.IProductService;
import com.spring.shopappbackend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService iProductService;

    @GetMapping() //http://localhost:8080/api/v1/products?page=1&limit=10
    public ResponseEntity<String> getProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        return ResponseEntity.ok(String.format("get all products,page = %d,limit = %d",page,limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable("id") String productId){
        return ResponseEntity.ok("ok "+productId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id){
        return ResponseEntity.ok(String.format("delete product with id =%d success",id));
    }

    @PostMapping()
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO,
                                           BindingResult bindingResult
                                            ){
        try{
            //check error attribute
            if(bindingResult.hasErrors()){
                List<String> errorMessage = bindingResult.getFieldErrors().stream()
                                            .map(FieldError::getDefaultMessage).toList();

                return ResponseEntity.badRequest().body(errorMessage);
            }
            Product newProduct = iProductService.createProduct(productDTO);

            return ResponseEntity.ok(newProduct);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping(value = "uploads/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("id") long id,@ModelAttribute("files") List<MultipartFile> files){

        try {
            Product existProduct = iProductService.getProductById(id);

            files = files == null ? new ArrayList<MultipartFile>() : files;
            List<ProductImage> productImages = new ArrayList<>();

            for(MultipartFile file :files){
                //check if file = "" continue to next loop
                if(file.getSize() == 0){
                    continue;
                }
                //check image file
                if(file.getSize() >10*1024*1024){ //10MB
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("file too large");
                }

                //check file type
                String contentType = file.getContentType();
                if(contentType == null || !contentType.startsWith("image/")){
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("not image");
                }
                try {
                //save file and update thumbnail in DTO
                String filename = storeFile(file);

                    ProductImage productImage = iProductService.createProductImages(existProduct.getId(),
                            ProductImageDTO.builder()
                                    .imageUrl(filename)
                                    .build());
                    productImages.add(productImage);
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            }
            return ResponseEntity.ok().body(productImages);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String storeFile(MultipartFile file) throws IOException{
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        //change file name to be unique
        String uniqueFileName = UUID.randomUUID().toString() + "_" + filename;
        //link to images storage
        Path uploadDir = Paths.get("uploads");
        //check file exist
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        //full link to file
        Path destination = Paths.get(uploadDir.toString(),uniqueFileName);
        //copy file to destination
        Files.copy(file.getInputStream(),destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

}
