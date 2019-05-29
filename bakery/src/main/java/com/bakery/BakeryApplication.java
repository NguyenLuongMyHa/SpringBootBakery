package com.bakery;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bakery.entity.User;
import com.bakery.entity.security.Role;
import com.bakery.entity.security.UserRole;
import com.bakery.service.UserService;
import com.bakery.utility.SecurityUtility;

import it.ozimov.springboot.mail.configuration.EnableEmailTools;
@EnableEmailTools
@SpringBootApplication
public class BakeryApplication implements CommandLineRunner
{
	@Autowired
	private UserService userService;

	public static void main(String[] args)
	{
		SpringApplication.run(BakeryApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception
	{
		User user2 = new User();
		user2.setFirstName("Thuy");
		user2.setLastName("Ngan");
		user2.setUsername("Thuy Ngan");
		user2.setPassword(SecurityUtility.passwordEncoder().encode("p"));
		user2.setEmail("nguyenthuyngan0807@gmail.com");
		Set<UserRole> userRoles = new HashSet<>();
		Role role1 = new Role();
		role1.setRoleId(0);
		role1.setName("ROLE_USER");
		userRoles.add(new UserRole(user2, role1));

		userService.createUser(user2, userRoles);
	}
}
