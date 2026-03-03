package com.scaracat.shopping_cart.service.cart;

import java.math.BigDecimal;

import com.scaracat.shopping_cart.model.Cart;
import com.scaracat.shopping_cart.model.User;

public interface ICartService {

	// handling cart
	
	Cart getCart(Long id);
	void clearCartItems(Long id);
	BigDecimal getTotalPrice(Long id);
	Cart initializeNewCart(User user);
	Cart getCartByUserId(Long userId);
	
	// ---------------------------------------
	// handling cart items
	
	void addItemToCart(Long cartId, Long productId, int quantity);
	void removeItemFromCart(Long cartId, Long productId);
	void updateItemQuantity(Long cartId, Long productId, int quantity);
	
	
}
