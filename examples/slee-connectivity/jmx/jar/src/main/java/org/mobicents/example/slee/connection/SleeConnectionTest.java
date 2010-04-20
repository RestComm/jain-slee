package org.mobicents.example.slee.connection;

import java.util.Properties;

import javax.naming.Context;
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
import org.mobicents.slee.connector.server.RemoteSleeService;
import org.mobicents.slee.service.events.CustomEvent;

public class SleeConnectionTest implements SleeConnectionTestMBean {

	private static final Logger logger = Logger.getLogger(SleeConnectionTest.class);

	private final static String eventName = "org.mobicents.slee.service.connectivity.Event_1";
	private final static String eventVendor = "org.mobicents";
	private final static String eventVersion = "1.0";

	private String bindAddress = "127.0.0.1";
	private int jnpPort = 1099;
	private TransactionManager jta;

	public void start() {

	}

	public void fireEventWithRA(String messagePassed) {

		// RA is deployed localy, providing interface to some system! it is
		// bound to private JVM space.
		logger.info("Attempting call to RA in LOCAL JVM name space[" + bindAddress + ":" + jnpPort + "].");
		logger.warn("This operation is expected to fail if called to remote host!");

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

	public void fireEventWithRemoteSleeService(String messagePassed) {
		// this is not standard way, RA is proper, but its still available to
		// access this!
		logger.info("Attempting call to JVM name space[" + bindAddress + ":" + jnpPort + "].");
		logger.warn("This operation is expected not to fail if called to proper host!");

		try {
			Properties props = new Properties();
			props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
			props.put(Context.PROVIDER_URL, "jnp://" + bindAddress + ":" + jnpPort);
			props.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
			props.put("jnp.disableDiscovery", "true");

			InitialContext ic = new InitialContext(props);

			RemoteSleeService remoteSleeService = (RemoteSleeService) ic.lookup("/SleeService");

			ExternalActivityHandle handle = remoteSleeService.createActivityHandle();
			EventTypeID requestType = remoteSleeService.getEventTypeID(eventName, eventVendor, eventVersion);

			CustomEvent customEvent = new CustomEvent();
			customEvent.setMessage(messagePassed);
			logger.info("The event type is: " + requestType);
			remoteSleeService.fireEvent(customEvent, requestType, handle, null);

		} catch (Exception e) {
			logger.error("Exception caught in event fire method!", e);

		}
	}

	public void fireEventWithRA_JTA(final String messagePassed) throws SystemException, NotSupportedException, IllegalStateException,
			RollbackException, SecurityException, HeuristicMixedException, HeuristicRollbackException {
		// hack to show what is not allowed
		logger.info("About to fire event, event firing code embeded in JTA transaction context!");
		
		boolean started = false;
		if (this.jta.getTransaction() == null) {
			this.jta.begin();
			started = true;
		}

		fireEventWithRA(messagePassed);

		if (started) {
			this.jta.commit();
		}
	}

	public void fireEventWithRemoteSleeService_JTA(final String messagePassed) throws SystemException, NotSupportedException,
			IllegalStateException, RollbackException, SecurityException, HeuristicMixedException, HeuristicRollbackException {
		boolean started = false;
		if (this.jta.getTransaction() == null) {
			this.jta.begin();
			started = true;
		}
		logger.info("About to fire event, event firing code embeded in JTA transaction context!");
		fireEventWithRemoteSleeService(messagePassed);

		if (started) {
			this.jta.commit();
		}
	}

	public void fireEventWithRA_JTA_BeforeCompletion(final String messagePassed) throws SystemException, NotSupportedException,
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

				fireEventWithRA(messagePassed);
			}

			public void afterCompletion(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		if (started) {
			this.jta.commit();
		}
	}

	public void fireEventWithRemoteSleeService_JTA_BeforeCompletion(final String messagePassed) throws SystemException,
			NotSupportedException, IllegalStateException, RollbackException, SecurityException, HeuristicMixedException,
			HeuristicRollbackException {
		boolean started = false;
		if (this.jta.getTransaction() == null) {
			this.jta.begin();
			started = true;
		}
		logger.info("Abou to fire event, event firing code embeded in JTA transaction context!");
		logger.warn("Event will be fired after TX commits, but before it completes!");
		this.jta.getTransaction().registerSynchronization(new Synchronization() {

			public void beforeCompletion() {

				fireEventWithRemoteSleeService(messagePassed);
			}

			public void afterCompletion(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		if (started) {
			this.jta.commit();
		}
	}

	public void fireEventWithRA_JTA_AfterCompletion(final String messagePassed) throws SystemException, NotSupportedException,
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
				fireEventWithRA(messagePassed);

			}
		});
		if (started) {
			this.jta.commit();
		}
	}

	public void fireEventWithRemoteSleeService_JTA_AfterCompletion(final String messagePassed) throws SystemException,
			NotSupportedException, IllegalStateException, RollbackException, SecurityException, HeuristicMixedException,
			HeuristicRollbackException {
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
				fireEventWithRemoteSleeService(messagePassed);

			}
		});
		if (started) {
			this.jta.commit();
		}
	}

	/**
	 * @return the bindAddress
	 */
	public String getBindAddress() {
		return bindAddress;
	}

	/**
	 * @param bindAddress
	 *            the bindAddress to set
	 */
	public void setBindAddress(String bindAddress) {
		this.bindAddress = bindAddress;
	}

	/**
	 * @return the jnpPort
	 */
	public int getJnpPort() {
		return jnpPort;
	}

	/**
	 * @param jnpPort
	 *            the jnpPort to set
	 */
	public void setJnpPort(int jnpPort) {
		this.jnpPort = jnpPort;
	}

	public void setTransactionManager(TransactionManager jta) {
		this.jta = jta;
	}

	public TransactionManager getTransactionManager() {
		return this.jta;
	}

}
