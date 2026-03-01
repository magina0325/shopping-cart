package com.scaracat.shopping_cart.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class CartItem {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private int quantity = 0;

	@ManyToOne
	@JoinColumn(name="product_id")
	@JsonManagedReference
	private Product product;
	
	@ManyToOne
	@JoinColumn(name="cart_id")
	@JsonBackReference
	private Cart cart;
	
	public CartItem(Product product) {
		this.product = product;
		
	}
	
	public CartItem(Product product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}
	
	public BigDecimal getUnitPrice() {
		return this.product == null ? BigDecimal.ZERO : product.getPrice();
	}
	
	public BigDecimal getTotalPrice() {
		return this.getUnitPrice().multiply(BigDecimal.valueOf(quantity));
	}
	
}
