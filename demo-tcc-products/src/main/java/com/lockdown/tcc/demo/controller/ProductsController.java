package com.lockdown.tcc.demo.controller;

import com.lockdown.tcc.demo.DeliveryApply;
import com.lockdown.tcc.demo.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductsController {


    @Autowired
    private ProductsService productsService;

    @RequestMapping(value = "/product/delivery",method = RequestMethod.PUT)
    public void deliveryProduct(
            @RequestParam int pid,
            @RequestParam int amount,
            @RequestParam String orderNo,
            @RequestParam String buyerId) throws Exception{

        productsService.deliveryProduct(new DeliveryApply(pid,amount,orderNo,buyerId));
    }

}
