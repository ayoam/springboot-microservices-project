package com.ayoam.orderservice.controller;

import com.ayoam.orderservice.model.Order;
import com.ayoam.orderservice.model.OrderLineItems;
import com.ayoam.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order")
    @CircuitBreaker(name="inventory",fallbackMethod ="fallbackMethod")
    public ResponseEntity<String> placeOrder(@RequestBody List<OrderLineItems> orderLineItemsList){
        orderService.placeOrder(orderLineItemsList);
        return new ResponseEntity<String>("order placed successfully!", HttpStatus.CREATED);
    }

    @GetMapping("/order")
    public ResponseEntity<List<Order>> getAllOrders(){
        return new ResponseEntity<List<Order>>(orderService.getAllOrders(),HttpStatus.OK);
    }

    public ResponseEntity<String> fallbackMethod(List<OrderLineItems> orderLineItemsList,RuntimeException runtimeException){
        return new ResponseEntity<String>("Oops! Something went wrong.", HttpStatus.BAD_REQUEST);
    }
}
