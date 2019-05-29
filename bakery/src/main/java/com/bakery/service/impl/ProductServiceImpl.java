package com.bakery.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bakery.entity.Product;
import com.bakery.repository.ProductRepository;
import com.bakery.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService
{
	@Autowired
	private ProductRepository productRepository;
	
	public List<Product> findAll() {
		return (List<Product>) productRepository.findAll();
	}
	public Product findOne(Long id) 
	{
		return productRepository.findById(id).get();
	}
}
