package org.mobicents.example.slee.connection;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

public interface SleeConnectionTestMBean {

	// methods to fire

	/**
	 * Fires event using Java RA contract.
	 */
	public void fireEvent(String messagePassed);

	/**
	 * Fires event embeded in regular JTA transaction. Event is fired by means of RA contract.
	 * @param messagePassed
	 * @throws SystemException
	 * @throws NotSupportedException
	 * @throws IllegalStateException
	 * @throws RollbackException
	 * @throws SecurityException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 */
	public void fireEventWithJTA(String messagePassed) throws SystemException, NotSupportedException, IllegalStateException,
			RollbackException, SecurityException, HeuristicMixedException, HeuristicRollbackException;

	/**
	 * Fires event embeded in regular JTA transaction. Event is fired by means of RA contract.
	 * Event is fired before tx complets.
	 * @param messagePassed
	 * @throws SystemException
	 * @throws NotSupportedException
	 * @throws IllegalStateException
	 * @throws RollbackException
	 * @throws SecurityException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 */
	public void fireEventWithJTABeforeCompletion(final String messagePassed) throws SystemException, NotSupportedException,
			IllegalStateException, RollbackException, SecurityException, HeuristicMixedException, HeuristicRollbackException;

	/**
	 * Fires event embeded in regular JTA transaction. Event is fired by means of RA contract.
	 * Event is fired after TX complets.
	 * @param messagePassed
	 * @throws SystemException
	 * @throws NotSupportedException
	 * @throws IllegalStateException
	 * @throws RollbackException
	 * @throws SecurityException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 */
	public void fireEventWithJTAAfterCompletion(final String messagePassed) throws SystemException, NotSupportedException,
			IllegalStateException, RollbackException, SecurityException, HeuristicMixedException, HeuristicRollbackException;



	public void setTransactionManager(TransactionManager jta);

	public TransactionManager getTransactionManager();
}
