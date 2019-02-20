package com.lockdown.tcc.demo.remote;

import com.lockdown.tcctoy.core.ATccRemoteParticipant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-products")
public interface RemoteProductsService {



    @ATccRemoteParticipant
    @RequestMapping(value = "/product/delivery",method = RequestMethod.PUT)
    public void deliveryProduct(
            @RequestParam("pid") int pid,
            @RequestParam("amount")int amount,
            @RequestParam("orderNo") String orderNo,
            @RequestParam("buyerId") String buyerId) throws Exception;

}
