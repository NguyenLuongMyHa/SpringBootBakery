package com.admin.service;

import java.util.Set;

import com.admin.entity.User;
import com.admin.entity.security.UserRole;

public interface UserService 
{
	User createUser(User user, Set<UserRole> userRoles) throws Exception;
	
	User save(User user);
}
