package com.ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.service.ICategoryService;
import com.ecom.service.IProductService;
import com.ecom.service.impl.ProductServiceImpl;

@Controller
public class HomeController {

	@Autowired
	private ICategoryService categoryService;
	
	@Autowired
	private IProductService productService;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	@GetMapping("/products")
	public String products(Model m,@RequestParam(value="category",defaultValue = "") String category) {
		List<Category> categories = categoryService.getAllActiveCategory();
		List<Product> product = productService.getAllActiveProducts(category);
		m.addAttribute("products", product);
        m.addAttribute("categories",categories);	
        m.addAttribute("paramValue",category);
		return "product";
	}
	
	@GetMapping("/product/{id}")
	public String product(@PathVariable int id,Model m) {
		Product productById = productService.getProductById(id);
		m.addAttribute("p",productById);
		return "view_product";
	}
	
	
}
