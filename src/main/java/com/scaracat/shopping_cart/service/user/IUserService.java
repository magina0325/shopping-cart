package com.scaracat.shopping_cart.service.user;

import com.scaracat.shopping_cart.model.User;
import com.scaracat.shopping_cart.request.CreateUserRequest;
import com.scaracat.shopping_cart.request.UserUpdateRequest;

public interface IUserService {
	
	User getUserById(Long userId);
	User createUser(CreateUserRequest request);
	User updateUser(UserUpdateRequest request, Long userId);
	void deleteUser(Long userId);
	
}
