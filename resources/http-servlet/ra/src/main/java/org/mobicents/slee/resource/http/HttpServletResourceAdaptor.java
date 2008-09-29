package org.mobicents.slee.resource.http;

import java.util.concurrent.ConcurrentHashMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.CouldNotStartActivityException;
import javax.slee.resource.FailureReason;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.resource.ResourceException;
import javax.slee.resource.SleeEndpoint;

import net.java.slee.resource.http.HttpServletRaActivityContextInterfaceFactory;
import net.java.slee.resource.http.events.HttpServletRequestEvent;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.resource.http.events.HttpServletRequestEventImpl;

public class HttpServletResourceAdaptor implements ResourceAdaptor {

	private static transient Logger logger = Logger
			.getLogger(HttpServletResourceAdaptor.class.getName());

	/**
	 * The BootstrapContext provides the resource adaptor with the required
	 * capabilities in the SLEE to execute its work. The bootstrap context is
	 * implemented by the SLEE. The BootstrapContext object holds references to
	 * a number of objects that are of interest to many resource adaptors. For
	 * further information see JSLEE v1.1 Specification Page 305. The
	 * bootstrapContext will be set in entityCreated() method.
	 */
	private transient BootstrapContext bootstrapContext = null;

	/**
	 * The SLEE endpoint defines the contract between the SLEE and the resource
	 * adaptor that enables the resource adaptor to deliver events
	 * asynchronously to SLEE endpoints residing in the SLEE. This contract
	 * serves as a generic contract that allows a wide range of resources to be
	 * plugged into a SLEE environment via the resource adaptor architecture.
	 * For further information see JSLEE v1.1 Specification Page 307 The
	 * sleeEndpoint will be initialized in entityCreated() method.
	 */
	private transient SleeEndpoint sleeEndpoint = null;

	/**
	 * the EventLookupFacility is used to look up the event id of incoming
	 * events
	 */
	private transient EventLookupFacility eventLookup = null;

	/**
	 * The map of activities
	 */
	private transient ConcurrentHashMap<ActivityHandle,Object> activities = null;

	private transient RequestLock requestLock = null;

	/**
	 * the activity context interface factory defined in
	 * HttpServletRaActivityContextInterfaceFactoryImpl
	 */
	private transient HttpServletRaActivityContextInterfaceFactory acif = null;

	private HttpServletResourceEntryPoint resourceEntryPoint;

	private HttpServletRaSbbInterfaceImpl httpRaSbbinterface;

	/**
	 * caches the eventIDs, avoiding lookup in container
	 */
	private transient static final EventIDCache eventIdCache = new EventIDCache();

	/**
	 * tells the RA if an event with a specified ID should be filtered or not
	 */
	private transient static final EventIDFilter eventIDFilter = new EventIDFilter();
	
	/**
	 * ...
	 */
	public HttpServletResourceAdaptor() {}

