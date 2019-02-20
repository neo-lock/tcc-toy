package com.lockdown.tcc.demo;

public class OrderPayRequest {
	
	private String orderNo;
	
	private boolean paySuccess;
	
	public OrderPayRequest() {}

	public OrderPayRequest(String orderNo, boolean paySuccess) {
		super();
		this.orderNo = orderNo;
		this.paySuccess = paySuccess;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public boolean isPaySuccess() {
		return paySuccess;
	}

	public void setPaySuccess(boolean paySuccess) {
		this.paySuccess = paySuccess;
	}
	
	
	
}
