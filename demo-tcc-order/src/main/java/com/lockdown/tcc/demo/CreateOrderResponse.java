package com.lockdown.tcc.demo;

public class CreateOrderResponse {
	
	private String orderNo;
	private OrderStatus status;
	
	
	
	public CreateOrderResponse(String orderNo, OrderStatus status) {
		super();
		this.orderNo = orderNo;
		this.status = status;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	
	
	
}
