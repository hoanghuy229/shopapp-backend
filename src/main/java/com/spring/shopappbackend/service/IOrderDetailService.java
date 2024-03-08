package com.spring.shopappbackend.service;

import com.spring.shopappbackend.dto.OrderDetailDTO;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.model.OrderDetail;
import com.spring.shopappbackend.response.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailService {
    OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
    OrderDetailResponse getOrderDetail(long id) throws DataNotFoundException;
    OrderDetailResponse updateOrderDetail(long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
    void deleteOrderDetail(long id);
    List<OrderDetailResponse> getListOrderDetail(long id);
}
