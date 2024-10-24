package com.test.electronic.model.dto.response;

import lombok.Data;

@Data
public class CardDetailsResponse {

        private String cardNumber;
        private String cardHolderName;
        private String expirationDate;
        private String expiryDate;
        private Long userId;


}
