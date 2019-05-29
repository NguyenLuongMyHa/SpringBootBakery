package com.admin.service;
import java.util.List;
import com.admin.entity.Product;
public interface ProductService
{
	Product save(Product product);
	List<Product> findAll();
	Product findOne(Long id);
}
