package org.mobicents.slee.resource.sip;

import gov.nist.javax.sip.header.CSeq;
import gov.nist.javax.sip.header.CallID;
import gov.nist.javax.sip.header.Via;

import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
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
import java.util.logging.Level;
import java.util.logging.Logger;

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
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.FactoryException;
import javax.slee.InvalidStateException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.FacilityException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ActivityIsEndingException;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.FailureReason;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.resource.ResourceException;
import javax.slee.resource.SleeEndpoint;

import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.resource.ResourceAdaptorState;
import org.mobicents.slee.resource.sip.wrappers.ClientTransactionWrapper;
import org.mobicents.slee.resource.sip.wrappers.DialogWrapper;
import org.mobicents.slee.resource.sip.wrappers.RequestEventWrapper;
import org.mobicents.slee.resource.sip.wrappers.ResponseEventWrapper;
import org.mobicents.slee.resource.sip.wrappers.ServerTransactionWrapper;
import org.mobicents.slee.resource.sip.wrappers.TimeoutEventWrapper;
import org.mobicents.slee.resource.sip.wrappers.TransactionTerminatedEventWrapper;
import org.mobicents.slee.resource.sip.wrappers.WrapperSuperInterface;

import org.mobicents.slee.runtime.ActivityContext;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

public class SipResourceAdaptor implements SipListener, ResourceAdaptor,
		Serializable {

	static private transient Logger log;

	static {
		log = Logger.getLogger(SipResourceAdaptor.class.getCanonicalName());
	}

	// SIP Props -------------------------------------------

	private static final String SIP_BIND_ADDRESS = "javax.sip.IP_ADDRESS";

	private static final String SIP_PORT_BIND = "javax.sip.PORT";

	private static final String TRANSPORTS_BIND = "javax.sip.TRANSPORT";

	private static final String STACK_NAME_BIND = "javax.sip.STACK_NAME";

	private int port = 5060;

	// private String transport = "udp";
	private Set<String> transports = new HashSet<String>();

	private Set<String> allowedTransports = new HashSet<String>();

	private String stackAddress = "0.0.0.0";

	private String stackPrefix = "gov.nist";

	private transient SipProvider provider;

	private transient SleeSipProviderImpl providerProxy = null;
	// SIP Methods related -------------------------------------

	// METHODS - here we store methods which are rfc 3261 compilant
	private static Set rfc3261Methods = new HashSet();
	private static Set<String> stxedRequests=new HashSet<String>();
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
		
		stxedRequests.add(Request.ACK);
		stxedRequests.add(Request.CANCEL);
		stxedRequests.add(Request.BYE);
	}

	// SLEE Related Props --------------------------------------

	// Activity related ====================
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

	private transient SipStack sipStack = null;

	private transient SipFactory sipFactory = null;

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

	private Properties loadProperties(BootstrapContext bootstrapContext) {
		Properties props = new Properties();

		// Load default values
		try {
			props.load(getClass().getResourceAsStream("sipra.properties"));
			if (log.isLoggable(Level.FINER)) {
				log.finer("Loading default SIP RA properties: " + props);
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

		if (log.isLoggable(Level.FINER)) {
			log.finer("Configuring RA" + properties);
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
			if (log.isLoggable(Level.FINER)) {
				Iterator it = properties.entrySet().iterator();
				while (it.hasNext()) {
					Entry en = (Entry) it.next();
					if (log.isLoggable(Level.FINER)) {
						log.finer("---[SetHotProps][" + this.entityName + "] "
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
							log.severe(" TRANSPORT[" + tmp[i]
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

		// Try to bind to the specified port. It could be the case that the
		// forwarder is running on this port.

		int i = 0;
		for (i = 0; i < 10; i++) {

			InetSocketAddress sockAddress = new InetSocketAddress(stackAddress,
					this.port);
			try {
				log.info("Trying to bind to " + sockAddress);
				DatagramSocket socket = new DatagramSocket(sockAddress);

				this.properties.setProperty("javax.sip.PORT", Integer.valueOf(
						this.port).toString());
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

		state = ResourceAdaptorState.CONFIGURED;

	}

	public void start() throws ResourceException {

		try {

			initializeNamingContext();

			// this.mux = this.registerMultiplexer();

			initializeStack();

			activities = new ConcurrentHashMap();

			state = ResourceAdaptorState.ACTIVE;

			boolean created = false;
			log.severe("---> START " + Arrays.toString(transports.toArray()));

			for (Iterator it = transports.iterator(); it.hasNext();) {
				String trans = (String) it.next();
				ListeningPoint lp = this.sipStack.createListeningPoint(
						this.stackAddress, this.port, trans);

				if (!created) {
					this.provider = this.sipStack.createSipProvider(lp);
					// this.provider
					// .setAutomaticDialogSupportEnabled(automaticDialogSupport);
					created = true;
				} else
					this.provider.addListeningPoint(lp);

				try {
					log.severe("ADDING SLEF AS LISTENER");
					this.provider.addSipListener(this);
				} catch (Exception ex) {
					log.severe("Unexpected exception \n"
							+ SipToSLEEUtility.doMessage(ex));
					throw new ResourceException(
							"SIP RA failed to register as SipListener");
				}

			}

			// LETS CREATE FP
			// SipFactory sipFactory = SipFactory.getInstance();
			AddressFactory addressFactory = sipFactory.createAddressFactory();
			HeaderFactory headerFactory = sipFactory.createHeaderFactory();
			MessageFactory messageFactory = sipFactory.createMessageFactory();

			this.providerProxy = new SleeSipProviderImpl(addressFactory,
					headerFactory, messageFactory, sipStack, this, provider,
					this.tm);

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
			if (log.isLoggable(Level.FINER)) {
				log.finer("Keys for this RA: "
						+ myComponentKeys.keySet().toString());
			}

			// initfiner();
		} catch (Exception ex) {
			log.severe("error in initializing resource adaptor\n"
					+ SipToSLEEUtility.doMessage(ex));
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
		// activityContextFactory =
		// serviceContainer.getActivityContextFactory();
		tm = serviceContainer.getTransactionManager();
		ResourceAdaptorEntity resourceAdaptorEntity = ((ResourceAdaptorEntity) container
				.getResourceAdaptorEnitity(this.bootstrapContext
						.getEntityName()));
		ResourceAdaptorTypeID raTypeId = resourceAdaptorEntity
				.getInstalledResourceAdaptor().getRaType()
				.getResourceAdaptorTypeID();

		this.acif = new SipActivityContextInterfaceFactoryImpl(
				resourceAdaptorEntity.getServiceContainer(),
				this.bootstrapContext.getEntityName(), this,
				resourceAdaptorEntity.getServiceContainer()
						.getTransactionManager());

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
				if (log.isLoggable(Level.FINER)) {
					log.finer("jndiName prefix =" + prefix + "; jndiName = "
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
							.severe("Object in use -- retrying to delete listening point");
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
			log.severe("Cannot unbind naming context");
		}

		this.state = ResourceAdaptorState.UNCONFIGURED;

		if (log.isLoggable(Level.FINER)) {
			log.finer("Sip Resource Adaptor stopped.");
		}
	}

	// XXX -- Lifecycle/creatation methods in order of first call

	public void entityCreated(BootstrapContext bootstrapContext)
			throws ResourceException {
		if (log.isLoggable(Level.FINER)) {
			log.finer("SipResourceAdaptor: init()");
		}
		this.bootstrapContext = bootstrapContext;
		this.sleeEndpoint = bootstrapContext.getSleeEndpoint();
		this.eventLookup = bootstrapContext.getEventLookupFacility();

		// properties = new Properties();
		this.state = ResourceAdaptorState.UNCONFIGURED;

	}

	public void entityActivated() throws javax.slee.resource.ResourceException {
		try {
			log.severe("entityActivated1");
			this.configure(this.provisionedProperties);
			this.start();
			log.severe("entityActivated2");
			initDebug();
		} catch (ResourceException e) {
			e.printStackTrace();
			throw new javax.slee.resource.ResourceException(
					"Failed to Activate Resource Adaptor!", e);
		} catch (InvalidStateException e) {
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
		tearDownDebug();
	}

	public void entityRemoved() {

	}

	public void activityEnded(ActivityHandle handle) {

		if (log.isLoggable(Level.FINE)) {
			log.fine("Removing activity for handle[" + handle + "] activity["
					+ this.activities.get(handle) + "].");
		}
		synchronized (activities) {
			this.activities.remove(handle);
		}

	}

	public void activityUnreferenced(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	public void eventProcessingFailed(ActivityHandle arg0, Object arg1,
			int arg2, Address arg3, int arg4, FailureReason arg5) {
		// TODO Auto-generated method stub

	}

	public void eventProcessingSuccessful(ActivityHandle arg0, Object arg1,
			int arg2, Address arg3, int arg4) {
		// TODO Auto-generated method stub

	}

	public Object getActivity(ActivityHandle arg0) {
		if (arg0 instanceof SipActivityHandle) {
			return this.activities.get(arg0);
		} else {
			return null;
		}
	}

	public ActivityHandle getActivityHandle(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Marshaler getMarshaler() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getSBBResourceAdaptorInterface(String arg0) {

		return providerProxy;
	}

	public void queryLiveness(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	// XXX -- SERVICE PART + FITLERING

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

	public void serviceActivated(String arg0) {
		// TODO Auto-generated method stub

	}

	public void serviceDeactivated(String arg0) {
		// TODO Auto-generated method stub

	}

	public void serviceInstalled(String arg0, int[] arg1, String[] arg2) {
		// TODO Auto-generated method stub

	}

	public void serviceUninstalled(String arg0) {
		// TODO Auto-generated method stub

	}

	// XXX -- SipListenerMethods - here we process incoming data

	public void processIOException(IOExceptionEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void processRequest(RequestEvent req) {

		SipToSLEEUtility.displayMessage(log, "Received Request",
				"SipResourceAdaptor.processRequest", req.getRequest(),
				Level.FINEST);

		if (req.getRequest().getHeader(MaxForwardsHeader.NAME) == null) {
			try {
				MaxForwardsHeader mfh = this.providerProxy.getHeaderFactory()
						.createMaxForwardsHeader(69);
				req.getRequest().addHeader(mfh);
			} catch (InvalidArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		ServerTransaction st = null;

		st = req.getServerTransaction();
		ServerTransactionWrapper STW = null;
		//CANCEL, ACK and BYE have Transaction created for them
		
		log.info("GOT R[" + req.getRequest().getMethod() + "] ST[" + st + "]");
		//if (st == null || ((stxedRequests.contains(st.getRequest().getMethod()))
		//		&& st.getApplicationData() == null)) {
		if (st == null || 
				 st.getApplicationData() == null) {
			try {
				STW = (ServerTransactionWrapper) this.providerProxy
						.getNewServerTransaction(req.getRequest(), st, true);
				st = (ServerTransaction) STW.getWrappedObject();
				if (log.isLoggable(Level.FINER)) {
					log
							.finer("\n----------------- CREATED NEW STx ---------------------\nBRANCH: "
									+ st.getBranchId()
									+ "\n-------------------------------------------------------");
				}

			} catch (Exception e) {
				// e.printStackTrace();
				log
						.severe("\n-------------------------\nREQUEST:\n-------------------------\n"
								+ req.getRequest()
								+ "\n-------------------------");
				e.printStackTrace();
				return;
			}
		} else {
			// TODO:add error send response on type cast exctption

			STW = (ServerTransactionWrapper) st.getApplicationData();
		}
		// WE HAVE SET UP ALL WHAT WE NEED, NOW DO SOMETHING
		ComponentKey eventKey = null;
		SipActivityHandle SAH = null;

		DialogWrapper DW = null;
		boolean inDialog = false;

		if (st.getDialog() != null) {

			// TODO: add check for fork?

			Dialog d = st.getDialog();

			if (d.getApplicationData() != null
					&& d.getApplicationData() instanceof DialogActivity) {
				DW = (DialogWrapper) d.getApplicationData();
				inDialog = true;
				SAH = DW.getActivityHandle();
			} else {
				SipToSLEEUtility
						.displayMessage(
								log,
								"Failed - dialog exists, but no wrapper is present. Delivering on TX!!!",
								"SipResourceAdaptor.processRequest", "",
								Level.SEVERE);
				inDialog = false;
				SAH = STW.getActivityHandle();
			}

		} else {
			// This means that ST is activity

			SAH = STW.getActivityHandle();

		}

		RequestEventWrapper REW = new RequestEventWrapper(this.providerProxy,
				STW, DW, req.getRequest());

		eventKey = SipToSLEEUtility.generateEventKey(REW, inDialog);
		int eventID = this.generateEventID(eventKey);

		if (eventID < 0) {

			SipToSLEEUtility.displayMessage(log,
					this.getClass().getName() + "",
					"Couldnt deliver event as id is less than zero", eventKey,
					Level.SEVERE);

		} else {

			SipToSLEEUtility.displayDeliveryMessage(log, this.getClass()
					.getName()
					+ "", eventID, eventKey, SAH, Level.INFO);

			
			try{
				this.tm.begin();
				log.info("--- ATTACHED"+((SipActivityContextInterfaceFactoryImpl)acif).getActivityContextForActivity(this.activities.get(SAH)).getSbbAttachmentSetForDebug());
				this.tm.commit();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
			try {
				
				this.sleeEndpoint.fireEvent(SAH, REW, eventID, null);
				// TODO: Add INTERNAL_ERROR response to error
			} catch (ActivityIsEndingException e) {

				e.printStackTrace();
			} catch (NullPointerException e) {

				e.printStackTrace();
			} catch (IllegalArgumentException e) {

				e.printStackTrace();
			} catch (IllegalStateException e) {

				e.printStackTrace();
			} catch (UnrecognizedActivityException e) {

				e.printStackTrace();
			}
		}

	}

	public void processResponse(ResponseEvent resp) {
		ClientTransaction ct = resp.getClientTransaction();
		if (ct == null) {
			if (log.isLoggable(Level.FINE)) {
				log
						.fine("===> CT is NULL - RTR ? CALLID["
								+ ((CallID) resp.getResponse().getHeader(
										CallID.NAME)).getCallId()
								+ "] BRANCH["
								+ ((Via) resp.getResponse()
										.getHeaders(Via.NAME).next())
										.getBranch()
								+ "] METHOD["
								+ ((CSeq) resp.getResponse().getHeader(
										CSeq.NAME)).getMethod() + "] CODE["
								+ resp.getResponse().getStatusCode() + "]");
			}
			return;
		}

		if (ct.getApplicationData() == null
				|| !(ct.getApplicationData() instanceof ClientTransactionWrapper)) {
			// ERROR?
			SipToSLEEUtility.displayMessage(log,
					"Received wrong AppData in ClientTransaction",
					"SipResourceAdaptor.processResponse", "Received app data["
							+ ct.getApplicationData()
							+ "] - should be instance of wrapper class!!",
					Level.SEVERE);
			// TODO: Send SERVER_ERROR ?
			return;
		}

		// WE HAVE SET UP ALL WHAT WE NEED, NOW DO SOMETHING
		ComponentKey eventKey = null;
		SipActivityHandle SAH = null;
		ClientTransactionWrapper CTW = (ClientTransactionWrapper) ct
				.getApplicationData();
		DialogWrapper DW = null;
		boolean inDialog = false;

		if (ct.getDialog() != null) {
			// TODO: add check for fork?

			Dialog d = ct.getDialog();

			if (d.getApplicationData() != null
					&& d.getApplicationData() instanceof DialogActivity) {
				DW = (DialogWrapper) d.getApplicationData();
				SAH = DW.getActivityHandle();
				inDialog = true;
			} else {
				SipToSLEEUtility
						.displayMessage(
								log,
								"Failed - dialog exists, but no wrapper is present. Delivering on TX!!!",
								"SipResourceAdaptor.processRequest", "",
								Level.SEVERE);
				inDialog = false;
				SAH = CTW.getActivityHandle();

			}

		} else {

			SAH = CTW.getActivityHandle();

		}

		ResponseEventWrapper REW = new ResponseEventWrapper(this.providerProxy,
				CTW, DW, resp.getResponse());
		eventKey = SipToSLEEUtility.generateEventKey(REW, inDialog);
		int eventID = this.generateEventID(eventKey);
		try {
			this.tm.begin();
			ActivityContext ac = ((SipActivityContextInterfaceFactoryImpl) acif)
					.getActivityContextForActivity(this.getActivity(SAH));
			log.info("=-=-=-=-=KEY[" + eventKey + "] SAH[" + SAH + "]  AC["
					+ ac.getSortedCopyOfSbbAttachmentSet() + "] A["
					+ this.getActivity(SAH) + "] ");
			this.tm.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (eventID < 0) {

			SipToSLEEUtility.displayMessage(log,
					this.getClass().getName() + "",
					"Couldnt deliver event as id is less than zero", eventKey,
					Level.SEVERE);

		} else {

			SipToSLEEUtility.displayDeliveryMessage(log, this.getClass()
					.getName()
					+ "", eventID, eventKey, SAH, Level.FINER);

			try {
				this.sleeEndpoint.fireEvent(SAH, REW, eventID, null);
				// TODO: Add INTERNAL_ERROR response to error
			} catch (ActivityIsEndingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnrecognizedActivityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void processTimeout(TimeoutEvent arg0) {

		Transaction t = null;
		boolean inDialog = false;
		SipActivityHandle handle = null;
		ComponentKey eventKey = null;
		SipToSLEEUtility.displayMessage(log, "processTimeout", this.getClass()
				.getName()
				+ "", "Processing Timeout for C[" + arg0.getClientTransaction()
				+ "] S[" + arg0.getServerTransaction() + "]", Level.FINE);

		TimeoutEventWrapper tew = null;
		if (arg0.getClientTransaction() != null) {
			t = arg0.getClientTransaction();

			if (t.getApplicationData() == null
					|| !(t.getApplicationData() instanceof ClientTransactionWrapper)) {
				SipToSLEEUtility.displayMessage(log,
						"FAILURE on processTimeout - CTX",
						"SipResourceAdaptor.processTimeout", "Wrong app data["
								+ t.getApplicationData() + "]", Level.SEVERE);
				return;
			} else {
				tew = new TimeoutEventWrapper(this.providerProxy,
						(ClientTransaction) t.getApplicationData(), arg0
								.getTimeout());
				ClientTransactionWrapper ctw = (ClientTransactionWrapper) t
						.getApplicationData();
				if (ctw.getDialog() != null
						&& ctw.getDialog() instanceof DialogWrapper) {
					inDialog = true;
					handle = ((DialogWrapper) ctw.getDialog())
							.getActivityHandle();

				} else {
					handle = ctw.getActivityHandle();
				}

				eventKey = SipToSLEEUtility.generateEventKey(tew, inDialog);
				String result = fireEvent(tew, handle, eventKey);
				if (result != null) {
					SipToSLEEUtility.displayMessage(log,
							"Error on sending message",
							"SipResourceAdaptor.processTimeout", result,
							Level.SEVERE);
				}
			}

		} else {
			t = arg0.getServerTransaction();
		}

		sendActivityEndEvent((Transaction) t.getApplicationData());

	}

	public void processTransactionTerminated(
			TransactionTerminatedEvent txTerminatedEvent) {

		
		try {
			Thread.currentThread().sleep(2500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Transaction t = null;

		// TransactionTerminatedEventWrapper TTEW = null;

		if (txTerminatedEvent.getClientTransaction() != null) {
			t = txTerminatedEvent.getClientTransaction();
		} else {
			t = txTerminatedEvent.getServerTransaction();
		}
		if (log.isLoggable(Level.FINE)) {

			log.fine("TransactionTerminatedEvent received ["
					+ txTerminatedEvent + "]["
					+ txTerminatedEvent.getClientTransaction() + "]["
					+ txTerminatedEvent.getServerTransaction() + "]["
					+ txTerminatedEvent.getSource() + "]");
		}

		// if (txTerminatedEvent.isServerTransaction()) {

		// SipToSLEEUtility.displayMessage(log,
		// "processTransactionTerminated", "SipResourceAdaptor",
		// "Skipping processing, received STX, this doesnt have TTE ["
		// + t + "][" + t.getApplicationData() + "]",
		// Level.FINE);
		// t = txTerminatedEvent.getServerTransaction();
		// Object activity=this.activities.remove(ah);
		// terminatedActivities.put(ah,activity);
		// TTEW = new TransactionTerminatedEventWrapper(this.providerProxy,
		// (ServerTransaction) t);

		// } else {
		// SipToSLEEUtility.displayMessage(log,
		// "processTransactionTerminated", "SipResourceAdaptor",
		// "Creating TTE for CTX. [" + t + "]["
		// + t.getApplicationData() + "]", Level.FINE);
		// t = txTerminatedEvent.getClientTransaction();

		// Object activity=this.activities.remove(ah);
		// terminatedActivities.put(ah,activity);
		// TTEW = new TransactionTerminatedEventWrapper(this.providerProxy,
		// (ClientTransaction) t);
		// }

		if (t.getApplicationData() == null
				|| !(t.getApplicationData() instanceof WrapperSuperInterface)) {
			if (log.isLoggable(Level.FINE)) {

				log
						.fine("TransactionTerminatedEvent droping due to wrong app data ["
								+ t.getApplicationData()
								+ "]["
								+ txTerminatedEvent + "]");
				// FIXME : This could cause a leaky
				return;
			}

		} else if (t.getApplicationData() != null
				&& (t.getApplicationData() instanceof WrapperSuperInterface)) {

			// TODO: add event fire and filter
			if (!this.activities.containsValue(t.getApplicationData())) {
				// wrong, this is not an activity, we do nothing
				if (log.isLoggable(Level.FINE)) {

					log
							.fine("TransactionTerminatedEvent droping due to transction not present as activity ["
									+ t.getApplicationData()
									+ "]["
									+ txTerminatedEvent + "]");
					return;
				}
			}
			// LETS END AO LIFE
			// XXX: here is small danger, but all wrapper kept in t.setApData
			// are implementations of this interface
			this.sendActivityEndEvent((Transaction) t.getApplicationData());

		} else {
			SipToSLEEUtility.displayMessage(log,
					"processTransactionTerminated", "SipResourceAdaptor",
					"Some error occuer, Im not sure of its nature!!!",
					Level.FINE);
		}

	}

	public void processDialogTerminated(DialogTerminatedEvent dte) {
		// TODO Auto-generated method stub
		Dialog d = dte.getDialog();
		if (d.getApplicationData() == null
				|| !(d.getApplicationData() instanceof DialogActivity)) {
			if (log.isLoggable(Level.FINE)) {

				log
						.fine("DialogTerminatedEvent droping due to wrong app data ["
								+ d.getApplicationData() + "][" + dte + "]");
				return;
			}

		} else if (d.getApplicationData() != null
				&& (d.getApplicationData() instanceof DialogActivity)) {

			// TODO: add event fire and filter
			if (!this.activities.containsValue(d.getApplicationData())) {
				// wrong, this is not an activity, we do nothing
				if (log.isLoggable(Level.FINE)) {

					log
							.fine("DialogTerminatedEvent droping due to dialog activity not present as activity ["
									+ d.getApplicationData() + "][" + dte + "]");
					return;
				}
			}
			// LETS END AO LIFE
			// XXX: here is small danger, but all wrapper kept in t.setApData
			// are implementations of this interface
			this.sendActivityEndEvent((DialogActivity) d.getApplicationData());

		} else {
			// this wont happen :}
		}
	}

	// *************** Event Life cycle

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
		// 
		WrapperSuperInterface wsi = (WrapperSuperInterface) sipT;
		ah = wsi.getActivityHandle();
		wsi.clearAssociations();
		// lets help GC
		((javax.sip.Transaction) wsi.getWrappedObject())
				.setApplicationData(null);
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
		WrapperSuperInterface wsi = (WrapperSuperInterface) dial;
		ah = wsi.getActivityHandle();

		wsi.clearAssociations();
		((javax.sip.Dialog) wsi.getWrappedObject()).setApplicationData(null);
		try {
			this.sleeEndpoint.activityEnding(ah);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// **************** PROVISIONING

	public void setIp(String ip) {

		if (ip.equals("null"))
			this.stackAddress = System.getProperty("jboss.bind.address");
		else
			stackAddress = ip;// TODO regex cehck ?

		provisionedProperties.put(this.SIP_BIND_ADDRESS, stackAddress);

	}

	public void setPort(Integer port) {
		if (port.intValue() < 1024)
			this.port = 5060;
		else
			this.port = port.intValue();
		provisionedProperties.put(this.SIP_PORT_BIND, "" + this.port);
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
						log.severe(" TRANSPORT[" + tmp[i]
								+ "] IS NOT A VALID TRANSPORT!!!");
					}
				}
			}
		}
		provisionedProperties.put(this.TRANSPORTS_BIND, transportsToSet);
	}

	public void addActivity(SipActivityHandle sah,
			WrapperSuperInterface wrapperActivity) {
		log.info("ADDING[" + sah + "][" + wrapperActivity + "]");
		synchronized (this.activities) {
			this.activities.put(sah, wrapperActivity);
		}
	}

	public void removeActivity(SipActivityHandle activityHandle) {

		synchronized (this.activities) {
			this.activities.remove(activityHandle);
		}
	}

	public SleeEndpoint getSleeEndpoint() {
		return this.sleeEndpoint;
	}

	// ------- END OF PROVISIONING

	protected int generateEventID(ComponentKey key) {
		try {
			return this.eventLookup.getEventID(key.getName(), key.getVendor(),
					key.getVersion());
		} catch (FacilityException e) {

			e.printStackTrace();
		} catch (NullPointerException e) {

			e.printStackTrace();
		} catch (UnrecognizedEventException e) {

			e.printStackTrace();
		}
		return -1;
	}

	// *********************** DEBUG PART]
	private Timer debugTimer = new Timer();

	private org.apache.log4j.Logger debugLogger = org.apache.log4j.Logger
			.getLogger("org.mobicents.slee.resource.sip.DEBUG");

	private HashMap receivedEvents = new HashMap();
	// private ArrayList orderOfEvent = new ArrayList();
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
			// debugLogger.info("[" + runCount + "] --- EVENTS RECEVIED");
			// ArrayList orderCopy = new ArrayList(orderOfEvent);
			// count = 0;
			// for (Object event : orderCopy) {
			// debugLogger.info("[" + runCount + "] EVENT[" + count++ + "] E["
			// + event + "] STAMP[" + timeStamps.get(event) + "] A["
			// + receivedEvents.get(event) + "]");
			// }

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

	private String fireEvent(Object event, ActivityHandle handle,
			ComponentKey eventKey) {

		int eventID = this.generateEventID(eventKey);

		log.info("=-=-=-=-=KEY[" + eventKey + "] SAH[" + handle + "] EVENT");
		if (eventID < 0) {

			return "Event id for " + eventID
					+ " is less than zero, cant fire!!!";

		} else {

			SipToSLEEUtility.displayDeliveryMessage(log, this.getClass()
					.getName()
					+ "", eventID, eventKey, handle, Level.FINER);

			try {
				this.sleeEndpoint.fireEvent(handle, event, eventID, null);
				// TODO: Add INTERNAL_ERROR response to error
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return SipToSLEEUtility.doMessage(e);
			}

		}
		return null;

	}
}
