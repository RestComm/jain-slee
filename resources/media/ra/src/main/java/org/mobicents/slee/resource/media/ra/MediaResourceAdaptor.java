/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under GPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.resource.media.ra;

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
import javax.slee.resource.Marshaler;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.resource.ResourceException;
import javax.slee.resource.SleeEndpoint;

import org.apache.log4j.Logger;
import org.mobicents.media.server.impl.common.events.EventID;
import org.mobicents.mscontrol.MsConnection;
import org.mobicents.mscontrol.MsConnectionEvent;
import org.mobicents.mscontrol.MsConnectionListener;
import org.mobicents.mscontrol.MsLink;
import org.mobicents.mscontrol.MsLinkEvent;
import org.mobicents.mscontrol.MsLinkListener;
import org.mobicents.mscontrol.MsNotifyEvent;
import org.mobicents.mscontrol.MsProvider;
import org.mobicents.mscontrol.MsResource;
import org.mobicents.mscontrol.MsResourceListener;
import org.mobicents.mscontrol.MsSession;
import org.mobicents.mscontrol.MsSessionEvent;
import org.mobicents.mscontrol.MsSessionListener;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.resource.media.ratype.MediaRaActivityContextInterfaceFactory;

/**
 * 
 * The main Media Resource Adaptor class. Provides to SLEE services basic media
 * features such as establishing a media stream with a remote client, playing a
 * media file to the remote client and recording incoming media from the client.
 * 
 * @author torosvi
 * @author Ivelin Ivanov
 * @author Oleg Kulikov
 * @author amit.bhayani
 * 
 */
