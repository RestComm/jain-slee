/*
 * Mobicents: The Open Source SLEE Platform      
 *
 * Copyright 2003-2005, CocoonHive, LLC., 
 * and individual contributors as indicated
 * by the @authors tag. See the copyright.txt 
 * in the distribution for a full listing of   
 * individual contributors.
 *
 * This is free software; you can redistribute it
 * and/or modify it under the terms of the 
 * GNU Lesser General Public License as
 * published by the Free Software Foundation; 
 * either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that 
 * it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the 
 * GNU Lesser General Public
 * License along with this software; 
 * if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, 
 * Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org.
 */

package org.mobicents.slee.resource.sip;

import gov.nist.javax.sip.header.CSeq;
import gov.nist.javax.sip.header.CallID;
import gov.nist.javax.sip.header.Via;
import gov.nist.javax.sip.message.SIPRequest;

import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.NamingException;
import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogState;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.Transaction;
import javax.sip.TransactionAlreadyExistsException;
import javax.sip.TransactionState;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransactionUnavailableException;
import javax.sip.address.AddressFactory;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.EventTypeID;
import javax.slee.InvalidStateException;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.FacilityException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.FailureReason;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.resource.ResourceException;
import javax.slee.resource.SleeEndpoint;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.resource.ResourceAdaptorState;
import org.mobicents.slee.resource.sip.mbean.SipRaConfiguration;
import org.mobicents.slee.resource.sip.wrappers.ClientTransactionWrapper;
import org.mobicents.slee.resource.sip.wrappers.DialogTerminatedEventWrapper;
import org.mobicents.slee.resource.sip.wrappers.DialogWrapper;
import org.mobicents.slee.resource.sip.wrappers.RequestEventWrapper;
import org.mobicents.slee.resource.sip.wrappers.ResponseEventWrapper;
import org.mobicents.slee.resource.sip.wrappers.SecretWrapperInterface;
import org.mobicents.slee.resource.sip.wrappers.ServerTransactionWrapper;
import org.mobicents.slee.resource.sip.wrappers.TimeoutEventWrapper;
import org.mobicents.slee.resource.sip.wrappers.TransactionTerminatedEventWrapper;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
/**
 * 
 * Creates and Wraps the Sip Stack.
 * 
 * @author F.Moggia
 * @author Ivelin Ivanov
 * @author M. Ranganathan
 * @author B. Baranowski
 */
