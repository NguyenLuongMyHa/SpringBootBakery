package com.admin.repository;

import org.springframework.data.repository.CrudRepository;

import com.admin.entity.security.Role;


public interface RoleRepository extends CrudRepository<Role, Long> {
	Role findByname(String name);
}
