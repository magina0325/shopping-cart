package com.scaracat.shopping_cart.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scaracat.shopping_cart.dto.ImageDto;
import com.scaracat.shopping_cart.dto.ProductDto;
import com.scaracat.shopping_cart.exception.AlreadyExistException;
import com.scaracat.shopping_cart.exception.ResourceNotFoundException;
import com.scaracat.shopping_cart.model.Product;
import com.scaracat.shopping_cart.request.AddProductRequest;
import com.scaracat.shopping_cart.request.ProductUpdateRequest;
import com.scaracat.shopping_cart.response.ApiResponse;
import com.scaracat.shopping_cart.service.category.CategoryService;
import com.scaracat.shopping_cart.service.product.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;
	
	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAllProducts() {
		List<Product> products;
		try {
			products = productService.getAllProducts();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
		}
		
		return ResponseEntity.ok(new ApiResponse("Success.", productService.convertToDto(products)));
	}
	
	@GetMapping("/get/{productId}")
	public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
		Product product;
		try {
			product = productService.getProductById(productId);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
		}
		
		return ResponseEntity.ok(new ApiResponse("Success", productService.convertToDto(product)));
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest addProductRequest) {
		Product product;
		try {
			product = productService.addProduct(addProductRequest);
		} catch (AlreadyExistException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null)) ;
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
		}
		
		return ResponseEntity.ok(new ApiResponse("Product added.", product));
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/update/{productId}")
	public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest productUpdateRequest, @PathVariable Long productId) {
		Product product;
		try {
			product = this.productService.updateProduct(productUpdateRequest, productId);
		} catch(ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
		}
		return ResponseEntity.ok(new ApiResponse("Product updated.", product));
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/delete/{productId}")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
		try {
			productService.deleteProductById(productId);
		} catch(ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch(Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
		}
		return ResponseEntity.ok(new ApiResponse("Product with product Id: " + productId + " deleted successfully.", null));
	}
	
	@GetMapping("/get-by-brand-and-name")
	public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
		try {
			List<Product> res = this.productService.getProductsByBrandAndName(brand, name);
			if (res.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(new ApiResponse("Success", productService.convertToDto(res)));
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@GetMapping("/get-by-brand")
	public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brand) {
		try {
			List<Product> res = this.productService.getProductsByBrand(brand);
			if (res.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(new ApiResponse("Success", productService.convertToDto(res)));
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@GetMapping("/get-by-category-and-brand")
	public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
		try {
			List<Product> res = this.productService.getProductsByCategoryAndBrand(category, brand);
			return ResponseEntity.ok(new ApiResponse("Success", productService.convertToDto(res)));
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@GetMapping("/get-by-name")
	public ResponseEntity<ApiResponse> getProductsByName(@RequestParam String name) {
		try {
			List<Product> res = this.productService.getProductsByName(name);
			if (res.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(new ApiResponse("Success", productService.convertToDto(res)));
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/get-by-category")
	public ResponseEntity<ApiResponse> getProductsByCategory(@RequestParam String category) {
		try {
			List<Product> res = this.productService.getProductsByCategory(category);
			if (res.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(new ApiResponse("Success", productService.convertToDto(res)));
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	public ResponseEntity<ApiResponse> countProductByBrandAndName(@RequestParam String brand, @RequestParam String name) {
		return ResponseEntity.ok(new ApiResponse("Success", productService.countProductsByBrandAndName(brand, name)));
	}
	
}
