package com.lockdown.tcc.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.lockdown.tcc.demo.AccountScore;
import com.lockdown.tcc.demo.AccountScoreLog;
import com.lockdown.tcc.demo.ScoreSource;
import com.lockdown.tcc.demo.repository.ScoreRepository;
import com.lockdown.tcctoy.core.ATccTransaction;

@Service
public class BaseScoreService implements ScoreService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@Autowired
	private ScoreRepository scoreRepository;

	@ATccTransaction(cancelMethodName="cancelAddScore",confirmMethodName="confirmAddScore")
	@Transactional
	@Override
	public void addScore(String accountId, int score, String orderNo, ScoreSource orderPayment) throws Exception {
		if(existsScoreLog(accountId,orderNo)) {
			return;
		}
		scoreRepository.createOnNotExists(new AccountScore(accountId, 0));
		AccountScoreLog log = new AccountScoreLog(accountId, score, orderNo, orderPayment);
		scoreRepository.insertLog(log);
		
		
	}
	
	private AccountScore getAccountScore(String accountId) throws Exception {
		AccountScore as = scoreRepository.getScore(accountId);
		if(null==as) {
			throw new RuntimeException("score [accountId="+accountId+"] not found!");
		}else {
			return as;
		}
	}
	
	private AccountScoreLog getScoreLog(String accountId,String orderNo) throws Exception {
		AccountScoreLog log = scoreRepository.getLog(accountId,orderNo);
		if(null==log) {
			throw new RuntimeException("log [accountId="+accountId+",orderNo="+orderNo+"] not found!");
		}
		return log;
	}
	
	@Transactional
	public void confirmAddScore(String accountId, int score, String orderNo, ScoreSource orderPayment) throws Exception{
		AccountScoreLog log = getScoreLog(accountId, orderNo);
		if(log.isValid()) {
			return;
		}
		if(log.getScore() != score) {
			throw new IllegalStateException(" exits log score mismatch argument !");
		}
		AccountScore as = getAccountScore(accountId);
		as.setScore(as.getScore()+log.getScore());
		if(as.getScore()<0) {
			throw new IllegalStateException(" score  overflow! ");
		}
		log.setValid(true);
		logger.info(" confirm score "+JSON.toJSONString(as));
		optimisticUpdate(as);
		scoreRepository.updateLogStatus(log);
	}
	
	private boolean existsScoreLog(String accountId, String orderNo) throws Exception {
		return scoreRepository.findLog(accountId,orderNo) > 0;
	}
	
	private void optimisticUpdate(AccountScore score) throws Exception {
		score.updateVersion();
		if(scoreRepository.update(score)!= 1) {
			throw new IllegalStateException(" version mismatch ,retry operation !");
		}
	}
	

	@Transactional
	public void cancelAddScore(String accountId, int score, String orderNo, ScoreSource orderPayment) throws Exception{
		if(!existsScoreLog(accountId,orderNo)) {
			return;
		}
		scoreRepository.deleteLog(accountId,orderNo);
		
	}
	
	
	

}
