package com.bakery.repository;

import org.springframework.data.repository.CrudRepository;

import com.bakery.entity.ShoppingCart;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {

}

