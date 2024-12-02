package com.ecom.service.impl;

import java.util.List;
import java.util.Optional;

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
	    user.setIsEnable(true);
		String encode = passwordEncoder.encode(user.getPassword());
		user.setPassword(encode);
		
		UserDtls saveUser = userRepo.save(user);
		return saveUser;
	}


	@Override
	public UserDtls getUserByEmail(String email) {
		
		return userRepo.findByEmail(email);
	}


	@Override
	public List<UserDtls> getUsers(String role) {
		
		return userRepo.findByRole(role);
	}


	@Override
	public Boolean updateAccountStatus(Integer id, Boolean status) {
		Optional<UserDtls> byId = userRepo.findById(id);
		if(byId.isPresent()) {
			UserDtls userDtls = byId.get();
			userDtls.setIsEnable(status);
			userRepo.save(userDtls);
			return true;
		}
			
			
		return false;
	}

}
