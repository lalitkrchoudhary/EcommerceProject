package com.ecom.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ecom.model.Product;
import com.ecom.repository.ProductRepository;
import com.ecom.service.IProductService;

@Service
public class ProductServiceImpl implements IProductService {
	
	@Autowired
	private ProductRepository productRepo;
	

	@Override
	public Product saveProduct(Product product) {
		Product save = productRepo.save(product);
		return save;
	}


	@Override
	public List<Product> getAllProducts() {
		// TODO Auto-generated method stub
		return productRepo.findAll();
	}


	@Override
	public Boolean deleteProduct(Integer id) {
	  Product product = productRepo.findById(id).orElse(null);
		if(!ObjectUtils.isEmpty(product)) {
			productRepo.delete(product);
			return true;
		}
		return false;
	}

}
