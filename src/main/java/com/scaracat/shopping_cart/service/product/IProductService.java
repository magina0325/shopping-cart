package com.scaracat.shopping_cart.service.product;

import java.util.List;

import com.scaracat.shopping_cart.dto.ProductDto;
import com.scaracat.shopping_cart.model.Product;
import com.scaracat.shopping_cart.request.AddProductRequest;
import com.scaracat.shopping_cart.request.ProductUpdateRequest;

public interface IProductService {
	
	Product addProduct(AddProductRequest product);
	
	Product getProductById(Long id);
	List<Product> getAllProducts();
	
	List<Product> getProductsByCategory(String category);
	List<Product> getProductsByBrand(String brand);
	List<Product> getProductsByCategoryAndBrand(String category, String brand);
	List<Product> getProductsByName(String name);
	List<Product> getProductsByBrandAndName(String brand, String name);
	
	void deleteProductById(Long id);
	
	Product updateProduct(ProductUpdateRequest product, Long productId);
	
	Long countProductsByBrandAndName(String brand, String name);

	ProductDto convertToDto(Product product);

	List<ProductDto> convertToDto(List<Product> products);
}
