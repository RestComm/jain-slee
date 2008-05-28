package org.mobicents.slee.resource.http;

import java.util.concurrent.ConcurrentHashMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

import net.java.slee.resource.http.HttpServletRaActivityContextInterfaceFactory;
import net.java.slee.resource.http.HttpSessionActivity;
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
	 * The list of activites stored in this resource adaptor. If this resource
	 * adaptor were a distributed and highly available solution, this storage
	 * were one of the candidates for distribution.
	 */
	private transient ConcurrentHashMap<ActivityHandle,HttpSessionActivity> activities = null;

	private transient RequestLock requestLock = null;

	/**
	 * the activity context interface factory defined in
	 * HttpServletRaActivityContextInterfaceFactoryImpl
	 */
	private transient HttpServletRaActivityContextInterfaceFactory acif = null;

	private HttpServletResourceEntryPoint resourceEntryPoint;

	private HttpServletRaSbbInterfaceImpl httpRaSbbinterface;

	public HttpServletResourceAdaptor() {
		logger
				.debug("============== CREATED HttpServletResourceAdaptor ENTITY ===============");
	}

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
		logger.info("RAFrameResourceAdaptor.entityCreated() called.");
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
	public void entityRemoved() {
		logger
				.info("=================== HttpServletResourceAdaptor entityRemoved METHOD CALLED ===================");
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
			logger
					.debug("=============== ACTIVATING HttpServletResourceAdaptor ENTITY ====================");

			// SO EACH ACTIVITY CAN HAVE ACCESS TO SOME NEEDED METHODS DEFINED
			// BY THIS INTERFACE, THIS WAY WE DONT HAVE TO WORRY ABOUT PASSING
			// IT TO CONSTRUCTOR.
			initializeNamingContextBindings();

			logger
					.debug("=============== CREATING RA SBB INTERFACE ====================");
			httpRaSbbinterface = new HttpServletRaSbbInterfaceImpl();

			activities = new ConcurrentHashMap();
			requestLock = new RequestLock();

		} catch (NamingException e) {
			throw new javax.slee.resource.ResourceException(
					"============================== HttpServletResourceadaptor.entityActivated(): Failed to activate HttpServlet RA! ============================",
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
		logger
				.debug("======================== HttpServlet RA entityDeactivated METHOD CALLED ===================");
		
		logger
				.debug("======================== HttpServlet RA ending all activities ===================");
		for(ActivityHandle handle: activities.keySet()) {
			try {
				sleeEndpoint.activityEnding(handle);
			} catch (UnrecognizedActivityException uae) {
				logger.error("==== GOT UnrecognizedActivityException ==== ",
					uae);
			}
		}
		activities.clear();
		activities = null;
		
		logger
			.info("======================== HttpServlet RA cleaning naming context ===================");
		try {
			cleanNamingContextBindings();
		} catch (NamingException e) {
			logger.error("^^^ HttpServlet RA Cannot unbind naming context ^^^");
		}
		logger
				.debug("========================== HttpServlet RA stopped. ========================");
	}

	/**
	 * This method is called in context of project Mobicents in context of
	 * resource adaptor deactivation. More precisely,
	 * org.mobicents.slee.resource.ResourceAdaptorEntity.deactivate() calls this
	 * method entityDeactivating() PRIOR to invoking entityDeactivated(). This
	 * method signals the resource adaptor the transition from state "ACTIVE" to
	 * state "STOPPING".
	 */
	public void entityDeactivating() {
		logger
				.debug("================== HttpServlet RA entityDeactivating() METHOD CALLED ===================");

	}

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
		logger
				.debug(" ===================== HttpServlet RA eventProcessingSuccessful METHOD CALLED ===================");
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
	private synchronized void releaseHttpRequest(
			HttpServletRequestEvent hreqEvent) {
		logger.debug("releaseHttpRequest() enter");
		Object lock = requestLock.getLock(hreqEvent);
		if (lock != null) {
			synchronized (lock) {
				lock.notify();
			}
			requestLock.removeLock(hreqEvent);
		}

		logger.debug("releaseHttpRequest() exit");
	}

	/**
	 * Blocks the request thread until the request event has been processed in
	 * the SLEE or a timeout expires. Blocking is necessary due to the inherent
	 * blocking nature of servlet containers. As soon as control is returned to
	 * the servlet container it will close the response stream.
	 * 
	 */
	private void blockHttpRequest(HttpServletRequestEvent hreqEvent) {
		try {
			logger.debug("blockRequest() enter");
			Object lock = requestLock.getLock(hreqEvent);
			synchronized (lock) {
				lock.wait(20000);
			}
			logger.debug("blockRequest() exit");
		} catch (InterruptedException e) {
			logger.warn("blockRequest() interrupted", e);
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
		logger
				.debug(" ===================== HttpServlet RA eventProcessingFailed METHOD CALLED ===================");
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
		logger
				.debug(" ===================== HttpServlet RA activityEnded METHOD CALLED ===================");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 301 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor that the
	 * activity�s Activity Context object is no longer attached to any SBB
	 * entities and is no longer referenced by any SLEE Facilities. This enables
	 * the resource adaptor to implicitly end the Activity object.
	 */
	public void activityUnreferenced(
			javax.slee.resource.ActivityHandle activityHandle) {
		// remove the activity from the list of activities
		activities.remove(activityHandle);
		logger
				.debug(" ===================== HttpServlet RA activityUnreferenced METHOD CALLED ===================");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 301 for further information. <br>
	 * The SLEE calls this method to query if a specific activity belonging to
	 * this resource adaptor object is alive.
	 */
	public void queryLiveness(javax.slee.resource.ActivityHandle activityHandle) {
		logger
				.debug(" ===================== HttpServlet RA queryLiveness METHOD CALLED ===================");
	}

	public Object getActivity(ActivityHandle activityHandle) {
		Object act = activities.get(activityHandle);
		logger
				.debug(" ===================== HttpServlet RA getActivity METHOD CALLED ===================");
		return act;
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 301 for further information. <br>
	 * The SLEE calls this method to get an activity handle for an activity
	 * created by the underlying resource. This method is invoked by the SLEE
	 * when it needs to construct an activity context for an activity via an
	 * activity context interface factory method invoked by an SBB.
	 */
	public javax.slee.resource.ActivityHandle getActivityHandle(Object obj) {
		logger
				.debug(" ===================== HttpServlet RA getActivityHandle METHOD CALLED ===================");
		return null;
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 302 for further information. <br>
	 * The SLEE calls this method to get access to the underlying resource
	 * adaptor interface that enables the SBB to invoke the resource adaptor, to
	 * send messages for example.
	 */
	public Object getSBBResourceAdaptorInterface(String str) {
		logger
				.debug(" ===================== HttpServlet RA getSBBResourceAdaptorInterface METHOD CALLED: "
						+ str + " ===================");
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
		logger
				.debug(" ===================== HttpServlet RA getMarshaler METHOD CALLED ===================");
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
	public void serviceInstalled(String str, int[] values, String[] str2) {
		logger
				.debug(" ===================== HttpServlet RA serviceInstalled METHOD CALLED ===================");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 303 for further information. <br>
	 * The SLEE calls this method to signify that a service has been
	 * un-installed in the SLEE. The event types associated to the service key
	 * are no longer of interest to a particular application.
	 */
	public void serviceUninstalled(String str) {
		logger
				.debug(" ===================== HttpServlet RA serviceUninstalled METHOD CALLED: "
						+ str + " ===================");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 303 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor that a service
	 * has been activated and is interested in the event types associated to the
	 * service key. The service must be installed with the resource adaptor via
	 * the serviceInstalled method before it can be activated.
	 */
	public void serviceActivated(String str) {
		logger
				.debug(" ===================== HttpServlet RA serviceActivated METHOD CALLED: "
						+ str + " ===================");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 304 for further information. <br>
	 * The SLEE calls this method to inform the SLEE that a service has been
	 * deactivated and is no longer interested in the event types associated to
	 * the service key.
	 */
	public void serviceDeactivated(String str) {
		logger
				.debug(" ===================== HttpServlet RA serviceDeactivated METHOD CALLED: "
						+ str + "===================");
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
				resourceAdaptorEntity.getServiceContainer(), entityName);
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

		HttpSessionActivityHandle ah = null;

		// Explicitly create Session if it doesnot exist
		HttpSession session = request.getSession();

		HttpSessionActivity incomingRequest = new HttpSessionActivityImpl(this,
				session.getId());

		ah = new HttpSessionActivityHandle(incomingRequest.getSessionId());

		// PathInfo can be empty string and creation of Address will throw
		// exception
		// for empty String hence hardcoding prefix /mobicents
		String pathInfo = "/mobicents" + request.getPathInfo();

		logger.debug("Path Info = " + pathInfo);

		if (activities.get(ah) == null) {
			// New HttpSession activity
			activities.put(ah, incomingRequest);
		}

		// WE HAVE ACTIVITY LETS SEND EVENT TO SLEE...
		HttpServletRequestEvent hreqEvent = new HttpServletRequestEventImpl(
				request, response, this);

		// if (logger.isDebugEnabled()) {
		logger.debug(">->->->  HttpServletRA.onRequest: "
				+ request.getRequestURL());
		// }

		fireEventToSLEE(hreqEvent, request.getMethod(), pathInfo, ah);

		// block thread until event has been processed
		blockHttpRequest(hreqEvent);
	}

	/**
	 * This method is called to end the IncomingHttpServletRequestActivity
	 * activity when SBB calls sendHttpServletResponse() method on
	 * IncomingHttpServletRequestActivity
	 * 
	 * @param incomingRequest
	 */
	public void endHttpSessionActivity(String sessionId) {

		HttpSessionActivityHandle ah = new HttpSessionActivityHandle(sessionId);

		if (activities.get(ah) != null) {

			try {
				sleeEndpoint.activityEnding(ah);
			} catch (UnrecognizedActivityException uae) {
				logger.error("==== GOT UnrecognizedActivityException ==== ",
						uae);
				throw new RuntimeException(
						"<><><> EXCPETION IN Http Servlet RA <><><>", uae);
			}
		}
	}

	protected void fireEventToSLEE(HttpServletRequestEvent event,
			String method, String servletPath, ActivityHandle ah) {
		// assert event != null : "Cannot fire null event";

		int eventID = -1;
		try {
			// get event ID by descriptor (event name, vendor, version)
			eventID = eventLookup.getEventID(
					"net.java.slee.resource.http.events.incoming." + method,
					"net.java.slee", "1.0");

			if (eventID == -1) {
				// Silently drop the message because this is not a registered
				// event type.
				logger.info("===== COULDNT FIND EVENT REGISTRATOIN for: ("
						+ HttpServletRequestEvent.class.getName()
						+ ", net.java.slee, 1.0");
				return;
			}

			Address address = new Address(AddressPlan.URI, servletPath);

			logger.debug("==== FIRING HTTPREQUEST EVENT TO LOCAL SLEE, Event: "
					+ event + " ====");

			sleeEndpoint.fireEvent(ah, event, eventID, address);

		} catch (FacilityException fe) {
			logger.error("===== GOT FACILITY EXCEPTION: ======", fe);
			throw new RuntimeException(
					"<><><> EXCPETION IN Http Servlet RA <><><>", fe);
		} catch (UnrecognizedEventException uee) {
			logger.error("==== GOT UnrecognizedEventException ==== ", uee);
			throw new RuntimeException(
					"<><><> EXCPETION IN Http Servlet RA <><><>", uee);
		} catch (ActivityIsEndingException aiee) {
			logger.error("==== GOT ActivityIsEndingException ==== ", aiee);
			throw new RuntimeException(
					"<><><> EXCPETION IN Http Servlet RA  <><><>", aiee);
		} catch (NullPointerException npe) {
			logger.error("==== GOT NullPointerException ==== ", npe);
			throw new RuntimeException(
					"<><><> EXCPETION IN Http Servlet RA  <><><>", npe);
		} catch (IllegalArgumentException iae) {
			logger.error("==== GOT IllegalArgumentException ==== ", iae);
			throw new RuntimeException(
					"<><><> EXCPETION IN Http Servlet RA <><><>", iae);
		} catch (IllegalStateException ise) {
			logger.error("==== GOT IllegalStateException ==== ", ise);
			throw new RuntimeException(
					"<><><> EXCPETION IN Http Servlet RA <><><>", ise);
		} catch (UnrecognizedActivityException uae) {
			logger.error("==== GOT UnrecognizedActivityException ==== ", uae);
			throw new RuntimeException(
					"<><><> EXCPETION IN Http Servlet RA <><><>", uae);
		}

	}
}
