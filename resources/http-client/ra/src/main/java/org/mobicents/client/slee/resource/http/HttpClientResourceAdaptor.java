/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.client.slee.resource.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.EventFlags;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorContext;

import net.java.client.slee.resource.http.HttpClientActivity;
import net.java.client.slee.resource.http.HttpClientResourceAdaptorSbbInterface;
import net.java.client.slee.resource.http.event.ResponseEvent;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @author amit bhayani
 * 
 */
public class HttpClientResourceAdaptor implements ResourceAdaptor {

	private static final int EVENT_FLAGS = EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK;

	private static final String CFG_PROPERTY_HTTP_CLIENT_FACTORY = "HTTP_CLIENT_FACTORY";
	private static final String CFG_PROPERTY_MAX_CONNECTIONS_FOR_ROUTES = "MAX_CONNECTIONS_FOR_ROUTES";
	private static final String CFG_PROPERTY_MAX_CONNECTIONS_TOTAL = "MAX_CONNECTIONS_TOTAL";
	
	protected ResourceAdaptorContext resourceAdaptorContext;
	private ConcurrentHashMap<HttpClientActivityHandle, HttpClientActivity> activities;
	private HttpClientResourceAdaptorSbbInterface sbbInterface;
	private ExecutorService executorService;
	private Tracer tracer;
	protected HttpClient httpclient;
	protected volatile boolean isActive = false;

	// caching the only event this ra fires
	private FireableEventType fireableEventType;
	
	// configuration
	private int maxTotal;
	private Map<HttpRoute, Integer> maxForRoutes;
	private HttpClientFactory httpClientFactory;

