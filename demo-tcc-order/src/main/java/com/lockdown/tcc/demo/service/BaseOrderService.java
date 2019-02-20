package com.lockdown.tcc.demo.service;


import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lockdown.tcc.demo.CreateOrderResponse;
import com.lockdown.tcc.demo.Order;
import com.lockdown.tcc.demo.OrderRequest;
import com.lockdown.tcc.demo.OrderStatus;
import com.lockdown.tcc.demo.remote.RemoteScoreService;
import com.lockdown.tcc.demo.repository.OrderRepository;
import com.lockdown.tcctoy.core.ATccTransaction;


@Service
public class BaseOrderService implements OrderService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private RemoteScoreService remoteScoreService;

	

	@Transactional
	public void confirmOrder(String orderNo, boolean paySuccess) throws Exception{
		logger.info(" 确认订单状态 {} ",orderNo);
		Order order = getOrder(orderNo);
		if(order.getStatus().getValue() == OrderStatus.PAY_SUCCEED.getValue()) {
			return;
		}
		order.setStatus(paySuccess?OrderStatus.PAY_SUCCEED:OrderStatus.PAY_FAILED);
		order.setPrepareStatus(null);
		orderRepository.updatePrepareStatus(order);
	}
	
	
	
	@Transactional
	public void cancelOrder(String orderNo, boolean paySuccess) throws Exception{
		logger.info(" 取消订单状态 {} ",orderNo);
		Order order = getOrder(orderNo);
		if(order.getStatus().getValue() == OrderStatus.PAY_FAILED.getValue()) {
			return;
		}
		order.setPrepareStatus(null);
		orderRepository.updatePrepareStatus(order);
	}
	

	
	@ATccTransaction(confirmMethodName="confirmOrder",cancelMethodName="cancelOrder")
	@Transactional
	@Override
	public void perparedPay(String orderNo, boolean paySuccess) throws Exception {
		Order order = getOrder(orderNo);
		if(order.getStatus().getValue()>OrderStatus.PAYING.getValue()) {
			return;
		}
		order.setPrepareStatus(paySuccess?OrderStatus.PAY_SUCCEED:OrderStatus.PAY_FAILED);
		orderRepository.updatePrepareStatus(order);
		if(paySuccess) {
			remoteScoreService.preparedAddScore(order.getBuyerId(),order.getTotalPrice(),order.getOrderNo());
		}
	}
	
	
	private Order getOrder(String orderNo) throws Exception {
		Order order = orderRepository.getOrder(orderNo);
		if(null == order) {
			throw new RuntimeException(" order "+orderNo+" not found!");
		}
		return order;
	}
	


	
	@Transactional
	@Override
	public CreateOrderResponse generateOrder(OrderRequest request) throws Exception {
		Order order = new Order(request.getBuyerId(),request.getAmount(),request.getTotalPrice());
		order.setUpdateTime(new Date());
		order.setCreateTime(new Date());
		order.setBuyerId(request.getBuyerId());
		order.setOrderNo(UUID.randomUUID().toString());
		order.setPrepareStatus(OrderStatus.CREATED);
		order.setStatus(OrderStatus.CREATED);
		orderRepository.createOrder(order);
		return new CreateOrderResponse(order.getOrderNo(), order.getStatus());
	}



	
	
	


}
