package com.scaracat.shopping_cart.security.jwt;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

        // Set the response header to JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        // Set the 401 Unauthorized status
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Create a Map for the error body
        final Map<String, Object> body = new LinkedHashMap<>();
        //body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", "You are not authorized for this action.");
        //body.put("path", request.getServletPath());

        // Use Jackson to write the Map as JSON to the response stream
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
		
	}

}
