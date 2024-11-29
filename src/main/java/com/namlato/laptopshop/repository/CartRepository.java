package com.namlato.laptopshop.repository;

import com.namlato.laptopshop.domain.Cart;
import com.namlato.laptopshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
