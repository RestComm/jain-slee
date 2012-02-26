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

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.SLEEException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorContext;
import javax.slee.resource.SleeEndpoint;
import javax.slee.resource.StartActivityException;

import org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor;
import org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext;
import org.mobicents.slee.resource.cluster.ReplicatedData;
import org.mobicents.slee.resource.lab.message.IncorrectRequestFormatException;
import org.mobicents.slee.resource.lab.message.Message;
import org.mobicents.slee.resource.lab.message.MessageEvent;
import org.mobicents.slee.resource.lab.message.MessageFactory;
import org.mobicents.slee.resource.lab.message.MessageFactoryImpl;
import org.mobicents.slee.resource.lab.message.MessageParser;
import org.mobicents.slee.resource.lab.message.MessageParserImpl;
import org.mobicents.slee.resource.lab.ratype.MessageActivity;
import org.mobicents.slee.resource.lab.ratype.MessageResourceAdaptorSbbInterface;
import org.mobicents.slee.resource.lab.stack.RAFStack;
import org.mobicents.slee.resource.lab.stack.RAFStackListener;

/**
 * <p>
 * RAFrameResourceAdaptor wraps the RAFrameStack and controls its lifecycle
 * according to the lifecycle of a resource adaptor. For more information on the
 * lifecycle of a resource adaptor, please refer to JSLEE v1.1 Specification
 * </p>
 * <p>
 * The resource adaptor class is referenced in the deployment descriptor file
 * "resource-adaptor-jar.xml" in the tag <resource-adaptor-classes>, sub-tag
 * <resource-adaptor-class>, sub-tag <resource-adaptor-class-name>:
 * com.maretzke.raframe.ra.RAFrameResourceAdaptor For further Information please
 * refer to JAIN SLEE 1.1
 * </p>
 * 
 * @author amit bhayani
 */
