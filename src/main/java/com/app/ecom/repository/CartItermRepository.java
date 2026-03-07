package com.app.ecom.repository;

import com.app.ecom.entities.CartItem;
import com.app.ecom.entities.Product;
import com.app.ecom.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItermRepository extends JpaRepository<CartItem, Long> {
    CartItem findByUserAndProduct(User user, Product product);

    CartItem deleteByUserAndProduct(User user, Product product);

    List<CartItem> findByUser(User user);
}
