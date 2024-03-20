package com.spring.shopappbackend.controller;

import com.spring.shopappbackend.dto.OrderDTO;
import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.model.Order;
import com.spring.shopappbackend.model.User;
import com.spring.shopappbackend.response.OrderResponse;
import com.spring.shopappbackend.service.IOrderService;

import com.spring.shopappbackend.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService iOrderService;
    private final IUserService iUserService;


    @PostMapping()
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDTO orderDTO, BindingResult bindingResult){
        try{
            if(bindingResult.hasErrors()){
                List<String> errorMessage = bindingResult.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage).toList();

                return ResponseEntity.badRequest().body(errorMessage);
            }
            String response = iOrderService.createOrder(orderDTO);
            return ResponseEntity.ok().body(response);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@Valid @PathVariable long id){
        try {
            OrderResponse o = iOrderService.getById(id);
            return ResponseEntity.ok(o);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrdersByUserId(@RequestHeader("Authorization") String token,
                                               @Valid @PathVariable("user_id") Long userId){
        try {
            String extractToken = token.substring(7);
            User user = iUserService.getUserDetailFromToken(extractToken);// xác thực user
            if(user.getId() != userId){// kiểm tra user đang xem orders của mình hay của người khác
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); //403
            }
            List<OrderResponse> orders = iOrderService.getAll(userId);
            return ResponseEntity.ok(orders);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable long id,
            @Valid @RequestBody OrderDTO orderDTO
    ) throws DataNotFoundException {
        OrderResponse o = iOrderService.updateOrder(id,orderDTO);
        return ResponseEntity.ok(o);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(
            @Valid @PathVariable long id
    ){
        // solf delete => turn isActive to false
        iOrderService.deleteOrder(id);
        return ResponseEntity.ok("delete orders of user " + id);
    }

}
