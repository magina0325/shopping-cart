package com.scaracat.shopping_cart.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.scaracat.shopping_cart.dto.OrderDto;
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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

	private final CartService cartService;
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final ModelMapper modelMapper;
	
	@Override
	@Transactional
	public OrderDto placeOrder(Long userId) {
		
		// create and save the order
		Cart cart = cartService.getCartByUserId(userId);
		Order order = this.createOrder(cart);
		order.setItems(this.createOrderItems(order, cart));
		order.setTotalAmount(this.calculateTotalAmount(order.getItems()));
		order = this.orderRepository.save(order);
		
		// remove the items in the order from the cart
		this.cartService.clearCartItems(this.cartService.getCartByUserId(userId).getId());
		
		
		return this.convertOrderToOrderDto(order);
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
	public OrderDto getOrder(Long orderId) {
		return orderRepository.findById(orderId).map(this::convertOrderToOrderDto)
				.orElseThrow(() -> new ResourceNotFoundException("Order with ID: " + orderId + " does not exist."));
	}
	
	@Override
	public List<OrderDto> getOrdersOfUser(Long userId) {
		List<Order> orders =  this.orderRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Orders not found"));
		
		return orders.stream().map(this::convertOrderToOrderDto).toList();
	}

	private OrderDto convertOrderToOrderDto(Order order) {
		return modelMapper.map(order, OrderDto.class);
	}
	
	// ====================
	// order item ==========================
	// =====================
	
//	public OrderItem addItemToOrder() {
//		return null;
//	}
	
	
}



























