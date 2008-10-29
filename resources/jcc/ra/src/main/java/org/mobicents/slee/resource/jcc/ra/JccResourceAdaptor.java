/*
 * File Name     : JccResourceAdaptor.java
 *
 * The Java Call Control RA
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
package org.mobicents.slee.resource.jcc.ra;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;
import java.io.Serializable;
import java.util.Enumeration;

import java.util.Properties;

import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.FacilityException;
import javax.slee.resource.ActivityIsEndingException;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.resource.ResourceException;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.SleeEndpoint;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.FailureReason;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.ResourceAdaptorEntity;


import javax.csapi.cc.jcc.JccPeerFactory;
import javax.csapi.cc.jcc.JccPeer;
import javax.csapi.cc.jcc.JccConnectionListener;
import javax.csapi.cc.jcc.JccEvent;
import javax.csapi.cc.jcc.JccCall;
import javax.csapi.cc.jcc.JccConnection;
import javax.csapi.cc.jcc.JccCallEvent;
import javax.csapi.cc.jcc.JccConnectionEvent;

import org.apache.log4j.Logger;

import org.mobicents.slee.resource.jcc.local.JccProviderLocal;
import org.mobicents.slee.resource.jcc.local.JccCallLocal;
import org.mobicents.slee.resource.jcc.local.JccConnectionLocal;
import org.mobicents.slee.resource.jcc.local.JccCallEventLocal;
import org.mobicents.slee.resource.jcc.local.JccConnectionEventLocal;

import org.mobicents.slee.resource.jcc.ratype.JccActivityContextInterfaceFactory;

/**
 *
 * @author Oleg Kulikov
 * @author Pavel Mitrenko
 */
public class JccResourceAdaptor implements ResourceAdaptor, Serializable, JccConnectionListener {

    private transient SleeEndpoint sleeEndpoint = null;
    private transient EventLookupFacility eventLookup = null;
    private transient BootstrapContext bootstrapContext = null;
    private JccProviderLocal provider;
    private Address address = new Address(AddressPlan.IP, "127.0.0.1");
    private Logger logger = Logger.getLogger(JccResourceAdaptor.class);
    private ConcurrentReaderHashMap activities = new ConcurrentReaderHashMap();
    private ConcurrentReaderHashMap handlers = new ConcurrentReaderHashMap();
    private String peerName = null;
    private String configName;
    private JccActivityContextInterfaceFactoryImpl activityContextInterfaceFactory;
    private boolean stopped = false;
    private Thread monitor;

    /** Creates a new instance of JccResourceAdaptor */
    public JccResourceAdaptor() {
    }

    public String getJccPeer() {
        return peerName;
    }

