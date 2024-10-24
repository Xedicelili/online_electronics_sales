package com.test.electronic.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name ="category")
public class Category {
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "category",fetch = FetchType.LAZY)
    private List<Product> products;

    @OneToMany(mappedBy = "category",fetch = FetchType.LAZY)
    private List<Promotion> promotions;


    public Promotion getPromotion() {
        return null;
    }
}
