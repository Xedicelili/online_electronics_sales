package com.test.electronic.service.impl;

import com.test.electronic.exception.*;
import com.test.electronic.mapper.CardDetailsMapper;
import com.test.electronic.mapper.OrderMapper;
import com.test.electronic.mapper.PaymentMapper;
import com.test.electronic.mapper.ProductMapper;
import com.test.electronic.model.dto.request.CardDetailsRequest;
import com.test.electronic.model.dto.request.OrderRequest;
import com.test.electronic.model.dto.request.PaymentRequest;
import com.test.electronic.model.dto.response.OrderResponse;
import com.test.electronic.model.dto.response.PaymentResponse;
import com.test.electronic.model.entity.*;
import com.test.electronic.model.enums.OrderStatus;
import com.test.electronic.model.enums.PaymentStatus;
import com.test.electronic.repository.CardDetailsRepository;
import com.test.electronic.repository.OrderRepository;
import com.test.electronic.repository.ProductRepository;
import com.test.electronic.repository.UserRepository;
import com.test.electronic.service.OrderService;
import com.test.electronic.service.PaymentService;
import com.test.electronic.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Data

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final ProductService productService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;
    private final ProductMapper productMapper;
    private final CardDetailsRepository cardDetailsRepository;
    private final CardDetailsMapper cardDetailsMapper;


    @Override
    public OrderResponse createOrder(OrderRequest request) {
        List<Product> products = retrieveProducts(request);
        AtomicReference<BigDecimal> totalPrice = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> discountedTotalPrice = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> totalDiscountAmount = new AtomicReference<>(BigDecimal.ZERO);

        Map<Long, Integer> stockChanges = new HashMap<>();
        Map<Long, Integer> originalStockQuantities = new HashMap<>();

        List<OrderResponse.ProductResponse> productResponses = calculatePricesAndUpdateStock(
                request, products, totalPrice, discountedTotalPrice, totalDiscountAmount, stockChanges, originalStockQuantities);
        Order order = buildOrder(request, products, totalPrice, discountedTotalPrice);
        Order savedOrder = orderRepository.save(order);

        try {
            handlePayment(request, savedOrder, stockChanges, originalStockQuantities);
            sendOrderConfirmationEmail(savedOrder);

            return orderMapper.toOrderResponse(savedOrder);

        } catch (RuntimeException e) {
            handlePaymentFailure(savedOrder, stockChanges, originalStockQuantities, e);
            throw new OrderCreationException("Order creation failed due to payment error.");
        }
    }

    private void handlePayment(OrderRequest request, Order savedOrder,
                               Map<Long, Integer> stockChanges, Map<Long, Integer> originalStockQuantities) {
        CardDetails cardDetails = extractAndSaveCardDetails(request, savedOrder);

        PaymentResponse paymentResponse = processPayment(request, savedOrder);

        if (paymentResponse.getPaymentStatus() == PaymentStatus.FAILED) {
            throw new PaymentProcessingException("Payment processing failed.");
        }

        Long cardId = cardDetails.getId();
        savePaymentDetails(paymentResponse, savedOrder, cardId);

        savedOrder.setStatus(OrderStatus.SUCCESS);
        orderRepository.save(savedOrder);

        applyStockChanges(stockChanges);
    }


    private CardDetails extractAndSaveCardDetails(OrderRequest request, Order savedOrder) {
        CardDetailsRequest cardDetailsRequest = request.getPaymentRequest().getCardDetailsRequest();
        CardDetails cardDetails = cardDetailsMapper.toEntity(cardDetailsRequest, savedOrder.getUser().getId());

        return cardDetailsRepository.save(cardDetails);
    }


    private PaymentResponse processPayment(OrderRequest request, Order savedOrder) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setCardDetailsRequest(request.getPaymentRequest().getCardDetailsRequest());
        return paymentService.processPayment(paymentRequest, savedOrder.getId());
    }


    private void savePaymentDetails(PaymentResponse paymentResponse, Order savedOrder, Long cardId) {
        Payment payment = paymentMapper.toPayment(paymentResponse, savedOrder, cardId);
        paymentService.savePayment(payment);
    }


    private void applyStockChanges(Map<Long, Integer> stockChanges) {
        stockChanges.forEach((productId, newQuantity) -> {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
            product.setStockQuantity(newQuantity);
            productRepository.save(product);
        });
    }

    private void handlePaymentFailure(Order savedOrder,
                                      Map<Long, Integer> stockChanges, Map<Long, Integer> originalStockQuantities, RuntimeException e) {
        log.error("Payment processing failed, order will be cancelled: {}", e.getMessage());
        savedOrder.setStatus(OrderStatus.FAILED);
        orderRepository.save(savedOrder);

        revertStockChanges(originalStockQuantities);
    }

    private void revertStockChanges(Map<Long, Integer> originalStockQuantities) {
        originalStockQuantities.forEach((productId, originalQuantity) -> {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
            product.setStockQuantity(originalQuantity);
            productRepository.save(product);
        });
    }


    @Transactional
    public List<OrderResponse.ProductResponse> calculatePricesAndUpdateStock(OrderRequest request, List<Product> products,
                                                                             AtomicReference<BigDecimal> totalPrice,
                                                                             AtomicReference<BigDecimal> discountedTotalPrice,
                                                                             AtomicReference<BigDecimal> totalDiscountAmount,
                                                                             Map<Long, Integer> stockChanges,
                                                                             Map<Long, Integer> originalStockQuantities) {

        return products.stream().map(product -> {
            OrderRequest.ProductOrder productOrder = request.getProducts().stream()
                    .filter(p -> p.getProductId().equals(product.getId()))
                    .findFirst()
                    .orElseThrow(() -> new ProductNotFoundException("Product not found in request"));

            int quantity = productOrder.getQuantity();

            BigDecimal productPrice = product.getDiscountedPrice() != null ? product.getDiscountedPrice() : product.getPrice();
            BigDecimal productTotalPrice = productPrice.multiply(BigDecimal.valueOf(quantity));

            totalPrice.updateAndGet(t -> t.add(productTotalPrice));

            if (product.getDiscountedPrice() != null) {
                BigDecimal originalProductTotalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
                BigDecimal discountAmount = originalProductTotalPrice.subtract(productTotalPrice);
                totalDiscountAmount.updateAndGet(t -> t.add(discountAmount));
            }

            discountedTotalPrice.updateAndGet(t -> t.add(productTotalPrice));
            if (product.getStockQuantity() < quantity) {
                throw new OutOfStockException("Insufficient stock for product: " + product.getName());
            }

            originalStockQuantities.put(product.getId(), product.getStockQuantity());
            stockChanges.put(product.getId(), product.getStockQuantity() - quantity);

            return productMapper.toProductResponse(product);
        }).collect(Collectors.toList());
    }


    private void sendOrderConfirmationEmail(Order order) {
        String subject = "Order Confirmation";
        String text = "Your order was successfully completed. Thank you for choosing us.";
        emailService.sendEmail(order.getUser().getEmail(), subject, text);
    }


    private List<Product> retrieveProducts(OrderRequest request) {
        return productRepository.findAllById(
                request.getProducts().stream()
                        .map(OrderRequest.ProductOrder::getProductId)
                        .collect(Collectors.toList())
        );
    }


    private Order buildOrder(OrderRequest request, List<Product> products, AtomicReference<BigDecimal> totalPrice, AtomicReference<BigDecimal> discountedTotalPrice) {
        Order order = orderMapper.toOrder(request);
        order.setTotalPrice(totalPrice.get());
        order.setOrderDate(LocalDate.now());
        order.setStatus(OrderStatus.PENDING);
        order.setUser(userRepository.findById(request.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found")));
        order.setProducts(products);
        return order;
    }





    @Override
    public Page<OrderResponse> getOrdersByUser(User user, Pageable pageable) {
        Pageable adjustedPageable = PageRequest.of(pageable.getPageNumber(), 10, pageable.getSort());
        Page<Order> orders;
        if (user.getAuthorities().stream().anyMatch(auth -> "ROLE_ADMIN".equals(auth.getName()))) {
            orders = orderRepository.findAll(adjustedPageable);
        } else {
            orders = orderRepository.findByUserId(user.getId(), adjustedPageable);
        }
        return orders.map(orderMapper::toOrderResponse);
    }

    @Override
    public void deleteOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        orderRepository.delete(order);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order Not Found"));
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse updateOrderById(Long id, OrderRequest request) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        orderMapper.updateOrderFromRequest(request, existingOrder);
        existingOrder.setOrderDate(LocalDate.now());
        List<Product> products = productRepository.findAllById(
                request.getProducts().stream()
                        .map(OrderRequest.ProductOrder::getProductId)
                        .collect(Collectors.toList())
        );
        existingOrder.setProducts(products);

        Order updatedOrder = orderRepository.save(existingOrder);
        return orderMapper.toOrderResponse(updatedOrder);
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        Pageable adjustedPageable = PageRequest.of(pageable.getPageNumber(), 10, pageable.getSort());
        return orderRepository.findAll(adjustedPageable)
                .map(orderMapper::toOrderResponse);
    }
}