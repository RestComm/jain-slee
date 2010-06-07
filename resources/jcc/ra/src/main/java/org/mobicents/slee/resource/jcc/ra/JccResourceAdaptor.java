/*
 * File Name     : JccResourceAdaptor.java
 *
 * The Java Call Control RA
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */
package org.mobicents.slee.resource.jcc.ra;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;
import java.io.Serializable;
import java.util.Enumeration;

import java.util.Properties;

import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.FacilityException;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityIsEndingException;

import javax.slee.resource.ResourceAdaptor;

import javax.slee.resource.SleeEndpoint;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.EventFlags;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireEventException;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.IllegalEventException;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptorContext;
import javax.slee.resource.StartActivityException;
import javax.slee.resource.UnrecognizedActivityHandleException;

import javax.csapi.cc.jcc.JccPeerFactory;
import javax.csapi.cc.jcc.JccPeer;
import javax.csapi.cc.jcc.JccConnectionListener;
import javax.csapi.cc.jcc.JccEvent;
import javax.csapi.cc.jcc.JccCall;
import javax.csapi.cc.jcc.JccConnection;
import javax.csapi.cc.jcc.JccCallEvent;
import javax.csapi.cc.jcc.JccConnectionEvent;
import javax.csapi.cc.jcc.MethodNotSupportedException;
import javax.csapi.cc.jcc.ResourceUnavailableException;

import org.mobicents.slee.resource.jcc.local.JccProviderLocal;
import org.mobicents.slee.resource.jcc.local.JccCallLocal;
import org.mobicents.slee.resource.jcc.local.JccConnectionLocal;
import org.mobicents.slee.resource.jcc.local.JccCallEventLocal;
import org.mobicents.slee.resource.jcc.local.JccConnectionEventLocal;

import org.mobicents.slee.resource.jcc.ratype.JccActivityContextInterfaceFactory;

/**
 * 
 * @author Oleg Kulikov
 * @author Pavel Mitrenko
 * @author baranowb
 */
public class JccResourceAdaptor implements ResourceAdaptor, Serializable, JccConnectionListener {

	private static final String _CONFIG_OPT_NAME_JCCPEER = "jccPeer";
	private static final String _CONFIG_OPT_NAME_CONF = "configName";

	
	private JccProviderLocal provider;
	private Address address = new Address(AddressPlan.IP, "127.0.0.1");
	private Tracer logger = null;
	private ConcurrentReaderHashMap activities = new ConcurrentReaderHashMap();
	private ConcurrentReaderHashMap handlers = new ConcurrentReaderHashMap();
	private String peerName = null;
	private String configName;
	private JccActivityContextInterfaceFactory activityContextInterfaceFactory;
	private boolean stopped = false;
	private Thread monitor;
	private String xmlConvofiguration = "";

	/**
	 * 
	 */
	private ResourceAdaptorContext raContext;
	private SleeEndpoint sleeEndpoint;
	private EventLookupFacility eventLookupFacility;

	/**
	 * for all events we are interested in knowing when the event failed to be
	 * processed
	 */
	public static final int DEFAULT_EVENT_FLAGS = EventFlags.REQUEST_PROCESSING_FAILED_CALLBACK;

	private static final int ACTIVITY_FLAGS = ActivityFlags.REQUEST_ENDED_CALLBACK;// .NO_FLAGS;

	/** Creates a new instance of JccResourceAdaptor */
	public JccResourceAdaptor() {
	}

	// //////////////
	// Properties //
	// //////////////
	public String getJccPeer() {
		return peerName;
	}

