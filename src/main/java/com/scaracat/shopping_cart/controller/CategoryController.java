package com.scaracat.shopping_cart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.Conflict;

import com.scaracat.shopping_cart.exception.AlreadyExistException;
import com.scaracat.shopping_cart.exception.ResourceNotFoundException;
import com.scaracat.shopping_cart.model.Category;
import com.scaracat.shopping_cart.response.ApiResponse;
import com.scaracat.shopping_cart.service.category.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;
	
	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAllCategories() {
		List<Category> res;
		try {
			res = categoryService.getAllCategories();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
		}
		
		return ResponseEntity.ok(new ApiResponse("Success.", res));
	}
	
	@GetMapping("/get/{categoryId}/")
	public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long categoryId) {
		Category category;
		try {
			category = categoryService.getCategoryById(categoryId);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
		}
		return ResponseEntity.ok(new ApiResponse("Success", category));
	}
	
	@GetMapping("/get/{name}/")
	public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
		Category category;
		try {
			category = categoryService.getCategoryByName(name);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
		}
		return ResponseEntity.ok(new ApiResponse("Success", category));
	}
	
	@PostMapping("/add")
	public ResponseEntity<ApiResponse> saveCategory(@RequestBody Category category) {
		try {
			categoryService.addCategory(category);
		} catch(AlreadyExistException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
		}
		return ResponseEntity.ok(new ApiResponse("Category added.", null));
	}
	
	@DeleteMapping("/delete/{categoryId}")
	public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long categoryId) {
		try {
			categoryService.deleteCategoryById(categoryId);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
		}
		return ResponseEntity.ok(new ApiResponse("Delete Success", null));
	}
	
	@PutMapping("/update/{categoryId}")
	public ResponseEntity<ApiResponse> updateCategoryById(@PathVariable Long categoryId, @RequestBody Category category) {
		try {
			categoryService.updateCategory(category, categoryId);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
		}
		return ResponseEntity.ok(new ApiResponse("Update Success", null));
	}
	
}
