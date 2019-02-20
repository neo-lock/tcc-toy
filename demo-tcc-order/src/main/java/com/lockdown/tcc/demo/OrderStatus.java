package com.lockdown.tcc.demo;

public enum OrderStatus {
	
	CREATED(0),PAYING(1),PAY_FAILED(2),PAY_SUCCEED(2);
	
	private final int value;
	
	OrderStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	
}
