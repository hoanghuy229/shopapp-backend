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
import com.spring.shopappbackend.repository.ProductRepository;
import com.spring.shopappbackend.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final ModelMapper modelMapper;


    @Override
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
    public Product getProductById(long id) throws DataNotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new DataNotFoundException("cannot find product with id "+id));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        Page<Product> products = productRepository.findAll(pageRequest);
        return products.map(product -> modelMapper.map(product,ProductResponse.class));
    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException {
        Product existProduct = getProductById(id);
        if(existProduct != null){
            Category existCate = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException("cannot find category"));
            existProduct.setCategory(existCate);
            existProduct.setName(productDTO.getName());
            existProduct.setPrice(productDTO.getPrice());
            existProduct.setDes(productDTO.getDes());
            existProduct.setThumbnail(productDTO.getThumbnail());
            return productRepository.save(existProduct);
        }
        return null;
    }

    @Override
    public void deleteProduct(long id) throws DataNotFoundException {
        Product existProduct = getProductById(id);
        if(existProduct != null){
            productRepository.delete(existProduct);
        }
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }


    @Override
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
}
