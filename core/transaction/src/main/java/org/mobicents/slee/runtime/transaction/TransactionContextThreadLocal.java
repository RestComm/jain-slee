package org.mobicents.slee.runtime.transaction;

/**
 * 
 * 
 * @author martins
 *
 */
public class TransactionContextThreadLocal {

	/**
	 * 
	 */
	private static final ThreadLocal<TransactionContext> transactionContext = new ThreadLocal<TransactionContext>();
	
	/**
	 * 
	 * @param serviceID
	 */
	public static void setTransactionContext(TransactionContext value) {
		transactionContext.set(value);
	}

	/**
	 * Retrieves the id of the service, being invoked in the current thread.
	 * @return null if the thread is not invoking any server
	 */
	public static TransactionContext getTransactionContext() {
		return transactionContext.get();
	}
	
}
