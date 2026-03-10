package com.scaracat.shopping_cart.service.product;

import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.scaracat.shopping_cart.dto.ImageDto;
import com.scaracat.shopping_cart.dto.ProductDto;
import com.scaracat.shopping_cart.exception.AlreadyExistException;
import com.scaracat.shopping_cart.exception.ResourceNotFoundException;
import com.scaracat.shopping_cart.model.Category;
import com.scaracat.shopping_cart.model.Image;
import com.scaracat.shopping_cart.model.Product;
import com.scaracat.shopping_cart.repo.CategoryRepository;
import com.scaracat.shopping_cart.repo.ImageRepository;
import com.scaracat.shopping_cart.repo.ProductRepository;
import com.scaracat.shopping_cart.request.AddProductRequest;
import com.scaracat.shopping_cart.request.ProductUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
// works on fields that are non-static final
//commonly used to do constructor injection
@RequiredArgsConstructor
public class ProductService implements IProductService{

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ImageRepository imageRepository;
	private final ModelMapper modelMapper;
	
	@Override
	public Product addProduct(AddProductRequest request) {
		// check if the category exists in db
		// fetch if yes
		// persist if no
		if (this.productExists(request.getName(), request.getBrand())) {
			throw new AlreadyExistException(
					"Product with the name: " + request.getBrand() + " of the brand " + request.getBrand() + " already exists.");
		}
		
		Category category = categoryRepository.findByName(request.getCategory())
				.orElseGet(() -> {
					Category temp = new Category(request.getCategory());
					return categoryRepository.save(temp);
				});
		request.setCategory(category.getName());
		return productRepository.save(createProduct(request, category));
	}
	
	private boolean productExists(String name, String brand) {
		return productRepository.existsByNameAndBrand(name, brand);
	}
	
	private Product createProduct(AddProductRequest request, Category category) {
		return new Product(
				request.getName(),
				request.getBrand(),
				request.getPrice(),
				request.getInventory(),
				request.getDescription(),
				category
		);
	}

	@Override
	public Product getProductById(Long id) {
		return this.productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product of id " + id + " not found!"));
	}

	@Override
	public List<Product> getAllProducts() {
		return this.productRepository.findAll();
	}

	@Override
	public List<Product> getProductsByCategory(String category) {
		return this.productRepository.findByCategoryName(category);
	}

	@Override
	public List<Product> getProductsByBrand(String brand) {
		return this.productRepository.findByBrand(brand);
	}

	@Override
	public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
		return this.productRepository.findByCategoryNameAndBrand(category, brand);
	}

	@Override
	public List<Product> getProductsByName(String name) {
		return this.productRepository.findByName(name);
	}

	@Override
	public List<Product> getProductsByBrandAndName(String brand, String name) {
		return this.productRepository.findByBrandAndName(brand, name);
	}

	@Override
	public void deleteProductById(Long id) {
		/*
		Product product;
		try {
			product = this.getProductById(id);
		} catch (ResourceNotFoundException e) {
			throw e;
		}
		this.productRepository.delete(product);
		*/
		this.productRepository.findById(id)
			.ifPresentOrElse(productRepository::delete, 
					() -> {throw new ResourceNotFoundException(null);});
	}

	@Override
	public Product updateProduct(ProductUpdateRequest request, Long productId) {
		return this.productRepository.findById(productId)
			.map(existingProduct -> this.updateExistingProduct(existingProduct, request))
			.map(product -> this.productRepository.save(product))
			.orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
	}
	
	private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
		existingProduct.setName(request.getName());
		existingProduct.setBrand(request.getBrand());
		existingProduct.setPrice(request.getPrice());
		existingProduct.setInventory(request.getInventory());
		existingProduct.setDescription(request.getDescription());
		
		this.categoryRepository.findByName(request.getCategory().getName())
			.ifPresentOrElse(category -> existingProduct.setCategory(category), 
					() -> {throw new ResourceNotFoundException("Category not found!");});
		return existingProduct;
		
	}

	@Override
	public Long countProductsByBrandAndName(String brand, String name) {
		return this.productRepository.countByBrandAndName(brand, name);
	}
	
	@Override
	public List<ProductDto> convertToDto(List<Product> products) {
		List<ProductDto> res = products.stream().map(product -> {
			return convertToDto(product);
		}).toList();
		return res;
	}
	
	@Override
	public ProductDto convertToDto(Product product) {
		ProductDto res = modelMapper.map(product, ProductDto.class);
		List<Image> images = imageRepository.findByProductId(product.getId());
		List<ImageDto> imageDtos = images.stream()
				.map(image -> modelMapper.map(image, ImageDto.class))
				.toList();
		res.setImages(imageDtos);
		
		return res;
	}
	
}
