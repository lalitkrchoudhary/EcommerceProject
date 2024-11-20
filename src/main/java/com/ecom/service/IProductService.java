package com.ecom.service;

import java.util.List;

import com.ecom.model.Product;

public interface IProductService {
	
	public Product saveProduct(Product product);
	public List<Product> getAllProducts();
	public Boolean deleteProduct(Integer id);

}
