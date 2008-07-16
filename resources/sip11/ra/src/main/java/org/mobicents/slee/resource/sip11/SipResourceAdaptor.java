package org.mobicents.slee.resource.sip11;

import gov.nist.javax.sip.header.CSeq;
import gov.nist.javax.sip.header.CallID;
import gov.nist.javax.sip.header.Via;

import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
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
import javax.sip.TransactionState;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.address.AddressFactory;
import javax.sip.header.CSeqHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityEndEvent;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.InvalidStateException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.FailureReason;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.resource.ResourceException;
import javax.slee.resource.SleeEndpoint;

import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.resource.ResourceAdaptorState;
import org.mobicents.slee.resource.sip11.wrappers.ACKDummyTransaction;
import org.mobicents.slee.resource.sip11.wrappers.ClientTransactionWrapper;
import org.mobicents.slee.resource.sip11.wrappers.DialogWrapper;
import org.mobicents.slee.resource.sip11.wrappers.RequestEventWrapper;
import org.mobicents.slee.resource.sip11.wrappers.ResponseEventWrapper;
import org.mobicents.slee.resource.sip11.wrappers.ServerTransactionWrapper;
import org.mobicents.slee.resource.sip11.wrappers.TimeoutEventWrapper;
import org.mobicents.slee.resource.sip11.wrappers.TransactionTerminatedEventWrapper;
import org.mobicents.slee.resource.sip11.wrappers.WrapperSuperInterface;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

public class SipResourceAdaptor implements SipListener, ResourceAdaptor, Serializable {

	static private transient Logger log = Logger.getLogger(SipResourceAdaptor.class);

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

	private transient SipProvider provider;

	private transient SleeSipProviderImpl providerProxy = null;
	// SIP Methods related -------------------------------------

	// METHODS - here we store methods which are rfc 3261 compilant
	private static Set rfc3261Methods = new HashSet();
	// Keeps methods for which jsip stack creates transactions by itself
	private static Set<String> stxedRequests = new HashSet<String>();
	// SOME INITIALIZATION
	static {
		String[] tmp = { Request.ACK, Request.BYE, Request.CANCEL, Request.INFO, Request.INVITE, Request.MESSAGE,
				Request.NOTIFY, Request.OPTIONS, Request.PRACK, Request.PUBLISH, Request.REFER, Request.REGISTER,
				Request.SUBSCRIBE, Request.UPDATE };
		for (int i = 0; i < tmp.length; i++)
			rfc3261Methods.add(tmp[i]);

		log.info("\n================SIP METHODS====================\n" + rfc3261Methods
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

	/**
	 * caches the eventIDs, avoiding lookup in container
	 */
	private transient static final EventIDCache eventIdCache = new EventIDCache();

	/**
	 * tells the RA if an event with a specified ID should be filtered or not
	 */
	private transient static final EventIDFilter eventIDFilter = new EventIDFilter();

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
			throw new InvalidStateException("Cannot configure RA wrong state: " + this.state);
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
					log.debug("---[SetHotProps][" + this.entityName + "] " + en.getKey() + " [*] " + en.getValue());
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
							log.error(" TRANSPORT[" + tmp[i] + "] IS NOT A VALID TRANSPORT!!!");
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
			confValue = "SipResourceAdaptorStack_" + this.stackAddress + "_" + this.port;
		} else {
			confValue += "_" + this.stackAddress + "_" + this.port;
		}
		this.properties.put(STACK_NAME_BIND, confValue);
		confValue = null;

		// Try to bind to the specified port. It could be the case that the
		// forwarder is running on this port.

		int i = 0;
		for (i = 0; i < 10; i++) {

			InetSocketAddress sockAddress = new InetSocketAddress(stackAddress, this.port);
			try {
				log.info("Trying to bind to " + sockAddress);
				DatagramSocket socket = new DatagramSocket(sockAddress);

				this.properties.setProperty("javax.sip.PORT", Integer.valueOf(this.port).toString());
				socket.close();
				break;
			} catch (Exception ex) {

				this.port += 10;
			}
		}
		if (i == 10)
			throw new RuntimeException("Cannot create SIP Resource adaptor - no port available to bind to ");

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

			if (log.isDebugEnabled()) {
				log.debug("---> START " + Arrays.toString(transports.toArray()));
			}

			for (Iterator it = transports.iterator(); it.hasNext();) {
				String trans = (String) it.next();
				ListeningPoint lp = this.sipStack.createListeningPoint(this.stackAddress, this.port, trans);

				if (!created) {
					this.provider = this.sipStack.createSipProvider(lp);
					// this.provider
					// .setAutomaticDialogSupportEnabled(automaticDialogSupport);
					created = true;
				} else
					this.provider.addListeningPoint(lp);

				try {
					this.provider.addSipListener(this);
				} catch (Exception ex) {
					String msg = "SIP RA failed to register as SipListener";
					log.error(msg, ex);
					throw new ResourceException(msg);
				}
			}

			// LETS CREATE FP
			// SipFactory sipFactory = SipFactory.getInstance();
			AddressFactory addressFactory = sipFactory.createAddressFactory();
			HeaderFactory headerFactory = sipFactory.createHeaderFactory();
			MessageFactory messageFactory = sipFactory.createMessageFactory();

			this.providerProxy = new SleeSipProviderImpl(addressFactory, headerFactory, messageFactory, sipStack, this,
					provider, this.tm);

			// initfiner();
		} catch (Exception ex) {
			String msg = "error in initializing resource adaptor";
			log.error(msg, ex);
			throw new ResourceException(msg);
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
			throw new ResourceException("Cannot modify configuration properties wrong state: " + state);
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
				.getResourceAdaptorEnitity(this.bootstrapContext.getEntityName()));
		ResourceAdaptorTypeID raTypeId = resourceAdaptorEntity.getInstalledResourceAdaptor().getRaType()
				.getResourceAdaptorTypeID();

