package com.test.electronic.model.entity;


import com.test.electronic.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "product_quantity", nullable = false)
    private int productQuantity;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;



    @Enumerated(STRING)
    private OrderStatus status;


    @ManyToOne
    @ToStringExclude
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToMany
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id")
    )
    private List<Product> products;


}








