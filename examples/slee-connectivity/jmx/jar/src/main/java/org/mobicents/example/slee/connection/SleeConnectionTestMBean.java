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
	public void fireEventWithRA(String messagePassed);
	/**
	 * Fires event by means of custom API.
	 * @param messagePassed
	 */
	public void fireEventWithRemoteSleeService(String messagePassed);
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
	public void fireEventWithRA_JTA(String messagePassed) throws SystemException, NotSupportedException, IllegalStateException,
			RollbackException, SecurityException, HeuristicMixedException, HeuristicRollbackException;
	/**
	 * Fires event by means of custom API. Event firing action is embeded in JTA transaction.
	 * @param messagePassed
	 * @throws SystemException
	 * @throws NotSupportedException
	 * @throws IllegalStateException
	 * @throws RollbackException
	 * @throws SecurityException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 */
	public void fireEventWithRemoteSleeService_JTA(String messagePassed) throws SystemException, NotSupportedException,
			IllegalStateException, RollbackException, SecurityException, HeuristicMixedException, HeuristicRollbackException;
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
	public void fireEventWithRA_JTA_BeforeCompletion(final String messagePassed) throws SystemException, NotSupportedException,
			IllegalStateException, RollbackException, SecurityException, HeuristicMixedException, HeuristicRollbackException;
	/**
	 * Fires event by means of custom API. Event is fired before transaction complets.
	 * @param messagePassed
	 * @throws SystemException
	 * @throws NotSupportedException
	 * @throws IllegalStateException
	 * @throws RollbackException
	 * @throws SecurityException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 */
	public void fireEventWithRemoteSleeService_JTA_BeforeCompletion(final String messagePassed) throws SystemException,
			NotSupportedException, IllegalStateException, RollbackException, SecurityException, HeuristicMixedException,
			HeuristicRollbackException;
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
	public void fireEventWithRA_JTA_AfterCompletion(final String messagePassed) throws SystemException, NotSupportedException,
			IllegalStateException, RollbackException, SecurityException, HeuristicMixedException, HeuristicRollbackException;
	/**
	 * Fires event by means of custom API. Event is fired after transaction complets.
	 * @param messagePassed
	 * @throws SystemException
	 * @throws NotSupportedException
	 * @throws IllegalStateException
	 * @throws RollbackException
	 * @throws SecurityException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 */
	public void fireEventWithRemoteSleeService_JTA_AfterCompletion(final String messagePassed) throws SystemException,
			NotSupportedException, IllegalStateException, RollbackException, SecurityException, HeuristicMixedException,
			HeuristicRollbackException;

	/**
	 * @return the bindAddress
	 */
	public String getBindAddress();

	/**
	 * @param bindAddress
	 *            the bindAddress to set
	 */
	public void setBindAddress(String bindAddress);

	/**
	 * @return the jnpPort
	 */
	public int getJnpPort();

	/**
	 * @param jnpPort
	 *            the jnpPort to set
	 */
	public void setJnpPort(int jnpPort);

	public void setTransactionManager(TransactionManager jta);

	public TransactionManager getTransactionManager();
}
