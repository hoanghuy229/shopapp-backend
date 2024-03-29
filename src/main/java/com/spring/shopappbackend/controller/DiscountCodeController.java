package com.spring.shopappbackend.controller;

import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.exception.PermissionException;
import com.spring.shopappbackend.response.CouponseResponse;
import com.spring.shopappbackend.service.ICouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/discount")
public class DiscountCodeController {

    private final ICouponService iCouponService;


    @GetMapping()
    public ResponseEntity<CouponseResponse> calculateCoupon(@RequestParam("couponCode") String couponCode,
                                             @RequestParam("totalPrice") double totalPrice
    ){
        try{
            CouponseResponse couponseResponse = iCouponService.calculateCouponValue(couponCode,totalPrice);
            return ResponseEntity.ok().body(couponseResponse);
        }
        catch (DataNotFoundException | PermissionException e){
            return ResponseEntity.badRequest().body(CouponseResponse.builder()
                                                                    .finalPrice(totalPrice)
                                                                    .message(e.getMessage()).build());
        }

    }

}
