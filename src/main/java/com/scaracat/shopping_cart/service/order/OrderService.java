package com.scaracat.shopping_cart.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.scaracat.shopping_cart.enums.OrderStatus;
import com.scaracat.shopping_cart.exception.ResourceNotFoundException;
import com.scaracat.shopping_cart.model.Cart;
import com.scaracat.shopping_cart.model.Order;
import com.scaracat.shopping_cart.model.OrderItem;
import com.scaracat.shopping_cart.model.Product;
import com.scaracat.shopping_cart.repo.OrderItemRepository;
import com.scaracat.shopping_cart.repo.OrderRepository;
import com.scaracat.shopping_cart.repo.ProductRepository;
import com.scaracat.shopping_cart.service.cart.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

	private final CartService cartService;
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ProductRepository productRepository;
	
	@Override
	public Order placeOrder(Long userId) {
		
		// create and save the order
		Cart cart = cartService.getCartByUserId(userId);
		Order order = this.createOrder(cart);
		order.setItems(this.createOrderItems(order, cart));
		order.setTotalAmount(this.calculateTotalAmount(order.getItems()));
		order = this.orderRepository.save(order);
		
		// remove the items in the order from the cart
		this.cartService.clearCart(this.cartService.getCartByUserId(userId).getId());
		
		
		return order;
	}
	
	private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
		return orderItemList.stream()
				.map(orderItem -> orderItem.getPrice()
						.multiply(BigDecimal.valueOf(orderItem.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
				
		/*
		BigDecimal res = BigDecimal.ZERO;
		for (OrderItem orderItem : orderItemList) {
			res.add(orderItem.getPrice()
					.multiply(BigDecimal.valueOf(orderItem.getQuantity())));
		}
		return res;
		*/
	}
	
	private List<OrderItem> createOrderItems(Order order, Cart cart) {
		return cart.getItems().stream().map(cartItem -> {
			Product product = cartItem.getProduct();
			// TODO: handle insufficient inventory
			product.setInventory(product.getInventory() - cartItem.getQuantity());
			productRepository.save(product);
			
			return new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity(), cartItem.getUnitPrice());
		}).toList();
	}
	
	private Order createOrder(Cart cart) {
		Order res = new Order();
		res.setUser(cart.getUser());
		res.setOrderStatus(OrderStatus.PENDING);
		res.setOrderDate(LocalDate.now());
		return res;
	}

	@Override
	public Order getOrder(Long orderId) {
		return orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order with ID: " + orderId + " does not exist."));
	}
	
	@Override
	public List<Order> getOrdersOfUser(Long userId) {
		return this.orderRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Orders not found"));
	}

	// ====================
	// order item ==========================
	// =====================
	
//	public OrderItem addItemToOrder() {
//		return null;
//	}
	
	
}



























