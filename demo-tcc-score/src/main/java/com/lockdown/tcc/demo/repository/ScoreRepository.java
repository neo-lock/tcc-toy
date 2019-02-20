package com.lockdown.tcc.demo.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.lockdown.tcc.demo.AccountScore;
import com.lockdown.tcc.demo.AccountScoreLog;

@Mapper
public interface ScoreRepository {
	
	public final static String TABLE_NAME = "t_score";
	public final static String LOG_TABLE_NAME = "t_score_log";

	
	@Update("update "+TABLE_NAME+" set  score = #{score},updateTime = now(),version = #{version} where accountId = #{accountId} and version = ${version-1} ")
	public int update(AccountScore as) throws Exception;

	@Insert("insert into "+LOG_TABLE_NAME+" (accountId,score,orderNo,scoreSource) values (#{accountId},#{score},#{orderNo},#{scoreSource})")
	public void insertLog(AccountScoreLog log) throws Exception;

	@Select(" select * from "+TABLE_NAME +" where accountId = #{accountId} ")
	public AccountScore getScore(String accountId) throws Exception;


	@Select("select count(*) from "+LOG_TABLE_NAME+" where accountId = #{accountId} and orderNo = #{orderNo} ")
	public int findLog(@Param("accountId") String accountId, @Param("orderNo") String orderNo) throws Exception;

	@Delete(" delete from "+LOG_TABLE_NAME+" where  accountId = #{accountId} and orderNo = #{orderNo}")
	public void deleteLog(@Param("accountId") String accountId, @Param("orderNo") String orderNo) throws Exception;

	@Select(" select * from "+LOG_TABLE_NAME+" where accountId = #{accountId} and orderNo = #{orderNo}")
	public AccountScoreLog getLog(@Param("accountId") String accountId, @Param("orderNo") String orderNo) throws Exception;

	@Update("update "+LOG_TABLE_NAME+" set valid = #{valid} where id = #{id} ")
	public void updateLogStatus(AccountScoreLog log) throws Exception;

	@Insert("insert into "+TABLE_NAME+" (accountId,score,updateTime) values (#{accountId},#{score},now()) on duplicate key update updateTime= now()")
	public void createOnNotExists(AccountScore accountScore) throws Exception;

}
