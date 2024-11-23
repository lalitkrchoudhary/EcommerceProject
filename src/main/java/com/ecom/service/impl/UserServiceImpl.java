package com.ecom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.model.UserDtls;
import com.ecom.repository.UserRepository;
import com.ecom.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserRepository userRepo;
	 
	@Override
	public UserDtls saveUser(UserDtls user) {
		UserDtls saveUser = userRepo.save(user);
		return saveUser;
	}

}
