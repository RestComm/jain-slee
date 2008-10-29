/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 */
package org.mobicents.slee.resource.lab.ra;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Properties;

import javax.naming.NamingException;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.FacilityException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ActivityIsEndingException;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.FailureReason;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.resource.ResourceException;
import javax.slee.resource.SleeEndpoint;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.resource.lab.message.IncorrectRequestFormatException;
import org.mobicents.slee.resource.lab.message.Message;
import org.mobicents.slee.resource.lab.message.MessageEvent;
import org.mobicents.slee.resource.lab.message.MessageFactory;
import org.mobicents.slee.resource.lab.message.MessageFactoryImpl;
import org.mobicents.slee.resource.lab.message.MessageParser;
import org.mobicents.slee.resource.lab.message.MessageParserImpl;
import org.mobicents.slee.resource.lab.ratype.MessageActivity;
import org.mobicents.slee.resource.lab.ratype.MessageActivityContextInterfaceFactory;
import org.mobicents.slee.resource.lab.ratype.MessageResourceAdaptorSbbInterface;
import org.mobicents.slee.resource.lab.stack.RAFStack;
import org.mobicents.slee.resource.lab.stack.RAFStackListener;

/**
 * RAFrameResourceAdaptor wraps the RAFrameStack and controls its lifecycle
 * according to the lifecycle of a resource adaptor. For more information on the
 * lifecycle of a resource adaptor, please refer to JSLEE v1.1 Specification
 * Early Draft Review Page 297. <br>
 * The resource adaptor class is referenced in the deployment descriptor file
 * "resource-adaptor-jar.xml" in the tag <resource-adaptor-classes>, sub-tag
 * <resource-adaptor-class>, sub-tag <resource-adaptor-class-name>:
 * com.maretzke.raframe.ra.RAFrameResourceAdaptor For further Information please
 * refer to JAIN SLEE 1.0 Specification, Final Release Page 243 and JSLEE v1.1
 * Specification, Early Draft Review Page 292
 * 
 * @author Michael Maretzke
 * @author amit bhayani
 */
