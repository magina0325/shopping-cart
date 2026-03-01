package com.scaracat.shopping_cart.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scaracat.shopping_cart.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
