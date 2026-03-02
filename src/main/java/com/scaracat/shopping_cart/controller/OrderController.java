package com.scaracat.shopping_cart.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scaracat.shopping_cart.exception.ResourceNotFoundException;
import com.scaracat.shopping_cart.model.Order;
import com.scaracat.shopping_cart.response.ApiResponse;
import com.scaracat.shopping_cart.service.order.IOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

	private final IOrderService orderService;
	
	@PostMapping("/order")
	public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
		Order order;
		try {
			order = orderService.placeOrder(userId);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
		}
		
		return ResponseEntity.ok(new ApiResponse("Item order success.", order));
	}
	
	@GetMapping("/order/{orderId}")
	public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
		Order order;
		try {
			order = orderService.getOrder(orderId);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
		}
		return ResponseEntity.ok(new ApiResponse("Success", order));
	}
	
	@GetMapping("/order/{userId}")
	public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
		List<Order> res = null;
		try {
			res = orderService.getOrdersOfUser(userId);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
		}
		return ResponseEntity.ok(new ApiResponse("Success", res));
		
	}
	
	
	
}


