public class MessageResourceAdaptor implements RAFStackListener,
		ResourceAdaptor, Serializable {

	private static transient Logger logger = Logger
			.getLogger(MessageResourceAdaptor.class);

	// the local port to listen on - initial default value
	private int port = 40000;

	// the remote port to send information to - initial default value
	private int remoteport = 40001;

	// the remote host to send information to - initial default value
	private String remotehost = "127.0.0.1";

	// the name of the resource adaptor entity - initial default value
	private String stackName = "RAFrameResourceAdaptor";

	// reference to properties for future usage
	private transient Properties properties = null;

	/**
	 * The BootstrapContext provides the resource adaptor with the required
	 * capabilities in the SLEE to execute its work. The bootstrap context is
	 * implemented by the SLEE. The BootstrapContext object holds references to
	 * a number of objects that are of interest to many resource adaptors. For
	 * further information see JSLEE v1.1 Specification, Early Draft Review Page
	 * 305. The bootstrapContext will be set in entityCreated() method.
	 */
	private transient BootstrapContext bootstrapContext = null;

	/**
	 * The SLEE endpoint defines the contract between the SLEE and the resource
	 * adaptor that enables the resource adaptor to deliver events
	 * asynchronously to SLEE endpoints residing in the SLEE. This contract
	 * serves as a generic contract that allows a wide range of resources to be
	 * plugged into a SLEE environment via the resource adaptor architecture.
	 * For further information see JSLEE v1.1 Specification, Early Draft Review
	 * Page 307 The sleeEndpoint will be initialized in entityCreated() method.
	 */
	private transient SleeEndpoint sleeEndpoint = null;

	// the EventLookupFacility is used to look up the event id of incoming
	// events
	private transient EventLookupFacility eventLookup = null;

	// reference to the RAFStack - the stack which is controlled and wrapped by
	// this resource adaptor
	private transient RAFStack stack = null;

	// The list of activites stored in this resource adaptor. If this resource
	// adaptor were a distributed
	// and highly available solution, this storage were one of the candidates
	// for distribution.
	private transient HashMap activities = null;

	// the activity context interface factory defined in
	// RAFrameActivityContextInterfaceFactoryImpl
	private transient MessageActivityContextInterfaceFactory acif = null;

	// the message factory to create Message and MessageEvent objects
	private transient MessageFactory messageFactory = null;

	// a link to the RAFrameProvider which then will be exposed to Sbbs
	private transient MessageResourceAdaptorSbbInterface raProvider = null;

	// the message parser to validate incoming protocol messages
	private transient MessageParser messageParser;

	/**
	 * Constructor which accepts a Property object for further configuration of
	 * the resource adaptor. The following properties have a meaning:
	 * com.maretzke.raframe.stack.PORT sets the local port to listen on
	 * (serverport) (int) com.maretzke.raframe.stack.REMOTEPORT sets the remote
	 * port to which information is send to (int)
	 * com.maretzke.raframe.stack.REMOTEHOST is the remote computers' name to
	 * send information to (String) com.maretzke.raframe.stack.STACK_NAME is the
	 * stack's name (String)
	 * 
	 * @param properties
	 *            a Property object containing further information to configure
	 *            the resource adaptor
	 */
	public MessageResourceAdaptor(Properties properties) {
		this.properties = properties;
		this.port = Integer.parseInt(properties
				.getProperty("com.maretzke.raframe.stack.PORT"));
		this.remoteport = Integer.parseInt(properties
				.getProperty("com.maretzke.raframe.stack.REMOTEPORT"));
		this.remotehost = properties
				.getProperty("com.maretzke.raframe.stack.REMOTEHOST");
		this.stackName = properties
				.getProperty("com.maretzke.raframe.stack.STACK_NAME");
		logger.debug("RAFrameResourceAdaptor(" + properties + ") called.");
	}

	/**
	 * Constructor with the following parameters:
	 * 
	 * @param stackName
	 *            sets the name of the stack
	 * @param port
	 *            sets the local port to listen on (serversocket)
	 * @param remotehost
	 *            sets the remote computers' name to which information should be
	 *            send
	 * @param remoteport
	 *            sets the remote computers' port to which information should be
	 *            send
	 */
	public MessageResourceAdaptor(String stackName, int port,
			String remotehost, int remoteport) {
		this.port = port;
		this.remotehost = remotehost;
		this.remoteport = remoteport;
		this.stackName = stackName;
		logger.debug("RAFrameResourceAdaptor(" + stackName + ", " + port + ", "
				+ remotehost + ", " + remoteport + ") called.");
	}

	/**
	 * Creates a new instance of RAFrameResourceAdaptor and leaves the default
	 * values for the resource adaptor configuration untouched. The default
	 * configuration looks like: the local port to listen on - initial default
	 * value: 40000 the remote port to send information to - initial default
	 * value: 40001 the remote host to send information to - initial default
	 * value: localhost the name of the resource adaptor entity - initial
	 * default value: RAFrameResourceAdaptor
	 */
	public MessageResourceAdaptor() {
		logger.debug("RAFrameResourceAdaptor() called.");
	}

	/**
	 * implements com.maretzke.raframe.stack.RAFStackListener This method is
	 * invoked by the underlying RAFStack whenever incoming data is received.
	 */
	public void onEvent(String incomingData) {
		System.out.println("onEvent of RAFrameResourceAdaptor "+incomingData);
		MessageEvent event;
		Address address;
		int eventID;

		logger.debug("Incoming request: " + incomingData);

		// parse the incoming data
		try {
			Message message = messageParser.parse(incomingData);
			event = messageFactory.createMessageEvent(this, message);
		} catch (IncorrectRequestFormatException irfe) {
			// Unfortunately, the incoming messsage does not comply with the
			// protocol / message
			// format rules. The message is discarded.
			logger
					.debug("Incoming request: "
							+ incomingData
							+ " does not follow the defined message rules (ID COMMAND). Dropping the event.");
			irfe.printStackTrace();
			return;
		}

		// generate the activity handle which uniquely identifies the
		// appropriate activity context
		MessageActivityHandle handle = new MessageActivityHandle(event.getMessage()
				.getId());
		// lookup the activity
		MessageActivity activity = (MessageActivity) activities.get(handle);

		// activity does not exist - let's create one
		if (activity == null) {
			activity = new MessageActivityImpl(event.getMessage().getId());
			activities.put(handle, activity);
		}

		if (!activity.isValid(event.getMessage().getCommandId())) {
			logger
					.debug("Not a valid command. Command corrupts rules defined for the protocol.");
			System.out.println("Not a valid command. Command corrupts rules defined for the protocol.");
			return;
		}

		// the fireEvent() method needs a default address to where the events
		// should be fired to
		address = new Address(AddressPlan.IP, "127.0.0.1");

		// get the eventID from the JNDI tree
		try {
			eventID = eventLookup.getEventID(
					"org.mobicents.slee.resource.lab.message.incoming."
							+ event.getMessage().getCommand().toUpperCase(),
					"org.mobicents", "1.0");
		} catch (FacilityException fe) {
			logger.error("Caught a FacilityException: ");
			fe.printStackTrace();
			throw new RuntimeException(
					"RAFrameResourceAdapter.onEvent(): FacilityException caught. ",
					fe);
		} catch (UnrecognizedEventException uee) {
			logger.error("Caught an UnrecognizedEventException: ");
			uee.printStackTrace();
			throw new RuntimeException(
					"RAFrameResourceAdaptor.onEvent(): UnrecognizedEventException caught.",
					uee);
		}

		if (eventID == -1) {
			// Silently drop the message because this is not a registered event
			// type.
			System.out.println("Cant get EventId");
			logger
					.debug("RAFrameResourceAdaptor.onEvent(): Event lookup -- could not find event mapping -- check xml/slee-events.xml");
			return;
		}

		try {
			if (event.getMessage().getCommand().toLowerCase().compareTo("end") == 0) {
				// if the command is an end command, the connected activity
				// needs to end
				// this is signalled to the SLEE via activityEnding()
				logger
						.debug("RAFrameResourceAdaptor.onEvent(): RAFrameRA signals ending activity to SLEE. EventID: "
								+ eventID
								+ "; CallID:  "
								+ event.getMessage().getId()
								+ "; Command: "
								+ event.getMessage().getCommand());
				sleeEndpoint.activityEnding(new MessageActivityHandle(event
						.getMessage().getId()));
			} else {
				// fire the event into the SLEE and proceed
				logger
						.debug("RAFrameResourceAdaptor.onEvent(): RAFrameRA fires event into SLEE. EventID: "
								+ eventID
								+ "; CallID:  "
								+ event.getMessage().getId()
								+ "; Command: "
								+ event.getMessage().getCommand());
				
				System.out.println("RAFrameResourceAdaptor.onEvent(): RAFrameRA fires event into SLEE. EventID: "
						+ eventID
						+ "; CallID:  "
						+ event.getMessage().getId()
						+ "; Command: "
						+ event.getMessage().getCommand());
				sleeEndpoint.fireEvent(new MessageActivityHandle(event.getMessage()
						.getId()), (Object) event, eventID, address);
			}
		} catch (IllegalStateException ise) {
			logger.error("Caught an IllegalStateException: ");
			ise.printStackTrace();
		} catch (ActivityIsEndingException aiee) {
			logger.error("Caught an ActivityIsEndingException: ");
			aiee.printStackTrace();
		} catch (UnrecognizedActivityException uaee) {
			logger.error("Caught an UnrecognizedActivityException: ");
			uaee.printStackTrace();
		}
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 298 for further information. <br>
	 * This method is called by the SLEE when a resource adaptor object instance
	 * is bootstrapped, either when a resource adaptor entity is created or
	 * during SLEE startup. The SLEE implementation will construct the resource
	 * adaptor object and then invoke the entityCreated method before any other
	 * operations can be invoked on the resource adaptor object.
	 */
	public void entityCreated(BootstrapContext bootstrapContext)
			throws ResourceException {
		logger.debug("RAFrameResourceAdaptor.entityCreated() called.");
		this.bootstrapContext = bootstrapContext;
		this.sleeEndpoint = bootstrapContext.getSleeEndpoint();
		this.eventLookup = bootstrapContext.getEventLookupFacility();

		stack = null;
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 299 for further information. <br>
	 * This method is called by the SLEE when a resource adaptor object instance
	 * is being removed, either when a resource adaptor entity is deleted or
	 * during SLEE shutdown. When receiving this invocation the resource adaptor
	 * object is expected to close any system resources it has allocated.
	 */
	public void entityRemoved() {
		logger.debug("RAFrameResourceAdaptor.entityRemoved() called.");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 300 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor object that the
	 * specified event was processed successfully by the SLEE. An event is
	 * considered to be processed successfully if the SLEE has attempted to
	 * deliver the event to all interested SBBs.
	 */
	public void eventProcessingSuccessful(ActivityHandle activityHandle,
			Object obj, int param, Address address, int flags) {
		logger
				.debug("RAFrameResourceAdaptor.eventProcessingSuccessful() called.");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 300 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor object that the
	 * specified event was processed unsuccessfully by the SLEE. Event
	 * processing can fail if, for example, the SLEE doesn�t have enough
	 * resource to process the event, a SLEE node fails during event processing
	 * or a system level failure prevents the SLEE from committing transactions.
	 */
	public void eventProcessingFailed(ActivityHandle activityHandle,
			Object obj, int param, Address address, int flags,
			FailureReason failureReason) {
		logger.debug("RAFrameResourceAdaptor.eventProcessingFailed() called.");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 301 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor that the SLEE
	 * has completed activity end processing for the activity represented by the
	 * activity handle. The resource adaptor should release any resource related
	 * to this activity as the SLEE will not ask for it again.
	 */
	public void activityEnded(javax.slee.resource.ActivityHandle activityHandle) {
		// remove the handle from the list of activities
		activities.remove(activityHandle);
		logger.debug("RAFrameResourceAdaptor.activityEnded() called.");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 301 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor that the
	 * activity�s Activity Context object is no longer attached to any SBB
	 * entities and is no longer referenced by any SLEE Facilities. This enables
	 * the resource adaptor to implicitly end the Activity object.
	 */
	public void activityUnreferenced(
			javax.slee.resource.ActivityHandle activityHandle) {
		logger.debug("RAFrameResourceAdaptor.activityUnreferenced() called.");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 301 for further information. <br>
	 * The SLEE calls this method to query if a specific activity belonging to
	 * this resource adaptor object is alive.
	 */
	public void queryLiveness(javax.slee.resource.ActivityHandle activityHandle) {
		logger.debug("RAFrameResourceAdaptor.queryLifeness() called.");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 301 for further information. <br>
	 * The SLEE calls this method to get access to the underlying activity for
	 * an activity handle. The resource adaptor is expected to pass back a
	 * non-null object.
	 */
	public Object getActivity(javax.slee.resource.ActivityHandle activityHandle) {
		logger.debug("RAFrameResourceAdaptor.getActivity() called.");
		return activities.get(activityHandle);
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 301 for further information. <br>
	 * The SLEE calls this method to get an activity handle for an activity
	 * created by the underlying resource. This method is invoked by the SLEE
	 * when it needs to construct an activity context for an activity via an
	 * activity context interface factory method invoked by an SBB.
	 */
	public javax.slee.resource.ActivityHandle getActivityHandle(Object obj) {
		logger.debug("RAFrameResourceAdaptor.getActivityHandle(obj) called.");
		return null;
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 302 for further information. <br>
	 * The SLEE calls this method to get access to the underlying resource
	 * adaptor interface that enables the SBB to invoke the resource adaptor, to
	 * send messages for example.
	 */
	public Object getSBBResourceAdaptorInterface(String str) {
		logger.debug("RAFrameResourceAdaptor.getSBBResourceAdapterInterface("
				+ str + ") called.");
		return raProvider;
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 302 for further information. <br>
	 * The SLEE calls this method to get reference to the Marshaler object. The
	 * resource adaptor implements the Marshaler interface. The Marshaler is
	 * used by the SLEE to convert between object and distributable forms of
	 * events and event handles.
	 */
	public javax.slee.resource.Marshaler getMarshaler() {
		logger.debug("RAFrameResourceAdaptor.getMarshaler() called.");
		return null;
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 302 for further information. <br>
	 * The SLEE calls this method to signify to the resource adaptor that a
	 * service has been installed and is interested in a specific set of events.
	 * The SLEE passes an event filter which identifies a set of event types
	 * that services in the SLEE are interested in. The SLEE calls this method
	 * once a service is installed.
	 */
	public void serviceInstalled(String str, int[] values, String[] str2) {
		logger.debug("RAFrameResourceAdaptor.serviceInstalled() called.");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 303 for further information. <br>
	 * The SLEE calls this method to signify that a service has been
	 * un-installed in the SLEE. The event types associated to the service key
	 * are no longer of interest to a particular application.
	 */
	public void serviceUninstalled(String str) {
		logger.debug("RAFrameResourceAdaptor.serviceUninstalled() called.");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 303 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor that a service
	 * has been activated and is interested in the event types associated to the
	 * service key. The service must be installed with the resource adaptor via
	 * the serviceInstalled method before it can be activated.
	 */
	public void serviceActivated(String str) {
		logger.debug("RAFrameResourceAdaptor.serviceActivated() called.");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 304 for further information. <br>
	 * The SLEE calls this method to inform the SLEE that a service has been
	 * deactivated and is no longer interested in the event types associated to
	 * the service key.
	 */
	public void serviceDeactivated(String str) {
		logger.debug("RAFrameResourceAdaptor.serviceDeactivated() called.");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor The JSLEE v1.1
	 * Specification does not include entityActivated(). However, the API
	 * description of JSLEE v1.1 does include this method already. So, the
	 * documentation follows the code. <br>
	 * This method is called in context of project Mobicents in context of
	 * resource adaptor activation. More precisely,
	 * org.mobicents.slee.resource.ResourceAdaptorEntity.activate() calls this
	 * method entityActivated(). This method signals the resource adaptor the
	 * transition from state "INACTIVE" to state "ACTIVE".
	 */
	public void entityActivated() throws javax.slee.resource.ResourceException {
		logger.debug("RAFrameResourceAdaptor.entityActivated() called.");
		try {
			logger.debug("Starting RAFStack at port " + port);
			try {
				messageFactory = new MessageFactoryImpl();
				raProvider = new MessageResourceAdaptorSbbInterfaceImpl(this, messageFactory);
				messageParser = new MessageParserImpl();

				stack = new RAFStack(port, remotehost, remoteport);
				// register the resource adaptor to receive messages from the
				// stack -- onEvent()
				stack.addListener(this);
				stack.start();
				logger.debug("RAFStack up and running.");
				System.out.println("RAFStack up and running.");
				initializeNamingContext();
			} catch (Exception ex) {
				logger
						.error("RAFrameResouceAdaptor.start(): Exception caught! ");
				ex.printStackTrace();
				throw new ResourceException(ex.getMessage());
			}
			activities = new HashMap();
		} catch (ResourceException e) {
			e.printStackTrace();
			throw new javax.slee.resource.ResourceException(
					"RAFrameResourceAdaptor.entityActivated(): Failed to activate RAFrame Resource Adaptor!",
					e);
		}
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor The JSLEE v1.1
	 * Specification does not include entityDeactivated(). However, the API
	 * description of JSLEE v1.1 does include this method already. So, the
	 * documentation follows the code. <br>
	 * This method is called in context of project Mobicents in context of
	 * resource adaptor deactivation. More precisely,
	 * org.mobicents.slee.resource.ResourceAdaptorEntity.deactivate() calls this
	 * method entityDeactivated(). The method call is done AFTER the call to
	 * entityDeactivating(). This method signals the resource adaptor the
	 * transition from state "STOPPING" to state "INACTIVE".
	 */
	public void entityDeactivated() {
		logger.debug("RAFrameResourceAdaptor.entityDeactivated() called.");
		try {
			cleanNamingContext();
		} catch (NamingException e) {
			logger.error("Cannot unbind naming context");
		}
		logger.debug("RAFrame Resource Adaptor stopped.");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor The JSLEE v1.1
	 * Specification does not include entityDeactivating(). However, the API
	 * description of JSLEE v1.1 does include this method already. So, the
	 * documentation follows the code. <br>
	 * This method is called in context of project Mobicents in context of
	 * resource adaptor deactivation. More precisely,
	 * org.mobicents.slee.resource.ResourceAdaptorEntity.deactivate() calls this
	 * method entityDeactivating() PRIOR to invoking entityDeactivated(). This
	 * method signals the resource adaptor the transition from state "ACTIVE" to
	 * state "STOPPING".
	 */
	public void entityDeactivating() {
		logger.debug("RAFrameResourceAdaptor.entityDeactivating() called.");
		logger.debug("Shuting down the stack.");

		synchronized (this) {
			try {
				// set the flag in the stack to terminate
				stack.shutdown();
				// the stack will stop every 1000ms to accept incoming connections
				// and check the shutdown flag
				wait(1000);
			} catch (InterruptedException ie) {
			}
		}
	}

	// set up the JNDI naming context
	private void initializeNamingContext() throws Exception {
		// get the reference to the SLEE container from JNDI
		SleeContainer container = SleeContainer.lookupFromJndi();
		// get the entities name
		String entityName = bootstrapContext.getEntityName();

		ResourceAdaptorEntity resourceAdaptorEntity = container.getResourceManagement().getResourceAdaptorEntity(entityName);
		ResourceAdaptorTypeID raTypeId = resourceAdaptorEntity
				.getInstalledResourceAdaptor().getRaType()
				.getResourceAdaptorTypeID();
		// create the ActivityContextInterfaceFactory
		acif = new MessageActivityContextInterfaceFactoryImpl(
				resourceAdaptorEntity.getServiceContainer(), entityName);
		// set the ActivityContextInterfaceFactory
		resourceAdaptorEntity.getServiceContainer().getResourceManagement()
				.getActivityContextInterfaceFactories().put(raTypeId, acif);

		try {
			if (this.acif != null) {
				// parse the string = java:slee/resources/RAFrameRA/raframeacif
				String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) acif)
						.getJndiName();
				int begind = jndiName.indexOf(':');
				int toind = jndiName.lastIndexOf('/');
				String prefix = jndiName.substring(begind + 1, toind);
				String name = jndiName.substring(toind + 1);
				logger.debug("jndiName prefix =" + prefix + "; jndiName = "
						+ name);
				
				System.out.println("jndiName prefix =" + prefix + "; jndiName = "
						+ name);
				SleeContainer.registerWithJndi(prefix, name, this.acif);
			}
		} catch (IndexOutOfBoundsException e) {
			// not register with JNDI
			logger.debug(e);
		}
	}

	// clean the JNDI naming context
	private void cleanNamingContext() throws NamingException {
		try {
			if (this.acif != null) {
				// parse the string = java:slee/resources/RAFrameRA/raframeacif
				String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) this.acif)
						.getJndiName();
				// remove "java:" prefix
				int begind = jndiName.indexOf(':');
				String javaJNDIName = jndiName.substring(begind + 1);
				logger.debug("JNDI name to unregister: " + javaJNDIName);
				SleeContainer.unregisterWithJndi(javaJNDIName);
				logger.debug("JNDI name unregistered.");
			}
		} catch (IndexOutOfBoundsException e) {
			logger.debug(e);
		}
	}

	// method to send a message with the bound RAFStack instance
	public void send(String message) {
		logger.debug("Sending message to stack: " + message);
		stack.send(message);
	}
}