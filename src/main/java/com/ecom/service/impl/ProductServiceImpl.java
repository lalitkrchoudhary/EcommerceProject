package com.ecom.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

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


	@Override
	public Product getProductById(Integer id) {
		Product product = productRepo.findById(id).orElse(null);
		return product;
	}


	@Override
	public Product updateProduct(Product product, MultipartFile image) {
		Product dbProduct = getProductById(product.getId());
		String imageName= image.isEmpty() ? dbProduct.getImage() : image.getOriginalFilename();
		
		dbProduct.setTitle(product.getTitle());
		dbProduct.setDescription(product.getDescription());
		dbProduct.setCategory(product.getCategory());
		dbProduct.setPrice(product.getPrice());
		dbProduct.setStock(product.getStock());
		dbProduct.setImage(imageName);
		dbProduct.setIsActive(product.getIsActive());
		
		dbProduct.setDiscount(product.getDiscount());
		
		//5=100*(5/100); 100-5=95
		 Double discount=product.getPrice()*(product.getDiscount()/100.0);
		 Double discountPrice= product.getPrice()-discount;
		 dbProduct.setDiscountPrice(discountPrice);
		 
		 
		Product updateProduct = productRepo.save(dbProduct);
		if(!ObjectUtils.isEmpty(updateProduct)) {
			// TO STORE IMAGE IN FILE
			if(!image.isEmpty()) {
						try {
							File saveFile = new ClassPathResource("static/img").getFile();

							Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
									+ image.getOriginalFilename());
							System.out.println(path);
							Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			}
			
			return product;

						//
		}
		
	
		return null;
	}

}
