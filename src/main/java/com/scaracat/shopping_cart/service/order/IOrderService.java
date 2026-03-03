package com.scaracat.shopping_cart.service.order;

import java.util.List;

import com.scaracat.shopping_cart.dto.OrderDto;




public interface IOrderService {
	
	// order ===============
	
	OrderDto placeOrder(Long userId);
	OrderDto getOrder(Long orderId);
	List<OrderDto> getOrdersOfUser(Long userId);
	
	// orderitem ============
	
	
	
}
