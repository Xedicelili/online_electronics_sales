package com.test.electronic.service.impl;

import com.test.electronic.mapper.CardDetailsMapper;
import com.test.electronic.mapper.PaymentMapper;
import com.test.electronic.model.dto.request.CardDetailsRequest;
import com.test.electronic.model.dto.request.PaymentRequest;
import com.test.electronic.model.dto.response.PaymentResponse;
import com.test.electronic.model.entity.CardDetails;
import com.test.electronic.model.entity.Payment;
import com.test.electronic.model.entity.User;
import com.test.electronic.model.enums.PaymentStatus;
import com.test.electronic.repository.CardDetailsRepository;
import com.test.electronic.repository.PaymentRepository;
import com.test.electronic.service.PaymentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
@Data
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final CardDetailsRepository cardDetailsRepository;
    private final CardDetailsMapper cardDetailsMapper;
    private static final String COMPANY_BANK_ACCOUNT = "1458963978529485";


    @Override
    public PaymentResponse processPayment(PaymentRequest paymentRequest, Long orderId) {
        CardDetailsRequest cardDetails = paymentRequest.getCardDetailsRequest();

        if (!cardDetails.validateCardDetails()) {
            log.info("Card details are incorrect: {}", cardDetails.getCardNumber());
            return createFailedPaymentResponse();
        }

        log.info("Payment successful: {}", cardDetails.getCardNumber());
        saveCardDetails(cardDetails);
        return createSuccessfulPaymentResponse(paymentRequest);
    }

    @Override
    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }


    private void saveCardDetails(CardDetailsRequest cardDetailsRequest) {
        CardDetails cardDetails = cardDetailsMapper.toEntity(cardDetailsRequest);
        cardDetailsRepository.save(cardDetails);
    }


    private PaymentResponse createSuccessfulPaymentResponse(PaymentRequest paymentRequest) {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentDate(LocalDate.now());
        response.setPaymentStatus(PaymentStatus.SUCCESS);
        response.setCompanyBankAccount(COMPANY_BANK_ACCOUNT);
        response.setMessage("Payment successfully completed.");
        return response;
    }

    private PaymentResponse createFailedPaymentResponse() {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentStatus(PaymentStatus.FAILED);
        response.setMessage("Your card details are incorrect.");
        return response;
    }


    @Override
    public Page<PaymentResponse> getPaymentsByUser(User user, Pageable pageable) {
        Pageable adjustedPageable = PageRequest.of(pageable.getPageNumber(), 10, pageable.getSort());
        Page<Payment> payments;

        if (user.getAuthorities().stream().anyMatch(auth -> "ROLE_ADMIN".equals(auth.getName()))) {
            payments = paymentRepository.findAll(adjustedPageable);
        } else {
            payments = paymentRepository.findByUser(user, adjustedPageable);
        }

        return payments.map(paymentMapper::toPaymentResponse);
    }


}




