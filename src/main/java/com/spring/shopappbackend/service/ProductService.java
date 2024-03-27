package com.spring.shopappbackend.service;

import com.spring.shopappbackend.dto.ProductDTO;
import com.spring.shopappbackend.dto.ProductImageDTO;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.exception.InvalidParamException;
import com.spring.shopappbackend.model.Category;
import com.spring.shopappbackend.model.Product;
import com.spring.shopappbackend.model.ProductImage;
import com.spring.shopappbackend.repository.CategoryRepository;
import com.spring.shopappbackend.repository.ProductImageRepository;
import com.spring.shopappbackend.response.ProductImageResponse;
import com.spring.shopappbackend.repository.ProductRepository;
import com.spring.shopappbackend.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {

        Category existCate = categoryRepository.findById(productDTO.getCategoryId())
                          .orElseThrow(() -> new DataNotFoundException("cannot find category"));

        Product newproduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .des(productDTO.getDes())
                .category(existCate)
                .build();
        return productRepository.save(newproduct);
    }

    @Override
    public ProductResponse getProductById(long id) throws DataNotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new DataNotFoundException("cannot find product with id "+id));
        ProductResponse productResponse = new ProductResponse();
        modelMapper.map(product,productResponse);
        List<ProductImage> productImages = productImageRepository.findByProductId(id);
        List<ProductImageResponse> myResponse = productImages.stream().map(productImage -> modelMapper.map(productImage, ProductImageResponse.class)).toList();
        productResponse.setProductImageResponses(myResponse);
        return productResponse;
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword,Long categoryId,PageRequest pageRequest) {
        Page<Product> products = productRepository.searchProducts(keyword,categoryId,pageRequest);
        return products.map(product -> modelMapper.map(product,ProductResponse.class));
    }

    @Override
    @Transactional
    public Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException {
        Product existProduct = productRepository.findById(id).orElseThrow(() -> new DataNotFoundException("cannot find product"));


            Category existCate = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException("cannot find category"));
            existProduct.setCategory(existCate);
            existProduct.setName(productDTO.getName());
            existProduct.setPrice(productDTO.getPrice());
            existProduct.setDes(productDTO.getDes());
            existProduct.setThumbnail(productDTO.getThumbnail());
            return productRepository.save(existProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(long id) throws DataNotFoundException {
        productRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    public Product findById(long id) throws Exception{
        return productRepository.findById(id).orElseThrow(() -> new DataNotFoundException("cannot find product with id"));
    }

    @Override
    public List<ProductResponse> findProductByIds(List<Long> productIds){
        List<Product> products = productRepository.findProductsByIds(productIds);
        return products.stream().map(product -> modelMapper.map(product,ProductResponse.class)).toList();
    }

    @Override
    @Transactional
    public ProductImage createProductImages(long id, ProductImageDTO productImageDTO) throws Exception {
        Product existProduct = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("cannot find product with id"));

        ProductImage newPImage = ProductImage.builder()
                .product(existProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        int size = productImageRepository.findByProductId(id).size();
        if(size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT + 1){
            throw new InvalidParamException("Number of images must <= 5");
        }
        return productImageRepository.save(newPImage);
    }

    @Override
    public Page<ProductResponse> getCarousel(PageRequest pageRequest) {
        Page<Product> products = productRepository.getCarousel(pageRequest);
        return products.map(product -> modelMapper.map(product,ProductResponse.class));
    }
}