	/**
	 * Implements javax.slee.resource.ResourceAdaptor. Please refer to JSLEE
	 * v1.1 Specification Page 298 for further information. <br>
	 * This method is called by the SLEE when a resource adaptor object instance
	 * is bootstrapped, either when a resource adaptor entity is created or
	 * during SLEE startup. The SLEE implementation will construct the resource
	 * adaptor object and then invoke the entityCreated method before any other
	 * operations can be invoked on the resource adaptor object.
	 */
	public void entityCreated(BootstrapContext bootstrapContext)
			throws ResourceException {
		
		if (logger.isInfoEnabled()) {
			logger.info("entityCreated()");
		}
		
		this.bootstrapContext = bootstrapContext;
		this.sleeEndpoint = bootstrapContext.getSleeEndpoint();
		this.eventLookup = bootstrapContext.getEventLookupFacility();
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 299 for further information. <br>
	 * This method is called by the SLEE when a resource adaptor object instance
	 * is being removed, either when a resource adaptor entity is deleted or
	 * during SLEE shutdown. When receiving this invocation the resource adaptor
	 * object is expected to close any system resources it has allocated.
	 */
	public void entityRemoved() {}

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
		
		if (logger.isDebugEnabled()) {
			logger.debug("entityActivated() ");
		}
		
		try {
			
			// SO EACH ACTIVITY CAN HAVE ACCESS TO SOME NEEDED METHODS DEFINED
			// BY THIS INTERFACE, THIS WAY WE DONT HAVE TO WORRY ABOUT PASSING
			// IT TO CONSTRUCTOR.
			initializeNamingContextBindings();
			httpRaSbbinterface = new HttpServletRaSbbInterfaceImpl(this);
			activities = new ConcurrentHashMap();
			requestLock = new RequestLock();

		} catch (NamingException e) {
			throw new javax.slee.resource.ResourceException(
					"entityActivated(): Failed to activate HttpServlet RA",
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
		
		if (logger.isDebugEnabled()) {
			logger
			.debug("entityDeactivated(): ending all activities");
		}
		
		for(ActivityHandle handle: activities.keySet()) {
			try {
				sleeEndpoint.activityEnding(handle);
			} catch (UnrecognizedActivityException uae) {
				logger.error("failed to indicate activity has ended",
					uae);
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("entityDeactivated(): cleaning naming context");
		}
		try {
			cleanNamingContextBindings();
		} catch (NamingException e) {
			logger.error("failed to clean naming context",e);
		}
		
		if (logger.isDebugEnabled()) {
			logger
			.debug("entityDeactivated() completed");
		}
	}

	/**
	 * This method is called in context of project Mobicents in context of
	 * resource adaptor deactivation. More precisely,
	 * org.mobicents.slee.resource.ResourceAdaptorEntity.deactivate() calls this
	 * method entityDeactivating() PRIOR to invoking entityDeactivated(). This
	 * method signals the resource adaptor the transition from state "ACTIVE" to
	 * state "STOPPING".
	 */
	public void entityDeactivating() {}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 300 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor object that the
	 * specified event was processed successfully by the SLEE. An event is
	 * considered to be processed successfully if the SLEE has attempted to
	 * deliver the event to all interested SBBs.
	 */
	public void eventProcessingSuccessful(ActivityHandle activityHandle,
			Object event, int param, Address address, int flags) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("eventProcessingSuccessful: activityHandle="+activityHandle+", event="+event);
		}
		
		if (event instanceof HttpServletRequestEvent) {
			releaseHttpRequest((HttpServletRequestEvent) event);
		}
	}

	/**
	 * Allows control to be returned back to the servlet conainer, which
	 * delivered the http request. The container will mandatory close the
	 * response stream.
	 * 
	 */
	private void releaseHttpRequest(
			HttpServletRequestEvent hreqEvent) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("releaseHttpRequest() enter");
		}
		
