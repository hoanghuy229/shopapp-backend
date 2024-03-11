package com.spring.shopappbackend.service;

import com.spring.shopappbackend.dto.OrderDTO;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.model.Order;
import com.spring.shopappbackend.model.OrderStatus;
import com.spring.shopappbackend.model.User;
import com.spring.shopappbackend.repository.OrderRepository;
import com.spring.shopappbackend.repository.UserRepository;
import com.spring.shopappbackend.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;




    @Override
    @Transactional
    public OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException {
        User user = userRepository.findById(orderDTO.getUserId()).orElseThrow(() -> new DataNotFoundException("cannot find user"));
        //convert orderDTO => order
        //use Model Mapper
        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderDTO,order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        //check if shipping date >= today
        LocalDate shippingDate = orderDTO.getShippingDate() == null ?  LocalDate.now(): orderDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("date at least today");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public OrderResponse getById(long id) throws DataNotFoundException {
        Order o = orderRepository.findById(id).orElseThrow(() -> new DataNotFoundException("cannot found order"));
        return modelMapper.map(o,OrderResponse.class);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new DataNotFoundException("cannot find order"));
        User existUser = userRepository.findById(orderDTO.getUserId()).orElseThrow(() -> new DataNotFoundException("cannot find order"));
        modelMapper.typeMap(OrderDTO.class,Order.class).addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO,order);
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now():orderDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("date at least today");
        }
        order.setShippingDate(shippingDate);
        order.setUser(existUser);
        orderRepository.save(order);
        return modelMapper.map(order,OrderResponse.class);
    }

    @Override
    @Transactional
    public void deleteOrder(long id) {
        Order order = orderRepository.findById(id).orElse(null);
        //no hard delete => soft delete
        if(order != null){
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<OrderResponse> getAll(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(order -> modelMapper.map(order,OrderResponse.class)).toList();
    }
}
