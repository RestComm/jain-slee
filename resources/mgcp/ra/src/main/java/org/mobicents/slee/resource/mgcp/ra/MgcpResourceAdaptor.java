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

/*
 * MgcpResourceAdaptor.java
 *
 * Media Gateway Control Protocol (MGCP) Resource Adaptor.
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

package org.mobicents.slee.resource.mgcp.ra;

import jain.protocol.ip.mgcp.JainMgcpCommandEvent;
import jain.protocol.ip.mgcp.JainMgcpEvent;
import jain.protocol.ip.mgcp.JainMgcpResponseEvent;
import jain.protocol.ip.mgcp.message.AuditConnection;
import jain.protocol.ip.mgcp.message.AuditEndpoint;
import jain.protocol.ip.mgcp.message.Constants;
import jain.protocol.ip.mgcp.message.CreateConnection;
import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.DeleteConnection;
import jain.protocol.ip.mgcp.message.DeleteConnectionResponse;
import jain.protocol.ip.mgcp.message.EndpointConfiguration;
import jain.protocol.ip.mgcp.message.ModifyConnection;
import jain.protocol.ip.mgcp.message.NotificationRequest;
import jain.protocol.ip.mgcp.message.Notify;
import jain.protocol.ip.mgcp.message.RestartInProgress;
import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.EventName;
import jain.protocol.ip.mgcp.message.parms.RequestedEvent;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityFlags;
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
import javax.slee.resource.SleeEndpoint;

import net.java.slee.resource.mgcp.MgcpConnectionActivity;
import net.java.slee.resource.mgcp.event.TransactionTimeout;

import org.mobicents.protocols.mgcp.stack.JainMgcpStackImpl;
import org.mobicents.protocols.mgcp.stack.JainMgcpStackProviderImpl;

/**
 * 
 * @author Oleg Kulikov
 * @author eduardomartins
 * @author amit bhayani
 */
public class MgcpResourceAdaptor implements ResourceAdaptor {

	/**
	 * the manager of mgcp activities of this ra
	 */
	private MgcpActivityManager mgcpActivityManager = null;
	private ResourceAdaptorContext resourceAdaptorContext;
	private Tracer tracer;
	private int port = 2427;
	private String stackAddress = null;
	private JainMgcpStackImpl stack;
	// private JainMgcpProvider mgcpProvider;
	private final EventIDCache eventIdCache = new EventIDCache();

	/**
	 * This is local proxy of provider. Created and managed by RA, it callbacks RA on events when it processes it and
	 * are ready to fire into SLEE.
	 */
	private JainMgcpProviderImpl mgcpProvider;

	private transient SleeEndpoint sleeEndpoint = null;

	private static final String MGCP_BIND_ADDRESS = "jain.mgcp.IP_ADDRESS";

	private static final String MGCP_BIND_PORT = "jain.mgcp.PORT";

	private transient static final Address address = new Address(AddressPlan.IP, "localhost");

	public static final int ACTIVITY_FLAGS = ActivityFlags.REQUEST_ENDED_CALLBACK;// .NO_FLAGS;

	/**
	 * tells the RA if an event with a specified ID should be filtered or not
	 */
	private final EventIDFilter eventIDFilter = new EventIDFilter();

	private static final int EVENT_FLAGS = getEventFlags();

	private static int getEventFlags() {
		int eventFlags = EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK;
		EventFlags.setRequestProcessingFailedCallback(eventFlags);
		return eventFlags;
	}

