package com.scaracat.shopping_cart.service.category;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.scaracat.shopping_cart.exception.AlreadyExistException;
import com.scaracat.shopping_cart.exception.CategoryNotFoundException;
import com.scaracat.shopping_cart.exception.ResourceNotFoundException;
import com.scaracat.shopping_cart.model.Category;
import com.scaracat.shopping_cart.repo.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

	private final CategoryRepository categoryRepository;
	
	@Override
	public Category getCategoryById(Long id) {
		return this.categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
	}

	@Override
	public Category getCategoryByName(String name) {
		return this.categoryRepository.findByName(name)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
	}

	@Override
	public List<Category> getAllCategories() {
		return this.categoryRepository.findAll();
	}

	@Override
	public Category addCategory(Category category) {
		categoryRepository.findByName(category.getName())
			.ifPresent((existingCategory) -> {
				throw new AlreadyExistException("Category " + category.getName() + " already exist!");
			});
		return this.categoryRepository.save(category);
	}

	@Override
	public Category updateCategory(Category category, Long id) {
		return Optional.ofNullable(this.getCategoryById(id)).map(existingCategory -> {
			existingCategory.setName(category.getName());
			return categoryRepository.save(existingCategory);
		}).orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
	}

	@Override
	public void deleteCategoryById(Long id) {
		this.categoryRepository.findById(id)
			.ifPresentOrElse((existingCategory) -> this.categoryRepository.delete(existingCategory), 
				() -> {throw new ResourceNotFoundException("Category not found!");});
	}

}
