package com.spring.shopappbackend.service;

import com.spring.shopappbackend.dto.OrderDTO;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.response.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException;
    OrderResponse getById(long id) throws DataNotFoundException;
    OrderResponse updateOrder(long id,OrderDTO orderDTO) throws DataNotFoundException;
    void deleteOrder(long id);
    List<OrderResponse> getAll(Long userId);
}
