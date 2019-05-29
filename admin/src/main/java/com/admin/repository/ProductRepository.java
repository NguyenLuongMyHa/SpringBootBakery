package com.admin.repository;

import org.springframework.data.repository.CrudRepository;
import com.admin.entity.Product;
public interface ProductRepository  extends CrudRepository<Product, Long>
{

}
