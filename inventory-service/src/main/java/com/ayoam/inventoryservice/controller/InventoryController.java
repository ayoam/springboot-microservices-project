package com.ayoam.inventoryservice.controller;

import com.ayoam.inventoryservice.dto.InventoryResponse;
import com.ayoam.inventoryservice.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InventoryController {
    private InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/inventory")
    public ResponseEntity<List<InventoryResponse>> checkInventory(@RequestParam(name="sku") List<String> skuCodeList) throws Exception {
        return new ResponseEntity<List<InventoryResponse>>(inventoryService.isInStock(skuCodeList), HttpStatus.OK);
    }
}
