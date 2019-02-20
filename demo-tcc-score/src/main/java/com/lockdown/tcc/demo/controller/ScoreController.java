package com.lockdown.tcc.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lockdown.tcc.demo.ScoreSource;
import com.lockdown.tcc.demo.service.ScoreService;

@RestController
public class ScoreController {
	
	
	@Autowired
	private ScoreService scoreService;
	
	
	@RequestMapping(value="/order/score",method=RequestMethod.PUT)
	public void preparedAddScore(@RequestParam String buyerId,@RequestParam int totalPrice,@RequestParam String orderNo) throws Exception{
		scoreService.addScore(buyerId,totalPrice,orderNo,ScoreSource.ORDER_PAYMENT);
	}
}
