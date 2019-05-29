package com.bakery.repository;

import org.springframework.data.repository.CrudRepository;

import com.bakery.entity.User;

public interface UserRepository extends CrudRepository<User, Long>
{
	User findByUsername(String username);
	
	User findByEmail(String email);
}
