package com.lockdown.tcc.demo;

public class AccountScore {
	
	private String accountId;	
	private int score;
	private int version;
	
	public AccountScore() {}

	public AccountScore(String accountId, int score) {
		super();
		this.accountId = accountId;
		this.score = score;
	}
	
	public void updateVersion() {
		this.version += 1;
	}


	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
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
	
	
}
