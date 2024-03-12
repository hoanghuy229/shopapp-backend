package com.spring.shopappbackend.controller;

import com.github.javafaker.Faker;
import com.spring.shopappbackend.dto.ProductDTO;
import com.spring.shopappbackend.dto.ProductImageDTO;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.model.Product;
import com.spring.shopappbackend.model.ProductImage;
import com.spring.shopappbackend.response.ProductListResponse;
import com.spring.shopappbackend.response.ProductResponse;
import com.spring.shopappbackend.service.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService iProductService;
    private final ModelMapper modelMapper;

    @GetMapping() //http://localhost:8080/api/v1/products?page=0&category_id=0&limit=12&keyword=''
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam(defaultValue = "",name = "keyword") String keyword,
            @RequestParam(defaultValue = "0",name = "category_id") Long categoryId,
            @RequestParam(defaultValue = "0",name = "page") int page,
            @RequestParam(defaultValue = "12",name = "limit") int limit
    ){
        //get page and number of element in page and sort from newest to oldest
        PageRequest pageRequest = PageRequest.of(page,limit, Sort.by("id").ascending());
        Page<ProductResponse> productPage = iProductService.getAllProducts(keyword,categoryId,pageRequest);
        //get total page
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder().products(products).totalPages(totalPages).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long productId) throws DataNotFoundException {
        ProductResponse exist = iProductService.getProductById(productId);
        return ResponseEntity.ok(exist);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id) throws DataNotFoundException {
        iProductService.deleteProduct(id);
        return ResponseEntity.ok(String.format("delete product with id =%d success",id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable long id,
            @Valid @RequestBody ProductDTO productDTO) throws DataNotFoundException {

            Product product = iProductService.updateProduct(id,productDTO);
            return ResponseEntity.ok(product);

    }

    @GetMapping("/by-ids") //vd: 1,2,3,6,7,8
    public ResponseEntity<?> getProductsByIds(@RequestParam("ids") String ids){
        try{
            //biến chuỗi ids thành danh sách productIds có kiểu Long
            List<Long> productIds = Arrays.stream(ids.split(",")).map(Long::parseLong).toList();
            List<ProductResponse> productResponses = iProductService.findProductByIds(productIds);
            return ResponseEntity.ok(productResponses);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
    public ResponseEntity<?> uploadImages(@PathVariable("id") long id,
                                          @ModelAttribute("files") List<MultipartFile> files) throws Exception {

        try {
            Product existProduct = iProductService.findById(id);

            files = files == null ? new ArrayList<MultipartFile>() : files;
            if(files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
                return ResponseEntity.badRequest().body("<= 5 img");
            }

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

                    ProductImage productImage = iProductService.createProductImages(id,
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

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName){
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/"+imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());
            if(resource.exists()){
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
            }
            else{
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                                     .body(new UrlResource(Paths.get("uploads/notfound.jpg").toUri()));
            }
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    private String storeFile(MultipartFile file) throws Exception {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
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

    //@PostMapping("/generateFakeProducts")
//    public ResponseEntity<String> generateFakeProducts() throws DataNotFoundException {
//        Faker faker = new Faker();
//        for(int i=0; i<100000;i++){
//            String productName = faker.commerce().productName();
//            if(iProductService.existsByName(productName)){
//                continue;
//            }
//            ProductDTO productDTO = ProductDTO.builder()
//                    .name(productName)
//                    .price((float)faker.number().numberBetween(10,50_000_000))
//                    .des(faker.lorem().sentence())
//                    .thumbnail("")
//                    .categoryId((long)faker.number().numberBetween(2,4))
//                    .build();
//            iProductService.createProduct(productDTO);
//        }
//        return ResponseEntity.ok("fake success");
//    }
}
