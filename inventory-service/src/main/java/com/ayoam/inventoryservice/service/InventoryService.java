package com.ayoam.inventoryservice.service;

import com.ayoam.inventoryservice.dto.InventoryResponse;
import com.ayoam.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    private InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<InventoryResponse> isInStock(List<String> skuCodeList) throws Exception {
        List<InventoryResponse> inventoryResponseList= inventoryRepository.findBySkuCodeIn(skuCodeList).stream().map(
                inventory->InventoryResponse.builder().skuCode(inventory.getSkuCode()).isInStock(inventory.getQuantity()>0).build()
        ).collect(Collectors.toList());

        if(skuCodeList.size()!=inventoryResponseList.size()){
            throw new Exception("some products does not exist in database!");
        }

        return inventoryResponseList;
    }
}
