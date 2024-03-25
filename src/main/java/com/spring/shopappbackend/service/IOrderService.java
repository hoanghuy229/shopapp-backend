package com.spring.shopappbackend.service;

import com.spring.shopappbackend.dto.OrderDTO;
import com.spring.shopappbackend.dto.UpdateOrderDTO;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.model.Order;
import com.spring.shopappbackend.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IOrderService {
    String createOrder(OrderDTO orderDTO) throws DataNotFoundException;
    OrderResponse getById(long id) throws DataNotFoundException;
    OrderResponse updateOrder(long id, UpdateOrderDTO updateOrderDTO) throws DataNotFoundException;
    void deleteOrder(long id);
    List<OrderResponse> getAll(Long userId);
    Page<OrderResponse> getOrdersByKeyword(String keyword, PageRequest pageRequest);
}
