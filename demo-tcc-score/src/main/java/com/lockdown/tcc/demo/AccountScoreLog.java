package com.lockdown.tcc.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AccountScoreLog {
	
	private long id;
	private String accountId;
	private int score;
	private String orderNo;
	private ScoreSource scoreSource;
	private boolean valid;

	
	public AccountScoreLog(String accountId, int score,String orderNo, ScoreSource scoreSource) {
		super();
		this.accountId = accountId;
		this.score = score;
		this.orderNo = orderNo;
		this.scoreSource = scoreSource;
	}

	
}
