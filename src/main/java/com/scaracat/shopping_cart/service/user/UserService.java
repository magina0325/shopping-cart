package com.scaracat.shopping_cart.service.user;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scaracat.shopping_cart.dto.UserDto;
import com.scaracat.shopping_cart.exception.AlreadyExistException;
import com.scaracat.shopping_cart.exception.ResourceNotFoundException;
import com.scaracat.shopping_cart.model.User;
import com.scaracat.shopping_cart.repo.UserRepository;
import com.scaracat.shopping_cart.request.CreateUserRequest;
import com.scaracat.shopping_cart.request.UserUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
	
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;

	@Override
	public User getAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Can't retrieve user info"));
	}
	
	@Override
	public User getUserById(Long userId) {
		return this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found."));
	}

	@Override
	public User createUser(CreateUserRequest request) {
		if (userRepository.existsByEmail(request.getEmail()))
			throw new AlreadyExistException("User with email " + request.getEmail() + " already exist");
		
		User user = new User();
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		
		return this.userRepository.save(user);
	}

	@Override
	public User updateUser(UserUpdateRequest request, Long userId) {
		User user = this.getUserById(userId);
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		
		return this.userRepository.save(user);
	}

	@Override
	public void deleteUser(Long userId) {
		if (this.userRepository.existsById(userId))
			this.userRepository.deleteById(userId);
		else
			throw new ResourceNotFoundException("User not found.");
	}
	
	@Override
	public UserDto convertUserToUserDto(User user) {
		return modelMapper.map(user, UserDto.class);
	}
	
}
