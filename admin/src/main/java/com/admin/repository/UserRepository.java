package com.admin.repository;

import org.springframework.data.repository.CrudRepository;

import com.admin.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByUsername(String username);
}
