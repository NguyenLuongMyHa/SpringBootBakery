package com.admin.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.entity.Product;
import com.admin.repository.ProductRepository;
import com.admin.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService
{

	@Autowired
	private ProductRepository productRepository;
	
	public Product save(Product product) 
	{
		return productRepository.save(product);
	}

	@Override
	public List<Product> findAll()
	{
		return (List<Product>) productRepository.findAll();
	}
	public Product findOne(Long id) {
		return productRepository.findById(id).get();
	}
}
