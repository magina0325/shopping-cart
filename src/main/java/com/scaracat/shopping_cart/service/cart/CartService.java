package com.scaracat.shopping_cart.service.cart;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.scaracat.shopping_cart.exception.AlreadyExistException;
import com.scaracat.shopping_cart.exception.InsufficientInventoryException;
import com.scaracat.shopping_cart.exception.ResourceNotFoundException;
import com.scaracat.shopping_cart.model.Cart;
import com.scaracat.shopping_cart.model.CartItem;
import com.scaracat.shopping_cart.model.Product;
import com.scaracat.shopping_cart.model.User;
import com.scaracat.shopping_cart.repo.CartItemRepository;
import com.scaracat.shopping_cart.repo.CartRepository;
import com.scaracat.shopping_cart.repo.ProductRepository;
import com.scaracat.shopping_cart.service.product.ProductService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductService productService;
	
	@Override
	public Cart getCart(Long cartId) {
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
		
		return cart;
	}

	@Override
	public BigDecimal getTotalPrice(Long id) {
		return this.getCart(id).getTotalAmount();
	}

	@Override
	public Cart getCartByUserId(Long userId) {
		return cartRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
	}
	
	// ---------------------------------------
	// handling cart items
	
	// TODO: handle product quantity sufficient before adding/updating
	// no need to subtract, only subtract when placing order
	
	@Override
	@Transactional
	public void clearCartItems(Long id) {
		Cart cart = this.getCart(id);
		cartItemRepository.deleteAllByCartId(id);
		cart.getItems().clear();
	}
	
	@Override
	public void addItemToCart(Long cartId, Long productId, int quantity) {
		
		Cart cart = this.getCart(cartId);
		Product product = productService.getProductById(productId); 
		
		// get the cart item if exists
		CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId)
				.orElse(null);
		
		// initialize new cart
		if (cartItem == null) {
			cartItem = new CartItem(product, 0);
		}
		
		// check for inventory
		if (cartItem.getQuantity() + quantity > product.getInventory()) {
			throw new InsufficientInventoryException("There is not enough inventory for the selected product.");
		}
		cartItem.setQuantity(cartItem.getQuantity() + quantity);
		
		cartRepository.save(cart);
	}

	@Override
	public void removeItemFromCart(Long cartId, Long productId) {
		Cart cart = this.getCart(cartId);
		
		// to check if product actually exists
		productService.getProductById(productId);
		
		cartItemRepository.findByCartIdAndProductId(cartId, productId)
				.ifPresentOrElse(cart::removeItem, () -> {
					throw new ResourceNotFoundException("The product with ID: " + productId + " does not exist in the cart.");
				});;
		
		cartRepository.save(cart);
	}

	@Override
	public void updateItemQuantity(Long cartId, Long productId, int quantity) {
		Cart cart = this.getCart(cartId);
		
		// to check if product actually exists
		productService.getProductById(productId);
		
		CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId)
				.orElseThrow(() -> new ResourceNotFoundException("The product with ID: " + productId + " does not exist in the cart."));
		
		cartItem.setQuantity(quantity);
		cartItemRepository.save(cartItem);
	}

	@Override
	public Cart initializeNewCart(User user) {
		return cartRepository.findByUserId(user.getId())
			.orElseGet(() -> {
				Cart cart = new Cart();
				cart.setUser(user);
				return cartRepository.save(cart);
			});
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
