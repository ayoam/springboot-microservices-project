package com.ayoam.inventoryservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InventoryResponse {
    private String skuCode;
    private Boolean isInStock;
}
