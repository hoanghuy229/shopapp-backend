package com.spring.shopappbackend.service;

import com.spring.shopappbackend.dto.OrderDetailDTO;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.model.Order;
import com.spring.shopappbackend.model.OrderDetail;
import com.spring.shopappbackend.model.Product;
import com.spring.shopappbackend.repository.OrderDetailRepository;
import com.spring.shopappbackend.repository.OrderRepository;
import com.spring.shopappbackend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;



    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(() -> new DataNotFoundException("cannot find order "));
        Product product = productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(() -> new DataNotFoundException("cannot find product"));

        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .totalPrice(orderDetailDTO.getTotalPrice())
                .color(orderDetailDTO.getColor())
                .build();

        return orderDetailRepository.save(orderDetail);

    }

    @Override
    public OrderDetail getOrderDetail(long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id).orElseThrow(() -> new DataNotFoundException("cannot find"));
    }

    @Override
    public OrderDetail updateOrderDetail(long id, OrderDetailDTO orderDetailDTO) {
        return null;
    }

    @Override
    public void deleteOrderDetail(long id) {
        orderDetailRepository.deleteById(id);

    }

    @Override
    public List<OrderDetail> getListOrderDetail(long id) {
        return orderDetailRepository.findByOrderId(id);
    }
}
