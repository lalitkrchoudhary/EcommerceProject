package com.ecom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecom.model.UserDtls;
import com.ecom.repository.UserRepository;
import com.ecom.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	 
	@Override
	public UserDtls saveUser(UserDtls user) {
		user.setRole("ROLE_ADMIN");
		String encode = passwordEncoder.encode(user.getPassword());
		user.setPassword(encode);
		
		UserDtls saveUser = userRepo.save(user);
		return saveUser;
	}

}
