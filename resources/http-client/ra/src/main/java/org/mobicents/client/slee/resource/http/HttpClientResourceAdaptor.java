package org.mobicents.client.slee.resource.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.naming.NamingException;
import javax.slee.Address;
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.FailureReason;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.resource.ResourceException;
import javax.slee.resource.SleeEndpoint;

import net.java.client.slee.resource.http.HttpClientActivity;
import net.java.client.slee.resource.http.HttpClientActivityContextInterfaceFactory;
import net.java.client.slee.resource.http.HttpClientResourceAdaptorSbbInterface;
import net.java.client.slee.resource.http.event.Response;
import net.java.client.slee.resource.http.event.ResponseEvent;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.ResourceAdaptorEntity;

public class HttpClientResourceAdaptor implements ResourceAdaptor {

	private static transient Logger logger = Logger
			.getLogger(HttpClientResourceAdaptor.class.getName());

	private transient BootstrapContext bootstrapContext = null;

	private transient SleeEndpoint sleeEndpoint = null;

	private transient EventLookupFacility eventLookup = null;

	private transient HashMap activities = null;

	private transient HttpClientActivityContextInterfaceFactory acif = null;

	private transient HttpClientResourceAdaptorSbbInterface sbbInterface;

	private transient ExecutorService executorService = Executors
			.newCachedThreadPool();

	public void activityEnded(ActivityHandle activityHandle) {
		activities.remove(activityHandle);
		logger.debug("HttpClientResourceAdaptor.activityEnded() called.");

	}

	public void activityUnreferenced(ActivityHandle arg0) {
		logger
				.debug("HttpClientResourceAdaptor.activityUnreferenced() called.");

	}

	public void entityActivated() throws ResourceException {
		logger.debug("HttpClientResourceAdaptor.entityActivated() called.");
		try {
			logger.debug("Starting ");
			try {
				sbbInterface = new HttpClientResourceAdaptorSbbInterfaceImpl(
						this);
				initializeNamingContext();
			} catch (Exception ex) {
				logger
						.error("HttpClientResourceAdaptor.entityActivated(): Exception caught! ");
				ex.printStackTrace();
				throw new ResourceException(ex.getMessage());
			}
			activities = new HashMap();
		} catch (ResourceException e) {
			e.printStackTrace();
			throw new javax.slee.resource.ResourceException(
					"HttpClientResourceAdaptor.entityActivated(): Failed to activate HttpClient Resource Adaptor!",
					e);
		}

	}

	public void entityCreated(BootstrapContext bootstrapContext)
			throws ResourceException {
		logger.debug("HttpClientResourceAdaptor.entityCreated() called.");
		this.bootstrapContext = bootstrapContext;
		this.sleeEndpoint = bootstrapContext.getSleeEndpoint();
		this.eventLookup = bootstrapContext.getEventLookupFacility();

	}

	public void entityDeactivated() {
		this.executorService.shutdown();
		this.executorService = null;

		logger.debug("HttpClientResourceAdaptor.entityDeactivated() called.");
		try {
			cleanNamingContext();
		} catch (NamingException e) {
			logger.error("Cannot unbind naming context");
		}
		logger.debug("HttpClient Resource Adaptor stopped.");

	}

	public void entityDeactivating() {
		logger.debug("HttpClientResourceAdaptor.entityDeactivating() called.");

	}

	public void entityRemoved() {
		logger.debug("HttpClientResourceAdaptor.entityRemoved() called.");

	}

	public void eventProcessingFailed(ActivityHandle arg0, Object arg1,
			int arg2, Address arg3, int arg4, FailureReason arg5) {
		logger
				.debug("HttpClientResourceAdaptor.eventProcessingFailed() called.");

	}

	public void eventProcessingSuccessful(ActivityHandle arg0, Object arg1,
			int arg2, Address arg3, int arg4) {
		logger
				.debug("HttpClientResourceAdaptor.eventProcessingSuccessful() called.");

	}

	public Object getActivity(ActivityHandle activityHandle) {
		logger.debug("HttpClientResourceAdaptor.getActivity() called.");
		return activities.get(activityHandle);
	}

	public ActivityHandle getActivityHandle(Object arg0) {
		logger
				.debug("HttpClientResourceAdaptor.getActivityHandle(obj) called.");
		return null;
	}

	public Marshaler getMarshaler() {
		logger.debug("HttpClientResourceAdaptor.getMarshaler() called.");
		return null;
	}

	public Object getSBBResourceAdaptorInterface(String str) {
		logger
				.debug("HttpClientResourceAdaptor.getSBBResourceAdapterInterface("
						+ str + ") called.");
		return sbbInterface;
	}

	public void queryLiveness(ActivityHandle arg0) {
		logger.debug("HttpClientResourceAdaptor.queryLifeness() called.");

	}

	public void serviceActivated(String arg0) {
		logger.debug("HttpClientResourceAdaptor.serviceActivated() called.");

	}

	public void serviceDeactivated(String arg0) {
		logger.debug("HttpClientResourceAdaptor.serviceDeactivated() called.");

	}

	public void serviceInstalled(String arg0, int[] arg1, String[] arg2) {
		logger.debug("HttpClientResourceAdaptor.serviceInstalled() called.");

	}

