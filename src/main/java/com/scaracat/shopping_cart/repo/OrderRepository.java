package com.scaracat.shopping_cart.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scaracat.shopping_cart.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	Optional<List<Order>> findByUserId(Long userId);

}
