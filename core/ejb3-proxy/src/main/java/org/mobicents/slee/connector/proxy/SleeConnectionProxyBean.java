package org.mobicents.slee.connector.proxy;

import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJBException;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.resource.ResourceException;
import javax.slee.connection.SleeConnection;
import javax.slee.connection.SleeConnectionFactory;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.Depends;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.ejb.Service;

/**
 * This acts as EJB3 Bean which can be used to fire events from other EJB's
 * @author amit.bhayani 
 */

@Stateless
@RemoteBinding(jndiBinding="SleeConnectionProxyBean/remote")
@LocalBinding(jndiBinding="SleeConnectionProxyBean/local")
@Service
@Depends ("jboss.ejb:service=EJBTimerService,persistencePolicy=database")
public class SleeConnectionProxyBean implements java.io.Serializable,
		SleeConnectionProxyLocal, SleeConnectionProxyRemote {

	private String jndiSleeConnectionFactoryPath = "java:MobicentsConnectionFactory";

	private String propsFileName = "ejbconnection-proxy.properties";

	private transient SleeConnection connection = null;

	private transient SleeConnectionFactory connectionFactory = null;

	private String componentID = "";

	@SuppressWarnings("unused")
	private SessionContext scontext = null;

	private static transient Logger logger = Logger
			.getLogger(SleeConnectionProxyBean.class);

	public SleeConnectionProxyBean() {
		java.rmi.server.UID uid = new java.rmi.server.UID();
		componentID = "SleeConnectionProxyEJB:[ " + uid + "]";
		logger.debug("COMPONENT:[ " + componentID + " ] Created");
	}

	@PostConstruct
	public void fetchResources() {
		try {
			logger.debug("COMPONENT:[ " + componentID + " ] ACTIVATING");
			connectionFactory = lookupFactory();
			connection = connectionFactory.getConnection();
		} catch (ResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@PreDestroy
	public void closeResources() {
		try {
			logger.debug("COMPONENT:[ " + componentID + " ] PASIVATING");

			connection.close();

		} catch (ResourceException RE) {
			// RE.printStackTrace();
		}
		connection = null;
		connectionFactory = null;

	}

	public void setSessionContext(SessionContext ctx) throws EJBException,
			RemoteException {
		this.scontext = ctx;

	}

	/**
	 * Reads properites file in order to create Properties for initial context
	 */
	private Properties readProps() {

		Properties returnprops = new Properties();
		Properties fileProps = new Properties();
		boolean exceptionCaught = false;
		URL url = getClass().getResource(propsFileName);
		try {

			Class thisCLS = getClass();
			InputStream IS = getClass().getResourceAsStream(propsFileName);

			fileProps.load(IS);
			returnprops.put(InitialContext.INITIAL_CONTEXT_FACTORY, fileProps
					.getProperty("InitialContext.INITIAL_CONTEXT_FACTORY",
							"org.jnp.interfaces.NamingContextFactory"));
			returnprops.put(InitialContext.PROVIDER_URL, fileProps.getProperty(
					"InitialContext.PROVIDER_URL", "jnp://127.0.0.1:1099"));

		} catch (Exception e) {

			// exceptionCaught=true;
			e.printStackTrace();
		}
		//
		// if(exceptionCaught)
		// {
		// WE HAVE TO CHECH IF THOSE HAVE BEEN INTRIDUCED WITH -D ??
		// }

		// return fileProps;
		return returnprops;
	}

	/**
	 * Lookups SleeConnectionFactory in tarteg jvm/jboss
	 * 
	 * @return SleeConnectionFactoryImpl
	 */
	private SleeConnectionFactory lookupFactory() {
		SleeConnectionFactory factory = null;

		Properties props = readProps();

		try {
			InitialContext ctx = null;
			if (props == null)
				ctx = new InitialContext();
			else
				ctx = new InitialContext(props);

			factory = (SleeConnectionFactory) ctx
					.lookup(jndiSleeConnectionFactoryPath);
			ctx.close();

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return factory;
	}

	/**
	 * 
	 * Creates external activity handle.
	 * 
	 * @return Activity handle which can be used to reference activity to which
	 *         event can be fired.
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public javax.slee.connection.ExternalActivityHandle createActivityHandle()
			throws ResourceException {
		logger
				.debug("COMPONENT:[ "
						+ componentID
						+ " ] METHOD: [ public javax.slee.connection.ExternalActivityHandle createActivityHandle() ]");
		return connection.createActivityHandle();
	}

	/**
	 * 
	 * Lookups eventTypeID.
	 * 
	 * @return EventTypeID of partcilar event.
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public javax.slee.EventTypeID getEventTypeID(String eventName,
			String eventVendor, String eventVersion)
			throws javax.slee.UnrecognizedEventException,
			javax.resource.ResourceException {
		logger.debug("COMPONENT:[ " + componentID
				+ " ] METHOD: [ getEventTypeID(...) ]");
		return connection.getEventTypeID(eventName, eventVendor, eventVersion);
	}

	/**
	 * 
	 * Fires event to slee.
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void fireEvent(
			Object event,
			javax.slee.EventTypeID eventTypeID,
			javax.slee.connection.ExternalActivityHandle externalActivityHandle,
			javax.slee.Address address) throws java.lang.NullPointerException,
			javax.slee.UnrecognizedActivityException,
			javax.slee.UnrecognizedEventException,
			javax.resource.ResourceException {
		logger.debug("COMPONENT:[ " + componentID + " ] METHOD: [ fireEvent ]");
		/*
		 * try { ctx.getUserTransaction().setRollbackOnly(); } catch
		 * (IllegalStateException e) { // TODO Auto-generated catch block
		 * logger.info("METHOD: [ fireEvent ]"); e.printStackTrace(); } catch
		 * (SystemException e) { // TODO Auto-generated catch block
		 * logger.info("METHOD: [ fireEvent ]"); e.printStackTrace(); }
		 */
		connection.fireEvent(event, eventTypeID, externalActivityHandle,
				address);
	}

	/**
	 * 
	 * @throws ResourceException
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	@Remove
	public void close()  {
		logger.debug("COMPONENT:[ " + componentID + " ] METHOD: [ close() ]");
		//This is done in PreDestroy
		//connection.close();

	}
}
