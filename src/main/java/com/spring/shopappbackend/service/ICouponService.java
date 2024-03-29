package com.spring.shopappbackend.service;

import com.spring.shopappbackend.exception.DataNotFoundException;
import com.spring.shopappbackend.exception.PermissionException;
import com.spring.shopappbackend.response.CouponseResponse;

public interface ICouponService {
    CouponseResponse calculateCouponValue(String couponCode, double totalPrice) throws DataNotFoundException, PermissionException;
}
