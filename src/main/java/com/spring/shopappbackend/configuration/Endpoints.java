package com.spring.shopappbackend.configuration;

public class Endpoints {
    public static String[] PUBLIC_GET_ENDPOINTS = {
            "api/v1/products",
            "api/v1/products/{id}",
            "api/v1/categories",
            "api/v1/roles",
            "api/v1/products/images/*",
            "api/v1/orders/{id}"
    };
    public static String[] PUBLIC_POST_ENDPOINTS = {
            "api/v1/users/register",
            "api/v1/users/login",
            "api/v1/users/otp/*",
    };
    public static String[] ADMIN_USER_GET_ENDPOINTS = {
            "api/v1/order-details/{id}",
            "api/v1/orders/user/{user_id}",
            "api/v1/orders/{id}"
    };
    public static String[] ADMIN_USER_POST_ENDPOINTS = {
            "api/v1/users/details",
    };
    public static String[] ADMIN_GET_ENDPOINTS = {

    };
    public static String[] ADMIN_POST_ENDPOINTS = {
            "api/v1/products",
            "api/v1/categories",
            "api/v1/products/uploads/{id}"
    };
    public static String[] ADMIN_PUT_ENDPOINTS = {
            "api/v1/categories/{id}",
            "api/v1/orders/{id}",
            "api/v1/order-details/{id}",
            "api/v1/products/{id}",
            "api/v1/users/details/{id}"
    };
    public static String[] ADMIN_DELETE_ENDPOINTS = {
            "api/v1/categories/{id}",
            "api/v1/orders/{id}",
            "api/v1/order-details/{id}",
            "api/v1/products/{id}"
    };
    public static String[] USER_GET_ENDPOINTS = {

    };
    public static String[] USER_POST_ENDPOINTS = {
            "api/v1/orders",
            "api/v1/order-details"
    };
    public static String[] USER_PUT_ENDPOINTS = {
            "api/v1/users/details/{id}"
    };
    public static String[] USER_DELETE_ENDPOINTS = {

    };
}
