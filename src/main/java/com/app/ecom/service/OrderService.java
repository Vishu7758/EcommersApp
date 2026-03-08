package com.app.ecom.service;

import com.app.ecom.dto.OrderItmDTO;
import com.app.ecom.dto.OrderResponse;
import com.app.ecom.entities.CartItem;
import com.app.ecom.entities.Order;
import com.app.ecom.entities.OrderItem;
import com.app.ecom.entities.User;
import com.app.ecom.enums.OrderStatus;
import com.app.ecom.repository.OrderRepository;
import com.app.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartService cartService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    public Optional<OrderResponse> createOrder(String userId) {
        // validate for cart items
        List<CartItem> cartItems = cartService.getCartItems(userId);
        if (cartItems.isEmpty()) {
            return Optional.empty();
        }
        // validate for user
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
         User user = userOpt.get();
        // calculate total amount
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // create order
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProduct(),
                        order,
                        item.getQuantity(),
                        item.getPrice()
                )).toList();
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        // clear the cart
        cartService.clearCart(userId);
        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order savedOrder) {
        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getTotalAmount(),
                savedOrder.getStatus(),
                savedOrder.getItems().stream()
                        .map(item -> new OrderItmDTO(
                                item.getId(),
                                item.getProduct().getId(),
                                item.getQuantity(),
                                item.getPrice(),
                                item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                                )
                        ).toList(),
                savedOrder.getCreatedAt()
        );
    }
}
