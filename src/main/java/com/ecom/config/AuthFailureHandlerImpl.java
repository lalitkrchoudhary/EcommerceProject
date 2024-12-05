package com.ecom.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.ecom.model.UserDtls;
import com.ecom.repository.UserRepository;
import com.ecom.service.IUserService;
import com.ecom.util.AppConstant;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailureHandlerImpl  extends SimpleUrlAuthenticationFailureHandler{
	
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private IUserService userService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		String email = request.getParameter("username");
		UserDtls userDtls = userRepo.findByEmail(email);
		
		if(userDtls.getIsEnable()) {
			
			if(userDtls.getAccountNonLocked()) {
				
				if(userDtls.getFailedAttempt() <= AppConstant.ATTEMPT_TIME) {
					userService.increaseFailedAttempt(userDtls);
					
					
				}else {
					userService.userAccountLock(userDtls);
					exception = new LockedException("Your account is locked || failed attempt 3");
				}
				
			}else {
				if(userService.unlockAccountTimeExpired(userDtls)) {
					exception = new LockedException("Your account is unlocked || Try to login again");
				}else {
					
					exception = new LockedException("Your account is locked || Please try after sometimes");
				}
			}
			
		}else {
			exception = new LockedException("Your account is inactive");
		}
		
		super.setDefaultFailureUrl("/signin?error");
		
		super.onAuthenticationFailure(request, response, exception);
	}
	
	

}
