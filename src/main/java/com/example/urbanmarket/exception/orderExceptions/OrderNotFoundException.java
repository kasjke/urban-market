package com.example.urbanmarket.exception.orderExceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String id){
        super("This order not found %s".formatted(id));
    }
}
