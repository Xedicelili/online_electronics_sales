package com.test.electronic.model.dto.request;

import lombok.Data;

@Data
public class CardDetailsRequest {
        private String cardNumber;
        private String expirationDate;

    private String cvv;
    public boolean validateCardDetails() {
        if (cardNumber == null || cardNumber.length() != 16) {
            return false;
        }
        if (cvv == null || cvv.length() != 3) {
            return false;
        }
        if (!isExpiryDateValid()) {
            return false;
        }
        return true;
    }

    private boolean isExpiryDateValid() {
        if (expirationDate == null || !expirationDate.matches("(0[1-9]|1[0-2])/([0-9]{2})")) {
            return false;
        }
        return true;
    }
}




