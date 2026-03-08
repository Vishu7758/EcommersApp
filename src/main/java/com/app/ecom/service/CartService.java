package com.app.ecom.service;

import com.app.ecom.dto.CartItemRequestDto;
import com.app.ecom.entities.CartItem;
import com.app.ecom.entities.Product;
import com.app.ecom.entities.User;
import com.app.ecom.repository.CartItermRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final ProductRepository productRepository;
    private final CartItermRepository cartItermRepository;
    private final UserRepository userRepository;

    public boolean addToCart(String userId, CartItemRequestDto request) {
        Optional<Product> productOpt = productRepository.findById(request.getProductId());
        if(productOpt.isEmpty()) {
            return false;
        }
        Product product = productOpt.get();
        if(product.getStockQuantity() < request.getQuantity()) {
            return false;
        }
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if(userOpt.isEmpty()) {
            return false;
        }
        User user = userOpt.get();
        CartItem existingCartItem = cartItermRepository.findByUserAndProduct(user, product);
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItermRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItermRepository.save(cartItem);
        }
        return true;
    }

    public boolean deleeteItemFromCart(String userId, Long productId) {
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if(userOpt.isEmpty()) {
            return false;
        }
        User user = userOpt.get();
        Optional<Product> productOpt = productRepository.findById(productId);
        if(productOpt.isEmpty()) {
            return false;
        }
        Product product = productOpt.get();
        CartItem cartItem = cartItermRepository.deleteByUserAndProduct(user, product);
        return cartItem != null;
    }

    public List<CartItem> getCartItems(String userId) {
        return userRepository.findById(Long.valueOf(userId))
                .map(cartItermRepository::findByUser)
                .orElseGet(List::of);
    }

    public void clearCart(String userId) {
        userRepository.findById(Long.valueOf(userId))
                .ifPresent(cartItermRepository::deleteByUser);
    }
}
