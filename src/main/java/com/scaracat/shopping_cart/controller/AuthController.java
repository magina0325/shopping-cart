package com.scaracat.shopping_cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scaracat.shopping_cart.request.LoginRequest;
import com.scaracat.shopping_cart.response.ApiResponse;
import com.scaracat.shopping_cart.response.JwtResponse;
import com.scaracat.shopping_cart.security.jwt.JwtUtils;
import com.scaracat.shopping_cart.security.user.MyUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {
	
	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
		
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateTokenForUser(authentication);
			MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
			JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);
		
			return ResponseEntity.ok(new ApiResponse("Login Success", jwtResponse));
			
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
		}
		
	}
	
}















