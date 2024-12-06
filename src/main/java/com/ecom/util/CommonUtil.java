package com.ecom.util;

import jakarta.servlet.http.HttpServletRequest;

public class CommonUtil {
	
	public static Boolean sendMail(String url, String reciepentEmail) {
		return  false;
	}

	// Generate url:: http://localhost:8080/forgot-password // by default generate
	public static String generateUrl(HttpServletRequest request) {
		
		 String siteUrl = request.getRequestURL().toString();
		 
		// Generate url:: http://localhost:8080
		 
		 return siteUrl.replace(request.getServletPath(), "");
		 
	}

}
