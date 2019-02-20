package com.lockdown.tcctoy.core;

import java.io.Serializable;

import com.lockdown.tcctoy.api.TccTransactionStatus;
import com.lockdown.tcctoy.api.TccTransactionType;
import com.lockdown.tcctoy.api.TransactionId;

public class TransactionPermit implements Serializable {
	
	private static final long serialVersionUID = -4566401047831192475L;

	private final TransactionId id;
	private final TccTransactionStatus transactionStatus;
	private final TccTransactionType transactionType;
	private final String remoteSignature;
	
	

	public TransactionPermit(TransactionId id, TccTransactionStatus transactionStatus,TccTransactionType transactionType,String remoteSignature) {
		this.id = id;
		this.transactionStatus = transactionStatus;
		this.transactionType = transactionType;
		this.remoteSignature = remoteSignature;
	}

	public TransactionId getId() {
		return id;
	}

	public String getRemoteSignature() {
		return remoteSignature;
	}

	public TccTransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public TccTransactionType getTransactionType() {
		return transactionType;
	}

	
	@Override
	public String toString() {
		return "TransactionPermit [id=" + id + ", transactionStatus=" + transactionStatus + ", transactionType="
				+ transactionType + ", remoteSignature=" + remoteSignature + "]";
	}
	
	public static TransactionPermit empty() {
		return new TransactionPermit(null, TccTransactionStatus.PREPARATION, TccTransactionType.ROOT, null);
	}



}
