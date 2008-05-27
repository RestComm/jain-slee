package org.mobicents.slee.runtime.cache.tests;

import java.util.Stack;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

public class TransactionManagerMockup implements TransactionManager {

	/**
	 * Stack data structure allowing simulation of concurrent tx access  
	 */
	private Stack transactionStack = new Stack();

	public void begin() throws NotSupportedException, SystemException {
		throw new NotSupportedException("This mockup expects the driving test to set the current tx");
	}

	public void commit() throws RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SecurityException,
			IllegalStateException, SystemException {
		// TODO Auto-generated method stub
	}

	public int getStatus() throws SystemException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Transaction getTransaction() throws SystemException {
		if (transactionStack.isEmpty()) {
			throw new SystemException("There is no active transaction in this runtime context."); 
		} else {
			Transaction currentTx = (Transaction)transactionStack.peek();
			return currentTx;
		}
	}

	public void resume(Transaction arg0) throws InvalidTransactionException,
			IllegalStateException, SystemException {
		// TODO Auto-generated method stub

	}

	public void rollback() throws IllegalStateException, SecurityException,
			SystemException {
		// TODO Auto-generated method stub

	}

	public void setRollbackOnly() throws IllegalStateException, SystemException {
		// TODO Auto-generated method stub

	}

	public void setTransactionTimeout(int arg0) throws SystemException {
		// TODO Auto-generated method stub

	}

	public Transaction suspend() throws SystemException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * Pushes the given tx at the top of the tx stack.
	 * Used by the test suite to simulate 
	 * multi-transactional access to the transactional cache.
	 *
	 */
	void pushCurrentTransaction(Transaction newTx) {
		transactionStack.push(newTx); 
	}
	
	/**
	 * 
	 * Pops the tx at the top of the tx stack.
	 * Used by the test suite to simulate 
	 * multi-transactional access to the transactional cache.
	 *
	 */
	Object popCurrentTransaction() {
		return transactionStack.pop(); 
	}
}
