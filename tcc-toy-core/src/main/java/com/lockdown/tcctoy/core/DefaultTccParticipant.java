package com.lockdown.tcctoy.core;

import java.lang.reflect.InvocationTargetException;

import com.lockdown.tcctoy.api.TccParticipant;
import com.lockdown.tcctoy.api.TccParticipantType;
import com.lockdown.tcctoy.api.TransactionId;
import com.lockdown.tcctoy.api.exception.TccInvokeMethodException;

/**
 * 
 * 
 * @author neo
 *
 */
class DefaultTccParticipant implements TccParticipant {
	
	private static final long serialVersionUID = 5102042756434842284L;
	
	private TransactionId tid;
	private MethodInvocation confirmInvoker;
	private MethodInvocation cancelInvoker;
	private TccParticipantType participantType;
	
	public DefaultTccParticipant() {}
	
	public DefaultTccParticipant(TransactionId tid, MethodInvocation confirmInvoker, MethodInvocation cancelInvoker,TccParticipantType participantType) {
		this.tid = tid;
		this.confirmInvoker = confirmInvoker;
		this.cancelInvoker = cancelInvoker;
		this.participantType = participantType;
	}
	
	


	public void commit() {
		try {
			confirmInvoker.invoke();
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new TccInvokeMethodException(e);
		}
	}


	public void rollback() {
		try {
			cancelInvoker.invoke();
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new TccInvokeMethodException(e);
		}
	}

	

	


	public TransactionId getTid() {
		return tid;
	}




	public void setTid(TransactionId tid) {
		this.tid = tid;
	}




	public MethodInvocation getConfirmInvoker() {
		return confirmInvoker;
	}




	public void setConfirmInvoker(MethodInvocation confirmInvoker) {
		this.confirmInvoker = confirmInvoker;
	}




	



	public MethodInvocation getCancelInvoker() {
		return cancelInvoker;
	}




	public void setCancelInvoker(MethodInvocation cancelInvoker) {
		this.cancelInvoker = cancelInvoker;
	}




	public TccParticipantType getParticipantType() {
		return participantType;
	}




	public void setParticipantType(TccParticipantType participantType) {
		this.participantType = participantType;
	}




	@Override
	public TransactionId transactionId() {
		return tid;
	}
	
	
	
	
	
	
	
}