		Object lock = requestLock.removeLock(hreqEvent);
		if (lock != null) {
			synchronized (lock) {
				lock.notify();
			}
		}
		else {
			logger.warn("unable to wake up blocked servlet thread, did not found the lock for event "+hreqEvent);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("releaseHttpRequest() exit");
		}
	}

	

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 300 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor object that the
	 * specified event was processed unsuccessfully by the SLEE. Event
	 * processing can fail if, for example, the SLEE doesn�t have enough
	 * resource to process the event, a SLEE node fails during event processing
	 * or a system level failure prevents the SLEE from committing transactions.
	 */
	public void eventProcessingFailed(ActivityHandle activityHandle,
			Object event, int param, Address address, int flags,
			FailureReason failureReason) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("eventProcessingFailed: activityHandle="+activityHandle+", event="+event);
		}
				
		if (event instanceof HttpServletRequestEvent) {
			releaseHttpRequest((HttpServletRequestEvent) event);
		}
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 301 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor that the SLEE
	 * has completed activity end processing for the activity represented by the
	 * activity handle. The resource adaptor should release any resource related
	 * to this activity as the SLEE will not ask for it again.
	 */
	public void activityEnded(javax.slee.resource.ActivityHandle activityHandle) {
		
		// remove the activity from the list of activities
		activities.remove(activityHandle);
		
		if (logger.isDebugEnabled()) {
			logger
			.debug("activityEnded: activityHandle="+activityHandle);
		}
		
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 301 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor that the
	 * activity�s Activity Context object is no longer attached to any SBB
	 * entities and is no longer referenced by any SLEE Facilities. 
	 */
	public void activityUnreferenced(
			javax.slee.resource.ActivityHandle activityHandle) {}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 301 for further information. <br>
	 * The SLEE calls this method to query if a specific activity belonging to
	 * this resource adaptor object is alive.
	 */
	public void queryLiveness(javax.slee.resource.ActivityHandle activityHandle) {
		if (activityHandle instanceof HttpServletActivityHandle && !activities.containsKey(activityHandle)) {
			// seems an activity is lost
			try {
				sleeEndpoint.activityEnding(activityHandle);
			} catch (UnrecognizedActivityException uae) {
				logger.error("Failed to end a leaked activity",
						uae);
			}
		}
	}

	public Object getActivity(ActivityHandle activityHandle) {
		return activities.get(activityHandle);
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 301 for further information. <br>
	 * The SLEE calls this method to get an activity handle for an activity
	 * created by the underlying resource. This method is invoked by the SLEE
	 * when it needs to construct an activity context for an activity via an
	 * activity context interface factory method invoked by an SBB.
	 */
	public javax.slee.resource.ActivityHandle getActivityHandle(Object object) {
		if (object instanceof HttpSessionActivityImpl) {
			HttpServletActivityHandle ah = new HttpServletActivityHandle(((HttpSessionActivityImpl)object).getSessionId());
			if (activities.containsKey(ah)) {
				return ah;
			}
			else {
				return null;
			}
		}
		else if (object instanceof HttpServletRequestActivityImpl) {
			HttpServletActivityHandle ah = new HttpServletActivityHandle(((HttpServletRequestActivityImpl)object).getRequestID());
			if (activities.containsKey(ah)) {
				return ah;
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 302 for further information. <br>
	 * The SLEE calls this method to get access to the underlying resource
	 * adaptor interface that enables the SBB to invoke the resource adaptor, to
	 * send messages for example.
	 */
	public Object getSBBResourceAdaptorInterface(String str) {
		// currently HttpServlet RA does not have a provider
		return httpRaSbbinterface;
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 302 for further information. <br>
	 * The SLEE calls this method to get reference to the Marshaler object. The
	 * resource adaptor implements the Marshaler interface. The Marshaler is
	 * used by the SLEE to convert between object and distributable forms of
	 * events and event handles.
	 */
	public javax.slee.resource.Marshaler getMarshaler() {
		return null;
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 302 for further information. <br>
	 * The SLEE calls this method to signify to the resource adaptor that a
	 * service has been installed and is interested in a specific set of events.
	 * The SLEE passes an event filter which identifies a set of event types
	 * that services in the SLEE are interested in. The SLEE calls this method
	 * once a service is installed.
	 */
	public void serviceInstalled(String serviceID, int[] eventIDs,
			String[] resourceOptions) {		
		eventIDFilter.serviceInstalled(serviceID, eventIDs);
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 303 for further information. <br>
	 * The SLEE calls this method to signify that a service has been
	 * un-installed in the SLEE. The event types associated to the service key
	 * are no longer of interest to a particular application.
	 */
	public void serviceUninstalled(String serviceID) {
		eventIDFilter.serviceUninstalled(serviceID);
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 303 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor that a service
	 * has been activated and is interested in the event types associated to the
	 * service key. The service must be installed with the resource adaptor via
	 * the serviceInstalled method before it can be activated.
	 */
	public void serviceActivated(String serviceID) {
		eventIDFilter.serviceActivated(serviceID);
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 304 for further information. <br>
	 * The SLEE calls this method to inform the SLEE that a service has been
	 * deactivated and is no longer interested in the event types associated to
	 * the service key.
	 */
	public void serviceDeactivated(String serviceID) {
		eventIDFilter.serviceDeactivated(serviceID);
	}

	// set up the JNDI naming context
	private void initializeNamingContextBindings() throws NamingException {
		// get the reference to the SLEE container from JNDI
		SleeContainer container = SleeContainer.lookupFromJndi();
		// get the entities name
		String entityName = bootstrapContext.getEntityName();

		ResourceAdaptorEntity resourceAdaptorEntity = ((ResourceAdaptorEntity) container
				.getResourceAdaptorEnitity(entityName));
		ResourceAdaptorTypeID raTypeId = resourceAdaptorEntity
				.getInstalledResourceAdaptor().getRaType()
				.getResourceAdaptorTypeID();

		// Bind ACI Factory in JNDI
		acif = new HttpServletRaActivityContextInterfaceFactoryImpl(
				resourceAdaptorEntity.getServiceContainer(), entityName, this);
		// set the ActivityContextInterfaceFactory
		resourceAdaptorEntity.getServiceContainer()
				.getActivityContextInterfaceFactories().put(raTypeId, acif);

		try {
			if (this.acif != null) {
				// parse the string =
				// java:slee/resources/http-servlet-ra/http-servlet-ra-acif
				String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) acif)
						.getJndiName();
				int begind = jndiName.indexOf(':');
				int toind = jndiName.lastIndexOf('/');
				String prefix = jndiName.substring(begind + 1, toind);
				String name = jndiName.substring(toind + 1);
				logger.debug("========================== jndiName prefix ="
						+ prefix + "; jndiName = " + name + " ; ACIF: " + acif
						+ "=========================");
				SleeContainer.registerWithJndi(prefix, name, this.acif);
			}
		} catch (IndexOutOfBoundsException e) {
			// not register with JNDI
			logger.warn(e.getMessage(), e);
		}

		// Bind HttpServletResourceEntryPoint in JNDI. It will be the bridge
		// between
		// a designated HttpServlet and the RA
		resourceEntryPoint = new HttpServletResourceEntryPoint(entityName, this);

		try {
			if (this.resourceEntryPoint != null) {
				// parse the string =
				// java:slee/resources/http-servlet-ra/http-servlet-resource-entry-point
				String jndiName = resourceEntryPoint.getJndiName();
				int begind = jndiName.indexOf(':');
				int toind = jndiName.lastIndexOf('/');
				String prefix = jndiName.substring(begind + 1, toind);
				String name = jndiName.substring(toind + 1);
				logger.debug("========================== jndiName prefix ="
						+ prefix + "; jndiName = " + name
						+ " ; Resource Entry Point: " + resourceEntryPoint
						+ "=========================");
				SleeContainer.registerWithJndi(prefix, name,
						this.resourceEntryPoint);
			}
		} catch (IndexOutOfBoundsException e) {
			// not register with JNDI
			logger.warn(e.getMessage(), e);
		}

	}

	// clean the JNDI naming context
	private void cleanNamingContextBindings() throws NamingException {
		try {
			if (this.acif != null) {
				// parse the string
				String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) this.acif)
						.getJndiName();
				// remove "java:" prefix
				int begind = jndiName.indexOf(':');
				String javaJNDIName = jndiName.substring(begind + 1);
				logger.debug("=================== JNDI name to unregister: "
						+ javaJNDIName + " ====================");
				SleeContainer.unregisterWithJndi(javaJNDIName);
				logger
						.debug("====================== JNDI name unregistered.==========================");
			}

			if (this.resourceEntryPoint != null) {
				// parse the string =
				String jndiName = resourceEntryPoint.getJndiName();
				// remove "java:" prefix
				int begind = jndiName.indexOf(':');
				String javaJNDIName = jndiName.substring(begind + 1);
				logger.debug("=================== JNDI name to unregister: "
						+ javaJNDIName + " ====================");
				SleeContainer.unregisterWithJndi(javaJNDIName);
				logger
						.debug("====================== JNDI name unregistered.==========================");
			}

		} catch (IndexOutOfBoundsException e) {
			logger.warn(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * Called by
	 * {@link net.java.slee.resource.diameter.HttpServletResourceEntryPoint}
	 * class when http request is received.
	 * 
	 */
	public void onRequest(HttpServletRequest request,
			HttpServletResponse response) {

		HttpServletActivityHandle ah = null;

		// if session exists event will be fired on it's activity 
		HttpSession session = request.getSession(false);
		Object activity = null;
		boolean addedActivity = true;
		if (session == null) {	
			// create and store request activity
			HttpServletRequestActivityImpl httpServletRequestActivityImpl = new HttpServletRequestActivityImpl();
			ah = new HttpServletActivityHandle(httpServletRequestActivityImpl.getRequestID());			
			activities.put(ah, httpServletRequestActivityImpl);
			activity = httpServletRequestActivityImpl;
		}
		else {
			// create activity
			activity = new HttpSessionActivityImpl(session.getId());
			ah = new HttpServletActivityHandle(session.getId());
			// try to put in in the map, may already exist
			if (activities.putIfAbsent(ah,activity)==null) {
				// Set the HttpServletResourceEntryPoint in Session so that HttpServletRaSessionListener can
				// retrieve it latter to end the HttpSessionActivity
				session.setAttribute(HttpServletResourceEntryPoint.RA_ENTRY_POINT_PARAM, resourceEntryPoint);					
			}
			else {
				addedActivity = false;
			}
		}
		
		// PathInfo can be empty string and creation of Address will throw
		// exception
		// for empty String hence hardcoding prefix /mobicents
		String pathInfo = "/mobicents" + request.getPathInfo();
		if (logger.isDebugEnabled()) {
			logger.debug("Path Info = " + pathInfo);
		}	

		// WE HAVE ACTIVITY LETS SEND EVENT TO SLEE...
		HttpServletRequestEvent hreqEvent = new HttpServletRequestEventImpl(
				request, response, this);
		
		int eventID = -1;
		try {
			// get event ID by descriptor (event name, vendor, version)
			eventID = eventIdCache.getEventId(eventLookup, hreqEvent, session);

			if (eventID == -1) {
				// Silently drop the message because this is not a registered
				// event type.
				logger.warn("Unable to get ID for event "+eventIdCache.getEventName(hreqEvent, session)+"#"+EventIDCache.VENDOR+"#"+EventIDCache.VERSION);
				return;
			}

			if (eventIDFilter.filterEvent(eventID)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Filtering event: "+ hreqEvent);
				}
				if (addedActivity) {
					activities.remove(ah);
				}
				return;
			}
			
			Address address = new Address(AddressPlan.URI, pathInfo);
			if (logger.isDebugEnabled()) {
				logger.debug("Firing event: "+ hreqEvent);
			}
			Object lock = requestLock.getLock(hreqEvent);
			synchronized (lock) {
				sleeEndpoint.fireEvent(ah, hreqEvent, eventID, address);
				if (session == null) {
					// means it's not a session activity, and we must end it right away
					sleeEndpoint.activityEnding(ah);
				}
				// block thread until event has been processed
				// otherwise jboss web replies to the request
				lock.wait(20000);
			}
			
		} catch (Exception e) {
			logger.error("Failed to fire event into SLEE", e);
		}
	}

	/**
	 * This method is called to end the HttpSession activity
	 * 
	 * @param incomingRequest
	 */
	public void endHttpSessionActivity(String sessionId) {

		if (logger.isDebugEnabled()) {
			logger.debug("endHttpSessionActivity(sessionId="+sessionId+")");
		}
		
		HttpServletActivityHandle ah = new HttpServletActivityHandle(sessionId);

		if (activities.containsKey(ah)) {
			try {
				sleeEndpoint.activityEnding(ah);
			} catch (UnrecognizedActivityException uae) {
				logger.error("Failed to end the HttpSession activity",
						uae);
			}
		}
	}

	protected HttpSessionActivityImpl getHttpSessionActivityImpl(HttpSession httpSession) throws NullPointerException, IllegalStateException, ActivityAlreadyExistsException, CouldNotStartActivityException {
		
		HttpSessionActivityImpl activity = new HttpSessionActivityImpl(httpSession.getId());
		HttpServletActivityHandle ah = new HttpServletActivityHandle(httpSession.getId());
		Object anotherActivity = activities.putIfAbsent(ah,activity);
		if (anotherActivity == null) {
			sleeEndpoint.activityStarted(ah);
			 // Set the HttpServletResourceEntryPoint in Session so that HttpServletRaSessionListener can
			 // retrieve it latter to end the HttpSessionActivity
			httpSession.setAttribute(HttpServletResourceEntryPoint.RA_ENTRY_POINT_PARAM, resourceEntryPoint);
			return activity;
		}
		else {
			return (HttpSessionActivityImpl) anotherActivity;
		}
	}
}
