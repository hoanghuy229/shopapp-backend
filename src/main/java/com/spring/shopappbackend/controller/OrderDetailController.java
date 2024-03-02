package com.spring.shopappbackend.controller;

import com.spring.shopappbackend.dto.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order-details")
public class OrderDetailController {

    @PostMapping()
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO){
        return ResponseEntity.ok("success create order detail");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable long id){
        return ResponseEntity.ok("get success order detail "+id);
    }

    @GetMapping("/order/{orderId}") //get list order details of order
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId){
        return ResponseEntity.ok("get success order details of "+orderId);
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
        return ResponseEntity.ok("delete order detail "+id);
    }
}
