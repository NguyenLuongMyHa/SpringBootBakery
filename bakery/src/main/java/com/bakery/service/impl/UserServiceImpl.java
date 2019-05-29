package com.bakery.service.impl;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bakery.entity.ShoppingCart;
import com.bakery.entity.User;
import com.bakery.entity.security.PasswordResetToken;
import com.bakery.entity.security.UserRole;
import com.bakery.repository.PasswordResetTokenRepository;
import com.bakery.repository.RoleRepository;
import com.bakery.repository.UserRepository;
import com.bakery.service.UserService;

@Service
public class UserServiceImpl implements UserService
{

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Override
	public PasswordResetToken getPasswordResetToken(final String token)
	{
		return passwordResetTokenRepository.findByToken(token);
	}

	@Override
	public void createPasswordResetTokenForUser(final User user, final String token)
	{
		final PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(myToken);
	}

	@Override
	public User findByUsername(String username)
	{
		return userRepository.findByUsername(username);
	}

	@Override
	public User findByEmail(String email)
	{
		return userRepository.findByEmail(email);
	}

	@Override
	public User findById(Long id)
	{
		return userRepository.findById(id).get();
	}

	@Override
	@Transactional
	public User createUser(User user, Set<UserRole> userRoles)
	{
		User localUser = userRepository.findByUsername(user.getUsername());

		if (localUser != null)
		{
			LOG.info("user {} already exists. Nothing will be done.", user.getUsername());
		} else
		{
			for (UserRole ur : userRoles)
			{
				roleRepository.save(ur.getRole());
			}

			user.getUserRoles().addAll(userRoles);
			ShoppingCart shoppingCart = new ShoppingCart();
			shoppingCart.setUser(user);
			user.setShoppingCart(shoppingCart);

			localUser = userRepository.save(user);
		}

		return localUser;
	}

	@Override
	public User save(User user)
	{
		return userRepository.save(user);
	}
}