	public void setJccPeer(String jccPeer) {
		this.peerName = jccPeer;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	// ///////////////////////
	// Life Cycle Methods //
	// ///////////////////////
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#administrativeRemove(javax.slee.resource
	 * .ActivityHandle)
	 */
	public void administrativeRemove(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#eventProcessingFailed(javax.slee.
	 * resource.ActivityHandle, javax.slee.resource.FireableEventType,
	 * java.lang.Object, javax.slee.Address,
	 * javax.slee.resource.ReceivableService, int,
	 * javax.slee.resource.FailureReason)
	 */
	public void eventProcessingFailed(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4,
			int arg5, FailureReason arg6) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#eventProcessingSuccessful(javax.slee
	 * .resource.ActivityHandle, javax.slee.resource.FireableEventType,
	 * java.lang.Object, javax.slee.Address,
	 * javax.slee.resource.ReceivableService, int)
	 */
	public void eventProcessingSuccessful(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4,
			int arg5) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#eventUnreferenced(javax.slee.resource
	 * .ActivityHandle, javax.slee.resource.FireableEventType, java.lang.Object,
	 * javax.slee.Address, javax.slee.resource.ReceivableService, int)
	 */
	public void eventUnreferenced(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4, int arg5) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#getResourceAdaptorInterface(java.
	 * lang.String)
	 */
	public Object getResourceAdaptorInterface(String arg0) {
		return this.provider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.slee.resource.ResourceAdaptor#raConfigure(javax.slee.resource.
	 * ConfigProperties)
	 */
	public void raConfigure(ConfigProperties cps) {
		try {
			if(logger.isInfoEnabled())
			{
				logger.info("Configuring JCCRA: "+this.raContext.getEntityName());
			}
			// FIXME: get props!
			Properties properties = new Properties();
			properties.load(getClass().getResourceAsStream("/" + configName));
			logger.info("Loaded properties: " + properties);

			xmlConvofiguration = "<jcc-inap>";
			Enumeration keys = properties.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				xmlConvofiguration += ";" + key + "=" + properties.getProperty(key);
			}

			//FIXME: make this work
//			JccPeer peer = JccPeerFactory.getJccPeer(peerName);
//			JccProvider testProvider = peer.getProvider(xmlConvofiguration);
//			testProvider.shutdown();
			
		} catch (UnsatisfiedLinkError ex) {
			logger.warning("JCC Resource adaptor is not attached to baord driver", ex);
		} catch (Exception e) {
			if (logger.isSevereEnabled())
				logger.severe("Can not start Jcc Provider: ", e);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#raVerifyConfiguration(javax.slee.
	 * resource.ConfigProperties)
	 */
	public void raVerifyConfiguration(ConfigProperties cps) throws InvalidConfigurationException {
		try {
			

			if(logger.isInfoEnabled())
			{
				logger.info("Verifyin configuring JCCRA: "+this.raContext.getEntityName());
			}
			this.configName = (String) cps.getProperty(_CONFIG_OPT_NAME_CONF).getValue();
			this.peerName = (String) cps.getProperty(_CONFIG_OPT_NAME_JCCPEER).getValue();
			// FIXME: is there any better way ?
			if (this.configName == null) {
				throw new InvalidConfigurationException("No name set for configuration file.");
			}

			if (this.peerName == null) {
				throw new InvalidConfigurationException("No name set jcc peer.");
			}

			if (null == getClass().getResource("/" + configName)) {
				throw new InvalidConfigurationException("Configuration file: " + configName + ", can not be located.");
			}
			
		} catch (InvalidConfigurationException e) {
			throw e;
		} catch (Exception e) {
			throw new InvalidConfigurationException("Failed to test configuration options!", e);
		}
		logger.info("JCC Provider started successfuly");

		logger.info("Running monitoring thread");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.resource.ResourceAdaptor#raActive()
	 */
	public void raActive() {

		try {
			JccPeer peer = JccPeerFactory.getJccPeer(peerName);
			provider = new JccProviderLocal(peer.getProvider(xmlConvofiguration));

			provider.addConnectionListener(this, null);
			provider.addCallListener(this);

			logger.info("JCC Provider started successfuly");
			monitor = new Thread(new ActivityMonitor());
			monitor.start();
			logger.info("Running monitoring thread");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MethodNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#raConfigurationUpdate(javax.slee.
	 * resource.ConfigProperties)
	 */
	public void raConfigurationUpdate(ConfigProperties arg0) {

		// not supported?

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.resource.ResourceAdaptor#raStopping()
	 */
	public void raStopping() {
		try{
			//weird this causes exception?
			provider.shutdown();
			provider = null;
		}catch(Exception e)
		{
			
		}
		for (Object o : this.handlers.values()) {
			ActivityHandle ah = (ActivityHandle) o;
			try {
				sleeEndpoint.endActivity(ah);
			} catch (UnrecognizedActivityException uae) {
				if (logger.isSevereEnabled())
					logger.severe("Caught an UnrecognizedActivityException: ");
				uae.printStackTrace();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.resource.ResourceAdaptor#raInactive()
	 */
	public void raInactive() {
		stopped = true;
		monitor.interrupt();
		monitor = null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.resource.ResourceAdaptor#raUnconfigure()
	 */
	public void raUnconfigure() {
		this.xmlConvofiguration = null;
		this.configName = null;
		this.peerName = null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#serviceActive(javax.slee.resource
	 * .ReceivableService)
	 */
	public void serviceActive(ReceivableService arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#serviceInactive(javax.slee.resource
	 * .ReceivableService)
	 */
	public void serviceInactive(ReceivableService arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#serviceStopping(javax.slee.resource
	 * .ReceivableService)
	 */
	public void serviceStopping(ReceivableService arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#setResourceAdaptorContext(javax.slee
	 * .resource.ResourceAdaptorContext)
	 */
	public void setResourceAdaptorContext(ResourceAdaptorContext raContext) {
		this.raContext = raContext;
		this.logger = raContext.getTracer("JccResourceAdaptor");
		this.sleeEndpoint = raContext.getSleeEndpoint();
		this.eventLookupFacility = raContext.getEventLookupFacility();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.resource.ResourceAdaptor#unsetResourceAdaptorContext()
	 */
	public void unsetResourceAdaptorContext() {
		this.raContext = null;
		this.sleeEndpoint = null;
		this.eventLookupFacility = null;
	}

	public void activityEnded(ActivityHandle handle) {
		String ID = ((JccConnectionActivityHandle) handle).getID();

		if(logger.isInfoEnabled())
		{
			logger.info("Activity ended id: "+ID+", handle: "+handle);
		}
		//sleeEndpoint.endActivity(handle);
		Object activity = activities.remove(ID);
		if(logger.isInfoEnabled())
		{
			logger.info("Activity ended activity: "+activity);
		}
		if (logger.isFineEnabled())
			logger.fine("Removed activity: " + activity + " under key: " + ID);

		JccConnectionActivityHandle h = (JccConnectionActivityHandle) handlers.remove(activity.toString());
		if(logger.isInfoEnabled())
		{
			logger.info("Activity ended removed handler: "+h);
		}
		if (logger.isFineEnabled())
			logger.fine("Removed handle: " + h.getID() + " under key: " + activity.toString());
	}

	public void activityUnreferenced(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	public Object getActivity(ActivityHandle handle) {
		String ID = ((JccConnectionActivityHandle) handle).getID();
		return activities.get(ID);
	}

	public ActivityHandle getActivityHandle(Object obj) {
		return (ActivityHandle) handlers.get(obj.toString());
	}

	public Marshaler getMarshaler() {
		return null;
	}

	public void queryLiveness(ActivityHandle activityHandle) {
	}

	// ////////////////////////
	// JCC Listener methods //
	// ////////////////////////

	// JccConnectionEventListener
	public void callActive(JccCallEvent event) {
		if (logger.isFineEnabled())
			logger.fine("Receive JccCallEvent.CALL_ACTIVE event");
		JccCallEvent evt = wrapJccCallEvent(event);
		JccCall call = evt.getCall();

		ActivityHandle handle = getActivityHandle(call);
		fireEvent("javax.csapi.cc.jcc.JccCallEvent.CALL_ACTIVE", handle, evt);
	}

	public void callCreated(JccCallEvent event) {
		if (logger.isFineEnabled())
			logger.fine("Receive JccCallEvent.CALL_CREATED event");
		JccCallEvent evt = wrapJccCallEvent(event);
		JccCall call = evt.getCall();

		// create new activity handle
		JccCallActivityHandle handle = new JccCallActivityHandle(call);
		try {
			if(logger.isInfoEnabled())
			{
				logger.info("Starting activity: "+call+", handle: "+handle);
			}
			this.sleeEndpoint.startActivity(handle, call,ACTIVITY_FLAGS);
			//FIXME: add this
		} catch (ActivityAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SLEEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StartActivityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		//FIXME: Oleg is this the way it should be ?
		activities.put(call, handle);

		fireEvent("javax.csapi.cc.jcc.JccCallEvent.CALL_CREATED", handle, evt);
	}

	public void callEventTransmissionEnded(JccCallEvent event) {
		if (logger.isFineEnabled())
			logger.fine("Receive JccCallEvent.CALL_EVENT_TRANSMISSION_ENDED event");
		JccCallEvent evt = wrapJccCallEvent(event);
		JccCall call = evt.getCall();

		ActivityHandle handle = getActivityHandle(call);
		fireEvent("javax.csapi.cc.jcc.JccCallEvent.CALL_EVENT_TRANSMISSION_ENDED", handle, evt);

		try {
			if(logger.isInfoEnabled())
			{
				logger.info("callEventTransmissionEnded : ending activity "+handle);
			}
			sleeEndpoint.endActivity(handle);
		} catch (UnrecognizedActivityException uae) {
			if (logger.isSevereEnabled())
				logger.severe("Caught an UnrecognizedActivityException: ");
			uae.printStackTrace();
		}
	}

	public void callInvalid(JccCallEvent event) {
		if (logger.isFineEnabled())
			logger.fine("Receive JccCallEvent.CALL_INVALID event");
		JccCallEvent evt = wrapJccCallEvent(event);
		JccCall call = evt.getCall();

		ActivityHandle handle = getActivityHandle(call);
		fireEvent("javax.csapi.cc.jcc.JccCallEvent.CALL_INVALID", handle, evt);

		try {
			if(logger.isInfoEnabled())
			{
				logger.info("callInvalid : ending activity "+handle);
			}
			sleeEndpoint.endActivity(handle);
		} catch (UnrecognizedActivityException uae) {
			if (logger.isSevereEnabled())
				logger.severe("Caught an UnrecognizedActivityException: ");
			uae.printStackTrace();
		}
	}

	public void callSuperviseEnd(JccCallEvent event) {
		if (logger.isFineEnabled())
			logger.fine("Receive JccCallEvent.CALL_SUPERVISE_END event");
		JccCallEvent evt = wrapJccCallEvent(event);
		JccCall call = evt.getCall();

		ActivityHandle handle = getActivityHandle(call);
		fireEvent("javax.csapi.cc.jcc.JccCallEvent.CALL_CSUPERVISE_END", handle, evt);
	}

	public void callSuperviseStart(JccCallEvent event) {
		if (logger.isFineEnabled())
			logger.fine("Receive JccCallEvent.CALL_SUPERVISE_START event");
		JccCallEvent evt = wrapJccCallEvent(event);
		JccCall call = evt.getCall();

		ActivityHandle handle = getActivityHandle(call);
		fireEvent("javax.csapi.cc.jcc.JccCallEvent.CALL_CSUPERVISE_START", handle, evt);
	}

	public void connectionAddressAnalyze(JccConnectionEvent event) {
		if (logger.isFineEnabled())
			logger.fine("Receive JccConnection.CONNECTION_ADDRESS_ANALYZE event");

		JccConnectionEvent evt = wrapJccConnectionEvent(event);
		JccConnection connection = evt.getConnection();

		ActivityHandle handle = getActivityHandle(connection);
		fireEvent("javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_ADDRESS_ANALYZE", handle, evt);
	}

	public void connectionAddressCollect(JccConnectionEvent event) {
		if (logger.isFineEnabled())
			logger.fine("Receive JccConnection.CONNECTION_ADDRESS_COLLECT event");

		JccConnectionEvent evt = wrapJccConnectionEvent(event);
		JccConnection connection = evt.getConnection();

		ActivityHandle handle = getActivityHandle(connection);
		fireEvent("javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_ADDRESS_COLLECT", handle, evt);
	}

	public void connectionAlerting(JccConnectionEvent event) {
		if (logger.isFineEnabled())
			logger.fine("Receive JccConnection.CONNECTION_ALERTING event");

		JccConnectionEvent evt = wrapJccConnectionEvent(event);
		JccConnection connection = evt.getConnection();

		ActivityHandle handle = getActivityHandle(connection);
		fireEvent("javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_ALERTING", handle, evt);
	}

	public void connectionAuthorizeCallAttempt(JccConnectionEvent event) {
		if (logger.isFineEnabled())
			logger.fine("Receive JccConnection.CONNECTION_AUTHORIZE_CALL_ATTEMPT event");

		JccConnectionEvent evt = wrapJccConnectionEvent(event);
		JccConnection connection = evt.getConnection();

		ActivityHandle handle = getActivityHandle(connection);
		fireEvent("javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_AUTHORIZE_CALL_ATTEMPT", handle, evt);
	}

	public void connectionCallDelivery(JccConnectionEvent event) {
		if (logger.isFineEnabled())
			logger.fine("Receive JccConnection.CONNECTION_CALL_DELIVERY event");

		JccConnectionEvent evt = wrapJccConnectionEvent(event);
		JccConnection connection = evt.getConnection();

		ActivityHandle handle = getActivityHandle(connection);
		fireEvent("javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_CALL_DELIVERY", handle, evt);
	}

	public void connectionConnected(JccConnectionEvent event) {
		if (logger.isFineEnabled())
			logger.fine("Receive JccConnection.CONNECTION_CONNECTED event");

		JccConnectionEvent evt = wrapJccConnectionEvent(event);
		JccConnection connection = evt.getConnection();

		ActivityHandle handle = getActivityHandle(connection);
		fireEvent("javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_CONNECTED", handle, evt);
	}

	public void connectionCreated(JccConnectionEvent event) {
		if (logger.isFineEnabled())
			logger.fine("Receive JccConnection.CONNECTION_CREATED event");
		JccConnectionEvent evt = wrapJccConnectionEvent(event);
		JccConnection connection = evt.getConnection();

		// create new activity handle
		JccConnectionActivityHandle handle = new JccConnectionActivityHandle(connection);
		
		try {
			if(logger.isInfoEnabled())
			{
				logger.info("Starting activity: "+connection+", handle: "+handle);
			}
			this.sleeEndpoint.startActivity(handle, connection,ACTIVITY_FLAGS);
			//FIXME: add this 
		} catch (ActivityAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SLEEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StartActivityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		handlers.put(connection.toString(), handle);
		if (logger.isFineEnabled())
			logger.fine("onConnectionCreate():put handle: " + handle + " under key: " + connection.toString());

		activities.put(handle.getID(), connection);
		if (logger.isFineEnabled())
			logger.fine("onConnectionCreate():put connection " + connection + " under key: " + handle.getID());

		fireEvent("javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_CREATED", handle, evt);
	}

	public void connectionDisconnected(JccConnectionEvent event) {
		if (logger.isFineEnabled())
			logger.fine("Receive JccConnection.CONNECTION_DISCONNECTED event");
		JccConnectionEvent evt = wrapJccConnectionEvent(event);
		JccConnection connection = evt.getConnection();

		ActivityHandle handle = getActivityHandle(connection.toString());
		fireEvent("javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_DISCONNECTED", handle, evt);
		
		try {
			if(logger.isInfoEnabled())
			{
				logger.info("connectionDisconnected : ending activity "+handle+", activity: "+connection);
			}
			sleeEndpoint.endActivity(handle);
		} catch (UnrecognizedActivityException uae) {
			if (logger.isSevereEnabled())
				logger.severe("Caught an UnrecognizedActivityException: ");
			uae.printStackTrace();
		}
		
	}

	public void connectionFailed(JccConnectionEvent event) {
		if (logger.isFineEnabled())
			logger.fine("Receive JccConnection.CONNECTION_FAILED event");
		JccConnectionEvent evt = wrapJccConnectionEvent(event);
		JccConnection connection = evt.getConnection();

		if (logger.isFineEnabled())
			logger.fine("onfailed(): connectionID: " + connection.toString());
		ActivityHandle handle = getActivityHandle(connection);
		if(logger.isInfoEnabled())
		{
			logger.info("connectionFailed : ending activity "+handle+", activity: "+connection);
		}
		fireEvent("javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_FAILED", handle, evt);
		//called in {@link #connectionDisconnected(JccConnectionEvent)}
		//sleeEndpoint.endActivity(handle);
		if (logger.isFineEnabled())
			logger.fine("onfailed(): handle=" + handle);
		
	}

	public void connectionMidCall(JccConnectionEvent event) {
		if (logger.isFineEnabled())
			logger.fine("Receive JccConnection.CONNECTION_MID_CALLevent");

		JccConnectionEvent evt = wrapJccConnectionEvent(event);
		JccConnection connection = evt.getConnection();

		ActivityHandle handle = getActivityHandle(connection);
		fireEvent("javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_MID_CALL", handle, evt);
	}

	private JccConnectionEventLocal wrapJccConnectionEvent(JccConnectionEvent event) {
		JccCallLocal call = new JccCallLocal(event.getCall(), provider);
		JccConnectionLocal connection = new JccConnectionLocal(event.getConnection(), provider, call);
		return new JccConnectionEventLocal(connection, event.getID(), event.getCause(), event.getSource(), call);
	}

	private JccCallEventLocal wrapJccCallEvent(JccCallEvent event) {
		JccCallLocal call = new JccCallLocal(event.getCall(), provider);
		return new JccCallEventLocal(event.getID(), event.getCause(), event.getSource(), call);
	}

	private void fireEvent(String eventName, ActivityHandle activityHandle, JccEvent event) {
		FireableEventType eventID = null;
		try {
			EventTypeID eventTypeId = new EventTypeID(eventName, "javax.csapi.cc.jcc", "1.1");
			eventID = eventLookupFacility.getFireableEventType(eventTypeId);
		} catch (FacilityException fe) {
			if (logger.isSevereEnabled())
				logger.severe("Caught a FacilityException: ");
			fe.printStackTrace();
			throw new RuntimeException("JccResourceAdaptor.firEvent(): FacilityException caught. ", fe);
		} catch (UnrecognizedEventException ue) {
			if (logger.isSevereEnabled())
				logger.severe("Caught an UnrecognizedEventException: ");
			ue.printStackTrace();
			throw new RuntimeException("JccResourceAdaptor.firEvent(): UnrecognizedEventException caught.", ue);
		}

		if (eventID == null) {
			if (logger.isWarningEnabled())
				logger.warning("Unknown event type: " + eventName);
			return;
		}

		try {
			sleeEndpoint.fireEvent(activityHandle, eventID, event, address, null);

			if (logger.isFineEnabled())
				logger.fine("Fire event: " + eventName);
		} catch (IllegalStateException ise) {
			if (logger.isSevereEnabled())
				logger.severe("Caught an IllegalStateException: ");
			ise.printStackTrace();
		} catch (ActivityIsEndingException aiee) {
			if (logger.isSevereEnabled())
				logger.severe("Caught an ActivityIsEndingException: ");
			aiee.printStackTrace();
		} catch (UnrecognizedActivityException uaee) {
			if (logger.isSevereEnabled())
				logger.severe("Caught an UnrecognizedActivityException: ");
			uaee.printStackTrace();
		} catch (UnrecognizedActivityHandleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalEventException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SLEEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FireEventException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class ActivityMonitor implements Runnable {

		public void run() {
			while (!stopped) {
				try {
					Thread.currentThread().sleep(60000);
					logger.info("activities=" + activities.size() + ", handlers=" + handlers.size());
					logger.info("activities: "+activities);
					logger.info("handlers: "+handlers);
				} catch (InterruptedException e) {
					stopped = true;
				}
			}
		}
	}

}
