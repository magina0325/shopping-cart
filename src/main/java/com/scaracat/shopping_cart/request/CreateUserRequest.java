package com.scaracat.shopping_cart.request;

import lombok.Data;

@Data
public class CreateUserRequest {
	String email;
	String password;
	String firstName;
	String lastName;
}
