package com.test.electronic.model.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "card_details")
public class CardDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardNumber;
    private String expirationDate;
    private String cvv;

    @Column(name = "user_id")
    private Long userId;

}

