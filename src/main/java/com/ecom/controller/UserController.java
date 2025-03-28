package com.ecom.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecom.model.Cart;
import com.ecom.model.Category;
import com.ecom.model.OrderRequest;
import com.ecom.model.ProductOrder;
import com.ecom.model.UserDtls;
import com.ecom.service.ICartService;
import com.ecom.service.ICategoryService;
import com.ecom.service.IOrderService;
import com.ecom.service.IUserService;
import com.ecom.util.OrderStatus;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ICategoryService categoryService;
	
	@Autowired
	private ICartService cartService;
	
	@Autowired
	private IOrderService orderService; 
	
	
	@ModelAttribute
	public void getUserDetails(Principal p,Model m) {
		if(p!=null) {
			String email = p.getName();
			UserDtls user = userService.getUserByEmail(email);
			System.out.println(user);
			m.addAttribute("userDtls",user);
			
			Integer countCart = cartService.getCountCart(user.getId());
			m.addAttribute("countCart",countCart);

			
		}
		
		List<Category> allActiveCategory = categoryService.getAllActiveCategory();
		m.addAttribute("categorys",allActiveCategory);
	}

	@GetMapping("/")
	public String home() {
		
		return "user/home";
		
	}
	@GetMapping("/addCart")
	public String addToCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session) {
		
		Cart saveCart = cartService.saveCart(pid, uid);
		
		if(ObjectUtils.isEmpty(saveCart)) {
			session.setAttribute("errorMsg", "Failed to save the cart internal server error");
		}else {
			
			session.setAttribute("succMsg", "Product added to cart");
		}
		
		return "redirect:/product/"+ pid;
	}
	
	@GetMapping("/cart")
	public String loadCartPage(Principal p, Model m) {
		UserDtls user = getLoggedInUserDetails(p);
		List<Cart> carts = cartService.getCartByUser(user.getId());
		m.addAttribute("carts",carts);
		
		if(carts.size() >0) {
		Double totalOrderPrice = carts.get(carts.size()-1).getTotalOrderPrice();
		m.addAttribute("totalOrderPrice",totalOrderPrice);
		}
		return "/user/cart";
	}
	
	@GetMapping("/cartQuantityUpdate")
	public String updateCartQuantity(@RequestParam String sy, @RequestParam Integer cid) {
		cartService.updateQuantity(sy, cid);
		return "redirect:/user/cart";
	}
	
	

	private UserDtls getLoggedInUserDetails(Principal p) {
		String email = p.getName();
		UserDtls userDtls = userService.getUserByEmail(email);
		return userDtls;
	}
	
	
	@GetMapping("/orders")
	public String orderPage(Principal p, Model m) {
		
		UserDtls user = getLoggedInUserDetails(p);
		List<Cart> carts = cartService.getCartByUser(user.getId());
		m.addAttribute("carts",carts);
		
		if(carts.size() >0) {
			Double orderPrice = carts.get(carts.size()-1).getTotalOrderPrice();

		Double totalOrderPrice = carts.get(carts.size()-1).getTotalOrderPrice()+250+100;
		m.addAttribute("totalOrderPrice",totalOrderPrice);
		m.addAttribute("orderPrice",orderPrice);
		}
		
		return "/user/order";
	}
	
	@PostMapping("/save-order")
	public String saveOrder(@ModelAttribute OrderRequest request,Principal p) {
		System.out.println(request);
		
		UserDtls user = getLoggedInUserDetails(p);	
		
		orderService.saveOrder(user.getId(), request);
		
		return "redirect:/user/success";
	}
	
	@GetMapping("/success")
	public String loadSuccess() {
		return "/user/success";
	}
	
	@GetMapping("/user-orders")
	public String myOrder(Model m, Principal p) {
		UserDtls loginUser = getLoggedInUserDetails(p);
		List<ProductOrder> orders = orderService.getOrdersByUser(loginUser.getId());
		m.addAttribute("orders",orders);
		return "/user/my_orders";
	}
	
	@GetMapping("/update-status")
	public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, HttpSession session) {
		OrderStatus[] values = OrderStatus.values();
		String status = null;
		for(OrderStatus orderSt : values) {
			if(orderSt.getId().equals(st)) {
				status = orderSt.getName();
			}
		}
		Boolean updateOrder = orderService.updateOrderStatus(id, status);
		
		if(updateOrder) {
			session.setAttribute("succMsg", "Status updated");
		}else {
			session.setAttribute("errorMsg", "Status not updated");
		}
		System.out.println(values);
		return "redirect:/user/user-orders";
	}
	
	
	
}


