package org.mobicents.example.slee.connection;

import javax.naming.InitialContext;
import javax.slee.EventTypeID;
import javax.slee.connection.ExternalActivityHandle;
import javax.slee.connection.SleeConnection;
import javax.slee.connection.SleeConnectionFactory;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.apache.log4j.Logger;
import org.mobicents.slee.service.events.CustomEvent;

public class SleeConnectionTest implements SleeConnectionTestMBean {

	private static final Logger logger = Logger.getLogger(SleeConnectionTest.class);

	private final static String eventName = "org.mobicents.slee.service.connectivity.Event_1";
	private final static String eventVendor = "org.mobicents";
	private final static String eventVersion = "1.0";

	private TransactionManager jta;

	public void start() {

	}

	public void fireEvent(String messagePassed) {

		// depending on deployment it does following:
		// 1. lookup RA and make RMI calls through it
		// 2. lookup local Bean, which makes direct calls to container!
		logger.info("Attempting call to SleeConnectionFactory.");
		try {

			// this is called for local container, that is - RA is deployed
			// localy, and it accessess
			// remote host with SLEE, no props required here in CTX
			// initialization
			InitialContext ic = new InitialContext();
			// this is call to local JNDI space, private, it cant be accessed
			// from other JVM
			SleeConnectionFactory factory = (SleeConnectionFactory) ic.lookup("java:/MobicentsConnectionFactory");

			SleeConnection conn1 = null;
			conn1 = factory.getConnection();

			ExternalActivityHandle handle = conn1.createActivityHandle();

			EventTypeID requestType = conn1.getEventTypeID(eventName, eventVendor, eventVersion);
			CustomEvent customEvent = new CustomEvent();
			customEvent.setMessage(messagePassed);
			logger.info("The event type is: " + requestType);

			conn1.fireEvent(customEvent, requestType, handle, null);

			conn1.close();

		} catch (Exception e) {
			logger.error("Exception caught in event fire method!", e);

		}

	}

	

	public void fireEventWithJTA(final String messagePassed) throws SystemException, NotSupportedException, IllegalStateException,
			RollbackException, SecurityException, HeuristicMixedException, HeuristicRollbackException {
		// hack to show what is not allowed
		logger.info("About to fire event, event firing code embeded in JTA transaction context!");
		
		boolean started = false;
		if (this.jta.getTransaction() == null) {
			this.jta.begin();
			started = true;
		}

		fireEvent(messagePassed);

		if (started) {
			this.jta.commit();
		}
	}



	public void fireEventWithJTABeforeCompletion(final String messagePassed) throws SystemException, NotSupportedException,
			IllegalStateException, RollbackException, SecurityException, HeuristicMixedException, HeuristicRollbackException {
		// hack to show what is not allowed
		boolean started = false;
		if (this.jta.getTransaction() == null) {
			this.jta.begin();
			started = true;
		}
		logger.info("Abou to fire event, event firing code embeded in JTA transaction context!");
		logger.warn("Event will be fired after TX commits, but before it completes!");
		this.jta.getTransaction().registerSynchronization(new Synchronization() {

			public void beforeCompletion() {

				fireEvent(messagePassed);
			}

			public void afterCompletion(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		if (started) {
			this.jta.commit();
		}
	}


	public void fireEventWithJTAAfterCompletion(final String messagePassed) throws SystemException, NotSupportedException,
			IllegalStateException, RollbackException, SecurityException, HeuristicMixedException, HeuristicRollbackException {
		// hack to show what is not allowed
		boolean started = false;
		if (this.jta.getTransaction() == null) {
			this.jta.begin();
			started = true;
		}
		logger.info("Abou to fire event, event firing code embeded in JTA transaction context!");
		logger.warn("Event will be fired after TX commits and completes!");
		this.jta.getTransaction().registerSynchronization(new Synchronization() {

			public void beforeCompletion() {

				
			}

			public void afterCompletion(int arg0) {
				fireEvent(messagePassed);

			}
		});
		if (started) {
			this.jta.commit();
		}
	}

	



	public void setTransactionManager(TransactionManager jta) {
		this.jta = jta;
	}

	public TransactionManager getTransactionManager() {
		return this.jta;
	}

}
