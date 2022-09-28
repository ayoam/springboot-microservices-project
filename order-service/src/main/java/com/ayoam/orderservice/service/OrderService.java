package com.ayoam.orderservice.service;

import com.ayoam.orderservice.dto.InventoryResponse;
import com.ayoam.orderservice.event.OrderPlacedEvent;
import com.ayoam.orderservice.model.Order;
import com.ayoam.orderservice.model.OrderLineItems;
import com.ayoam.orderservice.repository.OrderRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private WebClient.Builder webClientBuiler;
    private KafkaTemplate<String,OrderPlacedEvent> kafkaTemplate;

    public OrderService(OrderRepository orderRepository, WebClient.Builder webClientBuiler, KafkaTemplate<String,OrderPlacedEvent> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.webClientBuiler = webClientBuiler;
        this.kafkaTemplate = kafkaTemplate;
    }
    public void placeOrder(List<OrderLineItems> orderLineItemsList){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderLineItemsList(orderLineItemsList);
        //check products inventory
        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSku)
                .toList();
        InventoryResponse[] result = new InventoryResponse[0];
             result = webClientBuiler.build().get()
                    .uri("http://inventory-service/api/v1/inventory",
                            uriBuilder -> uriBuilder.queryParam("sku",skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

        Boolean productsInStock = Arrays.stream(result).allMatch(InventoryResponse::getIsInStock);
        if(productsInStock){
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic",new OrderPlacedEvent(order.getOrderNumber()));
        }
        else{
            throw new IllegalArgumentException("product not available right now , try again later!");
        }
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
