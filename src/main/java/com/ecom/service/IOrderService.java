package com.ecom.service;

import com.ecom.model.OrderRequest;
import com.ecom.model.ProductOrder;

public interface IOrderService {
	
	public void saveOrder(Integer userid,OrderRequest orderRequest);

}