	public void serviceUninstalled(String arg0) {
		logger.debug("HttpClientResourceAdaptor.serviceUninstalled() called.");

	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void addActivity(ActivityHandle activityHandle,
			HttpClientActivity activity) {
		activities.put(activityHandle, activity);

	}

	public void endHttpClientActivity(HttpClientActivity activity) {

		HttpClientActivityHandle ah = new HttpClientActivityHandle(activity
				.getSessionId());

		if (activities.get(ah) != null) {

			try {
				sleeEndpoint.activityEnding(ah);
			} catch (UnrecognizedActivityException uae) {
				logger.error("==== GOT UnrecognizedActivityException ==== ",
						uae);
				throw new RuntimeException(
						"<><><> EXCPETION IN Http Client RA <><><>", uae);
			}
		}
	}

	/* Receives an Event and sends it to the SLEE */
	public void processResponseEvent(ResponseEvent event,
			HttpClientActivity activity) {

		HttpClientActivityHandle ah = new HttpClientActivityHandle(activity
				.getSessionId());

		if (activities.get(ah) == null) {
			// New HttpSession activity
			activities.put(ah, activity);
		}

		int eventID = -1;

		try {

			// get event ID by descriptor (event name, vendor, version)
			eventID = eventLookup.getEventID(
					"net.java.client.slee.resource.http.event.ResponseEvent",
					"net.java.client.slee", "1.0");

			if (eventID == -1) {
				// Silently drop the message because this is not a registered
				// event type.
				logger
						.info("===== COULDNT FIND EVENT REGISTRATOIN for: ("
								+ "net.java.client.slee.resource.http.event.ResponseEven"
								+ ", net.java.client.slee, 1.0");
				return;
			}

			logger
					.info("==== FIRING ResponseEvent EVENT TO LOCAL SLEE, Event: "
							+ event + " ====");

			sleeEndpoint.fireEvent(ah, event, eventID, null);
		} catch (Exception e) {
			logger.warn("unable to fire event", e);
		}
	}

	// set up the JNDI naming context
	private void initializeNamingContext() throws NamingException {
		// get the reference to the SLEE container from JNDI
		SleeContainer container = SleeContainer.lookupFromJndi();
		// get the entities name
		String entityName = bootstrapContext.getEntityName();

		ResourceAdaptorEntity resourceAdaptorEntity = ((ResourceAdaptorEntity) container
				.getResourceAdaptorEnitity(entityName));

		ResourceAdaptorTypeID raTypeId = resourceAdaptorEntity
				.getInstalledResourceAdaptor().getRaType()
				.getResourceAdaptorTypeID();

		// create the ActivityContextInterfaceFactory
		acif = new HttpClientActivityContextInterfaceFactoryImpl(
				resourceAdaptorEntity.getServiceContainer(), entityName);

		// set the ActivityContextInterfaceFactory
		resourceAdaptorEntity.getServiceContainer()
				.getActivityContextInterfaceFactories().put(raTypeId, acif);

		try {
			if (this.acif != null) {
				// parse the string =
				// java:slee/resources/HTTPClientRA/http-client-ra-acif
				String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) acif)
						.getJndiName();
				int begind = jndiName.indexOf(':');
				int toind = jndiName.lastIndexOf('/');
				String prefix = jndiName.substring(begind + 1, toind);
				String name = jndiName.substring(toind + 1);
				logger.debug("jndiName prefix =" + prefix + "; jndiName = "
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

	public SleeEndpoint getSleeEndpoint() {
		return sleeEndpoint;
	}

	public BootstrapContext getBootstrapContext() {
		return this.bootstrapContext;
	}

	protected class AsyncExecuteMethodHandler implements Runnable {

		private HttpMethod httpMethod;

		private HttpClient httpClient;

		private HttpClientActivity activity;

		public AsyncExecuteMethodHandler(HttpMethod httpMethod,
				HttpClient httpClient, HttpClientActivity activity) {
			this.httpMethod = httpMethod;
			this.httpClient = httpClient;
			this.activity = activity;
		}

		public void run() {
			ResponseEvent event = null;

			try {
				int statusCode = 0;
				byte[] responseBody = null;
				String responseBodyAsString = null;
				Header[] headers = null;
				Response response = null;

				statusCode = httpClient.executeMethod(httpMethod);
				responseBody = httpMethod.getResponseBody();
				responseBodyAsString = httpMethod.getResponseBodyAsString();
				headers = httpMethod.getResponseHeaders();
				response = new ResponseImpl(responseBody, responseBodyAsString,
						headers, statusCode);

				// create event with response
				event = new ResponseEvent(response);
			} catch (HttpException e) {
				logger
						.error(
								"executeMethod failed in AsyncExecuteHttpMethodHandler with HttpException",
								e);
				event = new ResponseEvent(e);

			} catch (IOException e) {
				logger
						.error(
								"executeMethod failed in AsyncExecuteHttpMethodHandler with IOException",
								e);
				event = new ResponseEvent(e);

			} catch (Exception e) {
				logger
						.error(
								"executeMethod failed in AsyncExecuteHttpMethodHandler with Exception",
								e);
				event = new ResponseEvent(e);
			} finally {
				// Release the connection.
				httpMethod.releaseConnection();
			}
			// process event
			processResponseEvent(event, this.activity);

			// If EndOnReceivingResponse is set to true, end the Activity
			if (this.activity.getEndOnReceivingResponse()) {
				endHttpClientActivity(this.activity);
			}
		}

	}

}
