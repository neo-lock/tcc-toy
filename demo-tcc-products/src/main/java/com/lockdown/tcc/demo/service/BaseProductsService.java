package com.lockdown.tcc.demo.service;

import com.lockdown.tcc.demo.DeliveryApply;
import com.lockdown.tcc.demo.Products;
import com.lockdown.tcc.demo.ProductsDeliveryLog;
import com.lockdown.tcc.demo.repository.ProductsRepository;
import com.lockdown.tcctoy.core.ATccTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BaseProductsService implements ProductsService {

    @Autowired
    private ProductsRepository productsRepository;

    @ATccTransaction(cancelClass = TccProductsHandler.class,cancelMethodName = "cancelDelivery",confirmClass = TccProductsHandler.class,confirmMethodName = "confirmDelivery")
    @Override
    public void deliveryProduct(DeliveryApply deliveryApply) throws Exception {
        if(exitsApply(deliveryApply.getPid(),deliveryApply.getOrderNo())){
            return;
        }
        //------------------------------这里最好用分布式锁，而不是数据库行级锁------------------------------------------------

        Products products = getProductsAndLock(deliveryApply.getPid());
        products.setAmount(products.getAmount()-deliveryApply.getAmount());
        if(products.getAmount()<0){
            throw new RuntimeException(" products amount overflow !");
        }
        optimisticUpdate(products);
        //------------------------------这里最好用分布式锁，而不是数据库行级锁------------------------------------------------
        ProductsDeliveryLog deliveryLog = new ProductsDeliveryLog(deliveryApply.getPid(),deliveryApply.getOrderNo(),deliveryApply.getAmount(),deliveryApply.getBuyerId());
        productsRepository.insertLog(deliveryLog);
    }

    private void optimisticUpdate(Products products) throws Exception {
        products.updateVersion();
        int result = productsRepository.updateProducts(products);
        if(result<=0){
            throw new RuntimeException(" optimistic update failed !");
        }
    }

    //使用数据库行级锁
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
