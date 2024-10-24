package com.test.electronic.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.test.electronic.model.enums.PromotionStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "promotions")
public class Promotion {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String title;
        private String description;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate startDate;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate endDate;


    @Column(name = "discount_price", precision = 38, scale = 2)
    private BigDecimal discountPrice;



    @ManyToOne
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Category category;



        @Enumerated(EnumType.STRING)
        @Column(name = "status")
        private PromotionStatus status;




        public void updateStatus() {
            LocalDate today = LocalDate.now();
            if (startDate != null && endDate != null) {
                if (today.isBefore(startDate) || today.isAfter(endDate)) {
                    this.status = PromotionStatus.INACTIVE;
                } else {
                    this.status = PromotionStatus.ACTIVE;
                }
            } else {
                this.status = PromotionStatus.INACTIVE;
            }
        }



}


