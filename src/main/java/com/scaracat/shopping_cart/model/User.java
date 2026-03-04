package com.scaracat.shopping_cart.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private Cart cart;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Order> orders;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, 
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "user_roles", 
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Collection<Role> roles = new HashSet<>();
	
}



























