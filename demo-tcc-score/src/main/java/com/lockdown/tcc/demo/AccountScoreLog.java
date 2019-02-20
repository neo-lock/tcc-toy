package com.lockdown.tcc.demo;

public class AccountScoreLog {
	
	private long id;
	private String accountId;
	private int score;
	private String orderNo;
	private ScoreSource scoreSource;
	private boolean valid;
	
	
	public AccountScoreLog() {}
	
	
	public AccountScoreLog(String accountId, int score,String orderNo, ScoreSource scoreSource) {
		super();
		this.accountId = accountId;
		this.score = score;
		this.orderNo = orderNo;
		this.scoreSource = scoreSource;
	}
	
	
	
	
	
	
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public boolean isValid() {
		return valid;
	}


	public void setValid(boolean valid) {
		this.valid = valid;
	}


	public String getOrderNo() {
		return orderNo;
	}


	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public ScoreSource getScoreSource() {
		return scoreSource;
	}
	public void setScoreSource(ScoreSource scoreSource) {
		this.scoreSource = scoreSource;
	}
	
	
	
}
