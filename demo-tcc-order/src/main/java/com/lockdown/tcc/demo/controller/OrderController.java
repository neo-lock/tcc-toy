package com.lockdown.tcc.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lockdown.tcc.demo.CreateOrderResponse;
import com.lockdown.tcc.demo.OrderPayRequest;
import com.lockdown.tcc.demo.OrderRequest;
import com.lockdown.tcc.demo.service.OrderService;

@RestController
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	/**
	 * 创建一个订单
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/order",method=RequestMethod.POST)
	public CreateOrderResponse generateOrder(@RequestBody OrderRequest request) throws Exception{
		return orderService.generateOrder(request);
	}
	
	/**
	 * 支付结果
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value="/order/pay",method=RequestMethod.POST)
	public void preparedOrderPay(@RequestBody OrderPayRequest request) throws Exception{
		orderService.preparedPay(request.getOrderNo(),request.isPaySuccess());
	}

}
