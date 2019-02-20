package com.lockdown.tcc.demo;

import java.util.Date;

public class Order {
	
	private long id;
	
	private String buyerId;
	
	private String orderNo;
	
	private int amount;
	
	private int totalPrice;
	
	private OrderStatus status;
	
	private OrderStatus prepareStatus;
	
	private Date createTime;
	
	private Date updateTime;
	
	
	
	public Order() {
	}
	
	public Order(String buyerId,int amount, int totalPrice) {
		this();
		this.buyerId = buyerId;
		this.amount = amount;
		this.totalPrice = totalPrice;
	}
	
	
	
	public OrderStatus getPrepareStatus() {
		return prepareStatus;
	}



	public void setPrepareStatus(OrderStatus prepareStatus) {
		this.prepareStatus = prepareStatus;
	}



	
	
	

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	

}
