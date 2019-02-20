package com.lockdown.tcc.demo.service;

import com.lockdown.tcc.demo.CreateOrderResponse;
import com.lockdown.tcc.demo.OrderRequest;

public interface OrderService {

	CreateOrderResponse generateOrder(OrderRequest request) throws Exception;

	void preparedPay(String orderNo, boolean paySuccess) throws Exception ;

	

}
