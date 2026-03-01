package com.scaracat.shopping_cart.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Cart {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	// to tell JPA not to create this column
	//@Transient
	//private BigDecimal totalAmount = BigDecimal.ZERO;
	
	@OneToMany(mappedBy="cart", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private Set<CartItem> items = new HashSet<>();
	
	@OneToOne
	@JsonBackReference
	@JoinColumn(name = "user_id")
	private User user;
	
	public void addItem(CartItem item) {
		this.items.add(item);
		item.setCart(this);
		//this.totalAmount.add(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
	}
	
	public boolean removeItem(CartItem item) {
		return this.items.remove(item);
		//this.totalAmount.subtract(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
	}
	
	public BigDecimal getTotalAmount() {
		return items.stream()
				.map(item -> item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	
	// scraped delta update
//	@PostLoad
//	private void calculateTotalAmount() {
//		this.totalAmount = items.stream()
//				.map(item -> item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())))
//				.reduce(BigDecimal.ZERO, BigDecimal::add);
//	}
//	public void updateItemQuantity(CartItem item, int prevQuantity) {
//		int quantityDiff = item.getQuantity() - prevQuantity;
//		BigDecimal priceDiff = item.getUnitPrice().multiply(BigDecimal.valueOf(quantityDiff));
//		this.totalAmount.add(priceDiff);
//	}
	
	
}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

