package com.spring.shopappbackend.configuration;

public class Endpoints {
    public static String[] PUBLIC_GET_ENDPOINTS = {
            "api/v1/products",
            "api/v1/products/carousel",
            "api/v1/products/{id}",
            "api/v1/categories",
            "api/v1/roles",
            "api/v1/products/images/*",
            "api/v1/comments"
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
            "api/v1/orders/admin/get-orders-admin",
            "api/v1/users/admin/getAll"
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
            "api/v1/products/{id}",
            "api/v1/users/admin/{id}"
    };
    public static String[] USER_GET_ENDPOINTS = {

    };
    public static String[] USER_POST_ENDPOINTS = {
            "api/v1/orders",
            "api/v1/order-details",
            "api/v1/comments"
    };
    public static String[] USER_PUT_ENDPOINTS = {
            "api/v1/users/details/{id}",
            "api/v1/comments/{id}"
    };
    public static String[] USER_DELETE_ENDPOINTS = {
            "api/v1/comments/{id}"
    };
}
