package com.bakery.service;
import java.util.List;
import com.bakery.entity.Product;
public interface ProductService
{
	List<Product> findAll();
	Product findOne(Long id);

}
