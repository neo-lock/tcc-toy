package com.lockdown.tcc.demo.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.lockdown.tcc.demo.Order;

@Mapper
public interface OrderRepository {
	
	public static final String TABLE_NAME = "t_order";

	@Insert(" insert into "+TABLE_NAME +" (orderNo,buyerId,productId,amount,totalPrice,status,prepareStatus,createTime,updateTime) "
			+ " values (#{orderNo},#{buyerId},#{productId},#{amount},#{totalPrice},#{status},#{prepareStatus},#{createTime},#{updateTime}) ")
	void createOrder(Order order) throws Exception;


	@Select(" select * from "+TABLE_NAME+" where orderNo = #{orderNo}")
	Order getOrder(String orderNo) throws Exception ;

	@Update("update "+TABLE_NAME+" set prepareStatus = #{prepareStatus},status = #{status} , updateTime = now() where orderNo = #{orderNo}  ")
	void updatePrepareStatus(Order order);
	
	
}
