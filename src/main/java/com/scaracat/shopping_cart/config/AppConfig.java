package com.scaracat.shopping_cart.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.scaracat.shopping_cart.dto.CartDto;
import com.scaracat.shopping_cart.dto.CartItemDto;
import com.scaracat.shopping_cart.dto.ProductDto;
import com.scaracat.shopping_cart.model.Cart;
import com.scaracat.shopping_cart.model.CartItem;
import com.scaracat.shopping_cart.model.Category;
import com.scaracat.shopping_cart.model.Product;

@Configuration
public class AppConfig {
	
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		
		// configure mapping from category object to category name in string
		configureProductMapping(modelMapper);
		
		// add an extra totalAmount field which is derived in the db and model
		configureCartMapping(modelMapper);
		
		
		
		return modelMapper;
	}
	
	private void configureProductMapping(ModelMapper modelMapper) {
		Converter<Category, String> categoryConverter = (context) -> {
			Category source = context.getSource();
			return (source != null) ? source.getName() : null;
		};
		modelMapper.typeMap(Product.class, ProductDto.class).addMappings(mapper -> {
			mapper.using(categoryConverter)
				.map(Product::getCategory, ProductDto::setCategory);
		});
	}
	
	private void configureCartMapping(ModelMapper modelMapper) {
		modelMapper.createTypeMap(Cart.class, CartDto.class)
			.addMappings(mapper -> {
				mapper.map(src -> src.getTotalAmount(), CartDto::setTotalAmount);
			});
	}
	
	
	
	
}
