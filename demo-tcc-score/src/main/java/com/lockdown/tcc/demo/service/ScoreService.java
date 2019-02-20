package com.lockdown.tcc.demo.service;

import com.lockdown.tcc.demo.ScoreSource;

public interface ScoreService {

	void addScore(String buyerId, int totalPrice, String orderNo, ScoreSource orderPayment) throws Exception ;

	
}
