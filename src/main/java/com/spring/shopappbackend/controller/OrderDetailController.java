package com.spring.shopappbackend.controller;

import com.spring.shopappbackend.dto.OrderDetailDTO;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.model.OrderDetail;
import com.spring.shopappbackend.service.IOrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order-details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final IOrderDetailService iOrderDetailService;



    @PostMapping()
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        OrderDetail o = iOrderDetailService.createOrderDetail(orderDetailDTO);
        return ResponseEntity.ok(o);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable long id) throws DataNotFoundException {
        OrderDetail o = iOrderDetailService.getOrderDetail(id);
        return ResponseEntity.ok(o);
    }

    @GetMapping("/order/{orderId}") //get list order details of order
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId){
        List<OrderDetail> o = iOrderDetailService.getListOrderDetail(orderId);
        return ResponseEntity.ok(o);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable long id,
            @RequestBody OrderDetailDTO orderDetailDTO
    ){
        return ResponseEntity.ok("update order detail "+id+", orderDetailDTO: "+orderDetailDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable long id){
        iOrderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok("delete order detail "+id);
    }
}
