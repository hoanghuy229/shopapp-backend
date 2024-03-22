package com.spring.shopappbackend.service;

import com.spring.shopappbackend.dto.CartDTO;
import com.spring.shopappbackend.dto.OrderDTO;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.model.*;
import com.spring.shopappbackend.repository.OrderDetailRepository;
import com.spring.shopappbackend.repository.OrderRepository;
import com.spring.shopappbackend.repository.ProductRepository;
import com.spring.shopappbackend.repository.UserRepository;
import com.spring.shopappbackend.response.OrderDetailResponse;
import com.spring.shopappbackend.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;


    @Override
    @Transactional
    public String createOrder(OrderDTO orderDTO) throws DataNotFoundException {
        User user = userRepository.findById(orderDTO.getUserId()).orElseThrow(() -> new DataNotFoundException("cannot find user"));
        //convert orderDTO => order
        //use Model Mapper
        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> mapper.skip(Order::setId));
        // Cập nhật các trường của đơn hàng từ orderDTO
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
        //order.setTotalPrice(orderDTO.getTotalPrice());

        List<OrderDetail> orderDetails = new ArrayList<>(); // Tạo danh sách các đối tượng OrderDetails từ cartItems
        for(CartDTO cartDTO : orderDTO.getCartItems()){
            OrderDetail orderDetail = new OrderDetail(); // Tạo một đối tượng OrderDetail từ CartDTO
            orderDetail.setOrder(order);
            // Lấy thông tin sản phẩm từ cartDTO
            Long productId = cartDTO.getProductId();
            int quantity = cartDTO.getQuantity();
            Product product = productRepository.findById(productId).orElseThrow(() -> new DataNotFoundException("Product not found with id: " + productId));
            // Đặt thông tin cho OrderDetail
            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(quantity);
            orderDetail.setPrice(product.getPrice());

            orderDetails.add(orderDetail); // Thêm OrderDetail vào danh sách
        }

        orderRepository.save(order); // Lưu order
        orderDetailRepository.saveAll(orderDetails);// Lưu danh sách OrderDetail vào cơ sở dữ liệu
        return "order success";
    }

    @Override
    public OrderResponse getById(long id) throws DataNotFoundException {
        Order o =orderRepository.findById(id).orElseThrow(() -> new DataNotFoundException("cannot found order"));
        OrderResponse orderResponse = modelMapper.map(o, OrderResponse.class);


        modelMapper.typeMap(OrderDetail.class,OrderDetailResponse.class);
        List<OrderDetail> orderDetails = o.getOrderDetails();
        List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            orderDetailResponses.add(modelMapper.map(orderDetail, OrderDetailResponse.class));
        }
        orderResponse.setOrderDetailIds(orderDetailResponses);
        return orderResponse;
    }

    @Override
    public List<OrderResponse> getAll(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
            List<OrderDetail> orderDetails = order.getOrderDetails();
            List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();

            for (OrderDetail orderDetail : orderDetails) {
                OrderDetailResponse orderDetailResponse = modelMapper.map(orderDetail, OrderDetailResponse.class);
                orderDetailResponses.add(orderDetailResponse);
            }

            orderResponse.setOrderDetailIds(orderDetailResponses);
            orderResponses.add(orderResponse);
        }

        return orderResponses;

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
    public Page<OrderResponse> getOrdersByKeyword(String keyword, PageRequest pageRequest) {
        Page<Order> orderPage = orderRepository.findByKeyword(keyword, pageRequest);

        // Ánh xạ từng phần tử trong trang Page<Order> sang OrderResponse
        Page<OrderResponse> orderResponsePage = orderPage.map(order -> {
            OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);

            List<OrderDetail> orderDetails = order.getOrderDetails();
            List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();
            for (OrderDetail orderDetail : orderDetails) {
                orderDetailResponses.add(modelMapper.map(orderDetail, OrderDetailResponse.class));
            }

            orderResponse.setOrderDetailIds(orderDetailResponses);
            return orderResponse;
        });

        return orderResponsePage;
    }

}
