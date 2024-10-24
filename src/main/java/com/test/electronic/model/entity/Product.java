package com.test.electronic.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Product name cannot be empty")
    private String name;

    private String color;
    private String warrantyPeriod;
    private Integer stockQuantity;


    @Column(name = "price", precision = 38, scale = 2)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Category category;



    @ManyToMany(mappedBy = "products")
    private List<Comparison> comparisons;

    @ManyToMany(mappedBy = "products",cascade = CascadeType.ALL)
    private List<Order> orders;

    private BigDecimal discountedPrice;

    public void applyDiscount(BigDecimal discountPercentage) {
        if (discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discountAmount = price.multiply(discountPercentage).divide(BigDecimal.valueOf(100));
            this.discountedPrice = price.subtract(discountAmount);
        } else {
            this.discountedPrice = price;
        }
    }



}



