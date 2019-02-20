package com.lockdown.tcc.demo;

public class OrderRequest {
	
	private String buyerId;
	
	private int amount;
	
	private int totalPrice;
	
	public OrderRequest(){}

	public OrderRequest(String buyerId,int amount, int totalPrice) {
		super();
		this.buyerId = buyerId;
		this.amount = amount;
		this.totalPrice = totalPrice;
	}
	
	

	public String getBuyerId() {
		return buyerId;
	}



	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}



	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	
}
