package com.lockdown.tcctoy.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lockdown.tcctoy.api.TccParticipant;
import com.lockdown.tcctoy.api.TccTransaction;
import com.lockdown.tcctoy.api.TccTransactionStatus;
import com.lockdown.tcctoy.api.TccTransactionType;
import com.lockdown.tcctoy.api.TransactionId;

class TccLocalTransaction implements TccTransaction {
	
	private static final long serialVersionUID = -3035870197687069209L;
	
	private TransactionId tid;
	private TccTransactionType transactionType;
	private TccTransactionStatus transactionStatus = TccTransactionStatus.PREPARATION;
	private List<DefaultTccParticipant> paricipants = new ArrayList<DefaultTccParticipant>();
	private int verion = 1;
	private int invokeCount = 0;
	private Date createTime = new Date();
	private Date updateTime = new Date();
	
	
	
	public TccLocalTransaction(){
		this.tid = new TransactionId();
		this.transactionType = TccTransactionType.ROOT;
		
	}
	
	TccLocalTransaction(TransactionPermit permit){
		this.tid = permit.getId();
		this.transactionStatus = permit.getTransactionStatus();
		this.transactionType = TccTransactionType.BRANCH;
	}

	public TransactionId getTid() {
		return tid;
	}

	public void setTid(TransactionId tid) {
		this.tid = tid;
	}

	public TccTransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TccTransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public TccTransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(TccTransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public List<DefaultTccParticipant> getParicipants() {
		return paricipants;
	}

	public void setParicipants(List<DefaultTccParticipant> paricipants) {
		this.paricipants = paricipants;
	}

	public int getVerion() {
		return verion;
	}

	public void setVerion(int verion) {
		this.verion = verion;
	}

	public int getInvokeCount() {
		return invokeCount;
	}

	public void setInvokeCount(int invokeCount) {
		this.invokeCount = invokeCount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public void commit() {
		for(DefaultTccParticipant paritipant : paricipants) {
			paritipant.commit();
		}
	}

	@Override
	public void rollback() {
		for(DefaultTccParticipant paritipant : paricipants) {
			paritipant.rollback();
		}
	}

	@Override
	public void registerParticipant(TccParticipant participant) {
		paricipants.add((DefaultTccParticipant)participant);
	}

	@Override
	public TransactionId transactionId() {
		return this.tid.clone();
	}

	@Override
	public TccTransaction changeStatus(TccTransactionStatus newStatus) {
		this.transactionStatus = newStatus;
		return this;
	}

	@Override
	public void plusInvokeCount() {
		this.invokeCount += 1;
	}

	@Override
	public TccTransaction updateVersion() {
		this.verion +=1;
		this.updateTime = new Date();
		return this;
	}


	

}
