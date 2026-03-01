package com.scaracat.shopping_cart.service.cart;

import java.math.BigDecimal;

import com.scaracat.shopping_cart.model.Cart;

public interface ICartService {

	// handling cart
	
	Cart getCart(Long id);
	void clearCart(Long id);
	BigDecimal getTotalPrice(Long id);
	Long initializeNewCart();
	
	
	// ---------------------------------------
	// handling cart items
	
	
	void addItemToCart(Long cartId, Long productId, int quantity);
	void removeItemFromCart(Long cartId, Long productId);
	void updateItemQuantity(Long cartId, Long productId, int quantity);
	Cart getCartByUserId(Long userId);
	

	
}
