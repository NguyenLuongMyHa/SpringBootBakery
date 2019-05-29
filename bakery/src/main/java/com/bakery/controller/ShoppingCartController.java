package com.bakery.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bakery.entity.Product;
import com.bakery.entity.CartItem;
import com.bakery.entity.ShoppingCart;
import com.bakery.entity.User;
import com.bakery.service.ProductService;
import com.bakery.service.CartItemService;
import com.bakery.service.ShoppingCartService;
import com.bakery.service.UserService;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@RequestMapping("/cart")
	public String shoppingCart(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		ShoppingCart shoppingCart = user.getShoppingCart();
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		shoppingCartService.updateShoppingCart(shoppingCart);
		
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", shoppingCart);
		
		return "shoppingCart";
	}

	@RequestMapping("/addItem")
	public String addItem(
			@ModelAttribute("product") Product product,
			@ModelAttribute("qty") String qty,
			Model model, Principal principal
			) {
		User user = userService.findByUsername(principal.getName());
		product = productService.findOne(product.getId());
		
		if (Integer.parseInt(qty) > product.getInStockNumber()) {
			model.addAttribute("notEnoughStock", true);
			return "forward:/productDetail?id="+product.getId();
		}
		
		CartItem cartItem = cartItemService.addProductToCartItem(product, user, Integer.parseInt(qty));
		model.addAttribute("addProductSuccess", true);
		
		return "forward:/productDetail?id="+product.getId();
	}
	
	@RequestMapping("/updateCartItem")
	public String updateShoppingCart(
			@ModelAttribute("id") Long cartItemId,
			@ModelAttribute("qty") int qty
			) {
		CartItem cartItem = cartItemService.findById(cartItemId);
		cartItem.setQty(qty);
		cartItemService.updateCartItem(cartItem);
		
		return "forward:/shoppingCart/cart";
	}
	
	@RequestMapping("/removeItem")
	public String removeItem(@RequestParam("id") Long id) {
		cartItemService.removeCartItem(cartItemService.findById(id));
		
		return "forward:/shoppingCart/cart";
	}
}