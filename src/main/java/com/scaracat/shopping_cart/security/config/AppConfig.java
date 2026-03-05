package com.scaracat.shopping_cart.security.config;

import java.util.List;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.scaracat.shopping_cart.dto.CartDto;
import com.scaracat.shopping_cart.dto.CartItemDto;
import com.scaracat.shopping_cart.dto.ProductDto;
import com.scaracat.shopping_cart.model.Cart;
import com.scaracat.shopping_cart.model.CartItem;
import com.scaracat.shopping_cart.model.Category;
import com.scaracat.shopping_cart.model.Product;
import com.scaracat.shopping_cart.security.jwt.AuthTokenFilter;
import com.scaracat.shopping_cart.security.jwt.JwtAuthEntryPoint;
import com.scaracat.shopping_cart.security.jwt.JwtUtils;
import com.scaracat.shopping_cart.security.user.MyUserDetailsService;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class AppConfig {
	
	private static final List<String> SECURED_URLS = List.of("/api/v1/cart/**");
	
	private final MyUserDetailsService userDetailsService;
	private final JwtAuthEntryPoint authEntryPoint;
	private final JwtUtils jwtUtils;
	
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		
		// configure mapping from category object to category name in string
		configureProductMapping(modelMapper);
		
		// add an extra totalAmount field which is derived in the db and model
		configureCartMapping(modelMapper);
		
		
		
		return modelMapper;
	}
	
	private void configureProductMapping(ModelMapper modelMapper) {
		Converter<Category, String> categoryConverter = (context) -> {
			Category source = context.getSource();
			return (source != null) ? source.getName() : null;
		};
		modelMapper.typeMap(Product.class, ProductDto.class).addMappings(mapper -> {
			mapper.using(categoryConverter)
				.map(Product::getCategory, ProductDto::setCategory);
		});
	}
	
	private void configureCartMapping(ModelMapper modelMapper) {
		modelMapper.createTypeMap(Cart.class, CartDto.class)
			.addMappings(mapper -> {
				mapper.map(src -> src.getTotalAmount(), CartDto::setTotalAmount);
			});
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthTokenFilter authTokenFilter() {
		return new AuthTokenFilter(jwtUtils, userDetailsService);
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) {
		return authConfig.getAuthenticationManager();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.requestMatchers(SECURED_URLS.toArray(String[]::new)).authenticated()
            		.anyRequest().permitAll());
        
        http.authenticationProvider(daoAuthenticationProvider());
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
	
}




