public class MediaResourceAdaptor implements ResourceAdaptor, MsConnectionListener, MsResourceListener, MsLinkListener,
		MsSessionListener {
	public Properties properties;

	private MsProvider msProvider;

	static private transient Logger logger;
	static {
		logger = Logger.getLogger(MediaResourceAdaptor.class);
	}

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
	 * Page 307. The sleeEndpoint will be initialized in entityCreated() method.
	 */
	private transient SleeEndpoint sleeEndpoint = null;
	// The EventLookupFacility is used to look up the event id of incoming
	// events
	private transient EventLookupFacility eventLookup = null;

	// The list of activites stored in this resource adaptor is managed by
	// MediaActivityManager.
	MediaActivityManager mediaActivityManager = new MediaActivityManager();

	// The activity context interface factory defined in
	// MediaRaActivityContextInterfaceFactoryImpl
	private transient MediaRaActivityContextInterfaceFactory acif = null;

	/**
	 * Implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 298 for further information. <br>
	 * This method is called by the SLEE when a resource adaptor object instance
	 * is bootstrapped, either when a resource adaptor entity is created or
	 * during SLEE startup. The SLEE implementation will construct the resource
	 * adaptor object and then invoke the entityCreated method before any other
	 * operations can be invoked on the resource adaptor object.
	 */
	public void entityCreated(BootstrapContext bootstrapContext) throws ResourceException {
		this.bootstrapContext = bootstrapContext;
		this.sleeEndpoint = bootstrapContext.getSleeEndpoint();
		this.eventLookup = bootstrapContext.getEventLookupFacility();
	}

	/**
	 * Implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 299 for further information. <br>
	 * This method is called by the SLEE when a resource adaptor object instance
	 * is being removed, either when a resource adaptor entity is deleted or
	 * during SLEE shutdown. When receiving this invocation the resource adaptor
	 * object is expected to close any system resources it has allocated.
	 */
	public void entityRemoved() {
	}

	/**
	 * Implements javax.slee.resource.ResourceAdaptor The JSLEE v1.1
	 * Specification does not include entityActivated(). However, the API
	 * description of JSLEE v1.1 does include this method already. So, the
	 * documentation follows the code. <br>
	 * This method is called in context of project Mobicents in context of
	 * resource adaptor activation. More precisely,
	 * org.mobicents.slee.resource.ResourceAdaptorEntity.activate() calls this
	 * method entityActivated(). This method signals the resource adaptor the
	 * transition from state "INACTIVE" to state "ACTIVE".
	 */
	public void entityActivated() throws ResourceException {
		try {

			msProvider = new RaMsProviderImpl(this);
			msProvider.addConnectionListener(this);
			msProvider.addSessionListener(this);
			msProvider.addResourceListener(this);
			msProvider.addLinkListener(this);

			this.initializeNamingContext();
			// activities = new HashMap();
		} catch (Exception e) {
			e.printStackTrace();
			throw new javax.slee.resource.ResourceException("MediaResourceAdaptor.entityActivated(): "
					+ "Failed to activate RAFrame Resource Adaptor!", e);
		}
	}

	/**
	 * Implements javax.slee.resource.ResourceAdaptor The JSLEE v1.1
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
		try {
			cleanNamingContext();
		} catch (NamingException e) {
			logger.error("Cannot unbind naming context", e);
		}
	}

	/**
	 * Implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 300 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor object that the
	 * specified event was processed successfully by the SLEE. An event is
	 * considered to be processed successfully if the SLEE has attempted to
	 * deliver the event to all interested SBBs.
	 */
	public void eventProcessingSuccessful(ActivityHandle handle, Object event, int eventID, Address address, int flags) {
	}

	/**
	 * Implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 300 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor object that the
	 * specified event was processed unsuccessfully by the SLEE. Event
	 * processing can fail if, for example, the SLEE doesn�t have enough
	 * resource to process the event, a SLEE node fails during event processing
	 * or a system level failure prevents the SLEE from committing transactions.
	 */
	public void eventProcessingFailed(ActivityHandle handle, Object event, int eventID, Address address, int flags,
			FailureReason reason) {
	}

	/**
	 * Implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 301 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor that the SLEE
	 * has completed activity end processing for the activity represented by the
	 * activity handle. The resource adaptor should release any resource related
	 * to this activity as the SLEE will not ask for it again.
	 */
	public void activityEnded(ActivityHandle activityHandle) {
		// remove the handle from the list of activities
		// Object activity = activities.remove(activityHandle);
		// handlers.remove(activity.toString());

		mediaActivityManager.removeMediaActivity(activityHandle);
	}

	/**
	 * Implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 301 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor that the
	 * activity�s Activity Context object is no longer attached to any SBB
	 * entities and is no longer referenced by any SLEE Facilities. This enables
	 * the resource adaptor to implicitly end the Activity object.
	 */
	public void activityUnreferenced(ActivityHandle activityHandle) {
	}

	/**
	 * Implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 301 for further information. <br>
	 * The SLEE calls this method to query if a specific activity belonging to
	 * this resource adaptor object is alive.
	 */
	public void queryLiveness(ActivityHandle activityHandle) {
		if (!mediaActivityManager.containsActivityHandle(activityHandle)) {
			try {
				sleeEndpoint.activityEnding(activityHandle);
			} catch (Exception e) {
				logger.error("Unexpected error while ending activity", e);
			}
		}
	}

	/**
	 * Implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 301 for further information. <br>
	 * The SLEE calls this method to get access to the underlying activity for
	 * an activity handle. The resource adaptor is expected to pass back a
	 * non-null object.
	 */
	public Object getActivity(ActivityHandle activityHandle) {
		return mediaActivityManager.getActivity(activityHandle);

		// return activities.get(activityHandle);
	}

	/**
	 * Implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 301 for further information. <br>
	 * The SLEE calls this method to get an activity handle for an activity
	 * created by the underlying resource. This method is invoked by the SLEE
	 * when it needs to construct an activity context for an activity via an
	 * activity context interface factory method invoked by an SBB.
	 */
	public ActivityHandle getActivityHandle(Object activity) {
		return mediaActivityManager.getActivityHandle(activity);
		// return (ActivityHandle) handlers.get(activity.toString());
	}

	/**
	 * Implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 302 for further information. <br>
	 * The SLEE calls this method to get access to the underlying resource
	 * adaptor interface that enables the SBB to invoke the resource adaptor, to
	 * send messages for example.
	 */
	public Object getSBBResourceAdaptorInterface(String className) {
		return this.msProvider;
	}

	/**
	 * Implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 302 for further information. <br>
	 * The SLEE calls this method to get reference to the Marshaler object. The
	 * resource adaptor implements the Marshaler interface. The Marshaler is
	 * used by the SLEE to convert between object and distributable forms of
	 * events and event handles.
	 */
	public Marshaler getMarshaler() {
		return null;
	}

	/**
	 * Implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 302 for further information. <br>
	 * The SLEE calls this method to signify to the resource adaptor that a
	 * service has been installed and is interested in a specific set of events.
	 * The SLEE passes an event filter which identifies a set of event types
	 * that services in the SLEE are interested in. The SLEE calls this method
	 * once a service is installed.
	 */
	public void serviceInstalled(String serviceKey, int[] eventIDs, String[] resourceOptions) {
	}

	/**
	 * Implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 303 for further information. <br>
	 * The SLEE calls this method to signify that a service has been
	 * un-installed in the SLEE. The event types associated to the service key
	 * are no longer of interest to a particular application.
	 */
	public void serviceUninstalled(String serviceKey) {
	}

	/**
	 * Implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 303 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor that a service
	 * has been activated and is interested in the event types associated to the
	 * service key. The service must be installed with the resource adaptor via
	 * the serviceInstalled method before it can be activated.
	 */
	public void serviceActivated(String arg0) {
	}

	/**
	 * Implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification, Early Draft Review Page 304 for further information. <br>
	 * The SLEE calls this method to inform the SLEE that a service has been
	 * deactivated and is no longer interested in the event types associated to
	 * the service key.
	 */
	public void serviceDeactivated(String arg0) {
	}

	// Set up the JNDI naming context
	private void initializeNamingContext() throws NamingException {
		// Get the reference to the SLEE container from JNDI
		SleeContainer container = SleeContainer.lookupFromJndi();
		// Get the entities name
		String entityName = bootstrapContext.getEntityName();

		ResourceAdaptorEntity resourceAdaptorEntity = ((ResourceAdaptorEntity) container
				.getResourceAdaptorEnitity(entityName));
		ResourceAdaptorTypeID raTypeId = resourceAdaptorEntity.getInstalledResourceAdaptor().getRaType()
				.getResourceAdaptorTypeID();
		// Create the ActivityContextInterfaceFactory
		acif = new MediaRaActivityContextInterfaceFactoryImpl(resourceAdaptorEntity.getServiceContainer(), entityName,
				this);
		// Set the ActivityContextInterfaceFactory
		resourceAdaptorEntity.getServiceContainer().getActivityContextInterfaceFactories().put(raTypeId, acif);

		try {
			if (this.acif != null) {
				// Parse the string = java:slee/resources/MediaRA/mediaraacif
				String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) acif).getJndiName();
				int begind = jndiName.indexOf(':');
				int toind = jndiName.lastIndexOf('/');
				String prefix = jndiName.substring(begind + 1, toind);
				String name = jndiName.substring(toind + 1);
				logger.debug("jndiName prefix =" + prefix + "; jndiName = " + name);
				SleeContainer.registerWithJndi(prefix, name, this.acif);
			}
		} catch (IndexOutOfBoundsException e) {
			// Not register with JNDI
			logger.debug(e);
		}
	}

	// Clean the JNDI naming context
	private void cleanNamingContext() throws NamingException {
		try {
			if (this.acif != null) {
				// Parse the string = java:slee/resources/MediaRA/mediaraacif
				String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) this.acif).getJndiName();
				// Remove "java:" prefix
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

	private synchronized void fireEvent(String eventName, ActivityHandle activityHandle, Object event) {
		int eventID = -1;
		try {
			eventID = eventLookup.getEventID(eventName, "org.mobicents.media", "1.0");
		} catch (FacilityException fe) {
			logger.error("Caught a FacilityException: ");
			fe.printStackTrace();
			throw new RuntimeException("MediaResourceAdaptor.firEvent(): FacilityException caught. ", fe);
		} catch (UnrecognizedEventException ue) {
			logger.error("Caught an UnrecognizedEventException: ");
			ue.printStackTrace();
			throw new RuntimeException("MediaResourceAdaptor.firEvent(): UnrecognizedEventException caught.", ue);
		}

		if (eventID == -1) {
			logger.warn("Unknown event type: " + eventName);
			return;
		}

		Address address = new Address(AddressPlan.IP, "localhost");

		try {
			sleeEndpoint.fireEvent(activityHandle, event, eventID, address);
			logger.info("Fire event: " + eventName);
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

	SleeEndpoint getSleeEndpoint() {
		return sleeEndpoint;
	}

	// public void onConnecting(MsConnectionEvent evt) {
	// MsConnection connection = evt.getConnection();
	// // MediaActivityHandle handle = new
	// // MediaActivityHandle(connection.toString());
	// ActivityHandle handle = getActivityHandle(connection);
	// this.fireEvent("org.mobicents.slee.media.CONNECTION_CONNECTING", handle,
	// evt);
	// }

	// public void onConnected(MsConnectionEvent evt) {
	// MsConnection connection = evt.getConnection();
	// // MediaActivityHandle handle = new
	// // MediaActivityHandle(connection.toString());
	// ActivityHandle handle = getActivityHandle(connection);
	// this.fireEvent("org.mobicents.slee.media.CONNECTION_CONNECTED", handle,
	// evt);
	// }

	// public void onFailed(MsConnectionEvent evt) {
	// MsConnection connection = evt.getConnection();
	// // MediaActivityHandle handle = new
	// // MediaActivityHandle(connection.toString());
	// ActivityHandle handle = getActivityHandle(connection);
	// this.fireEvent("org.mobicents.slee.media.CONNECTION_FAILED", handle,
	// evt);
	// }

	// public void onDisconnected(MsConnectionEvent evt) {
	// MsConnection connection = evt.getConnection();
	// // MediaActivityHandle handle = new
	// // MediaActivityHandle(connection.toString());
	// ActivityHandle handle = getActivityHandle(connection);
	// this.fireEvent("org.mobicents.slee.media.CONNECTION_DISCONNECTED",
	// handle, evt);
	// try {
	// sleeEndpoint.activityEnding(handle);
	// } catch (Exception e) {
	// logger.error("Could not end activity: " + connection);
	// }
	// }

	public void update(MsNotifyEvent event) {
		MsResource resource = event.getSource();

		MsResourceActivityHandle msResourceActivityHandle = mediaActivityManager.getMsResourceActivityHandle(resource
				.getID());

		if (msResourceActivityHandle == null) {
			msResourceActivityHandle = mediaActivityManager.putMsResourceActivity(resource);
		}

		EventID eventid = event.getEventID();

		if (eventid.equals(EventID.PLAY)) {
			this.fireEvent("org.mobicents.slee.media.announcement.PLAY", msResourceActivityHandle, event);
		} else if (eventid.equals(EventID.COMPLETE)) {
			this.fireEvent("org.mobicents.slee.media.announcement.COMPLETE", msResourceActivityHandle, event);
		} else if (eventid.equals(EventID.FAIL)) {
			this.fireEvent("org.mobicents.slee.media.announcement.FAIL", msResourceActivityHandle, event);
		} else if (eventid.equals(EventID.DTMF)) {
			this.fireEvent("org.mobicents.slee.media.dtmf.DTMF", msResourceActivityHandle, event);
		}

		try {
			sleeEndpoint.activityEnding(msResourceActivityHandle);
		} catch (Exception e) {
			logger.error("Could not end activity: " + resource);
		}
	}

	// -----------------------------------------------------------------------------
	// Link events
	// -----------------------------------------------------------------------------

	public void linkCreated(MsLinkEvent evt) {
		MsLink link = evt.getSource();
		MsLinkActivityHandle msLinkActivityHandle = mediaActivityManager.getMsLinkActivityHandle(link.getId());
		this.fireEvent("org.mobicents.slee.media.LINK_CREATED", msLinkActivityHandle, evt);
	}

	public void linkJoined(MsLinkEvent evt) {
		MsLink link = evt.getSource();

		MsLinkActivityHandle msLinkActivityHandle = mediaActivityManager.getMsLinkActivityHandle(link.getId());
		this.fireEvent("org.mobicents.slee.media.JOIN_COMPLETE", msLinkActivityHandle, evt);
	}

	public void linkDropped(MsLinkEvent evt) {
		MsLink link = evt.getSource();

		MsLinkActivityHandle msLinkActivityHandle = mediaActivityManager.getMsLinkActivityHandle(link.getId());
		this.fireEvent("org.mobicents.slee.media.LINK_DROPPED", msLinkActivityHandle, evt);
		try {
			sleeEndpoint.activityEnding(msLinkActivityHandle);
		} catch (Exception e) {
			logger.error("Could not end activity: " + link);
		}
	}

	public void linkFailed(MsLinkEvent evt) {
		MsLink link = evt.getSource();
		MsLinkActivityHandle msLinkActivityHandle = mediaActivityManager.getMsLinkActivityHandle(link.getId());
		this.fireEvent("org.mobicents.slee.media.JOIN_FAIL", msLinkActivityHandle, evt);
		try {
			sleeEndpoint.activityEnding(msLinkActivityHandle);
		} catch (Exception e) {
			logger.error("Could not end activity: " + link);
		}
	}

	// -----------------------------------------------------------------------------
	// Connection events
	// -----------------------------------------------------------------------------

	public void connectionInitialized(MsConnectionEvent evt) {
		MsConnection connection = evt.getConnection();
		MsConnectionActivityHandle msConnectionActivityHandle = mediaActivityManager
				.getMsConnectionActivityHandle(connection.getId());
		this.fireEvent("org.mobicents.slee.media.CONNECTION_INITIALIZED", msConnectionActivityHandle, evt);

	}

	public void connectionCreated(MsConnectionEvent evt) {
		MsConnection connection = evt.getConnection();

		MsConnectionActivityHandle msConnectionActivityHandle = mediaActivityManager
				.getMsConnectionActivityHandle(connection.getId());
		this.fireEvent("org.mobicents.slee.media.CONNECTION_CREATED", msConnectionActivityHandle, evt);
	}

	public void connectionModifed(MsConnectionEvent event) {
		MsConnection connection = event.getConnection();
		MsConnectionActivityHandle handle = mediaActivityManager.getMsConnectionActivityHandle(connection.getId());
		this.fireEvent("org.mobicents.slee.media.CONNECTION_MODIFIED", handle, event);
	}

	public void connectionDeleted(MsConnectionEvent event) {
		MsConnection connection = event.getConnection();
		MsConnectionActivityHandle handle = mediaActivityManager.getMsConnectionActivityHandle(connection.getId());
		// ActivityHandle handle = getActivityHandle(connection);
		this.fireEvent("org.mobicents.slee.media.CONNECTION_DELETED", handle, event);
		try {
			sleeEndpoint.activityEnding(handle);
		} catch (Exception e) {
			logger.error("Could not end activity: " + connection);
		}
	}

	public void txFailed(MsConnectionEvent event) {
		MsConnection connection = event.getConnection();
		MsConnectionActivityHandle handle = mediaActivityManager.getMsConnectionActivityHandle(connection.getId());
		// ActivityHandle handle = getActivityHandle(connection);
		this.fireEvent("org.mobicents.slee.media.CONNECTION_TX_FAILED", handle, event);
	}

	// -----------------------------------------------------------------------------
	// Session events
	// -----------------------------------------------------------------------------
	public void sessionCreated(MsSessionEvent evt) {
		MsSession session = evt.getSource();
		MsSessionActivityHandle handle = mediaActivityManager.getMsSessionActivityHandle(session.getId());
		this.fireEvent("org.mobicents.slee.media.SESSION_CREATED", handle, evt);
	}

	public void sessionActive(MsSessionEvent evt) {
		MsSession session = evt.getSource();
		MsSessionActivityHandle handle = mediaActivityManager.getMsSessionActivityHandle(session.getId());
		this.fireEvent("org.mobicents.slee.media.SESSION_ACTIVE", handle, evt);
	}

	public void sessionInvalid(MsSessionEvent evt) {
		MsSession session = evt.getSource();
		MsSessionActivityHandle handle = mediaActivityManager.getMsSessionActivityHandle(session.getId());
		this.fireEvent("org.mobicents.slee.media.SESSION_INVALID", handle, evt);
		try {
			sleeEndpoint.activityEnding(handle);
		} catch (Exception e) {
			logger.error("Could not end activity: " + session);
		}
	}

	public MediaActivityManager getMediaActivityManager() {
		return this.mediaActivityManager;
	}

}