	// LIFECYCLE METHODS

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#setResourceAdaptorContext(javax.slee
	 * .resource.ResourceAdaptorContext)
	 */
	public void setResourceAdaptorContext(ResourceAdaptorContext arg0) {
		resourceAdaptorContext = arg0;
		tracer = resourceAdaptorContext.getTracer(HttpClientResourceAdaptor.class.getSimpleName());
		try {
			fireableEventType = resourceAdaptorContext.getEventLookupFacility().getFireableEventType(
					new EventTypeID("net.java.client.slee.resource.http.event.ResponseEvent", "net.java.client.slee", "4.0"));
		} catch (Throwable e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		sbbInterface = new HttpClientResourceAdaptorSbbInterfaceImpl(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.resource.ResourceAdaptor#raConfigure(javax.slee.resource.
	 * ConfigProperties)
	 */
	@SuppressWarnings("unchecked")
	public void raConfigure(ConfigProperties properties) {
		String httpClientFactoryClassName = (String)properties.getProperty(CFG_PROPERTY_HTTP_CLIENT_FACTORY).getValue();
		if (httpClientFactoryClassName.isEmpty()) {
			maxTotal = (Integer)properties.getProperty(CFG_PROPERTY_MAX_CONNECTIONS_TOTAL).getValue();
			maxForRoutes = new HashMap<HttpRoute, Integer>();
			String maxForRoutesString = (String)properties.getProperty(CFG_PROPERTY_MAX_CONNECTIONS_FOR_ROUTES).getValue();
			for(String maxForRoute : maxForRoutesString.split(",")) {
				if(maxForRoute.isEmpty()) {
					continue;
				}
				String[] maxForRouteParts = maxForRoute.split(":");
				maxForRoutes.put(new HttpRoute(new HttpHost(maxForRouteParts[0])), Integer.valueOf(maxForRouteParts[1]));
			}
		}
		else {
			try {
				httpClientFactory = ((Class<? extends HttpClientFactory>) Class.forName(httpClientFactoryClassName)).newInstance();
			} catch (Exception e) {
				tracer.severe("failed to load http client factory class",e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.resource.ResourceAdaptor#raActive()
	 */
	public void raActive() {
		activities = new ConcurrentHashMap<HttpClientActivityHandle, HttpClientActivity>();
		executorService = Executors.newCachedThreadPool();
		if (httpClientFactory != null) {
			httpclient = httpClientFactory.newHttpClient();
		}
		else {
			HttpParams params = new SyncBasicHttpParams();
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
			schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
			ThreadSafeClientConnManager threadSafeClientConnManager = new ThreadSafeClientConnManager(schemeRegistry);
			threadSafeClientConnManager.setMaxTotal(maxTotal);
			for (Entry<HttpRoute, Integer> entry : maxForRoutes.entrySet()) {
				threadSafeClientConnManager.setMaxForRoute(entry.getKey(), entry.getValue());
			}
			httpclient = new DefaultHttpClient(threadSafeClientConnManager,params);
		}
		isActive = true;
		if (tracer.isInfoEnabled()) {
			tracer.info(String.format("HttpClientResourceAdaptor=%s entity activated.", this.resourceAdaptorContext.getEntityName()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.resource.ResourceAdaptor#raStopping()
	 */
	public void raStopping() {
		this.isActive = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.resource.ResourceAdaptor#raInactive()
	 */
	public void raInactive() {
		this.isActive = false;
		activities.clear();
		activities = null;
		executorService.shutdown();
		executorService = null;

		this.httpclient.getConnectionManager().shutdown();

		this.httpclient = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.resource.ResourceAdaptor#raUnconfigure()
	 */
	public void raUnconfigure() {
		// nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.resource.ResourceAdaptor#unsetResourceAdaptorContext()
	 */
	public void unsetResourceAdaptorContext() {
		resourceAdaptorContext = null;
		tracer = null;
		sbbInterface = null;
	}

	// CONFIG MANAGENT

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#raVerifyConfiguration(javax.slee.
	 * resource.ConfigProperties)
	 */
	@SuppressWarnings("unchecked")
	public void raVerifyConfiguration(ConfigProperties properties) throws InvalidConfigurationException {
		String httpClientFactoryClassName = (String)properties.getProperty(CFG_PROPERTY_HTTP_CLIENT_FACTORY).getValue();
		if (httpClientFactoryClassName.isEmpty()) {
			try {
				Integer i = (Integer) properties.getProperty(CFG_PROPERTY_MAX_CONNECTIONS_TOTAL).getValue();
				if (i<1) {
					throw new InvalidConfigurationException(CFG_PROPERTY_MAX_CONNECTIONS_TOTAL+" must be > 0");
				}
			} catch (InvalidConfigurationException e) {
				throw e;		
			} catch (Exception e) {
				tracer.severe("failure in config validation", e);
				throw new InvalidConfigurationException(e.getMessage());
			}

			try {
		        String maxForRoutesString = (String)properties.getProperty(CFG_PROPERTY_MAX_CONNECTIONS_FOR_ROUTES).getValue();
		        for(String maxForRoute : maxForRoutesString.split(",")) {
		        	if(maxForRoute.isEmpty()) {
		        		continue;
		        	}
		        	String[] maxForRouteParts = maxForRoute.split(":");
		        	new HttpRoute(new HttpHost(maxForRouteParts[0]));
		        	Integer max = Integer.valueOf(maxForRouteParts[1]);
		        	if (max <1) {
						throw new InvalidConfigurationException(CFG_PROPERTY_MAX_CONNECTIONS_FOR_ROUTES+" entries must have max > 0");
		        	}
		        }
			} catch (InvalidConfigurationException e) {
				throw e;		
			} catch (Exception e) {
				tracer.severe("failure in config validation", e);
				throw new InvalidConfigurationException(e.getMessage());
			}
		}
		else {
			try {
				Class<? extends HttpClientFactory> c = (Class<? extends HttpClientFactory>) Class.forName(httpClientFactoryClassName);
				c.newInstance();
			} catch (Exception e) {
				tracer.severe("failed to load http client factory class",e);
				throw new InvalidConfigurationException("failed to load http client factory class",e);
			}
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
		// not supported
	}

	// EVENT FILTERING

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#serviceActive(javax.slee.resource
	 * .ReceivableService)
	 */
	public void serviceActive(ReceivableService arg0) {
		// no event filtering
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#serviceStopping(javax.slee.resource
	 * .ReceivableService)
	 */
	public void serviceStopping(ReceivableService arg0) {
		// no event filtering
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#serviceInactive(javax.slee.resource
	 * .ReceivableService)
	 */
	public void serviceInactive(ReceivableService arg0) {
		// no event filtering
	}

	// ACCESS INTERFACE

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#getResourceAdaptorInterface(java.
	 * lang.String)
	 */
	public Object getResourceAdaptorInterface(String arg0) {
		return sbbInterface;
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.resource.ResourceAdaptor#getMarshaler()
	 */
	public Marshaler getMarshaler() {
		return null;
	}

	// MANDATORY CALLBACKS

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#administrativeRemove(javax.slee.resource
	 * .ActivityHandle)
	 */
	public void administrativeRemove(ActivityHandle arg0) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.resource.ResourceAdaptor#getActivity(javax.slee.resource.
	 * ActivityHandle)
	 */
	public Object getActivity(ActivityHandle activityHandle) {
		return activities.get(activityHandle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#getActivityHandle(java.lang.Object)
	 */
	public ActivityHandle getActivityHandle(Object arg0) {
		if (arg0 instanceof HttpClientActivityImpl) {
			HttpClientActivityHandle handle = new HttpClientActivityHandle(((HttpClientActivityImpl) arg0).getSessionId());
			if (activities.containsKey(handle)) {
				return handle;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#queryLiveness(javax.slee.resource
	 * .ActivityHandle)
	 */
	public void queryLiveness(ActivityHandle arg0) {
		// if the activity is not in the map end it, its a leak
		if (!activities.contains(arg0)) {
			resourceAdaptorContext.getSleeEndpoint().endActivity(arg0);
		}
	}

	// OPTIONAL CALLBACKS

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#eventProcessingSuccessful(javax.slee
	 * .resource.ActivityHandle, javax.slee.resource.FireableEventType,
	 * java.lang.Object, javax.slee.Address,
	 * javax.slee.resource.ReceivableService, int)
	 */
	public void eventProcessingSuccessful(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4, int arg5) {
		// not used
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
	public void eventProcessingFailed(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4, int arg5,
			FailureReason arg6) {
		// not used
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
		if (tracer.isFineEnabled()) {
			tracer.fine(String.format("Event=%s unreferenced", arg2));
		}

		if (arg2 instanceof ResponseEvent) {
			ResponseEvent event = (ResponseEvent) arg2;
			HttpResponse response = event.getHttpResponse();
			
			//May be this event  is carrying Exception and not actual Response in which case
			//skip housekeeping
			if(response != null){
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					this.tracer.severe("Exception while housekeeping. Event unreferenced", e);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#activityEnded(javax.slee.resource
	 * .ActivityHandle)
	 */
	public void activityEnded(ActivityHandle activityHandle) {
		if (tracer.isFineEnabled()) {
			tracer.fine("activityEnded( handle = " + activityHandle + ")");
		}
		activities.remove(activityHandle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#activityUnreferenced(javax.slee.resource
	 * .ActivityHandle)
	 */
	public void activityUnreferenced(ActivityHandle arg0) {
		// not used
	}

	// OWN METHODS

	/**
	 * Retrieves the ra context
	 * 
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
	 * 
	 * @param activityHandle
	 * @param activity
	 */
	public void addActivity(HttpClientActivityHandle activityHandle, HttpClientActivity activity) {
		activities.put(activityHandle, activity);
	}

	/**
	 * Ends the specified activity
	 * 
	 * @param activity
	 */
	public void endActivity(HttpClientActivity activity) {

		final HttpClientActivityHandle ah = new HttpClientActivityHandle(activity.getSessionId());

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
	public void processResponseEvent(ResponseEvent event, HttpClientActivity activity) {

		HttpClientActivityHandle ah = new HttpClientActivityHandle(activity.getSessionId());

		if (tracer.isFineEnabled())
			tracer.fine("==== FIRING ResponseEvent EVENT TO LOCAL SLEE, Event: " + event + " ====");

		try {
			resourceAdaptorContext.getSleeEndpoint().fireEvent(ah, fireableEventType, event, null, null, EVENT_FLAGS);
		} catch (Throwable e) {
			tracer.severe(e.getMessage(), e);
		}
	}

	protected class AsyncExecuteMethodHandler implements Runnable {

		private final HttpRequest httpRequest;
		private final HttpContext httpContext;
		private final HttpHost httpHost;
		private final HttpClientActivity activity;
		private final Object requestApplicationData;

		protected AsyncExecuteMethodHandler(HttpUriRequest request, Object requestApplicationData, HttpClientActivity activity, HttpContext httpContext) {
			this(null, request, requestApplicationData, httpContext, activity);
		}

		protected AsyncExecuteMethodHandler(HttpHost target, HttpRequest request, Object requestApplicationData, HttpContext context, HttpClientActivity activity) {
			this.httpHost = target;
			this.httpRequest = request;
			this.httpContext = context;
			this.activity = activity;
			this.requestApplicationData = requestApplicationData;
		}

		public void run() {
			if(tracer.isFineEnabled()) {
				tracer.fine("Executing Request "+httpRequest);
			}

			ResponseEvent event = null;
			HttpResponse response = null;
			try {

				if (this.httpHost != null) {
					response = httpclient.execute(this.httpHost, this.httpRequest, this.httpContext);
				} else if (this.httpContext != null) {
					response = httpclient.execute((HttpUriRequest) this.httpRequest, this.httpContext);
				}

				if(tracer.isFineEnabled()) {
					tracer.fine("Executed Request "+httpRequest);
				}

				// create event with response
				event = new ResponseEvent(response,requestApplicationData);
			} catch (IOException e) {
				tracer.severe("executeMethod failed in AsyncExecuteHttpMethodHandler with IOException", e);
				event = new ResponseEvent(e, requestApplicationData);

			} catch (Exception e) {
				tracer.severe("executeMethod failed in AsyncExecuteHttpMethodHandler with Exception", e);
				event = new ResponseEvent(e, requestApplicationData);
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
