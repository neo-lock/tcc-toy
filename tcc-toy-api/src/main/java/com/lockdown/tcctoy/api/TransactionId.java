package com.lockdown.tcctoy.api;

import java.io.Serializable;
import java.util.UUID;

/**
 * 表示一个事务ID，全局唯一
 * @author neo
 *
 */
public class TransactionId implements Serializable,Cloneable {
	private static final long serialVersionUID = -3512100240303145083L;
	
	private String tid;
	private String branchQualifier;
	
	public TransactionId() {
		this(UUID.randomUUID().toString());
	}
	
	public TransactionId(String tid) {
		this.tid = tid;
		this.branchQualifier = "ROOT";
	}
	
	public TransactionId(String tid,String branchQualifier) {
		this.tid = tid;
		this.branchQualifier = branchQualifier;
	}

	public String getBranchQualifier() {
		return branchQualifier;
	}

	public void setBranchQualifier(String branchQualifier) {
		this.branchQualifier = branchQualifier;
	}

	public String getTid() {
		return tid;
	}
	
	

	public void setTid(String tid) {
		this.tid = tid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((branchQualifier == null) ? 0 : branchQualifier.hashCode());
		result = prime * result + ((tid == null) ? 0 : tid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransactionId other = (TransactionId) obj;
		if (branchQualifier == null) {
			if (other.branchQualifier != null)
				return false;
		} else if (!branchQualifier.equals(other.branchQualifier))
			return false;
		if (tid == null) {
			if (other.tid != null)
				return false;
		} else if (!tid.equals(other.tid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TransactionId [tid=" + tid + ", branchQualifier=" + branchQualifier + "]";
	}

	@Override
	public TransactionId clone() {
		return new TransactionId(this.tid,this.branchQualifier);
	}
	
	
	
}
