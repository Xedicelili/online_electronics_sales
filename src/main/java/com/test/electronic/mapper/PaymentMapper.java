package com.test.electronic.mapper;


import com.test.electronic.model.dto.response.PaymentResponse;
import com.test.electronic.model.entity.CardDetails;
import com.test.electronic.model.entity.Order;
import com.test.electronic.model.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "paymentResponse.paymentStatus", target = "paymentStatus")
    @Mapping(source = "savedOrder.totalPrice", target = "amount")
    @Mapping(source = "savedOrder", target = "order")
    @Mapping(expression = "java(java.time.LocalDate.now())", target = "paymentDate")
    @Mapping(target = "cardDetails", expression = "java(getCardDetails(cardId))") // Map cardDetails using cardId
    Payment toPayment(PaymentResponse paymentResponse, Order savedOrder, Long cardId);
    default CardDetails getCardDetails(Long cardId) {
        if (cardId == null) {
            return null;
        }
        CardDetails cardDetails = new CardDetails();
        cardDetails.setId(cardId); // Assuming CardDetails has a setId method
        return cardDetails;
    }


    PaymentResponse toPaymentResponse(Payment payment);

}


