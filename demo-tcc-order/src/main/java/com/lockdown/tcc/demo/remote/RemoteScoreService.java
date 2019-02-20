package com.lockdown.tcc.demo.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.lockdown.tcctoy.core.ATccRemoteParticipant;



@FeignClient("service-score")
public interface RemoteScoreService {
		
	
	@ATccRemoteParticipant
	@RequestMapping(value= {"/order/score"},method= {RequestMethod.PUT})
	public void preparedAddScore(@RequestParam("buyerId") String buyerId, @RequestParam("totalPrice") int totalPrice, @RequestParam("orderNo") String orderNo);
	

}
