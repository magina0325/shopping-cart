package com.scaracat.shopping_cart.data;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.scaracat.shopping_cart.model.User;
import com.scaracat.shopping_cart.repo.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent>{
	
	private final UserRepository userRepository;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		createDefaultUserIfNotExists();
	}
	
	private void createDefaultUserIfNotExists() {
		for (int i = 0; i < 5; i++) {
			String email = "user" + i + "@gmail.com";
			if (userRepository.existsByEmail(email)) {
				continue;
			}
			User user = new User();
			user.setEmail(email);
			user.setFirstName("The user");
			user.setLastName(" number " + i);
			user.setPassword("123456");
			userRepository.save(user);
			System.out.println("Created user: " + email);
		}
	}
	
}




















