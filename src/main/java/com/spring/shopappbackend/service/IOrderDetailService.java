package com.spring.shopappbackend.service;

import com.spring.shopappbackend.dto.OrderDetailDTO;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.model.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
    OrderDetail getOrderDetail(long id) throws DataNotFoundException;
    OrderDetail updateOrderDetail(long id, OrderDetailDTO orderDetailDTO);
    void deleteOrderDetail(long id);
    List<OrderDetail> getListOrderDetail(long id);
}
