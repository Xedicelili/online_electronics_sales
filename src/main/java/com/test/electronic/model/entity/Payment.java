package com.test.electronic.model.entity;

import com.test.electronic.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String amount;
    private LocalDate paymentDate;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    private String companyBankAccount = "COMPANY_BANK_ACCOUNT";





    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "card_details_id")
    private CardDetails cardDetails;




        @ManyToOne
        @JoinColumn(name = "user_id") // Ensure this column name matches your database schema
        private User user;

        // Other fields
    }



