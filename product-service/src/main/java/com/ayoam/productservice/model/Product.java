package com.ayoam.productservice.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Primary;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity(name="Product")
public class Product {
    @Id
    @NotNull
    private String sku;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Double price;
}