public class MessageResourceAdaptor implements
		FaultTolerantResourceAdaptor<MessageActivityHandle, MessageActivity>,
		RAFStackListener {

	public static final int NON_MARSHABLE_ACTIVITY_FLAGS = ActivityFlags.REQUEST_ENDED_CALLBACK;//.NO_FLAGS;
	
	public static final int MARSHABLE_ACTIVITY_FLAGS = ActivityFlags.setSleeMayMarshal(NON_MARSHABLE_ACTIVITY_FLAGS);
	
	private Tracer tracer;
	private ResourceAdaptorContext resourceAdaptorContext;
	private transient SleeEndpoint sleeEndpoint = null;

	// a link to the RAFrameProvider which then will be exposed to Sbbs
	private transient MessageResourceAdaptorSbbInterface raProvider = null;

	// the message factory to create Message and MessageEvent objects
	private transient MessageFactory messageFactory = null;

	// the message parser to validate incoming protocol messages
	private transient MessageParser messageParser;

	// reference to the RAFStack - the stack which is controlled and wrapped by
	// this resource adaptor
	private transient RAFStack stack = null;

	private String localHost = "127.0.0.1";

	// the local port to listen on - initial default value
	private int port = 40000;

	// the remote port to send information to - initial default value
	private int remoteport = 40001;

	// the remote host to send information to - initial default value
	private String remotehost = "127.0.0.1";

	// The list of activites stored in this resource adaptor. If this resource
	// adaptor were a distributed and highly available solution, this storage
	// were one of the candidates for distribution.
	// private transient HashMap<MessageActivityHandle, MessageActivity>
	// activities = null;

	private transient FireableEventTypeCache eventTypeCache;

	private transient EventLookupFacility eventLookup = null;

	
	
	public MessageResourceAdaptor() {
		super();
		this.messageFactory = new MessageFactoryImpl();
		this.raProvider = new MessageResourceAdaptorSbbInterfaceImpl(this,
				messageFactory);
	}

	/**
	 * RA Lifecycle methods
	 */

	@Override
	public void activityEnded(ActivityHandle actHandle) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("activityEnded called. ActivityHandle = "
					+ actHandle);
		}
		this.activities.remove((MessageActivityHandle) actHandle);
	}

	@Override
	public void activityUnreferenced(ActivityHandle actHandle) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("activityUnreferenced called. ActivityHandle = "
					+ actHandle);
		}
	}

	@Override
	public void administrativeRemove(ActivityHandle actHandle) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("administrativeRemove called. ActivityHandle = "
					+ actHandle);
		}
	}

	@Override
	public void eventProcessingFailed(ActivityHandle actHandle,
			FireableEventType fireEvtType, Object event, Address address,
			ReceivableService recService, int flags, FailureReason failreason) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("eventProcessingFailed called. ActivityHandle = "
					+ actHandle + " FireableEventType = " + fireEvtType
					+ " Event = " + event + " Address = " + address
					+ " ReceivableService = " + recService + " Flags = "
					+ flags + " FailureReason = " + failreason);
		}
	}

	@Override
	public void eventProcessingSuccessful(ActivityHandle actHandle,
			FireableEventType fireEvtType, Object event, Address address,
			ReceivableService recService, int flags) {
		if (this.tracer.isFineEnabled()) {
			this.tracer
					.fine("eventProcessingSuccessful called. ActivityHandle = "
							+ actHandle + " FireableEventType = " + fireEvtType
							+ " Event = " + event + " Address = " + address
							+ " ReceivableService = " + recService
							+ " Flags = " + flags);
		}
	}

	@Override
	public void eventUnreferenced(ActivityHandle actHandle,
			FireableEventType fireEvtType, Object event, Address address,
			ReceivableService recService, int flags) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("eventUnreferenced called. ActivityHandle = "
					+ actHandle + " FireableEventType = " + fireEvtType
					+ " Event = " + event + " Address = " + address
					+ " ReceivableService = " + recService + " Flags = "
					+ flags);
		}
	}

	@Override
	public Object getActivity(ActivityHandle key) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("getActivity called " + key);
		}
		return this.activities.get((MessageActivityHandle) key);
	}

	@Override
	public ActivityHandle getActivityHandle(Object obj) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("getActivityHandle called " + obj);
		}
		// NOTE: getLocalKeyset() - does not fetch data from cluster!
		for (MessageActivityHandle msgActHndle : this.activities
				.getLocalKeyset()) {
			MessageActivity mssAct = this.activities.get(msgActHndle);
			if (mssAct.equals(obj)) {
				return msgActHndle;
			}
		}

		return null;
	}

	@Override
	public Marshaler getMarshaler() {
		return new MessageActivityHandleMarshaler();
	}

	@Override
	public Object getResourceAdaptorInterface(String arg0) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("getResourceAdaptorInterface called " + arg0);
		}
		return this.raProvider;
	}

	@Override
	public void queryLiveness(ActivityHandle activityHandle) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("Query Liveness called for ActivityHandle "
					+ activityHandle);
		}
		MessageActivity mssActivity = this.activities
				.get((MessageActivityHandle) activityHandle);
		if (mssActivity == null) {
			this.tracer.warning("The queryLiveness failed for activity "
					+ activityHandle + " Ending this activity");
			this.sleeEndpoint.endActivity(activityHandle);
		}
	}

	@Override
	public void raActive() {

		try {
			if (this.tracer.isFineEnabled()) {
				this.tracer.fine("Activating RA Entity "
						+ this.resourceAdaptorContext.getEntityName());
			}

			
			this.messageParser = new MessageParserImpl();

			this.stack = new RAFStack(localHost, port, remotehost, remoteport,
					this.resourceAdaptorContext);
			// register the resource adaptor to receive messages from the stack
			// -- onEvent()
			this.stack.addListener(this);
			this.stack.start();
			if (this.tracer.isFineEnabled()) {
				this.tracer.fine("RAFStack up and running.");
			}

			this.sleeEndpoint = resourceAdaptorContext.getSleeEndpoint();

			// set failover flag to true, just to play with callbacks
			this.activities = this.ftResourceAdaptorContext
					.getReplicatedDataWithFailover(true);
		} catch (Throwable thr) {
			this.tracer.severe("Activation of RA "
					+ this.resourceAdaptorContext.getEntityName() + " failed",
					thr);
			throw new RuntimeException(thr);
		}

	}

	@Override
	public void raConfigurationUpdate(ConfigProperties properties) {
		if (tracer.isFineEnabled()) {
			tracer.fine("Configuring RA Entity "
					+ this.resourceAdaptorContext.getEntityName()
					+ " RA Configuration Update");
		}
	}

	@Override
	public void raConfigure(ConfigProperties properties) {
		if (tracer.isFineEnabled()) {
			tracer.fine("Configuring RA Entity "
					+ this.resourceAdaptorContext.getEntityName());
		}

		this.localHost = (String) properties.getProperty("localHost")
				.getValue();
		this.port = (Integer) properties.getProperty("localPort").getValue();
		this.remotehost = (String) properties.getProperty("remoteHost")
				.getValue();
		this.remoteport = (Integer) properties.getProperty("remotePort")
				.getValue();

		if (tracer.isFineEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("Configured RA. Poperties : \nLocalHost = ")
					.append(this.localHost).append("\nLocalPort = ")
					.append(port).append("\nRemoteHost=").append(remotehost)
					.append("\nRemotePort=").append(remoteport);
			tracer.fine(sb.toString());
		}

	}

	@Override
	public void raInactive() {
		this.stack.shutdown();
		if (tracer.isFineEnabled()) {
			tracer.fine("Resource Adaptor entity "
					+ this.resourceAdaptorContext.getEntityName()
					+ " de-activated.");
		}
	}

	@Override
	public void raStopping() {
		if (tracer.isFineEnabled()) {
			tracer.fine("Resource Adaptor entity "
					+ this.resourceAdaptorContext.getEntityName()
					+ " stopping.");
		}
	}

	@Override
	public void raUnconfigure() {
		this.localHost = null;
		this.port = -1;
		this.remotehost = null;
		this.remoteport = -1;
		if (tracer.isFineEnabled()) {
			tracer.fine("Resource Adaptor entity "
					+ this.resourceAdaptorContext.getEntityName()
					+ " Unconfigured.");
		}
	}

	@Override
	public void raVerifyConfiguration(ConfigProperties properties)
			throws InvalidConfigurationException {
		try {
			String localHostTemp = (String) properties.getProperty("localHost")
					.getValue();
			int portTemp = (Integer) properties.getProperty("localPort")
					.getValue();
			String remotehostTemp = (String) properties.getProperty(
					"remoteHost").getValue();
			int remoteportTemp = (Integer) properties.getProperty("remotePort")
					.getValue();

			InetSocketAddress local = new InetSocketAddress(localHostTemp,
					portTemp);
			ServerSocket server = new ServerSocket();
			server.bind(local);

			server.close();

			if (tracer.isFineEnabled()) {
				tracer.fine("Resource Adaptor entity "
						+ this.resourceAdaptorContext.getEntityName()
						+ " verified Configuration.");
			}

		} catch (Throwable e) {
			throw new InvalidConfigurationException(e.getMessage(), e);

		}
	}

	@Override
	public void serviceActive(ReceivableService receivableSer) {
		if (tracer.isFineEnabled()) {
			tracer.fine("Resource Adaptor entity "
					+ this.resourceAdaptorContext.getEntityName()
					+ " service Active " + receivableSer);
		}
	}

	@Override
	public void serviceInactive(ReceivableService receivableSer) {
		if (tracer.isFineEnabled()) {
			tracer.fine("Resource Adaptor entity "
					+ this.resourceAdaptorContext.getEntityName()
					+ " service Inactive " + receivableSer);
		}
	}

	@Override
	public void serviceStopping(ReceivableService receivableSer) {
		if (tracer.isFineEnabled()) {
			tracer.fine("Resource Adaptor entity "
					+ this.resourceAdaptorContext.getEntityName()
					+ " service stopping " + receivableSer);
		}
	}

	@Override
	public void setResourceAdaptorContext(
			ResourceAdaptorContext resourceAdaptorContext) {
		this.resourceAdaptorContext = resourceAdaptorContext;
		this.tracer = this.resourceAdaptorContext
				.getTracer(MessageResourceAdaptor.class.getSimpleName());

		this.eventTypeCache = new FireableEventTypeCache(this.tracer);

		this.eventLookup = this.resourceAdaptorContext.getEventLookupFacility();
		((MessageResourceAdaptorSbbInterfaceImpl)this.raProvider).initTracer();
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("Resource Adaptor entity "
					+ this.resourceAdaptorContext.getEntityName()
					+ " ResourceAdaptorContext set");
		}
	}

	@Override
	public void unsetResourceAdaptorContext() {
		this.resourceAdaptorContext = null;
		this.tracer = null;

		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("Resource Adaptor entity "
					+ this.resourceAdaptorContext.getEntityName()
					+ " ResourceAdaptorContext unset");
		}
	}

	/**
	 * RAFStackListener Method
	 */

	@Override
	public void onEvent(String incomingData) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("Incoming event " + incomingData);
		}

		MessageEvent event;
		Address address;

		// parse the incoming data
		try {
			Message message = this.messageParser.parse(incomingData);
			event = messageFactory.createMessageEvent(this, message);
		} catch (IncorrectRequestFormatException irfe) {
			// Unfortunately, the incoming messsage does not comply with the
			// protocol / message format rules. The message is discarded.

			if (this.tracer.isWarningEnabled()) {
				this.tracer
						.warning(
								"Incoming request: "
										+ incomingData
										+ " does not follow the defined message rules (ID COMMAND). Dropping the event.",
								irfe);
			}
			return;
		}

		// generate the activity handle which uniquely identifies the
		// appropriate activity context
		MessageActivityHandle handle = new MessageActivityHandle(event
				.getMessage().getId());

		// lookup the activity
		MessageActivity activity = activities.get(handle);

		// activity does not exist - let's create one
		if (activity == null) {
			activity = new MessageActivityImpl(event.getMessage().getId(),
					this.toString());
		}

		if (!activity.isValid(event.getMessage().getCommandId())) {
			if (this.tracer.isWarningEnabled()) {
				this.tracer
						.warning("Not a valid command. Command corrupts rules defined for the protocol.");
			}
			return;
		}
		switch (event.getMessage().getCommandId()) {
		case Message.INIT:
			activity.initReceived();
			break;
		case Message.ANY:
			activity.anyReceived();
			break;
		case Message.END:
			activity.endReceived();
			break;
		}
		
		

		// the fireEvent() method needs a default address to where the events
		// should be fired to
		address = new Address(AddressPlan.IP, "127.0.0.1");

		final FireableEventType eventType = this.eventTypeCache.getEventType(
				this.eventLookup,
				"org.mobicents.slee.resource.lab.message.incoming."
						+ event.getMessage().getCommand().toUpperCase());

		if (event.getMessage().getCommand().toLowerCase().compareTo("init") == 0) {
			try {
				this.sleeEndpoint.startActivity(handle, activity, MARSHABLE_ACTIVITY_FLAGS);
				activities.put(handle, activity);
			} catch (ActivityAlreadyExistsException e) {
				this.tracer
						.severe("ActivityAlreadyExistsException while starting the Actvity",
								e);
				return;
			} catch (NullPointerException e) {
				this.tracer.severe(
						"NullPointerException while starting the Actvity", e);
				return;
			} catch (IllegalStateException e) {
				this.tracer.severe(
						"IllegalStateException while starting the Actvity", e);
				return;
			} catch (SLEEException e) {
				this.tracer.severe("SLEEException while starting the Actvity",
						e);
				return;
			} catch (StartActivityException e) {
				this.tracer.severe(
						"StartActivityException while starting the Actvity", e);
				return;
			}
		}

		try {
			sleeEndpoint.fireEvent(handle, eventType, event, address, null);
			//update
			activities.put(handle, activity);
			if (tracer.isInfoEnabled()) {
				tracer.info("Fired event: " + eventType);
			}
		} catch (Throwable e) {
			this.tracer.severe("Failed to fire event " + eventType, e);
		}

		try {
			if (event.getMessage().getCommand().toLowerCase().compareTo("end") == 0) {
				this.sleeEndpoint.endActivity(handle);
				if (this.tracer.isFineEnabled()) {
					this.tracer.fine("Activity ended " + handle);
				}
			}

			
		} catch (Throwable e) {
			this.tracer.severe("Failed to fire event " + eventType, e);
		}

	}

	/**
	 * protected methods
	 */
	protected ResourceAdaptorContext getResourceAdaptorContext() {
		return this.resourceAdaptorContext;
	}

	// method to send a message with the bound RAFStack instance
	protected void send(String message) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("Sending message to stack: " + message);
		}
		stack.send(message);
	}

	// -------------- CLUSTER -----------------

	private ReplicatedData<MessageActivityHandle, MessageActivity> activities;
	private FaultTolerantResourceAdaptorContext<MessageActivityHandle, MessageActivity> ftResourceAdaptorContext;

	@Override
	public void dataRemoved(MessageActivityHandle handle) {
		this.tracer.info("Activity has been removed by cluster member: "
				+ handle);
		// perform resource cleanup here, if needed

	}

	@Override
	public void failOver(MessageActivityHandle handle) {
		this.tracer
				.info("Resource Adaptor which owns activity is no longer up, perform take over: "
						+ handle);
		// initiate resources here.
		((MessageActivityImpl) getActivity(handle)).setRaImprint(this
				.toString());
		this.activities.put(handle, (MessageActivityImpl) getActivity(handle));
	}

	@Override
	public void setFaultTolerantResourceAdaptorContext(
			FaultTolerantResourceAdaptorContext<MessageActivityHandle, MessageActivity> ctx) {
		this.ftResourceAdaptorContext = ctx;
		this.tracer.info("Setting up clustered context! Local["+ctx.isLocal()+"] Members"+Arrays.toString(ctx.getMembers()));
	}

	@Override
	public void unsetFaultTolerantResourceAdaptorContext() {
		this.ftResourceAdaptorContext = null;
	}

}