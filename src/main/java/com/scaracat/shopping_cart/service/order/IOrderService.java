package com.scaracat.shopping_cart.service.order;

import java.util.List;

import com.scaracat.shopping_cart.model.Order;

public interface IOrderService {
	
	// order ===============
	
	Order placeOrder(Long userId);
	Order getOrder(Long orderId);
	List<Order> getOrdersOfUser(Long userId);
	
	// orderitem ============
	
	
	
}
