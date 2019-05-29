package com.bakery.repository;

import org.springframework.data.repository.CrudRepository;

import com.bakery.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Long>
{

}
