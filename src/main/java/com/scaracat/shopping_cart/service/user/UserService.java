package com.scaracat.shopping_cart.service.user;

import org.springframework.stereotype.Service;

import com.scaracat.shopping_cart.repo.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
	private final UserRepository userRepository;
	
	
	
}
