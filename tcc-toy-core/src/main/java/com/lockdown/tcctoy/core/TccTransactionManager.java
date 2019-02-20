package com.lockdown.tcctoy.core;

import com.lockdown.tcctoy.api.TccTransaction;
import com.lockdown.tcctoy.api.TccTransactionStatus;
import com.lockdown.tcctoy.api.TccTransactionType;
import com.lockdown.tcctoy.api.TransactionId;
import com.lockdown.tcctoy.api.exception.TransactionCommitException;
import com.lockdown.tcctoy.api.exception.TransactionNotFoundException;
import com.lockdown.tcctoy.api.exception.TransactionRollbackException;
import com.lockdown.tcctoy.api.support.TccTransactionRepository;
import com.lockdown.tcctoy.core.util.RequestUtils;

public class TccTransactionManager{
	
	private static TccTransactionRepository repository;
	private static ThreadLocal<TccTransaction> currentTransaction = new ThreadLocal<TccTransaction>();
	private static ThreadLocal<TransactionPermit> transactionPermit = new ThreadLocal<TransactionPermit>();
	
	
	public static TccTransaction getCurrentTransaction() {
		return currentTransaction.get();
	}
	
	public static void generateRemotePermit(TransactionId id,TccTransactionStatus status,String remoteSignature) {
		transactionPermit.set(new TransactionPermit(id, status,TccTransactionType.BRANCH,remoteSignature));
	}
	
	public static TransactionPermit getValidRequestPermit() {
		TransactionPermit permit = RequestUtils.getValidRequestPermit();
		transactionPermit.set(permit);
		return permit;
	}

	
	public static TransactionPermit getRemotePermit() {
		return transactionPermit.get();
	}
	
	
	
	public static void clearRemotePermit() {
		transactionPermit.remove();
	}
	
	
	public static void registerTransaction(TccTransaction transaction) {
		currentTransaction.remove();
		currentTransaction.set(transaction);
	}
	
	
	public static TccTransaction propagationBeginTransaction(TransactionPermit permit) {
		TccLocalTransaction transaction = new TccLocalTransaction(permit);
		repository.create(transaction);
		currentTransaction.set(transaction);
		return transaction;
	}
	
	
	
	public static TccTransaction restarExistTransaction(TransactionPermit permit) {
		TccTransaction transaction = repository.findTransaction(permit.getId());
		if(null==transaction) {
			throw new TransactionNotFoundException("transaction "+permit.getId()+" not found !");
		}
		currentTransaction.set(transaction);
		return transaction;
	}
	
	public static TccTransaction beginTransaction(){
		TccLocalTransaction transaction = new TccLocalTransaction();
		repository.create(transaction);
		currentTransaction.set(transaction);
		return transaction;
	}
	
	
	
	public static void commitTransaction(){
		TccLocalTransaction transaction = (TccLocalTransaction) getCurrentTransaction();
		transaction.setTransactionStatus(TccTransactionStatus.CONFIRMING);
		repository.update(transaction);
		try {
			transaction.commit();
			repository.delete(transaction);
		}catch(Throwable t) {
			throw new TransactionCommitException(t);
		}
	}
	
	public static void releaseTransaction() {
		currentTransaction.remove();
	}
	
	
	public static boolean hasTransaction() {
		return null!=currentTransaction.get();
	}

	public static TccTransactionRepository getRepository() {
		return TccTransactionManager.repository;
	}

	public static void  setRepository(TccTransactionRepository repository) {
		TccTransactionManager.repository = repository;
	}

	public static void rollbackTransaction()  {
		TccLocalTransaction transaction = (TccLocalTransaction) getCurrentTransaction();
		transaction.setTransactionStatus(TccTransactionStatus.CANCELING);
		repository.update(transaction);
		try {
			transaction.rollback();
			repository.delete(transaction);
		}catch(Throwable t) {
			throw new TransactionRollbackException(t);
		}
		
	}

	public static void tryToDelete(TccTransaction transaction) {
		if(repository.hasBranch(transaction) == 0) {
			repository.delete(transaction);
		}
	}

	public static void clearTransaction() {
		currentTransaction.remove();
	}
	

}
