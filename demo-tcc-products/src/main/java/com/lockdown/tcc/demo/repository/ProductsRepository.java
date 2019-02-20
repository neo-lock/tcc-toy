package com.lockdown.tcc.demo.repository;

import com.lockdown.tcc.demo.Products;
import com.lockdown.tcc.demo.ProductsDeliveryLog;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ProductsRepository {

    public final static String TABLE_NAME = "t_products";
    public final static String LOG_TABLE_NAME = "t_products_delivery_log";


    @Select(" select count(*) from "+LOG_TABLE_NAME+" where pid = #{pid} and orderNo = #{orderNo}")
    int countApply(@Param("pid") int pid, @Param("orderNo") String orderNo) throws Exception;

    @Select(" select * from "+TABLE_NAME+" where id = #{id} for update ")
    Products getProductsAndLock(int pid) throws Exception;

    @Update(" update "+TABLE_NAME+" set amount = #{amount}, version = #{version} where id = #{id} and version = ${version-1} ")
    int updateProducts(Products products) throws Exception;


    @Insert(" insert into "+LOG_TABLE_NAME+" (pid,orderNo,amount,buyerId) values (#{pid},#{orderNo},#{amount},#{buyerId})")
    void insertLog(ProductsDeliveryLog deliveryLog) throws Exception;

    @Delete(" delete from "+LOG_TABLE_NAME+" where pid = #{pid} and orderNo = #{orderNo}")
    void deleteLog(@Param("pid") int pid, @Param("orderNo") String orderNo) throws Exception;

    @Select(" select * from "+LOG_TABLE_NAME+" where pid = #{pid} and orderNo = #{orderNo}")
    ProductsDeliveryLog getDeliveryLog(@Param("pid") int pid, @Param("orderNo") String orderNo) throws Exception;

    @Update(" update "+LOG_TABLE_NAME+" set valid = #{valid} where pid = #{pid} and orderNo = #{orderNo} ")
    void updateDeliveryLog(ProductsDeliveryLog deliveryLog) throws Exception;
}
