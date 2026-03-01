package com.scaracat.shopping_cart.service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.scaracat.shopping_cart.dto.ImageDto;
import com.scaracat.shopping_cart.exception.ResourceNotFoundException;
import com.scaracat.shopping_cart.model.Image;
import com.scaracat.shopping_cart.model.Product;
import com.scaracat.shopping_cart.repo.ImageRepository;
import com.scaracat.shopping_cart.service.product.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

	private final ImageRepository imageRepository;
	private final ProductService productService;
	
	@Override
	public Image getImageById(Long id) {
		return imageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Image of id:" + id + " is not found!"));
	}

	@Override
	public void deleteImageById(Long id) {
		imageRepository.findById(id)
			.ifPresentOrElse(imageRepository::delete, 
				() -> {throw new ResourceNotFoundException("Image of id:" + id + " is not found!");});
	}

	@Override
	public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
		Product product = productService.getProductById(productId);
		List<ImageDto> savedImageDto = new ArrayList<>();
		for (MultipartFile file : files) {
			try {
				Image image = new Image();
				image.setFileName(file.getOriginalFilename());
				image.setFileType(file.getContentType());
				image.setImage(new SerialBlob(file.getBytes()));
				image.setProduct(product);
				
				Image savedImage = imageRepository.save(image);
				String downloadUrl = "/api/v1/images/image/download/" + savedImage.getId();
				savedImage.setDownloadUrl(downloadUrl);
				imageRepository.save(savedImage);
				
				ImageDto imageDto = new ImageDto();
				imageDto.setId(savedImage.getId());
				imageDto.setFileName(savedImage.getFileName());
				imageDto.setDownloadUrl(savedImage.getDownloadUrl());
				savedImageDto.add(imageDto);
				
			} catch(IOException | SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return savedImageDto;
	}

	@Override
	public void updateImage(MultipartFile file, Long imageId) {
		Image image = this.getImageById(imageId);
		try {
			image.setFileName(file.getOriginalFilename());
			image.setFileType(file.getContentType());
			image.setImage(new SerialBlob(file.getBytes()));
			imageRepository.save(image);
		} catch(IOException | SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
		
		
		
	}
	
}
