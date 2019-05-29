package com.bakery.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bakery.entity.Product;
import com.bakery.entity.User;
import com.bakery.entity.security.PasswordResetToken;
import com.bakery.entity.security.Role;
import com.bakery.entity.security.UserRole;
import com.bakery.service.UserService;
import com.bakery.service.ProductService;

import com.bakery.service.impl.UserSecurityService;
import com.bakery.utility.MailConstructor;
import com.bakery.utility.SecurityUtility;

@Controller
public class HomeController
{

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MailConstructor mailConstructor;

	@Autowired
	private UserService userService;

	@Autowired
	private UserSecurityService userSecurityService;


	@Autowired
	private ProductService productService;

	@RequestMapping("/")
	public String index()
	{
		return "index";
	}


	@RequestMapping("/product")
	public String product(Model model) {
		List<Product> productlist = productService.findAll();
		model.addAttribute("productlist", productlist);
		
		return "product";
	}
	@RequestMapping("/Account")
	public String Account(Model model, Principal principal)
	{
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("classActiveEdit", true);
		return "Account";
	}

	
	@RequestMapping("/SignIn")
	public String SignIn(Model model)
	{
		model.addAttribute("classActiveLogin", true);
		return "SignIn";
	}
	@RequestMapping("/SignUp")
	public String SignUp(Model model)
	{
		model.addAttribute("classActiveNewAccount", true);
		return "SignUp";
	}
	@RequestMapping("/Recovery")
	public String Recovery(Model model)
	{
		model.addAttribute("classActiveForgetPassword", true);
		return "RecoveryAccount";
	}
	@RequestMapping("/forgetPassword")
	public String forgetPassword(HttpServletRequest request, 
			@ModelAttribute("email") String email, 
			Model model)
	{

		model.addAttribute("classActiveForgetPassword", true);

		User user = userService.findByEmail(email);

		if (user == null)
		{
			model.addAttribute("emailNotExist", true);
			return "RecoveryAccount";
		}

		String password = SecurityUtility.randomPassword();

		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);

		userService.save(user);

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);

		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user,
				password);

		mailSender.send(newEmail);

		model.addAttribute("forgetPasswordEmailSent", "true");

		return "SignIn";
	}

	@RequestMapping(value = "/newUser", method = RequestMethod.POST)
	public String newUserPost(HttpServletRequest request, @ModelAttribute("email") String userEmail,
			@ModelAttribute("username") String username, Model model) throws Exception
	{
		model.addAttribute("classActiveNewAccount", true);
		model.addAttribute("email", userEmail);
		model.addAttribute("username", username);

		if (userService.findByUsername(username) != null)
		{
			model.addAttribute("usernameExists", true);

			return "SignUp";
		}

		if (userService.findByEmail(userEmail) != null)
		{
			model.addAttribute("emailExists", true);

			return "SignUp";
		}

		User user = new User();
		user.setUsername(username);
		user.setEmail(userEmail);

		String password = SecurityUtility.randomPassword();

		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);

		Role role = new Role();
		role.setRoleId(1);
		role.setName("ROLE_USER");
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user, role));
		userService.createUser(user, userRoles);

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);

		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user,
				password);

		mailSender.send(email);

		model.addAttribute("emailSent", "true");

		return "SignIn";

	}

	@RequestMapping("/newUser")
	public String newUser(Locale locale, @RequestParam("token") String token, Model model)
	{
		PasswordResetToken passToken = userService.getPasswordResetToken(token);

		if (passToken == null)
		{
			String message = "Invalid Token.";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
		}

		User user = passToken.getUser();
		String username = user.getUsername();

		UserDetails userDetails = userSecurityService.loadUserByUsername(username);

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);
		model.addAttribute("user", user);

		model.addAttribute("classActiveEdit", true);
		return "Account";
	}

	@RequestMapping("/ProductDetail")
	public String ProductDetail(
			@PathParam("id") Long id, Model model, Principal principal
			) 
	{
		if(principal != null) 
		{
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		
		Product product = productService.findOne(id);
		
		model.addAttribute("product", product);
		
		List<Integer> qtyList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		
		model.addAttribute("qtyList", qtyList);
		model.addAttribute("qty", 1);
		
		return "ProductDetail";
	}
	
	@RequestMapping(value="/updateUserInfo", method=RequestMethod.POST)
	public String updateUserInfo(
			@ModelAttribute("user") User user,
			@ModelAttribute("newPassword") String newPassword,
			Model model
			) throws Exception {
		User currentUser = userService.findById(user.getId());
		
		if(currentUser == null) {
			throw new Exception ("User not found");
		}
		
		/*check email already exists*/
		if (userService.findByEmail(user.getEmail())!=null) {
			if(userService.findByEmail(user.getEmail()).getId() != currentUser.getId()) {
				model.addAttribute("emailExists", true);
				return "SignIn";
			}
		}
		
		/*check username already exists*/
		if (userService.findByUsername(user.getUsername())!=null) {
			if(userService.findByUsername(user.getUsername()).getId() != currentUser.getId()) {
				model.addAttribute("usernameExists", true);
				return "SignIn";
			}
		}
		
//		update password
		if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")){
			BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
			String dbPassword = currentUser.getPassword();
			if(passwordEncoder.matches(user.getPassword(), dbPassword)){
				currentUser.setPassword(passwordEncoder.encode(newPassword));
			} else {
				model.addAttribute("incorrectPassword", true);
				
				return "SignIn";
			}
		}
		
		currentUser.setFirstName(user.getFirstName());
		currentUser.setLastName(user.getLastName());
		currentUser.setUsername(user.getUsername());
		currentUser.setEmail(user.getEmail());
		
		userService.save(currentUser);
		
		model.addAttribute("updateSuccess", true);
		model.addAttribute("user", currentUser);
		model.addAttribute("classActiveEdit", true);
		
		UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "Account";
	}
	
}
