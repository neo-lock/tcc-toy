package com.lockdown.tcc.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {
	
	private long id;
	
	private String buyerId;


	private int productId;
	
	private String orderNo;
	
	private int amount;
	
	private int totalPrice;
	
	private OrderStatus status;
	
	private OrderStatus prepareStatus;
	
	private Date createTime;
	
	private Date updateTime;
	
	
	

	
	public Order(String buyerId,int amount, int totalPrice) {
		this();
		this.buyerId = buyerId;
		this.amount = amount;
		this.totalPrice = totalPrice;
	}

	public Order(String buyerId,int productId,int amount, int totalPrice) {
		this();
		this.productId = productId;
		this.buyerId = buyerId;
		this.amount = amount;
		this.totalPrice = totalPrice;
	}
	
	
	

}
