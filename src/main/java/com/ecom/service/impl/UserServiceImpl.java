package com.ecom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.model.UserDetails;
import com.ecom.repository.UserRepository;
import com.ecom.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserRepository userRepo;
	 
	@Override
	public UserDetails saveUser(UserDetails user) {
		UserDetails saveUser = userRepo.save(user);
		return saveUser;
	}

}
