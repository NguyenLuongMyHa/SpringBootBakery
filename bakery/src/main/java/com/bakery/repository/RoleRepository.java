package com.bakery.repository;

import org.springframework.data.repository.CrudRepository;

import com.bakery.entity.security.Role;

public interface RoleRepository extends CrudRepository<Role, Long>
{
	Role findByname(String name);
}
