package com.scaracat.shopping_cart.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scaracat.shopping_cart.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{
	
	public List<Image> findByProductId(Long productId);
	
}
