package com.lockdown.tcc.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AccountScore {
	
	private String accountId;	
	private int score;
	private int version;
	


	public AccountScore(String accountId, int score) {
		super();
		this.accountId = accountId;
		this.score = score;
	}
	
	public void updateVersion() {
		this.version += 1;
	}


}
