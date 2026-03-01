package com.scaracat.shopping_cart.service.cart;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.scaracat.shopping_cart.exception.AlreadyExistException;
import com.scaracat.shopping_cart.exception.ResourceNotFoundException;
import com.scaracat.shopping_cart.model.Cart;
import com.scaracat.shopping_cart.model.CartItem;
import com.scaracat.shopping_cart.model.Product;
import com.scaracat.shopping_cart.repo.CartItemRepository;
import com.scaracat.shopping_cart.repo.CartRepository;
import com.scaracat.shopping_cart.repo.ProductRepository;
import com.scaracat.shopping_cart.service.product.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductService productService;
	private final AtomicLong cartIdGenerator = new AtomicLong(0);
	
	@Override
	public Cart getCart(Long cartId) {
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
		
		return cart;
	}

	@Override
	public Cart getCartByUserId(Long userId) {
		return cartRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
	}
	
	@Override
	public void clearCart(Long id) {
		Cart cart = this.getCart(id);
		cartItemRepository.deleteAllByCartId(id);
		cart.getItems().clear();
		cartRepository.deleteById(id);
	}

	@Override
	public BigDecimal getTotalPrice(Long id) {
		return this.getCart(id).getTotalAmount();
	}

	
	// ---------------------------------------
	// handling cart items
	
	// TODO: handle product quantity sufficient before adding/updating
	// no need to subtract, only subtract when placing order
	
	@Override
	public void addItemToCart(Long cartId, Long productId, int quantity) {
		Cart cart = this.getCart(cartId);
		Product product = productService.getProductById(productId); 
				
		cartItemRepository.findByCartIdAndProductId(cartId, productId)
				.ifPresentOrElse((cartItem) -> {
					throw new AlreadyExistException("Product already exist in the cart.");
				}, () -> {
					CartItem cartItem = new CartItem(product, quantity);
					cart.addItem(cartItem);
				});
		
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
	public Long initializeNewCart() {
		Cart newCart = new Cart();
		return cartRepository.save(newCart).getId();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
