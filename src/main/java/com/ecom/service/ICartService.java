package com.ecom.service;

import java.util.List;

import com.ecom.model.Cart;

public interface ICartService {
	
	public Cart saveCart(Integer productId, Integer userId);
	public List<Cart> getCartByUser(Integer userId);
	public Integer getCountCart(Integer userId);

}