@SuppressWarnings("all")
public class SipResourceAdaptor implements SipListener, ResourceAdaptor,
		Serializable {


	// The IP address for this resource adaptor.

	static private transient Logger log;

	static {
		log = Logger.getLogger(SipResourceAdaptor.class);
	}

	// SIP Props -------------------------------------------

	private static final String SIP_BIND_ADDRESS = "javax.sip.IP_ADDRESS";

	private static final String SIP_PORT_BIND = "javax.sip.PORT";

	private static final String CANCEL_WAIT_BIND = "net.java.CANCEL_WAIT";

	private static final String DIALOG_TIMEOUT_BIND = "net.java.DIALOG_TIMEOUT";

	private static final String DIALOG_AUTOMATIC_CREATION_BIND="net.java.AUTOMATIC_DIALOG_SUPPORT";
	
	private static final String TRANSPORTS_BIND = "javax.sip.TRANSPORT";

	private static final String STACK_NAME_BIND = "javax.sip.STACK_NAME";

	private int port = 5060;

	// private String transport = "udp";
	private Set transports = new HashSet();

	private Set allowedTransports = new HashSet();

	private String stackAddress = "0.0.0.0";

	private String stackPrefix = "gov.nist";

	private transient SipProvider provider;

	// private SipProvider tcpProvider;

	private transient SipStack sipStack = null;

	private transient SipFactory sipFactory = null;

	private transient SipFactoryProvider sipFactoryProvider;

	private boolean automaticDialogSupport;

	private boolean createDialogAutomaticly;
	
	/**
	 * Timeout value for dialog in seconds INDICATES HOW LONG DIALOG CAN BE IN
	 * IDLE STATE (no transmisions) AFTER THAT TIME IT IS CONSIDERED TO BE DEAD -
	 * TimedOut
	 */
	private long dialogTimeout = 30000;

	/**
	 * Time Betwwen receiving cancel and firing it into slee this assures that
	 * sbb can have atleast chance of procesing INVITE
	 * 
	 */
	private long cancelWait = 1000;

	// SIP Methods related -------------------------------------

	// METHODS - here we store methods which are rfc 3261 compilant
	private static Set rfc3261Methods = new HashSet();
	// SOME INITIALIZATION
	static {
		String[] tmp = { Request.ACK, Request.BYE, Request.CANCEL,
				Request.INFO, Request.INVITE, Request.MESSAGE, Request.NOTIFY,
				Request.OPTIONS, Request.PRACK, Request.PUBLISH, Request.REFER,
				Request.REGISTER, Request.SUBSCRIBE, Request.UPDATE };
		for (int i = 0; i < tmp.length; i++)
			rfc3261Methods.add(tmp[i]);

		log.info("\n================SIP METHODS====================\n"
				+ rfc3261Methods
				+ "\n===============================================");

	}

	// SLEE Related Props --------------------------------------

	// Activity related ====================
	//private transient Map activities = new ConcurrentHashMap();
	private transient Map activities = null;
	private transient SipActivityContextInterfaceFactory acif;

	private String entityName = "SipRA";

	private ResourceAdaptorState state;

	private String configurationMBeanName = "SipRA_1_2_Configuration";

	// Pure SLEE and not qualified ==============

	private Properties properties;

	private Properties provisionedProperties = new Properties();

	private transient Address address;

	private transient SleeEndpoint sleeEndpoint;

	private transient EventLookupFacility eventLookup;

	private transient BootstrapContext bootstrapContext;

	private transient SleeContainer serviceContainer;

	private transient SleeTransactionManager tm = null;

	// WE NEED TO KNOW IF AC EXISTS FOR SOME ActivityObjects
	private ActivityContextFactory activityContextFactory;

	private transient Map sipToSleeEvent = new ConcurrentHashMap();

	// SLEE<-> SIP Constants

	public static final String EVENT_PREFIX_1_2 = "javax.sip.dialog.";

	public static final String EVENT_PREFIX_1_1 = "javax.sip.message.";

	public static final String VENDOR_1_2 = "net.java";

	public static final String VERSION_1_2 = "1.2";

	public static final String VENDOR_1_1 = "javax.sip";

	public static final String VERSION_1_1 = "1.1";

	// CONSTANTS FOR CERTAIN EVENTS 1.2:
	// REQUESTS:
	public static final String EVENT_REQUEST_DIALOG_TERMINATION_EVENT_NAME_1_2 = EVENT_PREFIX_1_2
			+ "TerminationRequest";

	// RESPONSES

	// CONSTANTS FOR CERTAIN EVENTS 1.1:
	// REQUESTS:
	public static final String EVENT_REQUEST_CANCEL_1_1 = EVENT_PREFIX_1_1
			+ "Request.CANCEL";

	// RESPONSES
	public static final String EVENT_RESPONSE_TRYING_1_1 = EVENT_PREFIX_1_1
			+ "Response.TRYING";

	public static final String EVENT_RESPONSE_INFORMATIONAL_1_1 = EVENT_PREFIX_1_1
			+ "Response.INFORMATIONAL";

	public static final String EVENT_RESPONSE_SUCCESS_1_1 = EVENT_PREFIX_1_1
			+ "Response.SUCCESS";

	public static final String EVENT_RESPONSE_REDIRECTION_1_1 = EVENT_PREFIX_1_1
			+ "Response.REDIRECTION";

	public static final String EVENT_RESPONSE_CLIENT_ERROR_1_1 = EVENT_PREFIX_1_1
			+ "Response.CLIENT_ERROR";

	public static final String EVENT_RESPONSE_SERVER_ERROR_1_1 = EVENT_PREFIX_1_1
			+ "Response.SERVER_ERROR";

	public static final String EVENT_RESPONSE_GLOBAL_FAILURE_1_1 = EVENT_PREFIX_1_1
			+ "Response.GLOBAL_FAILURE";

	// OTHER
	public static final String EVENT_TIMEOUT_TRANSACTION = "javax.sip.timeout.TRANSACTION";

	public static final String EVENT_TIMEOUT_DIALOG = "javax.sip.timeout.DIALOG";

	public static final String EVENT_DIALOG_STATE_SetupEarly = EVENT_PREFIX_1_2
			+ "SetupEarly";

	public static final String EVENT_DIALOG_STATE_SetupConfirmed = EVENT_PREFIX_1_2
			+ "SetupConfirmed";

	public static final String EVENT_DIALOG_STATE_SetupFailed = EVENT_PREFIX_1_2
			+ "SetupFailed";

	public static final String EVENT_DIALOG_STATE_SetupTimedOut = EVENT_PREFIX_1_2
			+ "SetupTimedOut";

	/* EVENT FILTERING -------------------------------------------------- */
	// Holds mappings String..ServiceID --> int[]..EventIDs
	private transient Map eventIDsOfServicesInstalled = new ConcurrentHashMap(
			31);

	// Holds mappings String..ServiceID --> String[]..eventOptions
	// eventOptions[i] are options for eventIDs[i] --> see
	// SleeContainer.installService ?
	private transient Map eventResourceOptionsOfServicesInstalled = new ConcurrentHashMap(
			31);

	// Holds mappings event ComponentKey --> Set(ServiceID) which are interested
	// in receiving event
	private transient Map myComponentKeys = new ConcurrentHashMap(31);
	
	private Timer timer=new Timer();
	
	public SipResourceAdaptor() {
		// Those values are defualt
		this.port = 5060;
		// this.transport = "udp";
		allowedTransports.add("udp");
		allowedTransports.add("tcp");
		transports.add("udp");
		// this.stackAddress = "127.0.0.1";

		// this.stackPrefix = "gov.nist";
	}

	// **************** PROVISIONING

	public String getIp() {
	  return this.stackAddress; 
	}
	
	public void setIp(String ip) {

		if (ip.equals("null"))
			this.stackAddress = System.getProperty("jboss.bind.address");
		else
			stackAddress = ip;// TODO regex cehck ?

		provisionedProperties.put(this.SIP_BIND_ADDRESS, stackAddress);

	}

	public Integer getPort() {
	  return Integer.valueOf(this.port);
	}
	
	public void setPort(Integer port) {
		if (port.intValue() < 1024)
			this.port = 5060;
		else
			this.port = port.intValue();
		provisionedProperties.put(this.SIP_PORT_BIND, "" + this.port);
	}

	public Long getCancelWait() {
	  return Long.valueOf(this.cancelWait);
	}
	
	public void setCancelWait(Long cancelWait) {
		if (cancelWait.longValue() > 0)
			this.cancelWait = 1000;
		else
			this.cancelWait = cancelWait.longValue();
		provisionedProperties.put(this.CANCEL_WAIT_BIND, "" + this.cancelWait);
	}

	public Long getDialogIdleTimeTimeout() {
	  return Long.valueOf(this.dialogTimeout);
	}
	
	public void setDialogIdleTimeTimeout(Long idleTimeOut) {
		
		
		
		if (idleTimeOut.longValue() < 0)
			this.dialogTimeout = 30000;
		else
			this.dialogTimeout = idleTimeOut.longValue();
		provisionedProperties.put(this.DIALOG_TIMEOUT_BIND, ""
				+ this.dialogTimeout);
	}

	public String getTransports() {
	  return Arrays.toString(transports.toArray()).
	    replaceFirst( "\\[", "" ).replaceFirst( "\\]", "" );
	}
	
	public void setTransports(String transportsToSet) {
		String[] tmp = transportsToSet.split(",");
		if (tmp.length > 0) {
			boolean valid = false;
			for (int i = 0; i < tmp.length; i++) {
				if (allowedTransports.contains(tmp[i].toLowerCase()))
					valid = true;
				break;
			}

			if (valid) {
				transports.clear();
				for (int i = 0; i < tmp.length; i++) {
					if (allowedTransports.contains(tmp[i].toLowerCase())) {
						transports.add(tmp[i].toLowerCase());
					} else {
						log.error(" TRANSPORT[" + tmp[i]
								+ "] IS NOT A VALID TRANSPORT!!!");
					}
				}
			}
		}
		provisionedProperties.put(this.TRANSPORTS_BIND, transportsToSet);
	}

	// ------- END OF PROVISIONING
	// ******* CONFIGURE

	private void init(BootstrapContext bootstrapContext) {

		if (log.isDebugEnabled()) {
			log.debug("SipResourceAdaptor: init()");
		}
		this.bootstrapContext = bootstrapContext;
		this.sleeEndpoint = bootstrapContext.getSleeEndpoint();
		this.eventLookup = bootstrapContext.getEventLookupFacility();

		// properties = new Properties();
		this.state = ResourceAdaptorState.UNCONFIGURED;


	}

	private Properties loadProperties(BootstrapContext bootstrapContext) {
		Properties props = new Properties();

		// Load default values
		try {
			props.load(getClass().getResourceAsStream("sipra.properties"));
			if (log.isDebugEnabled()) {
				log.debug("Loading default SIP RA properties: " + props);
			}

			String bindAddress = props.getProperty(SIP_BIND_ADDRESS);
			if (bindAddress == null) {
				bindAddress = System.getProperty("jboss.bind.address");
				if (bindAddress != null)
					props.setProperty(SIP_BIND_ADDRESS, bindAddress);
			}
			;

		} catch (java.io.IOException ex) {
			// Silently set default values
			props.setProperty("javax.sip.RETRANSMISSION_FILTER", "on");
		}

		return props;
	}

	public void configure(Properties properties) throws InvalidStateException {
		if (this.state != ResourceAdaptorState.UNCONFIGURED) {
			throw new InvalidStateException("Cannot configure RA wrong state: "
					+ this.state);
		}
	
		if (log.isDebugEnabled()) {
			log.debug("Configuring RA" + properties);
		}
		/*
		 * TODO: donot use JMX interface because it is very difficult to enter
		 * properties this.properties = properties;
		 * this.setPort(Integer.parseInt(properties.getProperty("gov.nist.slee.resource.sip.PORT")));
		 * this.setTransport(properties.getProperty("gov.nist.slee.resource.sip.TRANSPORT"));
		 */
		this.properties = loadProperties(bootstrapContext);
		
		
		// lets merge with props passed
		if (properties != null) {
			if (log.isDebugEnabled()) {
				Iterator it = properties.entrySet().iterator();
				while (it.hasNext()) {
					Entry en = (Entry) it.next();
					if (log.isDebugEnabled()) {
						log.debug("---[SetHotProps][" + this.entityName + "] "
								+ en.getKey() + " [*] " + en.getValue());
					}
				}
			}
			this.properties.putAll(properties);
		}

		String confValue = this.properties.getProperty(SIP_PORT_BIND);

		// If present lets use it, if not lets use default 5060
		if (confValue != null) {

			this.port = Integer.parseInt(confValue);
			confValue = null;
		}

		// Again properties file should contain this, this should be only passed
		// through configure(properties)
		confValue = this.properties.getProperty("javax.sip.TRANSPORT", "udp");

		if (confValue != null) {
			String[] tmp = confValue.split(",");
			if (tmp.length > 0) {
				boolean valid = false;
				for (int i = 0; i < tmp.length; i++) {
					if (allowedTransports.contains(tmp[i]))
						valid = true;
					break;
				}

				if (valid) {
					transports.clear();
					for (int i = 0; i < tmp.length; i++) {
						if (allowedTransports.contains(tmp[i])) {
							transports.add(tmp[i]);
						} else {
							log.error(" TRANSPORT[" + tmp[i]
									+ "] IS NOT A VALID TRANSPORT!!!");
						}
					}
				}
			}

			confValue = null;
		}

		// If present lets use it, otherwise lets use default 127.0.0.1 - this
		// can be overriden by conainer address
		confValue = this.properties.getProperty(SIP_BIND_ADDRESS);
		if (confValue != null) {
			this.stackAddress = confValue;
			this.properties.remove(SIP_BIND_ADDRESS);// so we can have
			// multiple stacks on
			// one IP.
			confValue = null;
		} else {
			this.stackAddress = System.getProperty("jboss.bind.address");
		}

		// we need to alter stack name slightly
		confValue = this.properties.getProperty(STACK_NAME_BIND);
		if (confValue == null) {
			confValue = "SipResourceAdaptorStack_" + this.stackAddress + "_"
					+ this.port;
		} else {
			confValue += "_" + this.stackAddress + "_" + this.port;
		}
		this.properties.put(STACK_NAME_BIND, confValue);
		confValue = null;

		this.stackPrefix = this.properties.getProperty(
				"javax.sip.STACK_PREFIX", "gov.nist");

		// Turn off automatic dialog support.
		confValue = this.properties
				.getProperty("javax.sip.AUTOMATIC_DIALOG_SUPPORT");
		if (confValue != null) {
			this.automaticDialogSupport = confValue.equals("off") ? false
					: true;
			confValue = null;
		}

		// Try to bind to the specified port. It could be the case that the
		// forwarder is running on this port.

		int i = 0;
		for (i = 0; i < 10; i++) {

			InetSocketAddress sockAddress = new InetSocketAddress(
					stackAddress, this.port);
			try {
				log.info("Trying to bind to " + sockAddress);
				DatagramSocket socket = new DatagramSocket(sockAddress);

				this.properties.setProperty("javax.sip.PORT", Integer
						.valueOf(this.port).toString());
				socket.close();
				break;
			} catch (Exception ex) {

				this.port += 10;
			}
		}
		if (i == 10)
			throw new RuntimeException(
					"Cannot create SIP Resource adaptor - no port available to bind to ");

		log.info("RA bound to " + this.port);
		
		// Dialog idle timeout - timer after dialog is considered to be invalid
		// when no trafic is generated in it.
		this.dialogTimeout = Long.parseLong(this.properties.getProperty(
				this.DIALOG_TIMEOUT_BIND, dialogTimeout + ""));
		// Time between cancel receive and processing - basicaly time
		// for sbbs to process invite.
		this.cancelWait = Long.parseLong(this.properties.getProperty(
				"net.java.CANCEL_WAIT", this.cancelWait + ""));
		// flag indicating if we want to expose internal state of ra

		this.createDialogAutomaticly=Boolean.valueOf(this.properties.getProperty(
				this.DIALOG_AUTOMATIC_CREATION_BIND, false + ""));
		
		this.configurationMBeanName = this.properties.getProperty(
				"net.java.sipra.configurationBeanName",
				this.configurationMBeanName);
		state = ResourceAdaptorState.CONFIGURED;

	}

	// ------------- END OF CONFIGURE

	// ********** IN SLEE LIFECYCLE
	public void entityCreated(BootstrapContext ctx)
			throws javax.slee.resource.ResourceException {
		this.init(ctx);
		
		
	}

	public void entityActivated() throws javax.slee.resource.ResourceException {
		try {
			try {
				this.configure(this.provisionedProperties);
			} catch (InvalidStateException e1) {

				e1.printStackTrace();
			}
			this.start();
		} catch (ResourceException e) {
			e.printStackTrace();
			throw new javax.slee.resource.ResourceException(
					"Failed to Activate Resource Adaptor!", e);
		}
	}

	public void entityDeactivating() {

		this.stopping();
	}

	public void entityDeactivated() {
		this.stop();
	}

	public void entityRemoved() {
		 //tearDownDebug();
	}

	@SuppressWarnings("unchecked")
	public void start() throws ResourceException {

		try {

			initializeNamingContext();

			// this.mux = this.registerMultiplexer();

			initializeStack();

			activities = new ConcurrentHashMap();
			sipToSleeEvent = new ConcurrentHashMap();
			eventIDsOfServicesInstalled = new ConcurrentHashMap(31);
			eventResourceOptionsOfServicesInstalled = new ConcurrentHashMap(31);
			myComponentKeys = new ConcurrentHashMap(31);

			state = ResourceAdaptorState.ACTIVE;

			boolean created = false;

			for (Iterator it = transports.iterator(); it.hasNext();) {
				String trans = (String) it.next();
				ListeningPoint lp = this.sipStack.createListeningPoint(
						this.stackAddress, this.port, trans);

				if (!created) {
					this.provider = this.sipStack.createSipProvider(lp);
					this.provider
							.setAutomaticDialogSupportEnabled(automaticDialogSupport);
					created = true;
				} else
					this.provider.addListeningPoint(lp);

				try {
					this.provider.addSipListener(this);
				} catch (Exception ex) {
					log.fatal("Unexpected exception ", ex);
					throw new ResourceException(
							"SIP RA failed to register as SipListener");
				}

			}

			// LETS CREATE FP
			// SipFactory sipFactory = SipFactory.getInstance();
			AddressFactory addressFactory = sipFactory.createAddressFactory();
			HeaderFactory headerFactory = sipFactory.createHeaderFactory();
			MessageFactory messageFactory = sipFactory.createMessageFactory();
			this.sipFactoryProvider = new SipFactoryProvider(addressFactory,
					messageFactory, headerFactory, this.provider, this);

			// NOW EVENT FILTERING
			// HERE WE BUILD SOME STRUCTURES WHICH CONTAINS INFO
			// ABOUT RAs EVENTS etc, ITS BASE FOR EVENT FILTERING - RA
			// SHOULD FIRE ONLY EVENTS THAT ARE GOING TO BE RECEIVED
			ResourceAdaptorEntity resourceAdaptorEntity = ((ResourceAdaptorEntity) serviceContainer
					.getResourceAdaptorEnitity(this.bootstrapContext
							.getEntityName()));
			this.entityName = resourceAdaptorEntity.getName();

			// event filtering
			EventTypeID[] eventTypeIDs = resourceAdaptorEntity
					.getInstalledResourceAdaptor().getRaType().getRaTypeDescr()
					.getEventTypes();
			for (int i = 0; i < eventTypeIDs.length; i++) {
				ComponentKey eventKey = serviceContainer
						.getEventKey(eventTypeIDs[i]);
				myComponentKeys.put(eventKey, Collections
						.synchronizedSet(new HashSet(10)));
			}
			if (log.isDebugEnabled()) {
				log.debug("Keys for this RA: "
						+ myComponentKeys.keySet().toString());
			}

			// Now Lets create MBean that shows our state
			String[] tmp = new String[transports.size()];
			tmp = (String[]) transports.toArray(tmp);
			SipRaConfiguration sipc = new SipRaConfiguration(
					this.automaticDialogSupport, this.stackAddress, tmp,
					this.port);

			// LETS INITIALIZE BASE CLASS FOR WRAPPERS
			ServerTransactionWrapper.setCancelWait(cancelWait);
			DialogWrapper.setDialogTimeout(dialogTimeout);

			sipc.startService(this.configurationMBeanName + "_" + entityName);

			 //initDebug();
		} catch (Exception ex) {
			log.error("error in initializing resource adaptor", ex);
			throw new ResourceException(ex.getMessage());
		}
	}

	protected void initializeStack() throws SipException {

		this.sipFactory = SipFactory.getInstance();
		this.sipFactory.setPathName("gov.nist"); // hmmm
		this.properties.remove("javax.sip.PORT");
		this.sipStack = this.sipFactory.createSipStack(this.properties);
		this.sipStack.start();

	}

	public void setProperties(Properties properties) throws ResourceException {
		if (state == ResourceAdaptorState.UNCONFIGURED) {
			this.properties = properties;
		} else {
			throw new ResourceException(
					"Cannot modify configuration properties wrong state: "
							+ state);
		}
	}

	public Properties getProperties(Properties pproperties) {
		pproperties.putAll(this.properties);
		return pproperties;
	}

	/**
	 * Registers this Resoruce adaptor activity context interface factory with
	 * jndi.
	 * 
	 * @throws NamingException
	 */
	private void initializeNamingContext() throws NamingException {
		SleeContainer container = SleeContainer.lookupFromJndi();
		serviceContainer = container;
		activityContextFactory = serviceContainer.getActivityContextFactory();
		tm = serviceContainer.getTransactionManager();
		ResourceAdaptorEntity resourceAdaptorEntity = ((ResourceAdaptorEntity) container
				.getResourceAdaptorEnitity(this.bootstrapContext
						.getEntityName()));
		ResourceAdaptorTypeID raTypeId = resourceAdaptorEntity
				.getInstalledResourceAdaptor().getRaType()
				.getResourceAdaptorTypeID();

		this.acif = new SipActivityContextInterfaceFactoryImpl(
				resourceAdaptorEntity.getServiceContainer(),
				this.bootstrapContext.getEntityName());

		resourceAdaptorEntity.getServiceContainer()
				.getActivityContextInterfaceFactories()
				.put(raTypeId, this.acif);
		entityName = this.bootstrapContext.getEntityName();

		try {
			if (this.acif != null) {
				// parse the string = java:slee/resource/SipRA/sipacif
				String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) this.acif)
						.getJndiName();
				int begind = jndiName.indexOf(':');
				int toind = jndiName.lastIndexOf('/');
				String prefix = jndiName.substring(begind + 1, toind);
				String name = jndiName.substring(toind + 1);
				if (log.isDebugEnabled()) {
					log.debug("jndiName prefix =" + prefix + "; jndiName = "
							+ name);
				}
				SleeContainer.registerWithJndi(prefix, name, this.acif);
			}
		} catch (IndexOutOfBoundsException e) {
			// not register with JNDI
			e.printStackTrace();
		}

	}

	private void cleanNamingContext() throws NamingException {
		try {
			if (this.acif != null) {
				String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) this.acif)
						.getJndiName();
				// remove "java:" prefix
				int begind = jndiName.indexOf(':');
				String javaJNDIName = jndiName.substring(begind + 1);
				SleeContainer.unregisterWithJndi(javaJNDIName);
			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	public void stopping() {
		state = ResourceAdaptorState.STOPPING;
	}

	public void stop() {

		SipRaConfiguration.stopService(this.configurationMBeanName + "_"
				+ entityName);
		this.provider.removeSipListener(this);
		ListeningPoint[] listeningPoints = this.provider.getListeningPoints();
		for (int i = 0; i < listeningPoints.length; i++) {
			ListeningPoint lp = listeningPoints[i];
			for (int k = 0; k < 10; k++) {
				try {
					this.sipStack.deleteListeningPoint(lp);
					this.sipStack.deleteSipProvider(this.provider);
					break;
				} catch (ObjectInUseException ex) {
					log
							.error("Object in use -- retrying to delete listening point");
					try {
						Thread.sleep(100);
					} catch (Exception e) {

					}
				}
			}
		}

		try {
			cleanNamingContext();
		} catch (NamingException e) {
			log.error("Cannot unbind naming context");
		}

		this.state = ResourceAdaptorState.UNCONFIGURED;
		
		if (log.isDebugEnabled()) {
			log.debug("Sip Resource Adaptor stopped.");
		}
	}

	// ---------- END OF INSLEE LIFE CYCLE

	// *** SLEE RELATED METHODS

	public Object getInterface() {

		return this.provider;
	}

	public Object getFactoryInterface() {
		return this.sipFactoryProvider;
	}

	public Object getActivityContextInterfaceFactory() {
		return acif;
	}

	/**
	 * @return Returns the sleeEndpoint.
	 */
	public SleeEndpoint getSleeEndpoint() {
		return sleeEndpoint;
	}

	public Object getSBBResourceAdaptorInterface(String arg0) {

		return this.getFactoryInterface();
	}

	public Marshaler getMarshaler() {

		return null;
	}

	public Map getActivities() {

		return this.activities;
	}

	public BootstrapContext getBootstrapContext() {
		return bootstrapContext;
	}

	public SipFactoryProvider getSipFactoryProvider() {
		return sipFactoryProvider;
	}

	// ********** ACTIVITIES AND PROCESSING RELATED

	/**
	 * Function checks if tx is INVITE - if it is it cancels possible CANCEL
	 * timer ticking in it. If tx isnt terminated it is here(function returns in
	 * hope it will be called again). After that SleeEndpoint.activityEnding(ah)
	 * is called.
	 * 
	 * @param sipT -
	 *            transaction which is going to be discarded as activity
	 */
	public void sendActivityEndEvent(javax.sip.Transaction sipT) {

		SipActivityHandle ah = null;
		// THIS CAN HAPPEN WHEN ACTIVITY ENDS
		// AN IT IS INVITE, WITH CANCEL WAS RECEIVED?
		if ((sipT instanceof ServerTransactionWrapper)
				&& (ah.transactionId.endsWith("INVITE"))) {
			((ServerTransactionWrapper) sipT).processCancelOnActivityEnd();
		}

		// CHECK IF WE HAVE TO TERMINATE
		if (sipT.getState() != TransactionState.TERMINATED) {
			try {
				sipT.terminate();
				return;
			} catch (ObjectInUseException e) {
				log
						.error(
								" TX WAS NOT TERMIATED BUT GOT ERROR, PROCEEDING WITH REMOVAL!!!",
								e);
			}
		}

		// lets help GC
		if (sipT instanceof SecretWrapperInterface) {
			((SecretWrapperInterface) sipT).getRealTransaction()
					.setApplicationData(null);
			ah = ((SecretWrapperInterface) sipT).getActivityHandle();
		} else {
			ah = ((SecretWrapperInterface) sipT.getApplicationData())
					.getActivityHandle();
			sipT.setApplicationData(null);
		}

		try {
			this.sleeEndpoint.activityEnding(ah);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Functions calls Dialog.delete() if state is not equal to TERMINATED or is
	 * null
	 * 
	 * @see #sendActivityEndEvent(javax.sip.Transaction sipT)
	 * @param dial -
	 *            dialog which is going to be discarded as activity
	 */
	public void sendActivityEndEvent(javax.sip.Dialog dial) {

		
		SipActivityHandle ah = null;
		if (dial.getState() == null
				|| dial.getState() != DialogState.TERMINATED) {
			if (log.isDebugEnabled()) {
				log
						.debug(" sendActivityEndEvent for non terminated dialog, terminating now "
								+ dial.getDialogId());
			}
			//dial.delete();
		}

		// lets help gc
		if (dial instanceof DialogWrapper) {
			((DialogWrapper) dial).getRealDialog().setApplicationData(null);
			ah = ((DialogWrapper) dial).getActivityHandle();
		} else {
			ah = ((DialogWrapper) dial.getApplicationData())
					.getActivityHandle();
			//dial.setApplicationData(null);
		}

		try {
			this.sleeEndpoint.activityEnding(ah);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void eventProcessingSuccessful(ActivityHandle ah, Object event,
			int arg2, Address arg3, int arg4) {

		if (log.isDebugEnabled()) {
			log.debug("==========================================\n"
					+ "| EVENT PROCESSING SUCC: |\n"
					+ "==========================\n"
					+ "===========\n| HANDLE: |\n===========\n" + ah + "\n"
					+ "==========\n| EVENT: |\n==========\n" + event + "\n"
					+ "============\n| ADDRESS: |\n============\n" + arg3
					+ "\n" + "==========================================");
		}

		Object activity = activities.get(ah);
		if ((activity instanceof ServerTransactionWrapper)) {
			if ((((SipActivityHandle) ah).transactionId.endsWith("INVITE"))) {
				((ServerTransactionWrapper) activity)
						.processCancelOnEventProcessingSucess();
			} else if ((((SipActivityHandle) ah).transactionId.endsWith("ACK"))) {
				activities.remove(ah);
			}
		}

	}

	public void eventProcessingFailed(ActivityHandle ah, Object event,
			int arg2, Address arg3, int arg4, FailureReason arg5) {
		if (log.isDebugEnabled()) {
			log.debug("==========================================\n"
					+ "| EVENT PROCESSING FAIL: |\n"
					+ "==========================\n"
					+ "===========\n| HANDLE: |\n===========\n" + ah + "\n"
					+ "==========\n| EVENT: |\n==========\n" + event + "\n"
					+ "============\n| ADDRESS: |\n============\n" + arg3
					+ "\n" + "===========\n| REASON: |\n===========\n" + arg5
					+ "\n" + "==========================================");
		}

		String id = ((SipActivityHandle) ah).transactionId;
		Object activity = activities.get(ah);
		if ((activity instanceof ServerTransactionWrapper)) {
			if (id.endsWith("INVITE")) {
				((ServerTransactionWrapper) activity)
						.processCancelOnEventProcessingSucess();
			} else if (id.endsWith("ACK")) {
				activities.remove(ah);
			}
		}

		if (!id.endsWith(Request.CANCEL)
				|| !(event instanceof RequestEventWrapper))
			return;

		// PROCESSING FAILED, WE HAVE TO SEND 481 response to CANCEL
		try {
			Response txDoesNotExistsResponse = sipFactoryProvider
					.getMessageFactory().createResponse(
							Response.CALL_OR_TRANSACTION_DOES_NOT_EXIST,
							((RequestEventWrapper) event).getRequest());
			provider.sendResponse(txDoesNotExistsResponse);
		} catch (ParseException e) {

			e.printStackTrace();
		} catch (SipException e) {

			e.printStackTrace();
		}
	}

	public void activityEnded(ActivityHandle ah) {

		Object activity = activities.remove(ah);

		if (log.isDebugEnabled()) {
			if (activity instanceof Dialog) {
				log.debug("Removed terminated dialog " + "[dialog id =  "
						+ ((Dialog) activity).getDialogId() + "]");
			} else if (activity instanceof Transaction) {
				Transaction t = (Transaction) activity;
				log.debug("Removed terminated transaction "
						+ "[transaction id =  " + t.getBranchId() + "_"
						+ t.getRequest().getMethod() + "]");
			}
			log.debug("activities map size=" + activities.size());
		}

		activity = null;
	}

	public void activityUnreferenced(ActivityHandle ah) {
		// TODO
	}

	public void queryLiveness(ActivityHandle ah) {
		// for now only send activity end event if ctivity does not exists
		// anymore
		if (!(ah instanceof SipActivityHandle)) {
			return;
		}
		if (!activities.containsKey(ah)) {
			try {
				// this.sleeEndpoint.activityEnding(arg0);
				//activityEnded(ah);
			} catch (Exception e) {
				// null handle
				e.printStackTrace();
			}
		}
	}

	public Object getActivity(ActivityHandle handle) {
		return this.activities.get(handle);
	}

	public ActivityHandle getActivityHandle(Object obj) {
		return null;
	}

	// ----- END OF ACTIVITIES RELATED

	// **** SERVICE RELATED
	public void serviceInstalled(String serviceID, int[] eventIDs,
			String[] resourceOptions) {

		if (log.isDebugEnabled()) {
			log.debug("service installed: service = " + serviceID
					+ ", eventIDs = " + Arrays.toString(eventIDs)
					+ ", resourceOptions  = "
					+ Arrays.toString(resourceOptions));
		}

		// STORE SOME INFORMATION FOR LATER
		eventIDsOfServicesInstalled.put(serviceID, eventIDs);
		eventResourceOptionsOfServicesInstalled.put(serviceID, resourceOptions);

	}

	public void serviceUninstalled(String serviceID) {
		if (log.isDebugEnabled()) {
			log.debug("service uninstalled: service = " + serviceID);
		}
		// LETS REMOVE INFORMATION OF EVENT IDS OF INTERES OF SERVICE FROM THE
		// RECORD
		eventIDsOfServicesInstalled.remove(serviceID);
		eventResourceOptionsOfServicesInstalled.remove(serviceID);
	}

	public void serviceActivated(String serviceID) {
		if (log.isDebugEnabled()) {
			log.debug("service activated: service = " + serviceID);
		}

		int[] eventIDs = (int[]) eventIDsOfServicesInstalled.get(serviceID);
		if (eventIDs != null) {
			for (int i = 0; i < eventIDs.length; i++) {
				EventTypeID eventTypeID = serviceContainer
						.getEventTypeID(eventIDs[i]);
				ComponentKey eventKey = serviceContainer
						.getEventKey(eventTypeID);
				Set servicesActivatedList = (Set) myComponentKeys.get(eventKey);
				if (servicesActivatedList != null) {
					servicesActivatedList.add(serviceID);
					if (log.isDebugEnabled()) {
						log
								.debug("Service "
										+ serviceID
										+ " is activated and registred to event with key "
										+ eventKey);
					}
				}
			}
		}

		
	}

	public void serviceDeactivated(String serviceID) {
		if (log.isDebugEnabled()) {
			log.debug("service deactivated: service = " + serviceID);
		}

		int[] eventIDs = (int[]) eventIDsOfServicesInstalled.get(serviceID);
		if (eventIDs != null) {
			for (int i = 0; i < eventIDs.length; i++) {
				EventTypeID eventTypeID = serviceContainer
						.getEventTypeID(eventIDs[i]);
				ComponentKey eventKey = serviceContainer
						.getEventKey(eventTypeID);
				Set servicesActivatedList = (Set) myComponentKeys.get(eventKey);
				if (servicesActivatedList != null) {
					servicesActivatedList.remove(serviceID);
					if (log.isDebugEnabled()) {
						log
								.debug("Service "
										+ serviceID
										+ " was deactivated and unregistred to event with key "
										+ eventKey);
					}
				}
			}
		}

	}

	// ------ END OF SERVICE RELATED

	// ************** EVENT HANDLING PART


	public void processIOException(IOExceptionEvent arg0) {

	}


	public void processDialogTerminated(
			DialogTerminatedEvent dialogTerminatedEvent) {
	
		DialogWrapper dialogWrapper = null;
		if (dialogTerminatedEvent.getDialog() instanceof DialogWrapper) {
			dialogWrapper = (DialogWrapper) dialogTerminatedEvent.getDialog();
		} else {
			dialogWrapper = (DialogWrapper) dialogTerminatedEvent.getDialog()
					.getApplicationData();
		}

		if (log.isDebugEnabled()) {
			log.debug("Processing dialog termination: "
					+ dialogWrapper.getDialogId());
		}

		ComponentKey key = null;
		// cancel timer, if not suceeeds than dialog timeout was triggered
		dialogWrapper.cancel();
		boolean c = dialogWrapper.hasBeenCanceled();
		boolean t1 = dialogWrapper.getHasTimedOut();

		// if (dialogWrapper.cancel() || dialogWrapper.getHasTimedOut()) {
		if (!c || t1) {
			// timeout event - if event has not run or flag has been set inside
			// timer task, it means that dialog timed out
			key = new ComponentKey("javax.sip.timeout.DIALOG", "net.java",
					"1.2");
		} else {

			// dialog terminated event - else its reglar term.
			key = new ComponentKey("javax.sip.dialog.Terminated", "net.java",
					"1.2");

		}

		if (!isEventGoingToBereceived(key)) {
			if (log.isDebugEnabled()) {
				log.debug("------------------ DIALOG[" + key + "] "
						+ dialogWrapper.getDialogId()
						+ " TERMINATED EVENT IS FILTERED ----------------");
			}

			this.sendActivityEndEvent(dialogWrapper);

		} else {

			int eventID = -1;
			try {
				eventID = eventLookup.getEventID(key.getName(),
						key.getVendor(), key.getVersion());

			} catch (Exception e) {
				e.printStackTrace();
			}

			if (eventID == -1) { // Silently drop the message because this is
				// not
				// a registered event
				// type.
				SipToSLEEUtility.displayMessage("Resource adaptor",
						"- Event is not a a registared event type", key);
				if (dialogWrapper.isInStateEventFireSequence()) {
					// schedule
					DTERemoveTask drt=new DTERemoveTask(dialogWrapper,eventID,key);
					timer.schedule(drt, 150);
					return;

				}
			} else {

				if (!dialogWrapper.isInStateEventFireSequence()) {
					SipToSLEEUtility.displayDeliveryMessage(
							"SipResourceAdaptor", eventID, key, dialogWrapper
									.getActivityHandle().transactionId);
					// fire event
					try {
						DialogTerminatedEventWrapper event = new DialogTerminatedEventWrapper(
								this.sipFactoryProvider.getSipProvider(),
								dialogWrapper);
						sleeEndpoint.fireEvent(dialogWrapper
								.getActivityHandle(), event, eventID,
								new Address(AddressPlan.SIP, dialogWrapper
										.getLocalParty().toString()));
					} catch (Exception e) {
						e.printStackTrace();
					}

					this.sendActivityEndEvent(dialogWrapper);

				} else {
					DTERemoveTask drt=new DTERemoveTask(dialogWrapper,eventID,key);
					timer.schedule(drt, 150);
					// schedule
					return;
				}
			}
		}

		// end dialog activity

	}

	/**
	 * Should be invoked for all requests except Request.CANCEL !!!
	 * 
	 * @param req
	 */
	private void processNotCancelRequests(RequestEventWrapper req) {

		// HERE WE HAVE STX and dialog Wrappers
		ServerTransaction stw = req.getServerTransaction();
		DialogWrapper dw = (DialogWrapper) stw.getDialog();

		if (log.isDebugEnabled()) {
			log.debug("processNotCancelRequests() Server Transaction: " + stw);
		}

		javax.slee.Address address = new Address(AddressPlan.SIP,
				((FromHeader) req.getRequest().getHeader(FromHeader.NAME))
						.getAddress().toString());

		// String branchId = req.getServerTransaction().getBranchId();

		// Now common stuff - tx
		SipActivityHandle txAH = ((SecretWrapperInterface) stw)
				.getActivityHandle();
		ComponentKey txEventKey = getKeyFor1_1(req.getRequest().getMethod());

		SipActivityHandle dialogAH = null;
		ComponentKey dialogEventKey = null;
		if (dw != null) {

			// if its not null lets fill more variables
			// dialogAH = new SipActivityHandle(dw.getDialogId());
			dialogAH = dw.getActivityHandle();
			dialogEventKey = getKeyFor1_2(req.getRequest().getMethod(), dw);

		} else if(this.createDialogAutomaticly){

			// Dialog may not exist till now, we will create one if 1.2 INVITE
			// will be received
			ComponentKey key = new ComponentKey(EVENT_PREFIX_1_2
					+ "Request.INVITE", this.VENDOR_1_2, this.VERSION_1_2);
			if ((req.getRequest().getMethod().compareTo(Request.INVITE) == 0)
					&& (isEventGoingToBereceived(key))
					&& stw instanceof ServerTransactionWrapper) {

				// We have to create dialog if there is none.
				try {

					dw = (DialogWrapper) ((SipProviderProxy) sipFactoryProvider
							.getSipProvider()).getNewDialog(stw, true);
					((ServerTransactionWrapper) stw).setDialogWrapper(dw);
					// dialogAH = new SipActivityHandle(dw.getDialogId());
					dialogAH = dw.getActivityHandle();
					dialogEventKey = key;
					activities.put(dialogAH, dw);
					// Bartek: do we have to notify slee that we started this -
					// this is part of RA actions.
				} catch (SipException e) {
					e.printStackTrace();
				}

			}

		}

		// HERE WE HAVE KEYs, LETS LOOKUP EVENT and fire

		// This is part of hack that enables dummy ack TXs
		boolean removeACK = false;
		// Tx event
		if (isEventGoingToBereceived(txEventKey)) {
			SipToSLEEUtility.displayMessage("Resource adaptor",
					"looking up transaction event", txEventKey);

			int eventID = -1;
			try {
				eventID = eventLookup.getEventID(txEventKey.getName(),
						txEventKey.getVendor(), txEventKey.getVersion());
			} catch (Exception e) {
				sendErrorResponse(req.getRequest(), Response.SERVER_INTERNAL_ERROR, e.getMessage());
				e.printStackTrace();
			}

			if (eventID == -1) { // Silently drop the message because this is
				// not
				// a registered event
				// type.
				SipToSLEEUtility.displayMessage("Resource adaptor",
						"- Event is not a a registared event type", txEventKey);
				return;
			}

			SipToSLEEUtility.displayDeliveryMessage("Resource adaptor",
					eventID, txEventKey, txAH.transactionId);

			try {
				sleeEndpoint.fireEvent(txAH, req, eventID, address);
				
			} catch (Exception e) {
				sendErrorResponse(req.getRequest(), Response.SERVER_INTERNAL_ERROR, e.getMessage());
				e.printStackTrace();
			}

		} else {

			if (log.isDebugEnabled()) {
				log.debug("\n======== EVENT[1][" + txEventKey
						+ "] IS NOT GOING TO BE RECEIVED, DROPING ========");
			}
			// We fake ack, so we have to remove it by hand ;[
			if (txEventKey != null && txEventKey.getName().endsWith("ACK"))
				removeACK = true;

		}

		if (dialogAH != null && isEventGoingToBereceived(dialogEventKey)) {
			SipToSLEEUtility.displayMessage("Resource adaptor",
					"looking up dialog event", dialogEventKey);
			int eventID;
			try {
				eventID = eventLookup
						.getEventID(dialogEventKey.getName(), dialogEventKey
								.getVendor(), dialogEventKey.getVersion());
			} catch (FacilityException e2) {
				throw new RuntimeException("Failed to lookup		  event!", e2);
			} catch (UnrecognizedEventException e2) {
				throw new RuntimeException("Failed to lookup  event!", e2);
			}

			if (eventID == -1) { // Silently drop the message because this is
				// not
				// a registered event
				// type.
				SipToSLEEUtility.displayMessage("Resource adaptor",
						"- Event is not a a registared event type",
						dialogEventKey);
				return;
			}

			SipToSLEEUtility.displayDeliveryMessage("Resource adaptor",
					eventID, dialogEventKey, dialogAH.transactionId);

			try {
				sleeEndpoint.fireEvent(dialogAH, req, eventID, address);
			} catch (Exception e) {
				e.printStackTrace();
				sendErrorResponse(req.getRequest(), Response.SERVER_INTERNAL_ERROR, e.getMessage());
			}

		} else {
			if (dialogEventKey != null)
				if (log.isDebugEnabled()) {
					log
							.debug("\n======== EVENT[2]["
									+ dialogEventKey
									+ "] IS NOT GOING TO BE RECEIVED, DROPING ========");
				}

			if (dialogEventKey != null
					&& dialogEventKey.getName().endsWith("ACK"))
				removeACK = true;

		}

		if (removeACK) {
			activities.remove(((SecretWrapperInterface) req
					.getServerTransaction()).getActivityHandle());
		}

	}

	/**
	 * As name sugests - this method should be called to process CANCEL requests -
	 * only!! It handles all that logic with 487, Response.SUCCESS
	 * 
	 * @param req
	 */
	private void processCancelRequest(RequestEventWrapper req) {

		ServerTransactionWrapper cancel_stw = (ServerTransactionWrapper) req
				.getServerTransaction();

		// SOME NEEDED STUFF
		String cancelTransactionID = cancel_stw.getBranchId() + "_"
				+ Request.CANCEL;
		String inviteTransactionID = cancel_stw.getBranchId() + "_"
				+ Request.INVITE;
		// String idForSIPHandle = null;
		SipActivityHandle activityHandle = null;

		Object inviteTx = activities.get(new SipActivityHandle(
				inviteTransactionID));

		if (log.isDebugEnabled()) {
			log.debug("\n+++++++++++++++++++++++++++\nINVITE TX:" + inviteTx
					+ "\ninviteTx instanceof ServerTransaction:"
					+ (inviteTx instanceof ServerTransaction) + "\n");
		}

		if (inviteTx != null && inviteTx instanceof ServerTransaction) {

			Response cancelOkResponse = null;
			// WE HAVE A MATCHING TX FOR CANCEL RQ
			// idForSIPHandle = inviteTransactionID;
			activityHandle = ((SecretWrapperInterface) inviteTx)
					.getActivityHandle();
			ServerTransactionWrapper invite_stw = (ServerTransactionWrapper) inviteTx;
			// FIXME - IS THIS CORRECT? WE DO THAT BEFORE AUTOMATIC OK ??
			// FIXME - TX STATES ?? ARE THESE CORRECT?

			if ((invite_stw.getState() == TransactionState.TERMINATED)
					|| (invite_stw.getState() == TransactionState.COMPLETED)
					|| (invite_stw.getState() == TransactionState.CONFIRMED)) {

				if (log.isDebugEnabled()) {
					log
							.debug("================================================== \nFINAL RESPONSE HAS BEEN SENT FOR TX, DROPPING CANCEL: \n"
									+ invite_stw
									+ "\n==================================================");
				}
				// FINAL
				// FINAL RESPONSE
				// HAS
				// BEEN
				// SENT?
				return;
			}

			// LETS SEND AUTOMATIC OK
			if (log.isDebugEnabled()) {
				log
						.debug("----------- SENDING AUTOAMTIC OK TO CANCEL -----------");
			}

			// LETS COMPLETE CancelTX
			// SENDING "OK" TO "CANCEL" - WE DONT NEED ANY RETRANSMISSIONS

				try {
					cancelOkResponse = sipFactoryProvider.getMessageFactory()
							.createResponse(Response.OK, req.getRequest());
					cancel_stw.sendResponse(cancelOkResponse);
				} catch (ParseException e1) {
					
					e1.printStackTrace();
					sendErrorResponse(req.getRequest(), Response.NOT_ACCEPTABLE, e1.getMessage());
				} catch (SipException e) {

					e.printStackTrace();
					sendErrorResponse(req.getRequest(), Response.NOT_ACCEPTABLE, e.getMessage());
				} catch (InvalidArgumentException e) {
					
					e.printStackTrace();
					sendErrorResponse(req.getRequest(), Response.SERVER_INTERNAL_ERROR, e.getMessage());
				}
				
			

			// NOW WE HAVE TO CHECK IF WE HEAVE DIALOG ASSOCIATED WITH INVITE TX
			// IF SO LETS SEND 487, DIALOG SHOULD BE TERMINATED FOR US BY STACK
			// with javax.sip.dialog.DialogTerminationEvent as result
			// BUT ITS NOT ;/

			DialogWrapper invite_dw = (DialogWrapper) invite_stw.getDialog();
			if (invite_dw != null) {
				// WE HAVE TO FIRE 487

				// AND CANCEL TIMEOUT
				invite_dw.cancel();

				if (invite_dw.getState().getValue() != DialogState.TERMINATED
						.getValue()) {

					if (log.isDebugEnabled()) {
						log.debug("TERMINATING DIALOG EXPLICITLY --> "
								+ invite_dw);
					}
					//invite_dw.delete();
				}
		
					try {
						invite_stw.sendResponse(sipFactoryProvider
								.getMessageFactory().createResponse(
										Response.REQUEST_TERMINATED,
										invite_stw.getRequest()));
					} catch (ParseException e1) {
						
						e1.printStackTrace();
						sendErrorResponse(req.getRequest(), Response.NOT_ACCEPTABLE, e1.getMessage());
					} catch (SipException e) {

						e.printStackTrace();
						sendErrorResponse(req.getRequest(), Response.NOT_ACCEPTABLE, e.getMessage());
					} catch (InvalidArgumentException e) {
						
						e.printStackTrace();
						sendErrorResponse(req.getRequest(), Response.SERVER_INTERNAL_ERROR, e.getMessage());
					}
			

			} else {

				if (log.isDebugEnabled()) {
					log.debug("============= POSTPONING CANCEL ===========");
				}
				invite_stw.setCancel(req);
				// idForSIPHandle = null;
				activityHandle = null;
			}

		} else {
			// WE DONT HAVE MATCHING TX TO CANCEL
			// LETS FIRE IT ON CANCEL TX
			activityHandle = ((SecretWrapperInterface) req
					.getServerTransaction()).getActivityHandle();

		}

		// if (idForSIPHandle == null) {
		if (activityHandle == null) {

			if (log.isDebugEnabled()) {
				log
						.debug("============= CANCEL POSTPONED - RETURNING ====================");
			}

			return;
		}

		// WE ARE HERE, IT MEANS THAT DIALOG WAS NOT NULL, OR NO INVITE TX WAS
		// FOUND
		// its 1.1 event since there is no indialog cancel. (c) by Ranga
		ComponentKey key = new ComponentKey(this.EVENT_REQUEST_CANCEL_1_1,
				this.VENDOR_1_1, this.VERSION_1_1);
		SipToSLEEUtility.displayMessage("Resource adaptor", "looking up", key);
		if (!isEventGoingToBereceived(key)) {

			if (log.isDebugEnabled()) {
				log.debug("\n======== EVENT[3]" + key
						+ " IS NOT GOING TO BE RECEIVED, DROPING ========");
			}

			return;
		}

		int eventID = -1;
		try {
			eventID = eventLookup.getEventID(key.getName(), key.getVendor(),
					key.getVersion());
		} catch (Exception e) {
			
			e.printStackTrace();
			sendErrorResponse(req.getRequest(), Response.SERVER_INTERNAL_ERROR, e.getMessage());
		}

		if (eventID == -1) {
			// Silently drop the message because this is
			// not a registered event type.
			SipToSLEEUtility.displayMessage("Resource adaptor",
					"- Event is not a a registared event type", key);
			return;
		}

		SipToSLEEUtility.displayDeliveryMessage("Resource adaptor", eventID,
				key, activityHandle.transactionId);
		try {
			sleeEndpoint.fireEvent(activityHandle, req, eventID, new Address(
					AddressPlan.SIP, ((ToHeader) req.getRequest().getHeader(
							ToHeader.NAME)).getAddress().toString()));
		} catch (Exception e) {
			
			e.printStackTrace();
			sendErrorResponse(req.getRequest(), Response.SERVER_INTERNAL_ERROR, e.getMessage());
		}
	}

	private ServerTransaction proccessACKReqeust(RequestEvent req) {

		// here we can have req.getDialog==null in case getNewDialog has not
		// been called on INVITE or != if it has been
		Dialog d = req.getDialog();

		if (d != null) {
			// Someone is acting statefull
			if (d.getApplicationData() != null) {
				DialogWrapper dw = (DialogWrapper) d.getApplicationData();
				d = dw;
			} else {
				log.error("GOT NULL DialogWrapper when dialog is not null!!!!");
				// this shouldnt happen
			}

		}

		return new ServerTransactionWrapper((ServerTransaction) ((SIPRequest) req.getRequest()).getTransaction(),
				(DialogWrapper) d, this);
	}

	private void sendErrorResponse(Request request,int code, String msg)
	{
		try{
			ContentTypeHeader contentType=this.sipFactoryProvider.getHeaderFactory().createContentTypeHeader("text", "plain");
			Response response=this.sipFactoryProvider.getMessageFactory().createResponse( code,request, contentType, msg.getBytes());
			this.sipFactoryProvider.getSipProvider().sendResponse(response);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipListener#processRequest(javax.sip.RequestEvent)
	 */
	// public void doProcessRequest(RequestEvent req, ServerTransaction st) {
	public void processRequest(RequestEvent req) {
		// TODO PROPER HANDLING OF EXCEPTIONS

		try{
			if (log.isInfoEnabled()) {
				log
						.info("\n-------------------------\nREQUEST:\n-------------------------\n"
								+ req.getRequest()
								+ "\n-------------------------");
			}
			if (log.isDebugEnabled()) {
				log.debug("Server Transaction: " + req.getServerTransaction());
			}
		
		ServerTransaction st = null;
		Dialog dialog = null;
		if (req.getRequest().getMethod().equals(Request.ACK)) {
			// ACK requires DUMMY stx as it is pseudo transact method ;[

			st = proccessACKReqeust(req);
			dialog = st.getDialog();

		} else {
			st = req.getServerTransaction();

			if (st == null) {
				 {
					try {
						st = provider.getNewServerTransaction(req.getRequest());
					} catch (TransactionAlreadyExistsException e) {
						
						e.printStackTrace();
					
						
						sendErrorResponse(req.getRequest(), Response.SERVER_INTERNAL_ERROR, e.getMessage());
						
					
					} catch (TransactionUnavailableException e) {
						
						e.printStackTrace();
						sendErrorResponse(req.getRequest(), Response.NOT_ACCEPTABLE, e.getMessage());
					}
					if (log.isDebugEnabled()) {
						log
								.debug("\n----------------- CREATED NEW STx ---------------------\nBRANCH: "
										+ st.getBranchId()
										+ "\n-------------------------------------------------------");
					}

				}
			}

			dialog = st.getDialog();
			DialogWrapper DW = null;

			if (dialog != null) {
				if (log.isDebugEnabled()) {
					log.debug("======= DIALOG NOT NULL IN STx: " + dialog
							+ " ========");
				}
				DW = (DialogWrapper) dialog.getApplicationData();
				if (DW == null) {
					// WE DONT HAVE DW?? Hmm lets make sure we have one...
					DW = new DialogWrapper(dialog, this);
					if (DW.getState() == null
							|| !DW.getState().equals(DialogState.TERMINATED))
						activities.put(DW.getActivityHandle(), DW);
				} else {
					// RENEW TIMER
					DW.renew();
				}
				// LETS SWITCH DW WITH Dialog
				dialog = DW;
			}

			ServerTransactionWrapper stw = (ServerTransactionWrapper) st
					.getApplicationData();
			if (stw == null) {
				stw = new ServerTransactionWrapper(st, DW, this);
			}
			st = stw;

			// THIS ONE WE ALWAYS NEED

		}

		// Bartek: what about rtr ?
		activities.put(((SecretWrapperInterface) st).getActivityHandle(), st);

		RequestEventWrapper REW = new RequestEventWrapper(
				this.sipFactoryProvider.getSipProvider(), st, dialog, req
						.getRequest());

		// WE HAVE SET UP ALL WHAT WE NEED, NOW DO SOMETHING

		if (req.getRequest().getMethod().compareTo(Request.CANCEL) != 0) {
			// HERE WE WILL PROCESS ALL NON CANCEL REQUESTS - except ACKs
			processNotCancelRequests(REW);
		} else {
			// QUITE CONTRARY, CANCEL IS PROCESSED DIFFERENTLY BECAUSE ITS
			// PROCESSING PATH IS QUITE DIFFERENT THAN OTHER REQUESTS
			processCancelRequest(REW);
		}
		}catch(Exception e)
		{
			e.printStackTrace();
			sendErrorResponse(req.getRequest(), Response.SERVER_INTERNAL_ERROR, e.getMessage());
		}
	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipListener#processResponse(javax.sip.ResponseEvent)
	 */
	public void processResponse(ResponseEvent resp) {

		// TODO PROPER HANDLING OF EXCEPTIONS

		int statusCode = resp.getResponse().getStatusCode();
		if (log.isInfoEnabled()) {
			log
					.info("\n-------------------------\nRESPONSE:\n-------------------------\n"
							+ resp.getResponse()
							+ "\n-------------------------");
		}

		ComponentKey key = null;

		if (statusCode == 100) {
			key = new ComponentKey(this.EVENT_RESPONSE_TRYING_1_1,
					this.VENDOR_1_1, this.VERSION_1_1);
		} else if (100 < statusCode && statusCode < 200) {
			key = new ComponentKey(this.EVENT_RESPONSE_INFORMATIONAL_1_1,
					this.VENDOR_1_1, this.VERSION_1_1);
		} else if (statusCode < 300) {
			key = new ComponentKey(this.EVENT_RESPONSE_SUCCESS_1_1,
					this.VENDOR_1_1, this.VERSION_1_1);
		} else if (statusCode < 400) {
			key = new ComponentKey(this.EVENT_RESPONSE_REDIRECTION_1_1,
					this.VENDOR_1_1, this.VERSION_1_1);
		} else if (statusCode < 500) {
			key = new ComponentKey(this.EVENT_RESPONSE_CLIENT_ERROR_1_1,
					this.VENDOR_1_1, this.VERSION_1_1);
		} else if (statusCode < 600) {
			key = new ComponentKey(this.EVENT_RESPONSE_SERVER_ERROR_1_1,
					this.VENDOR_1_1, this.VERSION_1_1);
		} else {
			key = new ComponentKey(this.EVENT_RESPONSE_GLOBAL_FAILURE_1_1,
					this.VENDOR_1_1, this.VERSION_1_1);
		}

		ClientTransaction ct = resp.getClientTransaction();
		if (ct == null) {
			log.error("===> CT is NULL - RTR ? CALLID["
					+ ((CallID) resp.getResponse().getHeader(CallID.NAME))
							.getCallId()
					+ "] BRANCH["
					+ ((Via) resp.getResponse().getHeaders(Via.NAME).next())
							.getBranch()
					+ "] METHOD["
					+ ((CSeq) resp.getResponse().getHeader(CSeq.NAME))
							.getMethod() + "] CODE["
					+ resp.getResponse().getStatusCode() + "]");
			return;
		}

		SipActivityHandle txAH =null;
		

		Dialog dialog = ct.getDialog();

		DialogWrapper DW = null;
		if (dialog != null) {

			DW = (DialogWrapper) dialog.getApplicationData();
			if (DW == null) {
				DW = new DialogWrapper(dialog, this);
				activities.put(DW.getActivityHandle(), DW);
			} else {
				// RENEW TIMER
				DW.renew();
			}
			dialog = DW;

			DW.startStateEventFireSequence();
			if (DW.fireDialogStateEvent(resp.getResponse())) {
				if (log.isDebugEnabled()) {
					log
							.debug(" == FIRED STATE EVENT, not firing response event ==");
				}

				return;
			}
		}

		ClientTransactionWrapper ctw = (ClientTransactionWrapper) ct
				.getApplicationData();
		if (ctw == null) {
			ctw = new ClientTransactionWrapper(ct, DW);
		}
		txAH = ctw.getActivityHandle();
		ct = ctw;

		// here we could possibly lock on thread and wait for another SLEE tx to
		// commit - this would happen
		// when SIP CTX creation SLEE TX didnt commit, therefore it did not
		// attach SBBE to it.

		SipToSLEEUtility.displayMessage("Resource adaptor", "looking up", key);

		int eventID = -1;
		if (!isEventGoingToBereceived(key)) {
			if (log.isDebugEnabled()) {
				log.debug("\n======== EVENT[4] " + key
						+ " IS NOT GOING TO BE RECEIVED, DROPING ========");
			}
			return;
		}

		try {
			eventID = eventLookup.getEventID(key.getName(), key.getVendor(),
					key.getVersion());
		} catch (Exception e1) {
			log.error(e1);
		}

		if (eventID == -1) {
			// Silently drop the message because this is not a registered event
			// type.
			SipToSLEEUtility.displayMessage("Resource adaptor",
					"- Event is not a a registared event type", key);
			return;
		}
	
		// NOW SOME LITTLE CHEAT, THIS WILL BE FIRED INSTEAD ORIGINAL
		// RequestEvent, it should provide
		// sbb with conveniant way of getting what it needs, no more magic, each
		// wrapper extends class it wrapps.
		// We just take care of proper initialization

		try {
			SipToSLEEUtility.displayDeliveryMessage("Resource adaptor",
					eventID, key, txAH.toString());
			sleeEndpoint.fireEvent(txAH, new ResponseEventWrapper(
					this.sipFactoryProvider.getSipProvider(), ct, dialog, resp
							.getResponse()), eventID, new Address(
					AddressPlan.SIP, ((ToHeader) resp.getResponse().getHeader(
							ToHeader.NAME)).getAddress().toString()));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipListener#processTimeout(javax.sip.TimeoutEvent)
	 */
	public void processTimeout(TimeoutEvent timeout) {

		// TODO PROPER HANDLING OF EXCEPTIONS

		if (log.isDebugEnabled()) {
			log
					.debug("\n==========================\nProcessing timeout:\n--------------------------\n"
							+ timeout.getTimeout().getValue()
							+ "\n==========================");
		}
		// KEY FOR TRANSACTION TIMEOUT
		ComponentKey key = key = new ComponentKey(
				this.EVENT_TIMEOUT_TRANSACTION, this.VENDOR_1_1,
				this.VERSION_1_1);

		// KEY FOR DIALOG SetupFailed if one existed and wasnt CONFIRMED
		ComponentKey dialogKey = null;

		if (log.isDebugEnabled()) {
			log.debug("\n------------------------ \nTIMEOUT:\n"
					+ timeout.getSource() + "\n"
					+ timeout.getClientTransaction() + "\n"
					+ timeout.getServerTransaction()
					+ "\n-------------------------------");
		}

		Transaction t = null;
		TimeoutEventWrapper TEW = null;
		boolean isInvite = false;
		if (timeout.getServerTransaction() != null) {
			t = timeout.getServerTransaction();
			TEW = new TimeoutEventWrapper(this.sipFactoryProvider
					.getSipProvider(), (ServerTransaction) t, timeout
					.getTimeout());
		} else {
			t = timeout.getClientTransaction();
			TEW = new TimeoutEventWrapper(this.sipFactoryProvider
					.getSipProvider(), (ClientTransaction) t, timeout
					.getTimeout());
		}
		SipActivityHandle activityHandle = ((SecretWrapperInterface) t
				.getApplicationData()).getActivityHandle();
		;
		if (t.getRequest().getMethod().equals(Request.INVITE))
			isInvite = true;

		Dialog dial = t.getDialog();
		// IF STATE WASNT CONFIRMED LETS FIRE.

		if (isInvite && dial != null)
			if (dial.getState() != DialogState.CONFIRMED) {
				dialogKey = new ComponentKey(
						this.EVENT_DIALOG_STATE_SetupTimedOut, this.VENDOR_1_2,
						this.VERSION_1_2);
				DialogWrapper DW = (DialogWrapper) dial.getApplicationData();
				if (DW != null)
					DW.cancel();
			}
		SipToSLEEUtility.displayMessage("Resource adaptor", "looking up", key);
		if (dialogKey != null)
			SipToSLEEUtility.displayMessage("Resource adaptor", "looking up",
					dialogKey);
		int eventID = -1, dialogEventID = -1;
		try {
			eventID = eventLookup.getEventID(key.getName(), key.getVendor(),
					key.getVersion());
			if (dialogKey != null)
				dialogEventID = eventLookup.getEventID(dialogKey.getName(),
						dialogKey.getVendor(), dialogKey.getVersion());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (eventID == -1) {
			// Silently drop the message because this is not a registered event
			// type.
			SipToSLEEUtility.displayMessage("Resource adaptor",
					"- Event is not a a registared event type", key);

			return;
		}

		try {

			SipToSLEEUtility.displayDeliveryMessage("Resource adaptor",
					eventID, key, t.getBranchId() + "_"
							+ t.getRequest().getMethod());

			sleeEndpoint.fireEvent(activityHandle, TEW, eventID, address);

			if (dialogKey != null && dialogEventID != -1) {
				SipToSLEEUtility.displayDeliveryMessage("Resource adaptor",
						eventID, dialogKey, dial.getDialogId());

				sleeEndpoint.fireEvent(((DialogWrapper) dial
						.getApplicationData()).getActivityHandle(), TEW,
						dialogEventID, address);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipListener#processTransactionTerminated(javax.sip.TransactionTerminatedEvent)
	 */
	public void processTransactionTerminated(
			TransactionTerminatedEvent txTerminatedEvent) {

		// TODO PROPER HANDLING OF EXCEPTIONS

		Transaction t = null;

		TransactionTerminatedEventWrapper TTEW = null;

		if (txTerminatedEvent.isServerTransaction()) {

			t = txTerminatedEvent.getServerTransaction();
			// Object activity=this.activities.remove(ah);
			// terminatedActivities.put(ah,activity);
			TTEW = new TransactionTerminatedEventWrapper(
					this.sipFactoryProvider.getSipProvider(),
					(ServerTransaction) t);

		} else {

			t = txTerminatedEvent.getClientTransaction();

			// Object activity=this.activities.remove(ah);
			// terminatedActivities.put(ah,activity);
			TTEW = new TransactionTerminatedEventWrapper(
					this.sipFactoryProvider.getSipProvider(),
					(ClientTransaction) t);
		}

	

		if (log.isDebugEnabled()) {
			log.debug("==== processTransactionTerminatedEvent() ID:"
					+ t.getBranchId() + "_" + t.getRequest().getMethod()
					+ " =====");
		}

		ComponentKey key = new ComponentKey("javax.sip.transaction.Terminated",
				"net.java", "1.2");


		if (!isEventGoingToBereceived(key)) {
			if (log.isDebugEnabled()) {
				log.debug("\n======== EVENT[" + key
						+ "] IS NOT GOING TO BE RECEIVED, DROPING ========");
			}
		} else {

			SipToSLEEUtility.displayMessage("Resource adaptor", "looking up",
					key);

			int eventID = -1;
			try {
				eventID = eventLookup.getEventID(
						"javax.sip.transaction.Terminated", "net.java", "1.2");
			} catch (Exception e2) {
				log.error(e2);
			}

			// IF NOT REGISTERED EVENT TYPE, DROP
			if (eventID == -1) {
				SipToSLEEUtility.displayMessage("Resource adaptor",
						"- Event is not a a registared event type", key);
			} else {
				try {
					SipToSLEEUtility.displayDeliveryMessage("Resource adaptor",
							eventID, key, t.getBranchId() + "_"
									+ t.getRequest().getMethod());
					sleeEndpoint.fireEvent(((SecretWrapperInterface) t
							.getApplicationData()).getActivityHandle(), TTEW,
							eventID, new Address(AddressPlan.SIP, ((ToHeader) t
									.getRequest().getHeader(ToHeader.NAME))
									.getAddress().toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// LETS END AO LIFE
		this.sendActivityEndEvent(t);

	}


	
	// ******* OTHER
	/**
	 * Creates compKey for 1.1 type of events
	 * 
	 * @param method
	 * @return
	 */
	private ComponentKey getKeyFor1_1(String method) {
		String localMethod = null;

		if (!isRFC3261Method(method))
			localMethod = "EXTENSION";
		else
			localMethod = method;

		if (log.isDebugEnabled()) {
			log
					.debug("\n=============== LOOKING COMPONENTKEY FOR METHOD ==================\nPASSED: "
							+ method
							+ "\nLOCAL: "
							+ localMethod
							+ "\n===================================");
		}

		ComponentKey key = (ComponentKey) sipToSleeEvent.get(localMethod
				+ "_TX");
		if (key == null) {
			key = new ComponentKey(EVENT_PREFIX_1_1 + "Request." + localMethod,
					this.VENDOR_1_1, this.VERSION_1_1);
			sipToSleeEvent.put(localMethod + "_TX", key);

		}

		return key;
	}

	/**
	 * Creates compKey for 1.2 type of events
	 * 
	 * @param method
	 * @return
	 */
	private ComponentKey getKeyFor1_2(String method, DialogWrapper dw) {
		String localMethod = null;
		if (!isRFC3261Method(method))
			localMethod = "EXTENSION";
		else
			localMethod = method;
		if (log.isDebugEnabled()) {
			log
					.debug("\n=============== LOOKING COMPONENTKEY FOR METHOD ==================\nPASSED: "
							+ method
							+ "\nLOCAL: "
							+ localMethod
							+ "\n===================================");
		}

		ComponentKey key = (ComponentKey) sipToSleeEvent.get(localMethod
				+ "_DIALOG");
		if (key == null) {
			key = new ComponentKey(EVENT_PREFIX_1_2 + "Request." + localMethod,
					this.VENDOR_1_2, this.VERSION_1_2);
			sipToSleeEvent.put(localMethod + "_DIALOG", key);

		}

		if (key.getName().equals("javax.sip.dialog.Request.BYE")
				&& dw.getTerminateOnBye()) {
			key = (ComponentKey) sipToSleeEvent
					.get(EVENT_REQUEST_DIALOG_TERMINATION_EVENT_NAME_1_2);
			if (key == null) {
				key = new ComponentKey(
						EVENT_REQUEST_DIALOG_TERMINATION_EVENT_NAME_1_2,
						this.VENDOR_1_2, this.VERSION_1_2);
				sipToSleeEvent.put(
						EVENT_REQUEST_DIALOG_TERMINATION_EVENT_NAME_1_2, key);
			}
		}
		return key;
	}

	/**
	 * 
	 * @param method -
	 *            name of the method. For instance value of
	 *            "javax.sip.Request.MESSAGE" constant.
	 * @return
	 *            <ul>
	 *            <li><b>true</b> - if method is one of RFC3261 SIP methods
	 *            <li><b>false</b> - otherwise
	 *            </ul>
	 */
	private boolean isRFC3261Method(String method) {

		return rfc3261Methods.contains(method);
	}

	protected void printActivities(String where) {
		if (log.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer();
			Iterator itKeys = activities.keySet().iterator();
			Object keyA = null;
			// Iterator itValues=activities.values().iterator();
			while (itKeys.hasNext()) {
				keyA = itKeys.next();
				sb.append(keyA + " == " + activities.get(keyA) + "\n");
			}
			log.debug("================ ACTIVITIES["+where+"] ================\n"
					+ sb.toString()
					+ "\n=============================================");
		}
	}

	/**
	 * Part of event filtering.
	 * 
	 * @param eventKey -
	 *            comp key of event
	 * @return
	 *            <ul>
	 *            <li><b>true</b> - if event is going to be received
	 *            <li><b>false</b> - otherwise
	 *            </ul>
	 */
	public boolean isEventGoingToBereceived(ComponentKey eventKey) {
		Set serviceIDSet = (Set) myComponentKeys.get(eventKey);
		if ((serviceIDSet != null) && (serviceIDSet.size() > 0))
			return true;
		else
			return false;

	}

	// *********************** DEBUG PART]
	private Timer debugTimer = new Timer();

	private org.apache.log4j.Logger debugLogger = Logger.getLogger("org.mobicents.slee.resource.sip.DEBUG");

	private HashMap receivedEvents = new HashMap();
	private ArrayList orderOfEvent = new ArrayList();
	private HashMap timeStamps = new HashMap();

	private class EventTimerTask extends TimerTask {

		int runCount = 0;

		@Override
		public void run() {

			debugLogger.info("-------------------- DEUMP RUN[" + runCount
					+ "]-----------------");
			debugLogger.info("[" + runCount + "] ACTIVITIES DUMP");
			TreeMap ac = new TreeMap(activities);
			int count = 0;
			for (Object key : ac.keySet()) {
				debugLogger.info("[" + runCount + "] AC[" + count++ + "] KEY["
						+ key + "] A[" + ac.get(key) + "]");
			}
			debugLogger.info("[" + runCount + "] --- EVENTS RECEVIED");
			ArrayList orderCopy = new ArrayList(orderOfEvent);
			count = 0;
			for (Object event : orderCopy) {
				debugLogger.info("[" + runCount + "] EVENT[" + count++ + "] E["
						+ event + "] STAMP[" + timeStamps.get(event) + "] A["
						+ receivedEvents.get(event) + "]");
			}

			debugLogger.info("[" + runCount
					+ "] ================================================");

			runCount++;
		}

	}

	private void initDebug() {
		debugTimer.scheduleAtFixedRate(new EventTimerTask(), 5000, 5000);
	}

	private void tearDownDebug() {
		debugTimer.cancel();
	}

	private class DTERemoveTask extends TimerTask {

		private DialogWrapper dw = null;
		private int eventID = -1;
		private ComponentKey key = null;
		private int runCount = 0;
		private int runLimit = 10;

		public DTERemoveTask( DialogWrapper dw,
				int eventID, ComponentKey key) {
			super();
			
			this.dw = dw;
			this.eventID = eventID;
			this.key = key;
			
		}


		public DialogWrapper getDw() {
			return dw;
		}

		public int getEventID() {
			return eventID;
		}

		public ComponentKey getKey() {
			return key;
		}

		public void run() {

			if (dw.isInStateEventFireSequence() && runCount < runLimit) {
				runCount++;
				DTERemoveTask drt=new DTERemoveTask(this.dw,this.eventID,this.key);
				drt.runCount=this.runCount;
				timer.schedule(drt, 150);
				return;
			}

			SipToSLEEUtility.displayDeliveryMessage("DTERemoveRunnable",
					eventID, key, dw.getActivityHandle().transactionId);
			// fire event
			try {
				DialogTerminatedEventWrapper event = new DialogTerminatedEventWrapper(
						sipFactoryProvider.getSipProvider(), dw);

				sleeEndpoint.fireEvent(dw.getActivityHandle(), event, eventID,
						new Address(AddressPlan.SIP, dw.getLocalParty()
								.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}

			sendActivityEndEvent(dw);

			

		}
	}



}