    public void setJccPeer(String jccPeer) {
        this.peerName = jccPeer;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public void activityEnded(ActivityHandle handle) {
        String ID = ((JccConnectionActivityHandle) handle).getID();

        Object activity = activities.remove(ID);
        logger.debug("Removed activity: " + activity + " under key: " + ID);

        JccConnectionActivityHandle h = (JccConnectionActivityHandle) handlers.remove(activity.toString());
        logger.debug("Removed handle: " + h.getID() + " under key: " + activity.toString());
    }

    public void activityUnreferenced(ActivityHandle activityHandle) {
    }

    public void entityActivated() throws ResourceException {
        logger.info("Starting JCC Provider, Jcc Peer Name=" + peerName);
        try {
            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("/" + configName));
            logger.info("Loaded properties: " + properties);

            JccPeer peer = JccPeerFactory.getJccPeer(peerName);

            String conf = "<jcc-inap>";
            Enumeration keys = properties.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                conf += ";" + key + "=" + properties.getProperty(key);
            }

            provider = new JccProviderLocal(peer.getProvider(conf));

            provider.addConnectionListener(this, null);
            provider.addCallListener(this);

            logger.info("JCC Provider started successfuly");
            initializeNamingContext();

            logger.info("Running monitoring thread");
            monitor = new Thread(new ActivityMonitor());
            monitor.start();
        } catch (Exception e) {
            logger.error("Can not start Jcc Provider: ", e);
            throw new ResourceException(e.getMessage());
        }
    }

    public void entityCreated(BootstrapContext bootstrapContext) throws ResourceException {
        this.sleeEndpoint = bootstrapContext.getSleeEndpoint();
        this.eventLookup = bootstrapContext.getEventLookupFacility();
        this.bootstrapContext = bootstrapContext;
    }

    public void entityDeactivated() {
        clearNamingContext();
        stopped = true;
        monitor.interrupt();
    }

    public void entityDeactivating() {
        provider.shutdown();
    }

    public void entityRemoved() {
    }

    public void eventProcessingFailed(ActivityHandle activityHandle, Object obj, int param, Address address, int param4, FailureReason failureReason) {
    }

    public void eventProcessingSuccessful(ActivityHandle activityHandle, Object obj, int param, Address address, int param4) {
    }

    public Object getActivity(ActivityHandle handle) {
        String ID = ((JccConnectionActivityHandle) handle).getID();
        return activities.get(ID);
    }

    public ActivityHandle getActivityHandle(Object obj) {
        return (ActivityHandle) handlers.get(obj.toString());
    }

    public Marshaler getMarshaler() {
        return null;
    }

    public Object getSBBResourceAdaptorInterface(String str) {
        return provider;
    }

    public void queryLiveness(ActivityHandle activityHandle) {
    }

    public void serviceActivated(String str) {
    }

    public void serviceDeactivated(String str) {
    }

    public void serviceInstalled(String serviceKey, int[] values, String[] str2) {
    }

    public void serviceUninstalled(String str) {
    }

    //JccConnectionEventListener
    public void callActive(JccCallEvent event) {
        logger.debug("Receive JccCallEvent.CALL_ACTIVE event");
        JccCallEvent evt = wrapJccCallEvent(event);
        JccCall call = evt.getCall();

        ActivityHandle handle = getActivityHandle(call);
        fireEvent("javax.csapi.cc.jcc.JccCallEvent.CALL_ACTIVE", handle, evt);
    }

    public void callCreated(JccCallEvent event) {
        logger.debug("Receive JccCallEvent.CALL_CREATED event");
        JccCallEvent evt = wrapJccCallEvent(event);
        JccCall call = evt.getCall();

        //create new activity handle
        JccCallActivityHandle handle = new JccCallActivityHandle(call);
        activities.put(call, handle);

        fireEvent("javax.csapi.cc.jcc.JccCallEvent.CALL_CREATED", handle, evt);
    }

    public void callEventTransmissionEnded(JccCallEvent event) {
        logger.debug("Receive JccCallEvent.CALL_EVENT_TRANSMISSION_ENDED event");
        JccCallEvent evt = wrapJccCallEvent(event);
        JccCall call = evt.getCall();

        ActivityHandle handle = getActivityHandle(call);
        fireEvent("javax.csapi.cc.jcc.JccCallEvent.CALL_EVENT_TRANSMISSION_ENDED", handle, evt);

        try {
            sleeEndpoint.activityEnding(handle);
        } catch (UnrecognizedActivityException uae) {
            logger.error("Caught an UnrecognizedActivityException: ");
            uae.printStackTrace();
        }
    }

    public void callInvalid(JccCallEvent event) {
        logger.debug("Receive JccCallEvent.CALL_INVALID event");
        JccCallEvent evt = wrapJccCallEvent(event);
        JccCall call = evt.getCall();

        ActivityHandle handle = getActivityHandle(call);
        fireEvent("javax.csapi.cc.jcc.JccCallEvent.CALL_INVALID", handle, evt);

        try {
            sleeEndpoint.activityEnding(handle);
        } catch (UnrecognizedActivityException uae) {
            logger.error("Caught an UnrecognizedActivityException: ");
            uae.printStackTrace();
        }
    }

    public void callSuperviseEnd(JccCallEvent event) {
        logger.debug("Receive JccCallEvent.CALL_SUPERVISE_END event");
        JccCallEvent evt = wrapJccCallEvent(event);
        JccCall call = evt.getCall();

        ActivityHandle handle = getActivityHandle(call);
        fireEvent("javax.csapi.cc.jcc.JccCallEvent.CALL_CSUPERVISE_END", handle, evt);
    }

    public void callSuperviseStart(JccCallEvent event) {
        logger.debug("Receive JccCallEvent.CALL_SUPERVISE_START event");
        JccCallEvent evt = wrapJccCallEvent(event);
        JccCall call = evt.getCall();

        ActivityHandle handle = getActivityHandle(call);
        fireEvent("javax.csapi.cc.jcc.JccCallEvent.CALL_CSUPERVISE_START", handle, evt);
    }

    public void connectionAddressAnalyze(JccConnectionEvent event) {
        logger.debug("Receive JccConnection.CONNECTION_ADDRESS_ANALYZE event");

        JccConnectionEvent evt = wrapJccConnectionEvent(event);
        JccConnection connection = evt.getConnection();

        ActivityHandle handle = getActivityHandle(connection);
        fireEvent("javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_ADDRESS_ANALYZE", handle, evt);
    }

    public void connectionAddressCollect(JccConnectionEvent event) {
        logger.debug("Receive JccConnection.CONNECTION_ADDRESS_COLLECT event");

        JccConnectionEvent evt = wrapJccConnectionEvent(event);
        JccConnection connection = evt.getConnection();

        ActivityHandle handle = getActivityHandle(connection);
        fireEvent("javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_ADDRESS_COLLECT", handle, evt);
    }

    public void connectionAlerting(JccConnectionEvent event) {
        logger.debug("Receive JccConnection.CONNECTION_ALERTING event");

        JccConnectionEvent evt = wrapJccConnectionEvent(event);
        JccConnection connection = evt.getConnection();

        ActivityHandle handle = getActivityHandle(connection);
        fireEvent("javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_ALERTING", handle, evt);
    }

    public void connectionAuthorizeCallAttempt(JccConnectionEvent event) {
        logger.debug("Receive JccConnection.CONNECTION_AUTHORIZE_CALL_ATTEMPT event");

        JccConnectionEvent evt = wrapJccConnectionEvent(event);
        JccConnection connection = evt.getConnection();

        ActivityHandle handle = getActivityHandle(connection);
        fireEvent("javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_AUTHORIZE_CALL_ATTEMPT", handle, evt);
    }

    public void connectionCallDelivery(JccConnectionEvent event) {
        logger.debug("Receive JccConnection.CONNECTION_CALL_DELIVERY event");

        JccConnectionEvent evt = wrapJccConnectionEvent(event);
        JccConnection connection = evt.getConnection();

        ActivityHandle handle = getActivityHandle(connection);
        fireEvent("javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_CALL_DELIVERY", handle, evt);
    }

    public void connectionConnected(JccConnectionEvent event) {
        logger.debug("Receive JccConnection.CONNECTION_CONNECTED event");

        JccConnectionEvent evt = wrapJccConnectionEvent(event);
        JccConnection connection = evt.getConnection();

        ActivityHandle handle = getActivityHandle(connection);
        fireEvent("javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_CONNECTED", handle, evt);
    }

    public void connectionCreated(JccConnectionEvent event) {
        logger.debug("Receive JccConnection.CONNECTION_CREATED event");
        JccConnectionEvent evt = wrapJccConnectionEvent(event);
        JccConnection connection = evt.getConnection();

        //create new activity handle
        JccConnectionActivityHandle handle = new JccConnectionActivityHandle(connection);
        handlers.put(connection.toString(), handle);
        logger.debug("onConnectionCreate():put handle: " + handle + " under key: " + connection.toString());

        activities.put(handle.getID(), connection);
        logger.debug("onConnectionCreate():put connection " + connection + " under key: " + handle.getID());

        fireEvent("javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_CREATED", handle, evt);
    }

    public void connectionDisconnected(JccConnectionEvent event) {
        logger.debug("Receive JccConnection.CONNECTION_DISCONNECTED event");
        JccConnectionEvent evt = wrapJccConnectionEvent(event);
        JccConnection connection = evt.getConnection();

        ActivityHandle handle = getActivityHandle(connection.toString());
        fireEvent("javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_DISCONNECTED", handle, evt);

        try {
            sleeEndpoint.activityEnding(handle);
        } catch (UnrecognizedActivityException uae) {
            logger.error("Caught an UnrecognizedActivityException: ");
            uae.printStackTrace();
        }
    }

    public void connectionFailed(JccConnectionEvent event) {
        logger.debug("Receive JccConnection.CONNECTION_FAILED event");
        JccConnectionEvent evt = wrapJccConnectionEvent(event);
        JccConnection connection = evt.getConnection();

        logger.debug("onfailed(): connectionID: " + connection.toString());
        ActivityHandle handle = getActivityHandle(connection);

        logger.debug("onfailed(): handle=" + handle);
        fireEvent("javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_FAILED", handle, evt);
    }

    public void connectionMidCall(JccConnectionEvent event) {
        logger.debug("Receive JccConnection.CONNECTION_MID_CALLevent");

        JccConnectionEvent evt = wrapJccConnectionEvent(event);
        JccConnection connection = evt.getConnection();

        ActivityHandle handle = getActivityHandle(connection);
        fireEvent("javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_MID_CALL", handle, evt);
    }

    private JccConnectionEventLocal wrapJccConnectionEvent(JccConnectionEvent event) {
        JccCallLocal call = new JccCallLocal(event.getCall(), provider);
        JccConnectionLocal connection = new JccConnectionLocal(event.getConnection(), provider, call);
        return new JccConnectionEventLocal(connection, event.getID(), event.getCause(), event.getSource(), call);
    }

    private JccCallEventLocal wrapJccCallEvent(JccCallEvent event) {
        JccCallLocal call = new JccCallLocal(event.getCall(), provider);
        return new JccCallEventLocal(event.getID(), event.getCause(), event.getSource(), call);
    }

    private void fireEvent(String eventName, ActivityHandle activityHandle, JccEvent event) {
        int eventID = -1;
        try {
            eventID = eventLookup.getEventID(eventName, "javax.csapi.cc.jcc", "1.1");
        } catch (FacilityException fe) {
            logger.error("Caught a FacilityException: ");
            fe.printStackTrace();
            throw new RuntimeException("JccResourceAdaptor.firEvent(): FacilityException caught. ", fe);
        } catch (UnrecognizedEventException ue) {
            logger.error("Caught an UnrecognizedEventException: ");
            ue.printStackTrace();
            throw new RuntimeException("JccResourceAdaptor.firEvent(): UnrecognizedEventException caught.", ue);
        }

        if (eventID == -1) {
            logger.warn("Unknown event type: " + eventName);
            return;
        }

        try {
            sleeEndpoint.fireEvent(activityHandle, event, eventID, address);
            logger.debug("Fire event: " + eventName);
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

    private void initializeNamingContext() throws Exception {
        logger.info("Initialize naming context");
        SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
        String entityName = bootstrapContext.getEntityName();

        ResourceAdaptorEntity resourceAdaptorEntity = sleeContainer.getResourceManagement().getResourceAdaptorEntity(entityName);
        ResourceAdaptorTypeID resourceAdaptorTypeId =
                resourceAdaptorEntity.getInstalledResourceAdaptor().getRaType().getResourceAdaptorTypeID();
        activityContextInterfaceFactory =
                new JccActivityContextInterfaceFactoryImpl(sleeContainer, entityName);
        sleeContainer.getResourceManagement().getActivityContextInterfaceFactories().put(resourceAdaptorTypeId,
                activityContextInterfaceFactory);
        String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) activityContextInterfaceFactory).getJndiName();
        int i = jndiName.indexOf(':');
        int j = jndiName.lastIndexOf('/');

        String prefix = jndiName.substring(i + 1, j);
        String name = jndiName.substring(j + 1);

        SleeContainer.registerWithJndi(prefix, name, activityContextInterfaceFactory);
    }

    private void clearNamingContext() {
        String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) activityContextInterfaceFactory).getJndiName();
        int i = jndiName.indexOf(':');
        String name = jndiName.substring(i + 1);

        SleeContainer.unregisterWithJndi(name);
    }

    private class ActivityMonitor implements Runnable {

        public void run() {
            while (!stopped) {
                try {
                    Thread.currentThread().sleep(60000);
                    logger.info("activities=" + activities.size() + ", handlers=" + handlers.size());
                } catch (InterruptedException e) {
                    stopped = true;
                }
            }
        }
    }
}
