package com.bakery.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bakery.entity.ProductToCartItem;
import com.bakery.entity.CartItem;
@Transactional
public interface ProductToCartItemRepository extends CrudRepository<ProductToCartItem, Long>
{
	void deleteByCartItem(CartItem cartItem);
}

