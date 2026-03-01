package com.scaracat.shopping_cart.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scaracat.shopping_cart.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{
	
	Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId); 
	void deleteAllByCartId(Long id);


}
