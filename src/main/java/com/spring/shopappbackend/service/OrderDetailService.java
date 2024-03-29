package com.spring.shopappbackend.service;

import com.spring.shopappbackend.dto.OrderDetailDTO;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.model.Order;
import com.spring.shopappbackend.model.OrderDetail;
import com.spring.shopappbackend.model.Product;
import com.spring.shopappbackend.repository.OrderDetailRepository;
import com.spring.shopappbackend.repository.OrderRepository;
import com.spring.shopappbackend.repository.ProductRepository;
import com.spring.shopappbackend.response.OrderDetailResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional
    public OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(() -> new DataNotFoundException("cannot find order "));
        Product product = productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(() -> new DataNotFoundException("cannot find product"));

        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .price(orderDetailDTO.getPrice())
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .build();
        orderDetailRepository.save(orderDetail);
        return modelMapper.map(orderDetail,OrderDetailResponse.class);

    }

    @Override
    public OrderDetailResponse getOrderDetail(long id) throws DataNotFoundException {
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(() -> new DataNotFoundException("cannot find"));
        return modelMapper.map(orderDetail,OrderDetailResponse.class);
    }

    @Override
    @Transactional
    public OrderDetailResponse updateOrderDetail(long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        OrderDetail existOrderDetail = orderDetailRepository.findById(id).orElseThrow(() -> new DataNotFoundException("cannot find order detail"));
        Product product = productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(()-> new DataNotFoundException("cannot find product"));
        Order order = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(()-> new DataNotFoundException("cannot find order order"));

        existOrderDetail.setOrder(order);
        existOrderDetail.setProduct(product);
        existOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        existOrderDetail.setPrice(orderDetailDTO.getPrice());

        orderDetailRepository.save(existOrderDetail);
        return modelMapper.map(existOrderDetail,OrderDetailResponse.class);
    }

    @Override
    @Transactional
    public void deleteOrderDetail(long id) {
        orderDetailRepository.deleteById(id);

    }

    @Override
    public List<OrderDetailResponse> getListOrderDetail(long id) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(id);
        return orderDetails.stream().map(orderDetail -> modelMapper.map(orderDetail,OrderDetailResponse.class)).toList();
    }
}
