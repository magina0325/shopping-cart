package com.scaracat.shopping_cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import com.scaracat.shopping_cart.exception.ResourceNotFoundException;
import com.scaracat.shopping_cart.model.Cart;
import com.scaracat.shopping_cart.model.User;
import com.scaracat.shopping_cart.response.ApiResponse;
import com.scaracat.shopping_cart.service.cart.ICartService;
import com.scaracat.shopping_cart.service.user.IUserService;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/carts")
@RequiredArgsConstructor
public class CartController {

	private final ICartService cartService;
	private final IUserService userService;

	@GetMapping("/get/{cartId}")
	public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId) {
		Cart cart;
		try {
			cart = cartService.getCart(cartId);
		} catch(ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch(Exception e) {
			return ResponseEntity.internalServerError()
					.body(new ApiResponse(e.getMessage(), null));
		}
		return ResponseEntity.ok(new ApiResponse("Success.", cart));
	}
	
	@DeleteMapping("/clear/{cartId}")
	public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
		try {
			cartService.clearCartItems(cartId);
		} catch(ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch(Exception e) {
			return ResponseEntity.internalServerError()
					.body(new ApiResponse(e.getMessage(), null));
		}
		return ResponseEntity.ok(new ApiResponse("Success.", null));
	}
	
	@GetMapping("/get-total-price/{cartId}")
	public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId) {
		try {
			cartService.getTotalPrice(cartId);
		} catch(ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch(Exception e) {
			return ResponseEntity.internalServerError()
					.body(new ApiResponse(e.getMessage(), null));
		}
		return ResponseEntity.ok(new ApiResponse("Success.", null));
	}
	
	// --------------
	// CART ITEM
	// -----------------
	
	@PostMapping("/add-item")
	public ResponseEntity<ApiResponse> addItemToCart(
			@RequestParam Long productId, 
			@RequestParam int quantity) {
		try {
			
			User user = userService.getAuthenticatedUser();
			Cart cart = cartService.initializeNewCart(user);
			
			cartService.addItemToCart(cart.getId(), productId, quantity);
		} catch(ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch(JwtException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
		} catch(Exception e) {
			return ResponseEntity.internalServerError()
					.body(new ApiResponse(e.getMessage(), null));
		}
		
		return ResponseEntity.ok(new ApiResponse("Success.", null));
	}
	
	@DeleteMapping("/delete-item")
	public ResponseEntity<ApiResponse> removeItemFromCart(
			@RequestParam Long cartId, 
			@RequestParam Long productId) {
		try {
			cartService.removeItemFromCart(cartId, productId);
		}  catch(ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch(Exception e) {
			return ResponseEntity.internalServerError()
					.body(new ApiResponse(e.getMessage(), null));
		}
		
		return ResponseEntity.ok(new ApiResponse("Success.", null));
	}
	
	@PutMapping("/update-item")
	public ResponseEntity<ApiResponse> updateItemQuantity(
			@RequestParam Long cartId, 
			@RequestParam Long productId, 
			@RequestParam int quantity) {
		try {
			cartService.updateItemQuantity(cartId, productId, quantity);
		}  catch(ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch(Exception e) {
			return ResponseEntity.internalServerError()
					.body(new ApiResponse(e.getMessage(), null));
		}
		
		return ResponseEntity.ok(new ApiResponse("Success.", null));
	}
	
}




















