package com.lockdown.tcc.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderPayRequest {
	
	private String orderNo;
	
	private boolean paySuccess;
	

	
}
