package com.ecom.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.model.UserDtls;
import com.ecom.repository.UserRepository;
import com.ecom.service.ICategoryService;
import com.ecom.service.IProductService;
import com.ecom.service.IUserService;
import com.ecom.service.impl.ProductServiceImpl;
import com.ecom.util.CommonUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private IProductService productService;

	@Autowired
	private IUserService userService;

	@Autowired
	private CommonUtil commonUtil;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@ModelAttribute
	public void getUserDetails(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			UserDtls user = userService.getUserByEmail(email);
			System.out.println(user);
			m.addAttribute("userDtls", user);

		}

		List<Category> allActiveCategory = categoryService.getAllActiveCategory();
		m.addAttribute("categorys", allActiveCategory);

	}

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/signin")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@GetMapping("/products")
	public String products(Model m, @RequestParam(value = "category", defaultValue = "") String category) {
		List<Category> categories = categoryService.getAllActiveCategory();
		List<Product> product = productService.getAllActiveProducts(category);
		m.addAttribute("products", product);
		m.addAttribute("categories", categories);
		m.addAttribute("paramValue", category);
		return "product";
	}

	@GetMapping("/product/{id}")
	public String product(@PathVariable int id, Model m) {
		Product productById = productService.getProductById(id);
		m.addAttribute("p", productById);
		return "view_product";
	}

	// Registration page controller started

	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute UserDtls user, @RequestParam("img") MultipartFile file, HttpSession session)
			throws IOException {

		String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();

		user.setProfileImage(imageName);
		UserDtls save = userService.saveUser(user);

		if (!ObjectUtils.isEmpty(save)) {

			if (!file.isEmpty()) {
				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
						+ file.getOriginalFilename());
				System.out.println(path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			session.setAttribute("succMsg", "User save succcessfully");

		} else {
			session.setAttribute("errorMsg", "Something went wrong while saving the user");
		}

		return "redirect:/register";
	}

	// Forget Password logic
	@GetMapping("/forgot-password")
	public String showForgotPassword() {

		return "forgot_password";
	}

	@PostMapping("/forgot-password")
	public String processForgotPassword(@RequestParam String email, HttpSession session, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {

		System.out.println("HomeController.FOrgot password ()");

		UserDtls userByEmail = userService.getUserByEmail(email);

		if (ObjectUtils.isEmpty(userByEmail)) {
			session.setAttribute("errorMsg", "invalid email");
		} else {

			String resetToken = UUID.randomUUID().toString();
			userService.updateUserResetToken(email, resetToken);

			// Generate url:: http://localhost:8080/reset-password?token=afjaophaghooa

			String url = CommonUtil.generateUrl(request) + "/reset-password?token=" + resetToken;

			Boolean sendMail = commonUtil.sendMail(url, email);

			if (sendMail) {
				session.setAttribute("succMsg", "Check you email to reset your password");
			} else {
				session.setAttribute("errorMsg", "Something went wrong while sending email");

			}
		}

		return "redirect:/forgot-password";
	}

	// Reset Password
	@GetMapping("/reset-password")
	public String showResetPassword(@RequestParam String token, HttpSession session, Model m) {
		UserDtls userByToken = userService.getUserByToken(token);

		if (userByToken == null) {

			m.addAttribute("msg", "Your link is invalid or expired");
			return "message";
		}
		
		m.addAttribute("tok",token);
		
		return "reset_password";
	}

	@PostMapping("/reset-password")
	public String showResetPassword(@RequestParam String token, @RequestParam String password, HttpSession session,Model m) {
		UserDtls userByToken = userService.getUserByToken(token);

		if (userByToken == null) {

			m.addAttribute("msg", "Your link is invalid");
			
			return "message";
		}else {
			userByToken.setPassword(passwordEncoder.encode(password));
			userByToken.setResetToken(null);
			userService.updateUser(userByToken);
			session.setAttribute("succMsg", "Password change successfully");
			m.addAttribute("msg", "Password change successfully");
			return "message";
			
		}

		 
	}

}
