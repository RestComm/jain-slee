package org.mobicents.client.slee.resource.http;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorContext;

import net.java.client.slee.resource.http.HttpClientActivity;
import net.java.client.slee.resource.http.HttpClientResourceAdaptorSbbInterface;
import net.java.client.slee.resource.http.event.Response;
import net.java.client.slee.resource.http.event.ResponseEvent;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;

public class HttpClientResourceAdaptor implements ResourceAdaptor {

	private ResourceAdaptorContext resourceAdaptorContext;
	private ConcurrentHashMap<HttpClientActivityHandle, HttpClientActivity> activities;
	private HttpClientResourceAdaptorSbbInterface sbbInterface;
	private ExecutorService executorService;
	private Tracer tracer;

	// caching the only event this ra fires
	private FireableEventType fireableEventType;
	
	// LIFECYCLE METHODS
	
	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#setResourceAdaptorContext(javax.slee.resource.ResourceAdaptorContext)
	 */
	public void setResourceAdaptorContext(ResourceAdaptorContext arg0) {
		resourceAdaptorContext = arg0;
		tracer = resourceAdaptorContext.getTracer(HttpClientResourceAdaptor.class.getSimpleName());
		try {
			fireableEventType = resourceAdaptorContext.getEventLookupFacility().getFireableEventType(new EventTypeID("net.java.client.slee.resource.http.event.ResponseEvent",
					"net.java.client.slee", "1.0"));
		} catch (Throwable e) {
			throw new RuntimeException(e.getMessage(),e);
		}
	}	
	
	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raConfigure(javax.slee.resource.ConfigProperties)
	 */
	public void raConfigure(ConfigProperties arg0) {
		// no config
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raActive()
	 */
	public void raActive() {
		activities = new ConcurrentHashMap<HttpClientActivityHandle, HttpClientActivity>();
		executorService = Executors.newCachedThreadPool();
		sbbInterface = new HttpClientResourceAdaptorSbbInterfaceImpl(this);
		tracer.info("entity activated.");
	}
	
	
	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raStopping()
	 */
	public void raStopping() {
		// nothing to do
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raInactive()
	 */
	public void raInactive() {
		activities.clear();
		activities = null;
		executorService.shutdown();
		executorService = null;
		sbbInterface = null;
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raUnconfigure()
	 */
	public void raUnconfigure() {
		// nothing to do
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#unsetResourceAdaptorContext()
	 */
	public void unsetResourceAdaptorContext() {
		resourceAdaptorContext = null;
		tracer = null;
	}
	
	// CONFIG MANAGENT
	
	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raVerifyConfiguration(javax.slee.resource.ConfigProperties)
	 */
	public void raVerifyConfiguration(ConfigProperties arg0)
			throws InvalidConfigurationException {
		// no config
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raConfigurationUpdate(javax.slee.resource.ConfigProperties)
	 */
	public void raConfigurationUpdate(ConfigProperties arg0) {
		// no config	
	}
	
	// EVENT FILTERING
	
	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#serviceActive(javax.slee.resource.ReceivableService)
	 */
	public void serviceActive(ReceivableService arg0) {
		// no event filtering
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#serviceStopping(javax.slee.resource.ReceivableService)
	 */
	public void serviceStopping(ReceivableService arg0) {
		// no event filtering	
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#serviceInactive(javax.slee.resource.ReceivableService)
	 */
	public void serviceInactive(ReceivableService arg0) {
		// no event filtering
	}	
	
	// ACCESS INTERFACE
		
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getResourceAdaptorInterface(java.lang.String)
	 */
	public Object getResourceAdaptorInterface(String arg0) {
		return sbbInterface;
	};
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getMarshaler()
	 */
	public Marshaler getMarshaler() {
		return null;
	}
	
	// MANDATORY CALLBACKS
	
	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#administrativeRemove(javax.slee.resource.ActivityHandle)
	 */
	public void administrativeRemove(ActivityHandle arg0) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getActivity(javax.slee.resource.ActivityHandle)
	 */
	public Object getActivity(ActivityHandle activityHandle) {
		return activities.get(activityHandle);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getActivityHandle(java.lang.Object)
	 */
	public ActivityHandle getActivityHandle(Object arg0) {
		if (arg0 instanceof HttpClientActivityImpl) {
			HttpClientActivityHandle handle = new HttpClientActivityHandle(((HttpClientActivityImpl)arg0).getSessionId());
			if (activities.containsKey(handle)) {
				return handle;
			}
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#queryLiveness(javax.slee.resource.ActivityHandle)
	 */
	public void queryLiveness(ActivityHandle arg0) {
		// if the activity is not in the map end it, its a leak
		if (!activities.contains(arg0)) {
			resourceAdaptorContext.getSleeEndpoint().endActivity(arg0);
		}
	}
	
	// OPTIONAL CALLBACKS
	
	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#eventProcessingSuccessful(javax.slee.resource.ActivityHandle, javax.slee.resource.FireableEventType, java.lang.Object, javax.slee.Address, javax.slee.resource.ReceivableService, int)
	 */
	public void eventProcessingSuccessful(ActivityHandle arg0,
			FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5) {
		// not used		
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#eventProcessingFailed(javax.slee.resource.ActivityHandle, javax.slee.resource.FireableEventType, java.lang.Object, javax.slee.Address, javax.slee.resource.ReceivableService, int, javax.slee.resource.FailureReason)
	 */
	public void eventProcessingFailed(ActivityHandle arg0,
			FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5, FailureReason arg6) {
		// not used
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#eventUnreferenced(javax.slee.resource.ActivityHandle, javax.slee.resource.FireableEventType, java.lang.Object, javax.slee.Address, javax.slee.resource.ReceivableService, int)
	 */
	public void eventUnreferenced(ActivityHandle arg0, FireableEventType arg1,
			Object arg2, Address arg3, ReceivableService arg4, int arg5) {
		// not used
	}
		
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#activityEnded(javax.slee.resource.ActivityHandle)
	 */
	public void activityEnded(ActivityHandle activityHandle) {
		if (tracer.isFineEnabled()) {
			tracer.fine("activityEnded( handle = "+activityHandle+")");
		}
		activities.remove(activityHandle);
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#activityUnreferenced(javax.slee.resource.ActivityHandle)
	 */
	public void activityUnreferenced(ActivityHandle arg0) {
		// not used
	}	

	// OWN METHODS
	
	/**
	 * Retrieves the ra context
	 * @return
	 */
	public ResourceAdaptorContext getResourceAdaptorContext() {
		return resourceAdaptorContext;
	}
	
	/**
	 * Retrieves the executor service
	 */
	public ExecutorService getExecutorService() {
		return executorService;
	}

	/**
	 * Maps the specified activity to the specified handle
	 * @param activityHandle
	 * @param activity
	 */
	public void addActivity(HttpClientActivityHandle activityHandle,
			HttpClientActivity activity) {
		activities.put(activityHandle, activity);
	}

	/**
	 * Ends the specified activity
	 * @param activity
	 */
	public void endActivity(HttpClientActivity activity) {

		final HttpClientActivityHandle ah = new HttpClientActivityHandle(activity
				.getSessionId());

		if (activities.containsKey(ah)) {
			resourceAdaptorContext.getSleeEndpoint().endActivity(ah);			
		}
	}

	/**
	 * Receives an Event from the HTTP client and sends it to the SLEE.
	 * 
	 * @param event
	 * @param activity
	 */
	public void processResponseEvent(ResponseEvent event,
			HttpClientActivity activity) {

		HttpClientActivityHandle ah = new HttpClientActivityHandle(activity
				.getSessionId());

		if (!activities.containsKey(ah)) {
			// New HttpSession activity
			activities.put(ah, activity);
		}

		if (tracer.isInfoEnabled())
			tracer
			.info("==== FIRING ResponseEvent EVENT TO LOCAL SLEE, Event: "
					+ event + " ====");

		try {
			resourceAdaptorContext.getSleeEndpoint().fireEvent(ah,fireableEventType,event,null,null);
		}
		catch (Throwable e) {
			tracer.severe(e.getMessage(),e);
		}
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
				tracer
						.severe(
								"executeMethod failed in AsyncExecuteHttpMethodHandler with HttpException",
								e);
				event = new ResponseEvent(e);

			} catch (IOException e) {
				tracer
						.severe(
								"executeMethod failed in AsyncExecuteHttpMethodHandler with IOException",
								e);
				event = new ResponseEvent(e);

			} catch (Exception e) {
				tracer
						.severe(
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
				endActivity(this.activity);
			}
		}

	}

}
