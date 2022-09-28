package com.ayoam.orderservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryResponse {
    private String skuCode;
    private Boolean isInStock;
}
