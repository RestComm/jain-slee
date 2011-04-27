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

package org.mobicents.slee.resource.tftp;

import java.util.concurrent.ConcurrentHashMap;

import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.SLEEException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ActivityIsEndingException;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireEventException;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.IllegalEventException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorContext;
import javax.slee.resource.SleeEndpoint;
import javax.slee.resource.StartActivityException;
import javax.slee.resource.UnrecognizedActivityHandleException;

import net.java.slee.resource.tftp.TransferActivity;
import net.java.slee.resource.tftp.events.RequestEvent;

import org.apache.commons.net.tftp.TFTP;
import org.apache.commons.net.tftp.TFTPPacket;

/**
 * A server resource adaptor for the TFTP protocol.
 * 
 * Inspiration:
 * 	- http-servlet RA by emartins.
 * 	- Apache Commons Net (http://commons.apache.org/net).
 * <P>
 * Shamelessly copied from tftp-client testcode by Dan Armbrust
 * <P>
 * Using the Apache Commons Net TFTP client handling code, this subsystem
 * implements a TFTP server, dressed-up as a JSLEE Resource Adaptor.
 * <P>
 * Thus, it enables Sbb's to intercept TFTP transfer requests and handle them
 * as they see fit.
 * 
 * @author tuijldert
 * <P>
 * TODO: Implement RFC 2347 - Option Extension.
 * TODO: Implement RFC 2349 - timeout interval and transfer size
 */
public class TftpServerResourceAdaptor implements ResourceAdaptor, Runnable {
	private transient Tracer trc;

	private ResourceAdaptorContext raContext;
	private SleeEndpoint sleeEndpoint;

	/**
	 * the EventLookupFacility is used to look up the event id of incoming
	 * events
	 */
	private EventLookupFacility eventLookup;

	/**
	 * caches the eventIDs, avoiding lookup in container
	 */
	private EventIDCache eventIdCache;

	/**
	 * tells the RA if an event with a specified ID should be filtered or not
	 */
	private EventIDFilter eventIDFilter;

	private static final String PORT_CONFIG_PROPERTY = "slee.resource.tftp.port";
    private static final int DEFAULT_TFTP_PORT = 69;
    public static enum ServerMode { GET_ONLY, PUT_ONLY, GET_AND_PUT; }

    private volatile boolean shutdownServer = false;
    private TFTP serverTftp_;
    private int port_;
    private ServerMode mode_;			// TODO: make ServerMode configurable

    private int maxTimeoutRetries_ = 3;
    private int socketTimeout_;
    private Thread serverThread;

	private transient ConcurrentHashMap<TransferActivityHandle, TransferActivityImpl>
				activities = new ConcurrentHashMap<TransferActivityHandle, TransferActivityImpl>();

	public TftpServerResourceAdaptor() { }

	public ResourceAdaptorContext getResourceAdaptorContext() {
		return raContext;
	}

	public SleeEndpoint getSleeEndpoint() {
		return sleeEndpoint;
	}

	// lifecycle methods

	public void setResourceAdaptorContext(ResourceAdaptorContext ctxt) {
		raContext = ctxt;
		trc = ctxt.getTracer(TftpServerResourceAdaptor.class.getSimpleName());

		eventIdCache = new EventIDCache(ctxt.getTracer(EventIDCache.class.getSimpleName()));
		eventIDFilter = new EventIDFilter();
		sleeEndpoint = ctxt.getSleeEndpoint();
		eventLookup = ctxt.getEventLookupFacility();
	}

	public void raConfigure(ConfigProperties properties) {
		port_ = DEFAULT_TFTP_PORT;
		if (properties.getProperty(PORT_CONFIG_PROPERTY) != null)
			port_ = (Integer) properties.getProperty(PORT_CONFIG_PROPERTY).getValue();
		mode_ = ServerMode.GET_AND_PUT;
	}

	public void raActive() {
        serverTftp_ = new TFTP();

        // This is the value used in response to each client.
        socketTimeout_ = serverTftp_.getDefaultTimeout();

        // we want the server thread to listen forever.
        serverTftp_.setDefaultTimeout(0);

        if (trc.isFineEnabled())
        	trc.fine(String.format(
        			"TFTP-server RA starting, listening on port %d, timeout %d",
        			port_, socketTimeout_));
        try {
	        serverTftp_.open(port_);

	        serverThread = new Thread(this);
	        serverThread.setDaemon(true);
	        serverThread.start();
		} catch (Exception e) {
			String msg = "Error initializing tftp server: ";
			trc.severe(msg, e);
			throw new RuntimeException(msg, e);
		}
	}

	public void raStopping() { }

	public void raInactive() {
		shutdown();
	}

	public void raUnconfigure() { }

	public void unsetResourceAdaptorContext() {
		raContext = null;
		trc = null;
		eventIdCache = null;
		eventIDFilter = null;
		sleeEndpoint = null;
		eventLookup = null;
	}

	// config management methods
	public void raVerifyConfiguration(ConfigProperties properties)
			throws javax.slee.resource.InvalidConfigurationException {		
	}

	public void raConfigurationUpdate(ConfigProperties properties) {
		throw new UnsupportedOperationException();
	}

	// event filtering methods

