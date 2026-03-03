package com.scaracat.shopping_cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

import com.scaracat.shopping_cart.dto.UserDto;
import com.scaracat.shopping_cart.exception.AlreadyExistException;
import com.scaracat.shopping_cart.exception.ResourceNotFoundException;
import com.scaracat.shopping_cart.model.User;
import com.scaracat.shopping_cart.request.CreateUserRequest;
import com.scaracat.shopping_cart.request.UserUpdateRequest;
import com.scaracat.shopping_cart.response.ApiResponse;
import com.scaracat.shopping_cart.service.user.IUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
	
	private final IUserService userService;
	
	// get
	@GetMapping("/get/{userId}")
	public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
		User user;
		try {
			user = userService.getUserById(userId);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(new ApiResponse(e.getMessage(), null));
		}
		
		UserDto res = userService.convertUserToUserDto(user);
		return ResponseEntity.ok(new ApiResponse("Success", res));
	}
	
	// create
	@PostMapping("/register")
	public ResponseEntity<ApiResponse> registerUser(@RequestBody CreateUserRequest request) {
		User user;
		try {
			user = userService.createUser(request);
		} catch (AlreadyExistException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ApiResponse(e.getMessage(), null));
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(new ApiResponse(e.getMessage(), null));
		}
		
		return ResponseEntity.ok(new ApiResponse("Success register", userService.convertUserToUserDto(user)));
	}
	
	// update
	@PutMapping("/update/{userId}")
	public ResponseEntity<ApiResponse> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest request) {
		User user;
		try {
			user = userService.updateUser(request, userId);
		} catch (AlreadyExistException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ApiResponse(e.getMessage(), null));
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(new ApiResponse(e.getMessage(), null));
		}
		UserDto res = userService.convertUserToUserDto(user);
		return ResponseEntity.ok(new ApiResponse("Success register", res));
	}
	
	// delete
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
		try {
			userService.deleteUser(userId);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(new ApiResponse(e.getMessage(), null));
		}
		
		return ResponseEntity.ok(new ApiResponse("Success", null));
	}
}