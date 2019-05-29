package com.bakery.service;

import java.util.List;

import com.bakery.entity.Product;
import com.bakery.entity.CartItem;
import com.bakery.entity.ShoppingCart;
import com.bakery.entity.User;

public interface CartItemService {
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	CartItem updateCartItem(CartItem cartItem);
	
	CartItem addProductToCartItem(Product product, User user, int qty);
	
	CartItem findById(Long id);
	
	void removeCartItem(CartItem cartItem);
	
	CartItem save(CartItem cartItem);
}