	public void serviceActive(ReceivableService service) {
		eventIDFilter.serviceActive(service);
	}

	public void serviceStopping(ReceivableService service) {
		eventIDFilter.serviceStopping(service);
	}

	public void serviceInactive(ReceivableService service) {
		eventIDFilter.serviceInactive(service);
	}

	// mandatory callbacks

	public void administrativeRemove(ActivityHandle handle) {

	}

	public Object getActivity(ActivityHandle activityHandle) {
		return activities.get(activityHandle);
	}

	public ActivityHandle getActivityHandle(Object activity) {
		TransferActivityHandle handle = null;
		if (activity instanceof TransferActivity) {
			handle = new TransferActivityHandle(((TransferActivity) activity).getTransferID());
			if (!activities.containsKey(handle)) {
				handle = null;
			}
		}
		return handle;
	}

	// optional call-backs
	public void activityEnded(ActivityHandle handle) {

	}

	public void activityUnreferenced(ActivityHandle activityHandle) {

	}

	public void eventProcessingFailed(ActivityHandle arg0,
			FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5, FailureReason arg6) {

	}

	public void eventProcessingSuccessful(ActivityHandle arg0,
			FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5) {

	}

	public void eventUnreferenced(ActivityHandle arg0, FireableEventType arg1,
			Object event, Address arg3, ReceivableService arg4, int arg5) {

	}

	public void queryLiveness(ActivityHandle activityHandle) {
		TFTPTransfer tt = activities.get(activityHandle).getSource();
		if ((tt == null) || !tt.isRunning())
			endActivity(activityHandle);
	}

	// interface accessors

	public Object getResourceAdaptorInterface(String arg0) {
		return null;
	}

	public Marshaler getMarshaler() {
		return null;
	}

	// ra logic

	private TFTPTransfer createTransferActivity(TFTPPacket tftpPacket) throws
					ActivityAlreadyExistsException, NullPointerException,
					IllegalStateException, SLEEException, StartActivityException {

		TransferActivityImpl activity = new TransferActivityImpl();
		TransferActivityHandle handle = new TransferActivityHandle(activity.getTransferID());
		TFTPTransfer tt = new TFTPTransfer(tftpPacket, activity, mode_,
											maxTimeoutRetries_, socketTimeout_, this, trc);
		activity.setSource(tt);
		// lookup the activity and check if already exists
		if (activities.get(handle) == null) {
			activities.put(handle, activity);
			sleeEndpoint.startActivity(handle, activity);
			if (trc.isFineEnabled())
				trc.fine("Started Tftp transfer activity: " + activity.getTransferID());
		} else {
			throw new ActivityAlreadyExistsException(
					"Duplicate transfer activity id: " + activity);
		}
		return tt;
	}

	protected void fireEvent(RequestEvent event, TransferActivity activity, String address)
				throws UnrecognizedActivityHandleException, IllegalEventException,
				ActivityIsEndingException, NullPointerException, SLEEException, FireEventException {
		if (trc.isFineEnabled())
			trc.fine("About to fire event " + EventIDCache.getEventName(event));

		FireableEventType eventType = eventIdCache.getEventType(eventLookup, event);
		sleeEndpoint.fireEvent(getActivityHandle(activity), eventType, event,
						new javax.slee.Address(AddressPlan.IP, address), null);
	}

	protected void endTransferRequestActivity(TransferActivity activity) {
		if (activity != null) {
			if (trc.isFineEnabled())
				trc.fine("Stopped Tftp request activity: " + activity.getTransferID());

			TransferActivityHandle handle = new TransferActivityHandle(activity.getTransferID());
			TFTPTransfer tt = activities.remove(handle).getSource();
			if (tt != null) {
				tt.shutdown();
			}
			endActivity(handle);
		}
	}

	private void endActivity(ActivityHandle handle) {
		try {
			sleeEndpoint.endActivity(handle);
		} catch (Throwable e) {
			trc.severe("Failed to end activity " + handle, e);
		}
	}

    /**
     * The actual tftp server-thread.
     * Fork a transfer-thread for each request received.
     */
    public void run() {
        try {
            while (!shutdownServer) {
                TFTPPacket tftpPacket;

                tftpPacket = serverTftp_.receive();

                TFTPTransfer tt = createTransferActivity(tftpPacket);
                Thread thread = new Thread(tt);
                thread.setDaemon(true);
                thread.start();
            }
        } catch (Exception e) {
            if (!shutdownServer) {
                trc.severe("Unexpected Error in TFTP Server - Server shut down! + " + e);
            }
        } finally {
            shutdownServer = true; // set this to true, so the launching thread can check to see if it started.
            if (serverTftp_ != null && serverTftp_.isOpen()) {
                serverTftp_.close();
            }
        }
    }

    /**
     * Stop the tftp server (and any currently running transfers) and release all
     * opened network resources.
     */
    public void shutdown() {
        shutdownServer = true;

        for (TransferActivityImpl xfer : activities.values())
        	xfer.getSource().shutdown();
        try {
            serverTftp_.close();
        } catch (RuntimeException e) {
            // noop
        }
        try {
            serverThread.join();
        } catch (InterruptedException e) {
            // we've done the best we could, return
        }
    }
}