		this.acif = new SipActivityContextInterfaceFactoryImpl(resourceAdaptorEntity.getServiceContainer(),
				this.bootstrapContext.getEntityName(), this, resourceAdaptorEntity.getServiceContainer()
						.getTransactionManager());

		resourceAdaptorEntity.getServiceContainer().getActivityContextInterfaceFactories().put(raTypeId, this.acif);
		entityName = this.bootstrapContext.getEntityName();

		try {
			if (this.acif != null) {
				// parse the string = java:slee/resource/SipRA/sipacif
				String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) this.acif).getJndiName();
				int begind = jndiName.indexOf(':');
				int toind = jndiName.lastIndexOf('/');
				String prefix = jndiName.substring(begind + 1, toind);
				String name = jndiName.substring(toind + 1);
				if (log.isDebugEnabled()) {
					log.debug("jndiName prefix =" + prefix + "; jndiName = " + name);
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
				String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) this.acif).getJndiName();
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
					log.error("Object in use -- retrying to delete listening point", ex);
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
			log.error("Cannot unbind naming context", e);
		}

		this.state = ResourceAdaptorState.UNCONFIGURED;

		if (log.isDebugEnabled()) {
			log.debug("Sip Resource Adaptor stopped.");
		}
	}

	// XXX -- Lifecycle/creatation methods in order of first call

	public void entityCreated(BootstrapContext bootstrapContext) throws ResourceException {
		if (log.isDebugEnabled()) {
			log.debug("SipResourceAdaptor: init()");
		}
		this.bootstrapContext = bootstrapContext;
		this.sleeEndpoint = bootstrapContext.getSleeEndpoint();
		this.eventLookup = bootstrapContext.getEventLookupFacility();

		// properties = new Properties();
		this.state = ResourceAdaptorState.UNCONFIGURED;

	}

	public void entityActivated() throws javax.slee.resource.ResourceException {
		try {
			this.configure(this.provisionedProperties);
			this.start();
			//initDebug();
		} catch (ResourceException e) {
			e.printStackTrace();
			throw new javax.slee.resource.ResourceException("Failed to Activate Resource Adaptor!", e);
		} catch (InvalidStateException e) {
			e.printStackTrace();
			throw new javax.slee.resource.ResourceException("Failed to Activate Resource Adaptor!", e);
		}
	}

	public void entityDeactivating() {

		this.stopping();
	}

	public void entityDeactivated() {

		this.stop();
		//tearDownDebug();
	}

	public void entityRemoved() {

	}

	public void activityEnded(ActivityHandle handle) {

		if (log.isDebugEnabled()) {
			log.debug("Removing activity for handle[" + handle + "] activity[" + this.activities.get(handle) + "].");
		}

		WrapperSuperInterface activity = (WrapperSuperInterface) this.removeActivity((SipActivityHandle)handle);
		if (activity != null) {
			activity.cleanup();
		}
	}

	public void activityUnreferenced(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	public void eventProcessingFailed(ActivityHandle ah, Object event, int arg2, Address arg3, int arg4,
			FailureReason arg5) {

		String id = ((SipActivityHandle) ah).getID();

		if (id.endsWith(Request.ACK) && !(event instanceof ActivityEndEvent)&& !(event instanceof TransactionTerminatedEvent)) {
			try {
				ServerTransactionWrapper stw = (ServerTransactionWrapper) activities.get(ah);
				if (stw.getWrappedTransaction() instanceof ACKDummyTransaction) {
					// its our fake
					TransactionTerminatedEventWrapper ttew = new TransactionTerminatedEventWrapper(this.providerProxy,
							(ServerTransaction) stw.getWrappedTransaction());
					this.processTransactionTerminated(ttew);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (!id.endsWith(Request.CANCEL) || !(event instanceof RequestEventWrapper))
			return;

		// PROCESSING FAILED, WE HAVE TO SEND 481 response to CANCEL
		try {
			Response txDoesNotExistsResponse = this.providerProxy.getMessageFactory().createResponse(
					Response.CALL_OR_TRANSACTION_DOES_NOT_EXIST, ((RequestEventWrapper) event).getRequest());
			provider.sendResponse(txDoesNotExistsResponse);
		} catch (ParseException e) {

			e.printStackTrace();
		} catch (SipException e) {

			e.printStackTrace();
		}
	}

	public void eventProcessingSuccessful(ActivityHandle ah, Object arg1, int arg2, Address arg3, int arg4) {

		String id = ((SipActivityHandle) ah).getID();
		if (id.endsWith(Request.ACK) && !(arg1 instanceof ActivityEndEvent) && !(arg1 instanceof TransactionTerminatedEvent)) {
			try {
				ServerTransactionWrapper stw = (ServerTransactionWrapper) activities.get(ah);
				if (stw.getWrappedTransaction() instanceof ACKDummyTransaction) {
					// its our fake
					TransactionTerminatedEventWrapper ttew = new TransactionTerminatedEventWrapper(this.providerProxy,
							(ServerTransaction) stw.getWrappedTransaction());
					this.processTransactionTerminated(ttew);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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

	public void serviceInstalled(String serviceID, int[] eventIDs, String[] resourceOptions) {

		eventIDFilter.serviceInstalled(serviceID, eventIDs);
	}

	public void serviceUninstalled(String serviceID) {

		eventIDFilter.serviceUninstalled(serviceID);
	}

	public void serviceActivated(String serviceID) {

		eventIDFilter.serviceActivated(serviceID);
	}

	public void serviceDeactivated(String serviceID) {

		eventIDFilter.serviceDeactivated(serviceID);
	}

	// XXX -- SipListenerMethods - here we process incoming data

	public void processIOException(IOExceptionEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void processRequest(RequestEvent req) {

		if (log.isInfoEnabled()) {
			log.info("Received Request:\n" + req.getRequest());
		}

		ServerTransaction st = req.getServerTransaction();
		ServerTransactionWrapper stw = null;
		// CANCEL, ACK and BYE have Transaction created for them

		
		if (st == null || st.getApplicationData() == null) {
			try {
				stw = (ServerTransactionWrapper) this.providerProxy
						.getNewServerTransaction(req.getRequest(), st, false);
				st = (ServerTransaction) stw.getWrappedTransaction();

				if (log.isDebugEnabled()) {
					log.debug("\n----------------- CREATED NEW STx ---------------------\nBRANCH: " + st.getBranchId()
							+ "\n-------------------------------------------------------");
				}

			} catch (Exception e) {
				log.error("\n-------------------------\nREQUEST:\n-------------------------\n" + req.getRequest()
						+ "\n-------------------------", e);
				sendErrorResponse(st, req.getRequest(), Response.SERVER_INTERNAL_ERROR, e.getMessage());
				return;
			}
		} else {
			// TODO:add error send response on type cast exctption

			stw = (ServerTransactionWrapper) st.getApplicationData();
		}

		if (req.getRequest().getMethod().equals(Request.CANCEL)) {
			processCancelRequest(st, stw, req);
		} else {
			processNotCancelRequest(st, stw, req);
		}

	}

	private void processCancelRequest(ServerTransaction st, ServerTransactionWrapper STW, RequestEvent req) {
		SipActivityHandle inviteHandle = STW.getInviteHandle();
		ServerTransactionWrapper inviteSTW = (ServerTransactionWrapper) getActivity(inviteHandle);

		boolean inDialog = false;
		SipActivityHandle SAH = null;
		// FIXME: There is no mentioen about state in specs...
		if (inviteSTW != null) {
			if (log.isDebugEnabled()) {
				log.debug("Found INVITE transaction CANCEL[" + STW + "] \nINVITE[" + inviteSTW + "]");
			}

			if ((inviteSTW.getState() == TransactionState.TERMINATED)
					|| (inviteSTW.getState() == TransactionState.COMPLETED)
					|| (inviteSTW.getState() == TransactionState.CONFIRMED)) {

				log
						.error("Invite transaction has been found in state other than proceeding, final response sent, sending BAD_REQUEST");

				// FINAL
				// FINAL RESPONSE
				// HAS
				// BEEN
				// SENT?
				Response response;
				try {
					response = this.providerProxy.getMessageFactory().createResponse(Response.BAD_REQUEST,
							req.getRequest());
					STW.sendResponse(response);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SipException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return;
			}

			if (inviteSTW.getDialog() != null) {
				if (log.isDebugEnabled()) {
					log.debug("Found DIALOG transaction CANCEL[" + STW + "]\nINVITE[" + inviteSTW + "]\nDialog["
							+ inviteSTW.getDialog() + "]\nSEQUENCE:Send200ToCANCEL,FireEventOnDialog,Send487ToInvite");
				}
				SAH = ((DialogWrapper) inviteSTW.getDialog()).getActivityHandle();
				inDialog = true;

				RequestEventWrapper REW = new RequestEventWrapper(this.providerProxy, STW,
						inviteSTW != null ? inviteSTW.getDialog() : null, req.getRequest());

				int eventID = eventIdCache.getEventId(eventLookup, REW.getRequest(), inDialog);

				fireEvent(REW, SAH, eventID, new Address(AddressPlan.SIP, ((ToHeader) req.getRequest().getHeader(
						ToHeader.NAME)).getAddress().toString()));

				try {

					Response response = this.providerProxy.getMessageFactory().createResponse(Response.OK,
							req.getRequest());
					STW.sendResponse(response);

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SipException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					Response response = this.providerProxy.getMessageFactory().createResponse(
							Response.REQUEST_TERMINATED, inviteSTW.getRequest());
					inviteSTW.sendResponse(response);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SipException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				if (log.isDebugEnabled()) {
					log.debug("DIALOG not found transaction CANCEL[" + STW + "]\nINVITE[" + inviteSTW + "]\nDialog["
							+ inviteSTW.getDialog() + "]\nSEQUENCE:FireEventOnInvite");
				}
				SAH = inviteSTW.getActivityHandle();
				inDialog = false;
				RequestEventWrapper REW = new RequestEventWrapper(this.providerProxy, STW,
						inviteSTW != null ? inviteSTW.getDialog() : null, req.getRequest());
				int eventID = eventIdCache.getEventId(eventLookup, REW.getRequest(), inDialog);

				fireEvent(REW, SAH, eventID, new Address(AddressPlan.SIP, ((ToHeader) req.getRequest().getHeader(
						ToHeader.NAME)).getAddress().toString()));
			}

		} else {
			if (log.isDebugEnabled()) {
				log.debug("INVITE not found transaction CANCEL[" + STW + "]\nSEQUENCE:FireEventOnCancel");
			}
			SAH = STW.getActivityHandle();
			inDialog = false;

			RequestEventWrapper REW = new RequestEventWrapper(this.providerProxy, STW, inviteSTW != null ? inviteSTW
					.getDialog() : null, req.getRequest());
			int eventID = eventIdCache.getEventId(eventLookup, REW.getRequest(), inDialog);

			fireEvent(REW, SAH, eventID, new Address(AddressPlan.SIP, ((ToHeader) req.getRequest().getHeader(
					ToHeader.NAME)).getAddress().toString()));
		}
	}

	private void processNotCancelRequest(ServerTransaction st, ServerTransactionWrapper STW, RequestEvent req) {
		// WE HAVE SET UP ALL WHAT WE NEED, NOW DO SOMETHING

		SipActivityHandle SAH = null;

		DialogWrapper DW = null;
		boolean inDialog = false;

		if (st != null && st.getDialog() != null) {

			// TODO: add check for fork?

			Dialog d = st.getDialog();

			if (d.getApplicationData() != null && d.getApplicationData() instanceof DialogActivity) {
				DW = (DialogWrapper) d.getApplicationData();
				inDialog = true;
				SAH = DW.getActivityHandle();
				DW.addOngoingTransaction(STW);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Dialog [" + d + "] exists, but no wrapper is present. Delivering event on TX");
				}
				inDialog = false;
				SAH = STW.getActivityHandle();
			}

		} else {
			// This means that ST is activity

			SAH = STW.getActivityHandle();
		}

		RequestEventWrapper REW = new RequestEventWrapper(this.providerProxy, STW, DW, req.getRequest());

		if (!fireEvent(REW, SAH, eventIdCache.getEventId(eventLookup, REW.getRequest(), inDialog), new Address(
				AddressPlan.SIP, ((ToHeader) req.getRequest().getHeader(ToHeader.NAME)).getAddress().toString()))) {
			sendErrorResponse(req.getServerTransaction(), req.getRequest(), Response.SERVER_INTERNAL_ERROR,
					"Failed to deliver request event to JAIN SLEE container");
		}
	}

	public void processResponse(ResponseEvent resp) {

		if (log.isInfoEnabled()) {
			log.info("Received Response:\n" + resp.getResponse());
		}

		ClientTransaction ct = resp.getClientTransaction();
		if (ct == null) {
			if (log.isDebugEnabled()) {
				log.debug("===> CT is NULL - RTR ? CALLID["
						+ ((CallID) resp.getResponse().getHeader(CallID.NAME)).getCallId() + "] BRANCH["
						+ ((Via) resp.getResponse().getHeaders(Via.NAME).next()).getBranch() + "] METHOD["
						+ ((CSeq) resp.getResponse().getHeader(CSeq.NAME)).getMethod() + "] CODE["
						+ resp.getResponse().getStatusCode() + "]");
			}
			return;
		}

		final int statusCode = resp.getResponse().getStatusCode();
		final String method = ((CSeqHeader) resp.getResponse().getHeader(CSeqHeader.NAME)).getMethod();
		if (ct.getApplicationData() == null || !(ct.getApplicationData() instanceof ClientTransactionWrapper)) {
			// ERROR?
			log.error("Received app data[" + ct.getApplicationData() + "] - should be instance of wrapper class!!");
			// TODO: Send SERVER_ERROR ?
			return;
		}

		// WE HAVE SET UP ALL WHAT WE NEED, NOW DO SOMETHING
		ComponentKey eventKey = null;
		SipActivityHandle SAH = null;
		ClientTransactionWrapper CTW = (ClientTransactionWrapper) ct.getApplicationData();
		DialogWrapper DW = null;
		boolean inDialog = false;

		if (ct.getDialog() != null) {
			// TODO: add check for fork?

			Dialog d = ct.getDialog();

			if (d.getApplicationData() != null && d.getApplicationData() instanceof DialogActivity) {
				DW = (DialogWrapper) d.getApplicationData();
				SAH = DW.getActivityHandle();
				inDialog = true;
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Dialog [" + d + "] exists, but no wrapper is present. Delivering event on TX");
				}
				inDialog = false;
				SAH = CTW.getActivityHandle();

			}

		} else {

			SAH = CTW.getActivityHandle();

		}

		ResponseEventWrapper REW = new ResponseEventWrapper(this.providerProxy, CTW, DW, resp.getResponse());
		int eventID = eventIdCache.getEventId(eventLookup, REW.getResponse(), inDialog);

		fireEvent(REW, SAH, eventID, new Address(AddressPlan.SIP, ((FromHeader) resp.getResponse().getHeader(
				FromHeader.NAME)).getAddress().toString()));

		if ((statusCode == 481 || statusCode == 408) && inDialog
				&& (!method.equals(Request.INVITE) || !method.equals(Request.SUBSCRIBE))) {

			try {
				Request bye = DW.createRequest(Request.BYE);
				this.provider.sendRequest(bye);
			} catch (SipException e) {

				e.printStackTrace();
			}
		}

		if (statusCode > 299 && DW.getState() != DialogState.CONFIRMED) {
			try {

				DW.delete();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void processTimeout(TimeoutEvent arg0) {

		Transaction t = null;
		WrapperSuperInterface wsi = null;

		try {
			boolean inDialog = false;
			SipActivityHandle handle = null;
			int eventID = -1;

			if (log.isInfoEnabled()) {
				if (!arg0.isServerTransaction()) {
					log.info("Server transaction " + arg0.getServerTransaction().getBranchId() + " timer expired");
				} else {
					log.info("Client transaction " + arg0.getClientTransaction().getBranchId() + " timer expired");
				}
			}

			TimeoutEventWrapper tew = null;
			if (!arg0.isServerTransaction()) {

				t = arg0.getClientTransaction();

				if (t.getApplicationData() == null || !(t.getApplicationData() instanceof ClientTransactionWrapper)) {
					log.error("FAILURE on processTimeout - CTX. Wrong app data[" + t.getApplicationData() + "]");
					return;
				} else {
					tew = new TimeoutEventWrapper(this.providerProxy, (ClientTransaction) t.getApplicationData(), arg0
							.getTimeout());
					ClientTransactionWrapper ctw = (ClientTransactionWrapper) t.getApplicationData();
					wsi = ctw;
					if (ctw.getDialog() != null && ctw.getDialog() instanceof DialogWrapper) {
						inDialog = true;
						handle = ((DialogWrapper) ctw.getDialog()).getActivityHandle();
					} else {
						handle = ctw.getActivityHandle();
					}

					fireEvent(tew, handle, eventIdCache.getTimeoutEventId(eventLookup, inDialog), new Address(
							AddressPlan.SIP, ((FromHeader) ctw.getRequest().getHeader(FromHeader.NAME)).getAddress()
									.toString()));
				}

			} else {
				t = arg0.getServerTransaction();
				wsi = (WrapperSuperInterface) t.getApplicationData();
			}

			// we have nessesary stuff past us/ now lets see if we need to
			// terinated
			// dialog/send BYE
			// Here we now we have WRAPPERS
			boolean sendBYE = true;
			boolean sendClientResponse = true;
			String method = t.getRequest().getMethod();

			if (t.getDialog() != null && t.getDialog().getApplicationData() instanceof DialogWrapper) {
				DialogWrapper da = (DialogWrapper) t.getDialog().getApplicationData();
				if (isDialogTermMethod(da, method)) {
					sendBYE = false;

				}

				if (t instanceof ServerTransaction) {
					sendClientResponse = false;
				}

				if (sendClientResponse) {
					try {
						Response response = providerProxy.getMessageFactory().createResponse(Response.REQUEST_TIMEOUT,
								t.getRequest());

						ResponseEventWrapper REW = new ResponseEventWrapper(this.providerProxy, (ClientTransaction) t
								.getApplicationData(), da, response);

						fireEvent(REW, da.getActivityHandle(),
								eventIdCache.getEventId(eventLookup, response, inDialog), new Address(AddressPlan.SIP,
										((FromHeader) response.getHeader(FromHeader.NAME)).getAddress().toString()));

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (sendBYE) {
					try {
						Request bye = da.createRequest(Request.BYE);
						this.provider.sendRequest(bye);
					} catch (SipException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		} finally {
			if (wsi != null)
				sendActivityEndEvent(wsi);
		}
	}

	private boolean isDialogTermMethod(DialogActivity da, String method) {
		return false;
	}

	public void processTransactionTerminated(TransactionTerminatedEvent txTerminatedEvent) {

		Transaction t = null;
		if (!txTerminatedEvent.isServerTransaction()) {
			t = txTerminatedEvent.getClientTransaction();
		} else {
			t = txTerminatedEvent.getServerTransaction();
		}

		if (log.isInfoEnabled()) {
			log.info("Transaction " + t.getBranchId() + " terminated");
		}

		//HACK FOR ACK.......
		if(t.getApplicationData()==null && t.getRequest().getMethod().equals(Request.ACK))
		{
			SipActivityHandle _sah=new SipActivityHandle(t.getBranchId()+"_"+Request.ACK);
			t.setApplicationData(activities.get(_sah));
		}
		if (t.getApplicationData() != null) {

			WrapperSuperInterface wsi = (WrapperSuperInterface) t.getApplicationData();
			DialogWrapper dw = null;
			if (t.getDialog() != null && t.getDialog().getApplicationData() instanceof DialogWrapper) {
				dw = (DialogWrapper) t.getDialog().getApplicationData();
				if (wsi instanceof ServerTransactionWrapper) {
					dw.removeOngoingTransaction((ServerTransactionWrapper) wsi);
				} else if (wsi instanceof ClientTransactionWrapper) {
					dw.removeOngoingTransaction((ClientTransactionWrapper) wsi);
				} else {
					log.error("Unknown type " + wsi.getClass()
							+ " of SIP Transaction, can't remove from dialog wrapper");
				}
			}

			if (!this.sendActivityEndEvent(wsi) && !t.getRequest().getMethod().equals(Request.ACK)) {
				//FIXME: what does that mean????????????
				// not a tx activity, cleanup references now
				wsi.cleanup();
			}
		} else {
			log.error("TransactionTerminatedEvent dropped due to null app data for ["+t.getBranchId()+"] ["+t.getRequest().getMethod()+"]");

		}
	}

	public void processDialogTerminated(DialogTerminatedEvent dte) {

		if (log.isInfoEnabled()) {
			log.info("Dialog " + dte.getDialog().getDialogId() + " terminated");
		}

		if (dte.getDialog().getApplicationData() != null) {
			this.sendActivityEndEvent(((DialogWrapper) dte.getDialog().getApplicationData()));
		} else {
			if (log.isDebugEnabled()) {
				log.debug("DialogTerminatedEvent droping due to null app data.");
			}
		}

	}

	// *************** Event Life cycle

	/**
	 * 
	 */
	public boolean sendActivityEndEvent(WrapperSuperInterface wsi) {
		try {
			if(wsi.isActivity() && this.activities.containsKey(wsi.getActivityHandle()))
			{
				this.sleeEndpoint.activityEnding(wsi.getActivityHandle());
				return true;
			}else
			{
				this.activityEnded(wsi.getActivityHandle());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
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
						log.error(" TRANSPORT[" + tmp[i] + "] IS NOT A VALID TRANSPORT!!!");
					}
				}
			}
		}
		provisionedProperties.put(this.TRANSPORTS_BIND, transportsToSet);
	}

	public void addActivity(SipActivityHandle sah, WrapperSuperInterface wrapperActivity) {

		if (log.isDebugEnabled()) {
			log.debug("Adding sip activity handle " + sah);
		}

		this.activities.put(sah, wrapperActivity);

	}

	public Object removeActivity(SipActivityHandle sah) {

		if (log.isDebugEnabled()) {
			log.debug("Removing sip activity handle " + sah);
		}
		return this.activities.remove(sah);
	}

	public SleeEndpoint getSleeEndpoint() {
		return this.sleeEndpoint;
	}

	// ------- END OF PROVISIONING

	private boolean fireEvent(Object event, ActivityHandle handle, int eventID, Address address, boolean useFiltering) {

		if (useFiltering && eventIDFilter.filterEvent(eventID)) {
			if (log.isDebugEnabled()) {
				log.debug("Event " + eventID + " filtered");
			}
		} else if (eventID < 0) {
			log.error("Event id for " + eventID + " is less than zero, cant fire!!!");
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Firing event " + event + " on handle " + handle);
			}
			try {
				this.sleeEndpoint.fireEvent(handle, event, eventID, address);
				return true;
			} catch (Exception e) {
				log.error("Error firing event.", e);
			}
		}
		return false;
	}

	private boolean fireEvent(Object event, ActivityHandle handle, int eventId, Address address) {
		return this.fireEvent(event, handle, eventId, address, true);
	}

	// --- XXX - error responses to be a good citizen
	private void sendErrorResponse(ServerTransaction serverTransaction, Request request, int code, String msg) {
		if (!request.getMethod().equals(Request.ACK)) {
			try {
				ContentTypeHeader contentType = this.providerProxy.getHeaderFactory().createContentTypeHeader("text",
						"plain");
				Response response = providerProxy.getMessageFactory().createResponse(code, request, contentType,
						msg.getBytes());
				if (serverTransaction != null) {
					serverTransaction.sendResponse(response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// *********************** DEBUG PART]

	//private Timer debugTimer = new Timer();

	//private org.apache.log4j.Logger debugLogger = org.apache.log4j.Logger
	//		.getLogger("org.mobicents.slee.resource.sip.DEBUG");

	//private HashMap receivedEvents = new HashMap();
	//private ArrayList orderOfEvent = new ArrayList();
	//private HashMap timeStamps = new HashMap();

	//private class EventTimerTask extends TimerTask {

	//	int runCount = 0;

	//	@Override
	//	public void run() {

	//		debugLogger.info("-------------------- DEUMP RUN[" + runCount + "]-----------------");
	//		debugLogger.info("[" + runCount + "] ACTIVITIES  DUMP");
	//		Map ac = new HashMap(activities);
	//		int count = 0;
	//		for (Object key : ac.keySet()) {
	//			debugLogger.info("[" + runCount + "] AC[" + count++ + "]	  KEY[" + key + "] A[" + ac.get(key) + "]");
	//		} // 

	//	}
	//}

	//private void initDebug() {
	//	debugTimer.scheduleAtFixedRate(new EventTimerTask(), 5000, 5000);
	//}

	//private void tearDownDebug() {
	//	debugTimer.cancel();
	//}
}
