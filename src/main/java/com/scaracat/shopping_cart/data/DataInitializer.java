package com.scaracat.shopping_cart.data;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.scaracat.shopping_cart.exception.ResourceNotFoundException;
import com.scaracat.shopping_cart.model.Role;
import com.scaracat.shopping_cart.model.User;
import com.scaracat.shopping_cart.repo.RoleRepository;
import com.scaracat.shopping_cart.repo.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent>{
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
		createDefaultRoleIfNotExists(defaultRoles);
		createDefaultUserIfNotExists();
		createDefaultAdminIfNotExists();
	}
	
	private void createDefaultUserIfNotExists() {
		Role userRole = roleRepository.findByName("ROLE_USER")
				.orElseThrow(() -> new ResourceNotFoundException("Role not found."));
		for (int i = 0; i < 5; i++) {
			String email = "user" + i + "@gmail.com";
			if (userRepository.existsByEmail(email)) {
				continue;
			}
			User user = new User();
			user.setEmail(email);
			user.setFirstName("The user");
			user.setLastName(" number " + i);
			user.setPassword(passwordEncoder.encode("123456"));
			user.setRoles(Set.of(userRole));
			userRepository.save(user);
			System.out.println("Created user: " + email);
		}
	}
	
	private void createDefaultAdminIfNotExists() {
		Role adminRole = roleRepository.findByName("ROLE_ADMIN")
				.orElseThrow(() -> new ResourceNotFoundException("Role not found."));
		for (int i = 0; i < 2; i++) {
			String email = "admin" + i + "@gmail.com";
			if (userRepository.existsByEmail(email)) {
				continue;
			}
			User user = new User();
			user.setEmail(email);
			user.setFirstName("The admin");
			user.setLastName(" number " + i);
			user.setPassword(passwordEncoder.encode("123456"));
			user.setRoles(Set.of(adminRole));
			userRepository.save(user);
			System.out.println("Created admin: " + email);
		}
	}
	
	private void createDefaultRoleIfNotExists(Set<String> roles) {
		roles.stream()
			.filter(role -> roleRepository.findByName(role).isEmpty())
			.map(Role::new).forEach(roleRepository::save);
	}
	
}




