	public void activityEnded(ActivityHandle handle) {
		mgcpActivityManager.removeMgcpActivity(handle);

		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("Activity with handle " + handle + " ended");
		}
	}

	public void activityUnreferenced(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	public void administrativeRemove(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	public void eventProcessingFailed(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5, FailureReason arg6) {
		// TODO Auto-generated method stub

	}

	public void eventProcessingSuccessful(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5) {
		// TODO Auto-generated method stub

	}

	public void eventUnreferenced(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5) {
		// TODO Auto-generated method stub

	}

	public Object getActivity(ActivityHandle handle) {
		return mgcpActivityManager.getActivity(handle);
	}

	public ActivityHandle getActivityHandle(Object activity) {
		return mgcpActivityManager.getActivityHandle(activity);
	}

	public Marshaler getMarshaler() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getResourceAdaptorInterface(String arg0) {
		return this.mgcpProvider;
	}

	public void queryLiveness(ActivityHandle handle) {
		// Object activity = mgcpActivityManager.getActivity(handle);

	}

	public void raActive() {
		try {
			InetAddress inetAddress = InetAddress.getByName(this.stackAddress);
			stack = new JainMgcpStackImpl(inetAddress, port);

			mgcpProvider.setProvider((JainMgcpStackProviderImpl) stack.createProvider());
			this.sleeEndpoint = resourceAdaptorContext.getSleeEndpoint();

			mgcpActivityManager = new MgcpActivityManager();
		} catch (Throwable ex) {
			String msg = "error in initializing resource adaptor";
			tracer.severe(msg, ex);
			throw new RuntimeException(msg, ex);
		}

		if (tracer.isFineEnabled()) {
			tracer.fine("MGCP Resource Adaptor entity active.");
		}
	}

	public void raConfigurationUpdate(ConfigProperties arg0) {
		// TODO Auto-generated method stub

	}

	public void raConfigure(ConfigProperties properties) {

		if (tracer.isFineEnabled()) {
			tracer.fine("Configuring RA.");
		}

		this.port = (Integer) properties.getProperty(MGCP_BIND_PORT).getValue();

		this.stackAddress = (String) properties.getProperty(MGCP_BIND_ADDRESS).getValue();
		if (this.stackAddress.equals("null")) {
			this.stackAddress = System.getProperty("jboss.bind.address");
		}

		tracer.info("MGCP RA will bound to " + this.stackAddress + ":" + this.port);
	}

	public void raInactive() {

		try {

			this.mgcpProvider.delete();
			// stack is closed automaticly after above.
			this.stack = null;
		} catch (Throwable ex) {
			String msg = "error in de-activating resource adaptor";
			tracer.severe(msg, ex);
			throw new RuntimeException(msg, ex);
		}

		if (tracer.isFineEnabled()) {
			tracer.fine("MGCP Resource Adaptor entity de-activated.");
		}
	}

	public void raStopping() {
		// TODO Auto-generated method stub

	}

	public void raUnconfigure() {
		// TODO Auto-generated method stub

	}

	public void raVerifyConfiguration(ConfigProperties properties) throws InvalidConfigurationException {
		try {
			// get port
			Integer port = (Integer) properties.getProperty(MGCP_BIND_PORT).getValue();
			// get host
			String stackAddress = (String) properties.getProperty(MGCP_BIND_ADDRESS).getValue();
			if (stackAddress.equals("null")) {
				stackAddress = System.getProperty("jboss.bind.address");
			}
			// try to open socket
			InetSocketAddress sockAddress = new InetSocketAddress(stackAddress, port);
			new DatagramSocket(sockAddress).close();
		} catch (Throwable e) {
			throw new InvalidConfigurationException(e.getMessage(), e);
		}
	}

	public void serviceActive(ReceivableService receivableService) {
		eventIDFilter.serviceActive(receivableService);

	}

	public void serviceInactive(ReceivableService receivableService) {
		eventIDFilter.serviceInactive(receivableService);
	}

	public void serviceStopping(ReceivableService receivableService) {
		eventIDFilter.serviceStopping(receivableService);
	}

	public void setResourceAdaptorContext(ResourceAdaptorContext raContext) {
		this.resourceAdaptorContext = raContext;
		tracer = resourceAdaptorContext.getTracer(MgcpResourceAdaptor.class.getSimpleName());
		mgcpProvider = new JainMgcpProviderImpl(this,tracer);
	}

	public void unsetResourceAdaptorContext() {
		this.resourceAdaptorContext = null;
		mgcpProvider = null;
	}

	public void processMgcpCommandEvent(JainMgcpCommandEvent event) {
		if (tracer.isFineEnabled()) {
			tracer.fine(this.resourceAdaptorContext.getEntityName() + " received event of type "
					+ event.getObjectIdentifier() + ". Request TX ID = " + event.getTransactionHandle());
		}
		switch (event.getObjectIdentifier()) {

		case Constants.CMD_AUDIT_CONNECTION:
			AuditConnection auditConnection = (AuditConnection) event;
			processNonCreateConnectionMgcpEvent(auditConnection.getConnectionIdentifier(), event
					.getEndpointIdentifier(), event.getTransactionHandle(),
					"net.java.slee.resource.mgcp.AUDIT_CONNECTION", event, false);
			break;

		case Constants.CMD_AUDIT_ENDPOINT:
			AuditEndpoint auditEndpoint = (AuditEndpoint) event;
			processEndpointMgcpEvent(auditEndpoint.getEndpointIdentifier(),
					"net.java.slee.resource.mgcp.AUDIT_ENDPOINT", event, false);
			break;

		case Constants.CMD_CREATE_CONNECTION:
			CreateConnection createConnection = (CreateConnection) event;
			processCreateConnectionMgcpEvent(createConnection);
			break;

		case Constants.CMD_DELETE_CONNECTION:
			DeleteConnection deleteConnection = (DeleteConnection) event;
			if (deleteConnection.getConnectionIdentifier() != null) {
				processNonCreateConnectionMgcpEvent(deleteConnection.getConnectionIdentifier(), event
						.getEndpointIdentifier(), event.getTransactionHandle(),
						"net.java.slee.resource.mgcp.DELETE_CONNECTION", event, false);
			} else {
				processEndpointMgcpEvent(deleteConnection.getEndpointIdentifier(),
						"net.java.slee.resource.mgcp.DELETE_CONNECTION", event, false);
			}

			break;

		case Constants.CMD_ENDPOINT_CONFIGURATION:
			EndpointConfiguration endpointConfiguration = (EndpointConfiguration) event;
			processEndpointMgcpEvent(endpointConfiguration.getEndpointIdentifier(),
					"net.java.slee.resource.mgcp.ENDPOINT_CONFIGURATION", event, false);
			break;

		case Constants.CMD_MODIFY_CONNECTION:
			ModifyConnection modifyConnection = (ModifyConnection) event;
			processNonCreateConnectionMgcpEvent(modifyConnection.getConnectionIdentifier(), event
					.getEndpointIdentifier(), event.getTransactionHandle(),
					"net.java.slee.resource.mgcp.MODIFY_CONNECTION", event, false);
			break;

		case Constants.CMD_NOTIFICATION_REQUEST:
			NotificationRequest notificationRequest = (NotificationRequest) event;

			boolean processOnEndpoint = false;
			List<String> connectionIds = new ArrayList<String>();

			RequestedEvent[] requestedEvents = notificationRequest.getRequestedEvents();

			if (requestedEvents != null) {
				for (RequestedEvent requestedEvent : requestedEvents) {
					EventName detectEvent = requestedEvent.getEventName();
					ConnectionIdentifier connectionIdentifier = detectEvent.getConnectionIdentifier();
					if (connectionIdentifier != null) {
						if (!connectionIds.contains(connectionIdentifier.toString())) {
							connectionIds.add(connectionIdentifier.toString());
						}
					} else {
						processOnEndpoint = true;
					}
				}
			}

			EventName[] signalEvents = notificationRequest.getSignalRequests();

			if (signalEvents != null) {
				for (EventName signalEvent : signalEvents) {
					ConnectionIdentifier connectionIdentifier = signalEvent.getConnectionIdentifier();
					if (connectionIdentifier != null) {
						if (!connectionIds.contains(connectionIdentifier.toString())) {
							connectionIds.add(connectionIdentifier.toString());
						}
					} else {
						processOnEndpoint = true;
					}
				}
			}

			for (String s : connectionIds) {
				processNonCreateConnectionMgcpEvent(new ConnectionIdentifier(s), event.getEndpointIdentifier(), event
						.getTransactionHandle(), "net.java.slee.resource.mgcp.NOTIFICATION_REQUEST", event, false);
			}

			if ((requestedEvents == null && signalEvents == null) || processOnEndpoint) {
				processEndpointMgcpEvent(notificationRequest.getEndpointIdentifier(),
						"net.java.slee.resource.mgcp.NOTIFICATION_REQUEST", event, false);
			}
			break;

		case Constants.CMD_NOTIFY:
			Notify notify = (Notify) event;

			boolean processOnEndpointNtfy = false;
			List<String> connectionIdsNtfy = new ArrayList<String>();

			EventName[] observedEvents = notify.getObservedEvents();

			if (observedEvents != null) {
				for (int i = 0; i < observedEvents.length; i++) {
					EventName observedEvent = observedEvents[i];
					ConnectionIdentifier connectionIdentifier = observedEvent.getConnectionIdentifier();
					if (connectionIdentifier != null) {
						if (!connectionIdsNtfy.contains(connectionIdentifier.toString())) {
							connectionIdsNtfy.add(connectionIdentifier.toString());
						}
					} else {
						processOnEndpointNtfy = true;
					}
				}
			} else {
				processOnEndpointNtfy = true;
			}

			for (String s : connectionIdsNtfy) {
				processNonCreateConnectionMgcpEvent(new ConnectionIdentifier(s), notify.getEndpointIdentifier(), notify
						.getTransactionHandle(), "net.java.slee.resource.mgcp.NOTIFY", event, false);
			}

			if (processOnEndpointNtfy) {
				processEndpointMgcpEvent(notify.getEndpointIdentifier(), "net.java.slee.resource.mgcp.NOTIFY", event,
						false);
			}

			break;

		case Constants.CMD_RESTART_IN_PROGRESS:
			RestartInProgress restartInProgress = (RestartInProgress) event;
			processEndpointMgcpEvent(restartInProgress.getEndpointIdentifier(),
					"net.java.slee.resource.mgcp.RESTART_IN_PROGRESS", event, false);
			break;

		default:
			tracer.warning("Unexpected event type: " + event.getObjectIdentifier());
		}
	}

	/**
	 * Processes a Response Event object (acknowledgment to a Command Event object) received from a JainMgcpProvider.
	 * 
	 * @param response
	 *            The JAIN MGCP Response Event Object that is to be processed.
	 */
	public void processMgcpResponseEvent(JainMgcpResponseEvent response, JainMgcpEvent command) {
		if (tracer.isFineEnabled()) {
			tracer.fine("Receive response TX ID = " + response.getTransactionHandle());
		}

		switch (response.getObjectIdentifier()) {
		case Constants.RESP_AUDIT_CONNECTION:
			AuditConnection auditConnection = (AuditConnection) command;
			processNonCreateConnectionMgcpEvent(auditConnection.getConnectionIdentifier(), auditConnection
					.getEndpointIdentifier(), response.getTransactionHandle(),
					"net.java.slee.resource.mgcp.AUDIT_CONNECTION_RESPONSE", response, false);
			break;
		case Constants.RESP_AUDIT_ENDPOINT:
			processEndpointMgcpEvent(((AuditEndpoint) command).getEndpointIdentifier(),
					"net.java.slee.resource.mgcp.AUDIT_ENDPOINT_RESPONSE", response, false);
			break;
		case Constants.RESP_CREATE_CONNECTION:
			ConnectionIdentifier cId = null;
			EndpointIdentifier endpointIdentifier = null;

			// No Need to check if its provisional as Provisional resposnes are
			// taken care at stack level and listeners are not called
			// if (!isProvisional(response.getReturnCode())) {
			CreateConnectionResponse createConnectionResponse = (CreateConnectionResponse) response;
			cId = createConnectionResponse.getConnectionIdentifier();
			endpointIdentifier = createConnectionResponse.getSpecificEndpointIdentifier();
			// }

			if (endpointIdentifier == null) {
				endpointIdentifier = ((CreateConnection) command).getEndpointIdentifier();
			}
			processNonCreateConnectionMgcpEvent(cId, endpointIdentifier, response.getTransactionHandle(),
					"net.java.slee.resource.mgcp.CREATE_CONNECTION_RESPONSE", response, false);
			if (isFailure(response.getReturnCode())) {
				// create connection didn't succeed, end the activity
				endActivity(mgcpActivityManager.getMgcpConnectionActivityHandle(cId, endpointIdentifier, response
						.getTransactionHandle()));
			}
			break;
		case Constants.RESP_DELETE_CONNECTION:
			DeleteConnection deleteConnection = (DeleteConnection) command;
			if (deleteConnection.getConnectionIdentifier() != null) {
				processNonCreateConnectionMgcpEvent(deleteConnection.getConnectionIdentifier(), deleteConnection
						.getEndpointIdentifier(), response.getTransactionHandle(),
						"net.java.slee.resource.mgcp.DELETE_CONNECTION_RESPONSE", response, false);
			} else {
				processEndpointMgcpEvent(deleteConnection.getEndpointIdentifier(),
						"net.java.slee.resource.mgcp.DELETE_CONNECTION_RESPONSE", response, false);

				List<MgcpConnectionActivity> connActivities = this.mgcpProvider
						.getConnectionActivities(deleteConnection.getEndpointIdentifier());

				for (MgcpConnectionActivity connActivitiy : connActivities) {
					processNonCreateConnectionMgcpEvent(new ConnectionIdentifier(connActivitiy
							.getConnectionIdentifier()), connActivitiy.getEndpointIdentifier(), response
							.getTransactionHandle(), "net.java.slee.resource.mgcp.DELETE_CONNECTION_RESPONSE",
							response, false);
				}
			}

			break;
		case Constants.RESP_ENDPOINT_CONFIGURATION:
			processEndpointMgcpEvent(((EndpointConfiguration) command).getEndpointIdentifier(),
					"net.java.slee.resource.mgcp.ENDPOINT_CONFIGURATION_RESPONSE", response, false);
			break;
		case Constants.RESP_MODIFY_CONNECTION:
			ModifyConnection modifyConnection = (ModifyConnection) command;
			processNonCreateConnectionMgcpEvent(modifyConnection.getConnectionIdentifier(), modifyConnection
					.getEndpointIdentifier(), response.getTransactionHandle(),
					"net.java.slee.resource.mgcp.MODIFY_CONNECTION_RESPONSE", response, false);
			break;
		case Constants.RESP_NOTIFICATION_REQUEST:

			NotificationRequest notificationRequest = (NotificationRequest) command;

			boolean processOnEndpoint = false;
			List<String> connectionIds = new ArrayList<String>();
			RequestedEvent[] requestedEvents = notificationRequest.getRequestedEvents();

			if (requestedEvents != null) {
				for (RequestedEvent requestedEvent : requestedEvents) {
					EventName detectEvent = requestedEvent.getEventName();
					ConnectionIdentifier connectionIdentifier = detectEvent.getConnectionIdentifier();
					if (connectionIdentifier != null) {
						if (!connectionIds.contains(connectionIdentifier.toString())) {
							connectionIds.add(connectionIdentifier.toString());
						}
					} else {
						processOnEndpoint = true;
					}
				}
			}

			EventName[] signalEvents = notificationRequest.getSignalRequests();

			if (signalEvents != null) {
				for (int i = 0; i < signalEvents.length; i++) {
					EventName signalEvent = signalEvents[i];
					ConnectionIdentifier connectionIdentifier = signalEvent.getConnectionIdentifier();
					if (connectionIdentifier != null) {
						if (!connectionIds.contains(connectionIdentifier.toString())) {
							connectionIds.add(connectionIdentifier.toString());
						}
					} else {
						processOnEndpoint = true;
					}
				}
			}

			for (String s : connectionIds) {
				processNonCreateConnectionMgcpEvent(new ConnectionIdentifier(s), notificationRequest
						.getEndpointIdentifier(), response.getTransactionHandle(),
						"net.java.slee.resource.mgcp.NOTIFICATION_REQUEST_RESPONSE", response, false);
			}

			if ((requestedEvents == null && signalEvents == null) || processOnEndpoint) {
				processEndpointMgcpEvent(notificationRequest.getEndpointIdentifier(),
						"net.java.slee.resource.mgcp.NOTIFICATION_REQUEST_RESPONSE", response, false);
			}

			break;

		case Constants.RESP_NOTIFY:

			Notify notify = (Notify) command;

			boolean processOnEndpointNtfy = false;
			List<String> connectionIdsNtfy = new ArrayList<String>();

			EventName[] observedEvents = notify.getObservedEvents();

			if (observedEvents != null) {
				for (int i = 0; i < observedEvents.length; i++) {
					EventName observedEvent = observedEvents[i];
					ConnectionIdentifier connectionIdentifier = observedEvent.getConnectionIdentifier();
					if (connectionIdentifier != null) {
						if (!connectionIdsNtfy.contains(connectionIdentifier.toString())) {
							connectionIdsNtfy.add(connectionIdentifier.toString());
						}
					} else {
						processOnEndpointNtfy = true;
					}
				}
			} else {
				processOnEndpointNtfy = true;
			}

			for (String s : connectionIdsNtfy) {
				processNonCreateConnectionMgcpEvent(new ConnectionIdentifier(s), notify.getEndpointIdentifier(), notify
						.getTransactionHandle(), "net.java.slee.resource.mgcp.NOTIFY_RESPONSE", response, false);
			}

			if (processOnEndpointNtfy) {
				processEndpointMgcpEvent(notify.getEndpointIdentifier(), "net.java.slee.resource.mgcp.NOTIFY_RESPONSE",
						response, false);
			}
			break;
		case Constants.RESP_RESTART_IN_PROGRESS:
			processEndpointMgcpEvent(((RestartInProgress) command).getEndpointIdentifier(),
					"net.java.slee.resource.mgcp.RESTART_IN_PROGRESS_RESPONSE", response, false);
			break;
		default:
			tracer.warning("Unexpected event type: " + response.getObjectIdentifier());
		}
	}

	protected void endActivity(ActivityHandle handle) {
		if (handle != null && mgcpActivityManager.containsActivityHandle(handle)) {
			try {
				// send activity end event to the container
				this.getSleeEndpoint().endActivity(handle);
			} catch (Exception e) {
				tracer.severe("Failed to end activity with handle " + handle, e);
			}
		}
	}

	/**
	 * processes a new non {@link CreateConnection} event coming from stack
	 */
	private void processNonCreateConnectionMgcpEvent(ConnectionIdentifier connectionIdentifier,
			EndpointIdentifier endpointIdentifier, int transactionHandle, String eventName, Object eventObject,
			boolean killActivity) {
		// get connection activity handle, create activity if does not exists
		MgcpConnectionActivityHandle handle = mgcpActivityManager.getMgcpConnectionActivityHandle(connectionIdentifier,
				endpointIdentifier, transactionHandle);
		if (handle != null) {
			// fire event
			fireEvent(eventName, handle, eventObject);

			if ((eventObject instanceof DeleteConnection) || (eventObject instanceof DeleteConnectionResponse)
					|| killActivity) {
				try {
					// send activity end event to the container
					getSleeEndpoint().endActivity(handle);
				} catch (Exception e) {
					tracer.severe("Failed to end activity with handle " + handle, e);
				}
			}
		} else {
			FireableEventType eventID = eventIdCache.getEventId(this.resourceAdaptorContext.getEventLookupFacility(),
					eventName);

			if (this.eventIDFilter.isInitialEvent(eventID)) {
				MgcpConnectionActivityImpl newConnectionActivity = (MgcpConnectionActivityImpl) this.mgcpProvider
						.getConnectionActivity(connectionIdentifier, endpointIdentifier, true);

				if (newConnectionActivity != null) {
					handle = newConnectionActivity.getActivityHandle();
					fireEvent(eventName, handle, eventObject);

					if ((eventObject instanceof DeleteConnection)
							|| ((eventObject instanceof DeleteConnectionResponse))) {
						try {
							// send activity end event to the container
							getSleeEndpoint().endActivity(handle);
						} catch (Exception e) {
							tracer.severe("Failed to end activity with handle " + handle, e);
						}
					}

				}

			} else {
				if (tracer.isInfoEnabled()) {

					tracer.info("No MgcpConnectionActivity found for transactionHandle " + transactionHandle
							+ " Not firirng event " + eventName);
				}
			}
		}

	}

	/**
	 * 
	 * processes a new {@link NotificationRequest} event coming from stack
	 * 
	 * @param notificationRequest
	 */
	private void processEndpointMgcpEvent(EndpointIdentifier endpointIdentifier, String eventName, Object eventObject,
			boolean killActivity) {
		// fire on endpoint activity
		MgcpEndpointActivityHandle handle = new MgcpEndpointActivityHandle(endpointIdentifier.toString());
		if (mgcpActivityManager.containsMgcpEndpointActivityHandle(handle)) {
			fireEvent(eventName, handle, eventObject);

			// end activity if delete connection request or response
			if (eventObject instanceof DeleteConnection || eventObject instanceof DeleteConnectionResponse
					|| killActivity) {
				try {
					// send activity end event to the container
					getSleeEndpoint().endActivity(handle);
				} catch (Exception e) {
					tracer.severe("Failed to end activity with handle " + handle, e);
				}
			}
		} else {

			FireableEventType eventID = eventIdCache.getEventId(this.resourceAdaptorContext.getEventLookupFacility(),
					eventName);

			if (this.eventIDFilter.isInitialEvent(eventID)) {
				MgcpEndpointActivityImpl mgcpEndpointActivity = (MgcpEndpointActivityImpl) this.mgcpProvider
						.getEndpointActivity(endpointIdentifier, true);
				if (mgcpEndpointActivity != null) {
					handle = mgcpEndpointActivity.getActivityHandle();
					fireEvent(eventName, handle, eventObject);

					// end activity if delete connection request or response
					if (eventObject instanceof DeleteConnection || eventObject instanceof DeleteConnectionResponse) {
						try {
							// send activity end event to the container
							getSleeEndpoint().endActivity(handle);
						} catch (Exception e) {
							tracer.severe("Failed to end activity with handle " + handle, e);
						}
					}

				}
			} else {
				if (tracer.isInfoEnabled()) {
					tracer.info("No MgcpEndpointActivity found for EndpointIdentifier " + endpointIdentifier
							+ " Not firirng event " + eventName);
				}
			}
		}
	}

	/**
	 * processes a new {@link CreateConnection} event coming from stack
	 * 
	 * @param createConnection
	 */
	private void processCreateConnectionMgcpEvent(CreateConnection createConnection) {
		// fire on new connection activity

		MgcpConnectionActivityImpl newConnectionActivity = (MgcpConnectionActivityImpl) this.mgcpProvider
				.getConnectionActivity(createConnection.getTransactionHandle(), createConnection
						.getEndpointIdentifier(), true);
		MgcpConnectionActivityHandle handle = newConnectionActivity.getActivityHandle();

		fireEvent("net.java.slee.resource.mgcp.CREATE_CONNECTION", handle, createConnection);
	}

	protected SleeEndpoint getSleeEndpoint() {
		return sleeEndpoint;
	}

	protected MgcpActivityManager getMgcpActivityManager() {
		return mgcpActivityManager;
	}

	/**
	 * Indicates the provider will send the specified {@link CreateConnectionResponse} event, which contains the
	 * {@link ConnectionIdentifier} to update the {@link MgcpConnectionActivity} related with the Mgcp transaction.
	 * 
	 * @param event
	 */
	protected void sendingCreateConnectionResponse(CreateConnectionResponse event) {
		mgcpActivityManager.updateMgcpConnectionActivity(event.getTransactionHandle(), event.getConnectionIdentifier(),
				event.getSpecificEndpointIdentifier());
	}

	/**
	 * Check whether the given return code indicates a failure/error.
	 * 
	 * @param rc
	 *            the return code
	 * @return true when the code indicates an error
	 */
	private static boolean isFailure(ReturnCode rc) {
		final int rval = rc.getValue();

		return (rval > 299);
	}

	/**
	 * Processes a timeout occurred in a sent {@link JainMgcpCommandEvent} event transaction, to prevent further
	 * failures the activity will end after the RA fires the {@link TransactionTimeout} event.
	 * 
	 * @param event
	 *            the command sent.
	 */
	public void processTxRxTimeout(JainMgcpCommandEvent event) {
		switch (event.getObjectIdentifier()) {

		case Constants.CMD_AUDIT_CONNECTION:

			AuditConnection auditConnection = (AuditConnection) event;
			processNonCreateConnectionMgcpEvent(auditConnection.getConnectionIdentifier(), auditConnection
					.getEndpointIdentifier(), event.getTransactionHandle(),
					"net.java.slee.resource.mgcp.TRANSACTION_TIMEOUT", new TransactionTimeout(auditConnection), true);
			break;

		case Constants.CMD_AUDIT_ENDPOINT:
			AuditEndpoint auditEndpoint = (AuditEndpoint) event;
			processEndpointMgcpEvent(auditEndpoint.getEndpointIdentifier(),
					"net.java.slee.resource.mgcp.TRANSACTION_TIMEOUT", new TransactionTimeout(auditEndpoint), true);
			break;

		case Constants.CMD_CREATE_CONNECTION:
			EndpointIdentifier endpointIdentifier = null;

			CreateConnection createConnection = (CreateConnection) event;
			endpointIdentifier = createConnection.getEndpointIdentifier();

			processNonCreateConnectionMgcpEvent(null, endpointIdentifier, createConnection.getTransactionHandle(),
					"net.java.slee.resource.mgcp.TRANSACTION_TIMEOUT", new TransactionTimeout(createConnection), true);
			break;

		case Constants.CMD_DELETE_CONNECTION:
			DeleteConnection deleteConnection = (DeleteConnection) event;
			if (deleteConnection.getConnectionIdentifier() != null) {
				processNonCreateConnectionMgcpEvent(deleteConnection.getConnectionIdentifier(), deleteConnection
						.getEndpointIdentifier(), deleteConnection.getTransactionHandle(),
						"net.java.slee.resource.mgcp.TRANSACTION_TIMEOUT", new TransactionTimeout(deleteConnection),
						true);
			} else {
				processEndpointMgcpEvent(deleteConnection.getEndpointIdentifier(),
						"net.java.slee.resource.mgcp.TRANSACTION_TIMEOUT", new TransactionTimeout(deleteConnection),
						true);

				List<MgcpConnectionActivity> connActivities = this.mgcpProvider
						.getConnectionActivities(deleteConnection.getEndpointIdentifier());

				// Should fire TIMEOUT Event to all the ConnectionActivity for this Endpoint
				for (MgcpConnectionActivity connActivitiy : connActivities) {
					processNonCreateConnectionMgcpEvent(new ConnectionIdentifier(connActivitiy
							.getConnectionIdentifier()), deleteConnection.getEndpointIdentifier(), deleteConnection
							.getTransactionHandle(), "net.java.slee.resource.mgcp.TRANSACTION_TIMEOUT",
							new TransactionTimeout(deleteConnection), true);
				}
			}
			break;

		case Constants.CMD_ENDPOINT_CONFIGURATION:
			EndpointConfiguration endpointConfiguration = (EndpointConfiguration) event;
			processEndpointMgcpEvent(endpointConfiguration.getEndpointIdentifier(),
					"net.java.slee.resource.mgcp.TRANSACTION_TIMEOUT", new TransactionTimeout(endpointConfiguration),
					true);
			break;

		case Constants.CMD_MODIFY_CONNECTION:
			ModifyConnection modifyConnection = (ModifyConnection) event;
			processNonCreateConnectionMgcpEvent(modifyConnection.getConnectionIdentifier(), modifyConnection
					.getEndpointIdentifier(), modifyConnection.getTransactionHandle(),
					"net.java.slee.resource.mgcp.TRANSACTION_TIMEOUT", new TransactionTimeout(modifyConnection), true);
			break;

		case Constants.CMD_NOTIFICATION_REQUEST:
			NotificationRequest notificationRequest = (NotificationRequest) event;

			boolean processOnEndpoint = false;
			List<String> connectionIds = new ArrayList<String>();
			RequestedEvent[] requestedEvents = notificationRequest.getRequestedEvents();

			if (requestedEvents != null) {
				for (RequestedEvent requestedEvent : requestedEvents) {
					EventName detectEvent = requestedEvent.getEventName();
					ConnectionIdentifier connectionIdentifier = detectEvent.getConnectionIdentifier();
					if (connectionIdentifier != null) {
						if (!connectionIds.contains(connectionIdentifier.toString())) {
							connectionIds.add(connectionIdentifier.toString());
						}
					} else {
						processOnEndpoint = true;
					}
				}
			}

			EventName[] signalEvents = notificationRequest.getSignalRequests();

			if (signalEvents != null) {
				for (int i = 0; i < signalEvents.length; i++) {
					EventName signalEvent = signalEvents[i];
					ConnectionIdentifier connectionIdentifier = signalEvent.getConnectionIdentifier();
					if (connectionIdentifier != null) {
						if (!connectionIds.contains(connectionIdentifier.toString())) {
							connectionIds.add(connectionIdentifier.toString());
						}
					} else {
						processOnEndpoint = true;
					}
				}
			}

			for (String s : connectionIds) {
				processNonCreateConnectionMgcpEvent(new ConnectionIdentifier(s), notificationRequest
						.getEndpointIdentifier(), notificationRequest.getTransactionHandle(),
						"net.java.slee.resource.mgcp.TRANSACTION_TIMEOUT", new TransactionTimeout(notificationRequest),
						true);
			}

			if ((requestedEvents == null && signalEvents == null) || processOnEndpoint) {
				processEndpointMgcpEvent(notificationRequest.getEndpointIdentifier(),
						"net.java.slee.resource.mgcp.TRANSACTION_TIMEOUT", new TransactionTimeout(notificationRequest),
						true);
			}
			break;

		case Constants.CMD_NOTIFY:
			Notify notify = (Notify) event;

			boolean processOnEndpointNtfy = false;
			List<String> connectionIdsNtfy = new ArrayList<String>();

			EventName[] observedEvents = notify.getObservedEvents();

			if (observedEvents != null) {
				for (int i = 0; i < observedEvents.length; i++) {
					EventName observedEvent = observedEvents[i];
					ConnectionIdentifier connectionIdentifier = observedEvent.getConnectionIdentifier();
					if (connectionIdentifier != null) {
						if (!connectionIdsNtfy.contains(connectionIdentifier.toString())) {
							connectionIdsNtfy.add(connectionIdentifier.toString());
						}
					} else {
						processOnEndpointNtfy = true;
					}
				}
			} else {
				processOnEndpointNtfy = true;
			}

			for (String s : connectionIdsNtfy) {
				processNonCreateConnectionMgcpEvent(new ConnectionIdentifier(s), notify.getEndpointIdentifier(), notify
						.getTransactionHandle(), "net.java.slee.resource.mgcp.TRANSACTION_TIMEOUT",
						new TransactionTimeout(notify), true);
			}

			if (processOnEndpointNtfy) {
				processEndpointMgcpEvent(notify.getEndpointIdentifier(),
						"net.java.slee.resource.mgcp.TRANSACTION_TIMEOUT", new TransactionTimeout(notify), true);
			}
			break;

		case Constants.CMD_RESTART_IN_PROGRESS:
			RestartInProgress restartInProgress = (RestartInProgress) event;
			processEndpointMgcpEvent(restartInProgress.getEndpointIdentifier(),
					"net.java.slee.resource.mgcp.TRANSACTION_TIMEOUT", new TransactionTimeout(restartInProgress), true);
			break;

		default:
			tracer.severe("Unexpected event type: " + event.getObjectIdentifier());
			break;
		}
	}

	/**
	 * Processes a timeout on a received {@link JainMgcpCommandEvent} event transaction, to prevent further failures the
	 * activity will end.
	 * 
	 * @param event
	 *            the command sent.
	 */
	public void processRxTimeout(JainMgcpCommandEvent event) {
		this.processTxRxTimeout(event);
	}
	
	public void processTxTimeout(JainMgcpCommandEvent event) {
		this.processTxRxTimeout(event);
	}

	private void fireEvent(String eventName, ActivityHandle handle, Object event) {

		FireableEventType eventID = eventIdCache.getEventId(this.resourceAdaptorContext.getEventLookupFacility(),
				eventName);

		if (eventID == null) {
			tracer.severe("Event id for " + eventID + " is unknown, cant fire!!!");
		} else {
			this.fireEvent(event, handle, eventID, address, true, false);
		}
	}

	private boolean fireEvent(Object event, ActivityHandle handle, FireableEventType eventID, Address address,
			boolean useFiltering, boolean transacted) {

		if (useFiltering && eventIDFilter.filterEvent(eventID)) {
			if (tracer.isFineEnabled()) {
				tracer.fine("Event " + eventID + " filtered");
			}
		} else {
			if (tracer.isFineEnabled()) {
				tracer.fine("Firing event " + event + " on handle " + handle);
			}
			try {
				if (transacted) {
					this.resourceAdaptorContext.getSleeEndpoint().fireEventTransacted(handle, eventID, event, address,
							null, EVENT_FLAGS);
				} else {
					this.resourceAdaptorContext.getSleeEndpoint().fireEvent(handle, eventID, event, address, null,
							EVENT_FLAGS);
				}
				return true;
			} catch (Exception e) {
				tracer.severe("Error firing event.", e);
			}
		}
		return false;
	}

}
