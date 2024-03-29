package com.spring.shopappbackend.service;

import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.exception.PermissionException;
import com.spring.shopappbackend.model.Coupon;
import com.spring.shopappbackend.repository.CouponRepository;
import com.spring.shopappbackend.response.CouponseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponService implements ICouponService{
    private final CouponRepository couponRepository;


    @Override
    public CouponseResponse calculateCouponValue(String couponCode, double totalPrice) throws DataNotFoundException, PermissionException {
        Coupon coupon = couponRepository.findByCode(couponCode).orElseThrow(() -> new DataNotFoundException("cannot find"));

        if(!coupon.isActive()){
            throw new PermissionException("coupon is not active");
        }
        LocalDate currentDate = LocalDate.now();
        LocalDate startTime = coupon.getStartTime();
        LocalDate endTime = coupon.getEndTime();
        if(currentDate.isBefore(startTime) || currentDate.isAfter(endTime)){
            throw new PermissionException("wrong time !!!");
        }
        double minPrice = Double.parseDouble(String.valueOf(coupon.getMinPrice()));
        if(totalPrice < minPrice){
            throw new PermissionException("total price is < than coupon min price");
        }
        double percent = Double.parseDouble(String.valueOf(coupon.getDiscount()));
        double moneyDiscount = totalPrice * percent / 100;
        double finalPrice =  totalPrice - moneyDiscount;

        // Round finalPrice to two decimal places
        finalPrice = Math.round(finalPrice * 100.0) / 100.0;

        return CouponseResponse.builder().finalPrice(finalPrice).message("discount success").build();
    }


}
