package com.lockdown.tcc.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderRequest {
	
	private String buyerId;

	private int productId;
	
	private int amount;
	
	private int totalPrice;
	

	
}
