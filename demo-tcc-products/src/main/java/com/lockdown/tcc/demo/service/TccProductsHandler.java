package com.lockdown.tcc.demo.service;

import com.lockdown.tcc.demo.DeliveryApply;
import com.lockdown.tcc.demo.Products;
import com.lockdown.tcc.demo.ProductsDeliveryLog;
import com.lockdown.tcc.demo.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 这是个测试范例
 */
@Component
public class TccProductsHandler {

    @Autowired
    private ProductsRepository productsRepository;

    @Transactional
    public void confirmDelivery(DeliveryApply deliveryApply) throws Exception{
        ProductsDeliveryLog deliveryLog = getDeliveryLog(deliveryApply.getPid(),deliveryApply.getOrderNo());
        if(deliveryLog.isValid()){
            return;
        }
        deliveryLog.setValid(true);
        productsRepository.updateDeliveryLog(deliveryLog);
    }

    @Transactional
    public void cancelDelivery(DeliveryApply deliveryApply) throws Exception{
        if(!exitsApply(deliveryApply.getPid(),deliveryApply.getOrderNo())){
            return;
        }
        Products products = getProductsAndLock(deliveryApply.getPid());
        products.setAmount(products.getAmount()+deliveryApply.getAmount());
        optimisticUpdate(products);
        productsRepository.deleteLog(deliveryApply.getPid(),deliveryApply.getOrderNo());
    }

    private void optimisticUpdate(Products products) throws Exception {
        products.updateVersion();
        int result = productsRepository.updateProducts(products);
        if(result<=0){
            throw new RuntimeException(" optimistic update failed !");
        }
    }

    private ProductsDeliveryLog getDeliveryLog(int pid,String orderNo) throws Exception{
        ProductsDeliveryLog deliveryLog = productsRepository.getDeliveryLog(pid,orderNo);
        if(null==deliveryLog){
            throw new RuntimeException(" delivery log not found !");
        }
        return deliveryLog;
    }

    private Products getProductsAndLock(int pid) throws Exception {
        Products products = productsRepository.getProductsAndLock(pid);
        if(null==products){
            throw new RuntimeException(" products not found! ");
        }
        return products;
    }

    private boolean exitsApply(int pid,String orderNo) throws Exception {
        return productsRepository.countApply(pid,orderNo) > 0;
    }

}
