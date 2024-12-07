package com.ecom.util;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {
	
	@Autowired
	private   JavaMailSender mailSender;
	
	public  Boolean sendMail(String url, String reciepentEmail) throws UnsupportedEncodingException, MessagingException {
		
		MimeMessage message = mailSender.createMimeMessage();
		  MimeMessageHelper helper = new  MimeMessageHelper(message);
		  
		  helper.setFrom("lalit8694123@gmail.com","EPoint.com");
		  helper.setTo(reciepentEmail);
		  
		  String content ="<p>Hello,</p>"+"<p> You have requested to reset your password.</p>"
		  +"<p>Click the link below to change your password</p>"+"<p><a href=\""+url+"\">Change your password</a></p>";
		  
		  helper.setSubject("Password Reset");
		  helper.setText(content,true);
		  mailSender.send(message);
		  
		  
		  return true;
		  
		
		
	}

	// Generate url:: http://localhost:8080/forgot-password // by default generate
	public static String generateUrl(HttpServletRequest request) {
		
		 String siteUrl = request.getRequestURL().toString();
		 
		// Generate url:: http://localhost:8080
		 
		 return siteUrl.replace(request.getServletPath(), "");
		 
	}

}
