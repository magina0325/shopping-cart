package com.scaracat.shopping_cart.request;

import java.math.BigDecimal;

import com.scaracat.shopping_cart.model.Category;

import lombok.Data;

@Data
public class AddProductRequest {
	private Long id;
	private String name;
	private String brand;
	private BigDecimal price;
	private int inventory;
	private String description;
	private String category;
}











