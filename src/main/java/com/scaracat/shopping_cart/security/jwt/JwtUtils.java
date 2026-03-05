package com.scaracat.shopping_cart.security.jwt;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.scaracat.shopping_cart.security.user.MyUserDetails;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
	
	@Value("${auth.token.jwtSecret}")
	private String jwtSecret;
	
	@Value("${auth.token.expirationInMils}")
	private int expirationTime;
	
	public String generateTokenForUser(Authentication authentication) {
		MyUserDetails userPrincipal = (MyUserDetails) authentication.getPrincipal();
		List<String> roles = userPrincipal.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority).toList();
		
		return Jwts.builder()
				.subject(userPrincipal.getEmail())
				.claim("id", userPrincipal.getId())
				.claim("roles", roles)
				.issuedAt(new Date())
				.expiration(new Date(new Date().getTime() + expirationTime))
				.signWith(key()).compact();
	}
	
	private SecretKey key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}
	
	public String getUsernameFromToken(String token) {
		return Jwts.parser()
	            .verifyWith(key()) 
	            .build()
	            .parseSignedClaims(token) 
	            .getPayload() 
	            .getSubject();
	}
	
	public boolean validateToken(String token) {
	    try {
	        Jwts.parser()
	            .verifyWith(key()) 
	            .build()
	            .parseSignedClaims(token);
	        
	    } catch (MalformedJwtException e) {
	        throw new JwtException("Invalid JWT token: " + e.getMessage());
	    } catch (ExpiredJwtException e) {
	    	throw new JwtException("JWT token is expired: " + e.getMessage());
	    } catch (UnsupportedJwtException e) {
	    	throw new JwtException("JWT token is unsupported: " + e.getMessage());
	    } catch (IllegalArgumentException e) {
	    	throw new JwtException("JWT claims string is empty: " + e.getMessage());
	    } catch (SecurityException e) {
	    	throw new JwtException("Invalid JWT signature: " + e.getMessage());
	    }
	    
	    return true;
	}
	
}






























