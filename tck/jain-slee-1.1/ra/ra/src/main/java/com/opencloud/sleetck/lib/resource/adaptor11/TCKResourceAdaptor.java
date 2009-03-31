/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.adaptor11;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.TraceLevel;
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
import javax.slee.resource.ConfigProperties.Property;
import javax.slee.resource.ReceivableService.ReceivableEvent;
import javax.slee.transaction.SleeTransactionManager;

import com.opencloud.logging.LogLevel;
import com.opencloud.logging.Logable;
import com.opencloud.sleetck.lib.TCKTestErrorException;
import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.adaptor.TCKResourceAdaptorInterface;
import com.opencloud.sleetck.lib.resource.adaptor.TCKResourceEventHandler;
import com.opencloud.sleetck.lib.resource.adaptor.TCKResourceFactory;
import com.opencloud.sleetck.lib.resource.adaptor.TCKResourceSetupInterface;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEvent;
import com.opencloud.sleetck.lib.resource.sbbapi.TCKActivity;
import com.opencloud.sleetck.lib.resource.sbbapi.TCKResourceAdaptorSbbInterface;
import com.opencloud.sleetck.lib.resource.sbbapi.TCKResourceSbbInterface;
import com.opencloud.sleetck.lib.resource.sbbapi.TransactionIDAccess;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceTestInterface;

/**
 * SLEE 1.1 Compliant Resource Adaptor
 * <p>
 * This Resource Adaptor is deployed into the SLEE and can be used by the TCK Test environment to initiate event firing.
 * <p>
 * This Resource Adaptor takes over the role of the SLEE specific Resource Adaptor implementations from the SLEE 1.0
 * TCK.
 * <p>
 */

public class TCKResourceAdaptor extends UnicastRemoteObject implements ResourceAdaptor, TCKResourceAdaptorSbbInterface, TCKResourceEventHandler {

    // These must be the same as those defined in resource-adaptor-type-jar.xml
    static final String EVENT_TYPE_VENDOR = "jain.slee.tck";
    static final String EVENT_TYPE_VERSION = "1.0";

    static final String TRACER_NAME = EVENT_TYPE_VENDOR + ".TCKResourceAdaptor";

    public TCKResourceAdaptor() throws RemoteException {
        super();
    }

    //
    // javax.slee.resource.ResourceAdaptor
    //

    public void activityEnded(ActivityHandle handle) {
        if (tracer.isFinestEnabled())
            tracer.finest("activityEnded(): " + handle);
        TCKActivity activity = (TCKActivity) activityMap.getActivity((TCKActivityHandleImpl) handle);
        if (activity != null) {
            try {
                resource.onActivityContextInvalid(activity.getID());
            } catch (RemoteException re) {
                tracer.severe("Caught RemoteException while calling TCKResourceAdaptorInterface:" + re);
            }
        } else
            tracer.severe("Received activityEnded() call for non registered activity. Activity Handle:" + handle);
        deregisterActivity((TCKActivityHandleImpl) handle);
    }

    // New in 1.1
    public void activityUnreferenced(ActivityHandle handle) {
        if (tracer.isFinestEnabled())
            tracer.finest("activityUnreferenced(): " + handle);
    }

    // New in 1.1
    public void administrativeRemove(ActivityHandle handle) {
        if (tracer.isFinestEnabled())
            tracer.finest("administrativeRemove(): " + handle);
    }

    public void raConfigurationUpdate(ConfigProperties properties) {
        if (tracer.isFinestEnabled())
            tracer.finest("raConfigurationUpdate(): " + properties);
    }

    public void raConfigure(ConfigProperties properties) {
        if (tracer.isFinestEnabled())
            tracer.finest("raConfigure(): " + properties);

        try {
            rmiPort = ((Integer) properties.getProperty("rmiPort").getValue()).intValue();

            if (context == null)
                throw new IllegalStateException("configureResourceAdaptor() called before setResourceAdaptorContext()");
            tracer.info("Creating TCK Resource Adaptor Entity");

            // SLEE facilities
            eventLookupFacility = context.getEventLookupFacility();
            sleeEndpoint = context.getSleeEndpoint();
            sleeTransactionManager = context.getSleeTransactionManager();

            tracer.info("TCK Resource Adaptor Entity created");
        } catch (Exception e) {
            tracer.severe("Exception caught while creating the TCK Resource Adaptor", e);
        }

    }

    public void raUnconfigure() {
        tracer.finest("raUnconfigure()");

        // Drop all facility references (except tracer)
        eventLookupFacility = null;
        sleeEndpoint = null;
        sleeTransactionManager = null;

        tracer.info("TCK Resource Adaptor Entity removed");
    }

    public void raActive() {
        tracer.finest("raActive()");

        tracer.info("Starting TCK Resource Adaptor");
        try {
            transactionIDAccess = new TCKTransactionIDAccessImpl(sleeTransactionManager);
            activityMap = new TCKActivityMap(tracer);
            eventTypes = new HashMap();
            marshaler = new TCKMarshalerImpl();

            // initialise the resource
            TCKResourceSetupInterface resourceSetup = TCKResourceFactory.createResource();
            resourceSetup.setLog(new TCKRALogger());

            resource = resourceSetup.getResourceAdaptorInterface();
            resourceSbbInterface = resource.getSbbInterface();

            // bind the test interface in the rmi registry
            tracer.info("Locating RMI registry at port " + rmiPort + " on localhost...");
            rmiRegistry = LocateRegistry.getRegistry(rmiPort);
            tracer.info("Registry located");

            tracer.info("Binding test interface...");
            TCKResourceTestInterface testInterface = resourceSetup.getTestInterface();
            rmiRegistry.rebind(TCKResourceTestInterface.RMI_NAME, testInterface);
            tracer.info("Test interface bound in RMI registry");

            resource.addEventHandler(this);
        } catch (Exception e) {
            tracer.severe("Exception caught while starting the TCK Resource Adaptor", e);
        }
        tracer.info("TCK Resource Adaptor started");
    }

    public void raInactive() {
        tracer.finest("raInactive()");

        try {
            resource.removeEventHandler(this);

            tracer.info("Unbinding from RMI Registry");
            rmiRegistry.unbind(TCKResourceTestInterface.RMI_NAME);
            tracer.info("Test interface unbound from RMI Registry");

            transactionIDAccess = null;
            activityMap = null;
            eventTypes = null;
            marshaler = null;
            resource = null;
            resourceSbbInterface = null;
            rmiRegistry = null;
        } catch (Exception e) {
            tracer.severe("Exception caught while stopping the TCK Resource Adaptor", e);
        }
        tracer.info("TCK Resource Adaptor stopped");
    }

    public void raStopping() {
        tracer.finest("raStopping()");
        tracer.info("Stopping TCK Resource Adaptor");
        try {
            resource.eventHandlerDeactivating(this);
        } catch (RemoteException e) {
            tracer.severe("Exception caught while deactivating the TCK Resource Adaptor event handler", e);
        }
    }

    public void raVerifyConfiguration(ConfigProperties properties) throws InvalidConfigurationException {
        tracer.finest("raVerifyConfiguration(): " + properties);
        Property rmiPortProperty = properties.getProperty("rmiPort");

        if (rmiPortProperty == null)
            throw new InvalidConfigurationException("Configuration property rmiPort has not been set");

        int rmiPort = ((Integer) rmiPortProperty.getValue()).intValue();

        if (rmiPort < 0 || rmiPort > 65535)
            throw new InvalidConfigurationException("Configuration property rmiPort has not been set to a valid value (0-65535): " + rmiPort);
    }

    // This was simplified from the 1.0 version of the RA.
    public void eventProcessingFailed(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags, FailureReason reason) {
        if (tracer.isFinestEnabled())
            tracer.finest("eventProcessingFailed(): handle=" + handle + ", eventTypeID=" + eventType.getEventType().toString() + ", event=" + event + ", address=" + address
                    + ", service=" + service + ", flags=" + flags + ", failureReason=" + reason);

        TCKActivity activity = (TCKActivity) getActivity(handle);

        String failureMessage = "Event processing failed. Event=" + event + ",Activity=" + activity + ",ServiceID=" + service + ",Flags=" + flags + ",Reason=" + reason;
        if (event != null && event instanceof TCKResourceEvent) {
            TCKResourceEvent tckResourceEvent = (TCKResourceEvent) event;
            try {
                resource.onEventProcessingFailed(tckResourceEvent.getEventObjectID(), failureMessage, null);
            } catch (RemoteException re) {
                tracer.severe("Caught RemoteException while calling TCKResourceAdaptorInterface:" + re);
            }
        } else {
            try {
                resource.onException(new TCKTestErrorException(failureMessage));
            } catch (RemoteException re) {
                tracer.severe("Caught RemoteException while calling TCKResourceAdaptorInterface:" + re);
            }
        }
        tracer.severe("Event processing failed for event: " + event);
    }

    public void eventProcessingSuccessful(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {
        if (tracer.isFinestEnabled())
            tracer.finest("eventProcessingSuccessful(): handle=" + handle + ", eventTypeID=" + eventType.getEventType().toString() + ", event=" + event + ", address=" + address
                    + ", service=" + service + ", flags=" + flags);

        if (event != null && event instanceof TCKResourceEvent) {
            TCKResourceEvent tckResourceEvent = (TCKResourceEvent) event;
            try {
                resource.onEventProcessingSuccessful(tckResourceEvent.getEventObjectID());
            } catch (RemoteException re) {
                tracer.severe("Caught RemoteException while calling TCKResourceAdaptorInterface:" + re);
            }
        }
        tracer.info("eventProcessingSuccessful() call received for event: " + event);
    }

    public void eventUnreferenced(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {
        if (tracer.isFinestEnabled())
            tracer.finest("eventUnreferenced(): handle=" + handle + ", eventID=" + eventType.getEventType() + ", event=" + event + ", address=" + address + ", service="
                    + service.getService() + ", flags=" + flags);
    }

    public Object getActivity(ActivityHandle handle) {
        if (tracer.isFinestEnabled())
            tracer.finest("getActivity():" + handle);
        if (handle instanceof TCKActivityHandleImpl)
            return activityMap.getActivity((TCKActivityHandleImpl) handle);
        return null;
    }

    public ActivityHandle getActivityHandle(Object activity) {
        if (tracer.isFinestEnabled())
            tracer.finest("getActivityHandle():" + activity);
        return activityMap.getActivityHandle(activity);
    }

    public Marshaler getMarshaler() {
        tracer.finest("getMarshaler()");
        return marshaler;
    }

    public Object getResourceAdaptorInterface(String className) {
        if (tracer.isFinestEnabled())
            tracer.finest("getResourceAdaptorInterface(): " + className);
        if (className.equals("com.opencloud.sleetck.lib.resource.sbbapi.TCKResourceAdaptorSbbInterface"))
            return this;
        return null;
    }

    public void queryLiveness(ActivityHandle handle) {
        if (tracer.isFinestEnabled())
            tracer.finest("queryLiveness(): " + handle);

        TCKActivity tckActivity = (TCKActivity) getActivity(handle);
        try {
            // enqueue an end event if the activity is not live
            if (!tckActivity.isLive())
                sleeEndpoint.endActivity(handle);
        } catch (Exception e) {
            try {
                resource.onException(e);
            } catch (RemoteException re) {
                tracer.severe("Caught RemoteException while calling TCKResourceAdaptorInterface:", re);
            }
            tracer.severe("Caught Exception while querying an activity for liveness", e);
        }
    }

    public void serviceActive(ReceivableService serviceInfo) {
        if (tracer.isFinestEnabled())
            tracer.finest("serviceActive(): service=" + serviceInfo.getService() + ", eventIDs=" + Arrays.toString(serviceInfo.getReceivableEvents()) + ", resourceOptions="
                    + Arrays.toString(extractResourceOptionArrFromEvents(serviceInfo.getReceivableEvents())));
    }

    public void serviceInactive(ReceivableService serviceInfo) {
        if (tracer.isFinestEnabled())
            tracer.finest("serviceInactive(): service=" + serviceInfo.getService() + ", eventIDs=" + Arrays.toString(serviceInfo.getReceivableEvents()) + ", resourceOptions="
                    + Arrays.toString(extractResourceOptionArrFromEvents(serviceInfo.getReceivableEvents())));
    }

    public void serviceStopping(ReceivableService serviceInfo) {
        if (tracer.isFinestEnabled())
            tracer.finest("serviceStopping(): service=" + serviceInfo.getService() + ", eventIDs=" + Arrays.toString(serviceInfo.getReceivableEvents()) + ", resourceOptions="
                    + Arrays.toString(extractResourceOptionArrFromEvents(serviceInfo.getReceivableEvents())));
    }

    public void setResourceAdaptorContext(ResourceAdaptorContext context) {
        if (context == null)
            throw new IllegalArgumentException("TCKResourceAdaptor: The SLEE passed a null context to the TCK Resource Adaptor");
        this.context = context;
        tracer = context.getTracer("TCK-RA");
        tracer.finest("setResourceadaptorContext(): " + context);
    }

    public void unsetResourceAdaptorContext() {
        tracer.finest("unsetResourceAdaptorContext()");
        this.tracer = null;
        this.context = null;
    }

    //
    // TCKResourceAdaptorSbbInterface
    //

    public TCKResourceSbbInterface getResource() {
        tracer.finest("getResource()");
        return resourceSbbInterface;
    }

    public TransactionIDAccess getTransactionIDAccess() {
        tracer.finest("getTransactionIDAccess()");
        return transactionIDAccess;
    }

    //
    // TCKResourceEventHandler
    //

    public void handleActivityCreatedBySbb(TCKActivityID activityID) throws TCKTestErrorException, RemoteException {
        if (tracer.isFinestEnabled())
            tracer.finest("handleActivityCreatedBySbb(): activityID=" + activityID);
        TCKActivity activity = resource.getActivity(activityID);
        TCKActivityHandleImpl ah = registerActivity(activity);

        try {
            int activity_flags = ActivityFlags.REQUEST_ENDED_CALLBACK | ActivityFlags.REQUEST_ACTIVITY_UNREFERENCED_CALLBACK;
            sleeEndpoint.startActivity(ah, activity, activity_flags);
        } catch (Exception e) {
            deregisterActivity(ah);
            throw new TCKTestErrorException("Can't register new activity, see enclosed exception", e);
        }
    }

    public void handleActivityEnd(TCKActivityID activityID, boolean endedBySbb) throws TCKTestErrorException, RemoteException {
        if (tracer.isFinestEnabled())
            tracer.finest("handleActivityEnd(): activityID=" + activityID + ", endedBySbb=" + endedBySbb);
        TCKActivity activity = resource.getActivity(activityID);
        if (activity == null)
            throw new TCKTestErrorException("Couldn't find activity for ID: " + activityID);
        synchronized (activityMap) {
            TCKActivityHandleImpl ah = activityMap.getActivityHandle(activity);
            if (ah != null) {
                try {
                    sleeEndpoint.endActivity(ah);
                } catch (Exception e) {
                    throw new TCKTestErrorException("Couldn't end activity with ID '" + activityID + "', see enclosed exception", e);
                }
            } else if (tracer.isFineEnabled())
                tracer.fine("Ignoring activity end for unknown activity: " + activityID);
        }
    }

    public void handleEvent(TCKResourceEvent event, String eventType, TCKActivityID activityID, Address address) throws TCKTestErrorException, RemoteException {
        if (tracer.isFinestEnabled())
            tracer.finest("handleEvent(): event=" + event + ", eventType=" + eventType + ", activityID=" + activityID + ", address=" + address);
        TCKActivity activity = resource.getActivity(activityID);
        FireableEventType fireableEventType = lookupFireableEventType(eventType);

        TCKActivityHandleImpl ah = null;
        boolean isNewActivity = false;
        synchronized (activityMap) {
            ah = activityMap.getActivityHandle(activity);
            if (ah == null) { // register the activity if necessary
                ah = registerActivity(activity);
                isNewActivity = true;
            }
        }

        if (isNewActivity) {
            try {
                int activity_flags = ActivityFlags.REQUEST_ENDED_CALLBACK | ActivityFlags.REQUEST_ACTIVITY_UNREFERENCED_CALLBACK;
                sleeEndpoint.startActivity(ah, activity, activity_flags);
            } catch (Exception e) {
                throw new TCKTestErrorException("Couldn't create Activity Handle: " + ah, e);
            }
        }

        try {
            // Event processing callback flags are required so that
            // eventProcessingFailed and eventProcessingSuccessful are
            // called (required by some of the tests from the 1.0 TCK).
            int flags = EventFlags.REQUEST_PROCESSING_FAILED_CALLBACK | EventFlags.REQUEST_PROCESSING_SUCCESSFUL_CALLBACK;
            sleeEndpoint.fireEvent(ah, fireableEventType, event, address, null, flags);
        } catch (Exception e) {
            throw new TCKTestErrorException("Couldn't fire event with ID '" + fireableEventType.getEventType() + "', see enclosed exception", e);
        }
    }

    /**
     * Uses the TCK RA's Tracer to log messages. Used by the TCK resource to log messages to the SLEE via the trace
     * facility.
     */
    public class TCKRALogger implements Logable {

        public void config(String message) {
            tracer.config(message);
        }

        public void config(Throwable t) {
            tracer.config(t.getMessage(), t);
        }

        public void critical(String message) {
            tracer.severe(message);
        }

        public void critical(Throwable t) {
            tracer.severe(t.getMessage(), t);
        }

        public void debug(String message) {
            tracer.fine(message);
        }

        public void debug(Throwable t) {
            tracer.fine(t.getMessage(), t);
        }

        public void error(String message) {
            tracer.severe(message);
        }

        public void error(Throwable t) {
            tracer.severe(t.getMessage(), t);
        }

        public void fine(String message) {
            tracer.fine(message);
        }

        public void fine(Throwable t) {
            tracer.fine(t.getMessage(), t);
        }

        public void finer(String message) {
            tracer.finer(message);
        }

        public void finer(Throwable t) {
            tracer.finer(t.getMessage(), t);
        }

        public void finest(String message) {
            tracer.finest(message);
        }

        public void finest(Throwable t) {
            tracer.finest(t.getMessage(), t);
        }

        public void info(String message) {
            tracer.info(message);
        }

        public void info(Throwable t) {
            tracer.info(t.getMessage(), t);
        }

        public boolean isLogable(int level) {
            switch (level) {
            case LogLevel.INFO:
            case LogLevel.NORMAL:
                return tracer.isInfoEnabled();
            case LogLevel.CONFIG:
                return tracer.isConfigEnabled();
            case LogLevel.FINE:
            case LogLevel.DEBUG:
                return tracer.isFineEnabled();
            case LogLevel.FINER:
                return tracer.isFinerEnabled();
            case LogLevel.FINEST:
                return tracer.isFinestEnabled();
            case LogLevel.ERROR:
            case LogLevel.CRITICAL:
            case LogLevel.SEVERE:
                return tracer.isSevereEnabled();
            }
            return false;
        }

        public void normal(String message) {
            tracer.info(message);
        }

        public void normal(Throwable t) {
            tracer.info(t.getMessage(), t);
        }

        public void setIgnoreLevel(int level) {
        }

        public void severe(String message) {
            tracer.severe(message);
        }

        public void severe(Throwable t) {
            tracer.severe(t.getMessage(), t);
        }

        public void warning(String message) {
            tracer.warning(message);
        }

        public void warning(Throwable t) {
            tracer.warning(t.getMessage(), t);
        }

        public void writeToLog(int level, String message) {
            switch (level) {
            case LogLevel.INFO:
            case LogLevel.NORMAL:
                tracer.trace(TraceLevel.INFO, message);
                break;
            case LogLevel.CONFIG:
                tracer.trace(TraceLevel.CONFIG, message);
                break;
            case LogLevel.FINE:
            case LogLevel.DEBUG:
                tracer.trace(TraceLevel.FINE, message);
                break;
            case LogLevel.FINER:
                tracer.trace(TraceLevel.FINER, message);
                break;
            case LogLevel.FINEST:
                tracer.trace(TraceLevel.FINEST, message);
                break;
            case LogLevel.WARNING:
                tracer.trace(TraceLevel.WARNING, message);
                break;
            case LogLevel.ERROR:
            case LogLevel.CRITICAL:
            case LogLevel.SEVERE:
                tracer.trace(TraceLevel.SEVERE, message);
                break;
            }
        }

        public void writeToLog(int level, Throwable t) {
            switch (level) {
            case LogLevel.INFO:
            case LogLevel.NORMAL:
                tracer.trace(TraceLevel.INFO, t.getMessage(), t);
                break;
            case LogLevel.CONFIG:
                tracer.trace(TraceLevel.CONFIG, t.getMessage(), t);
                break;
            case LogLevel.FINE:
            case LogLevel.DEBUG:
                tracer.trace(TraceLevel.FINE, t.getMessage(), t);
                break;
            case LogLevel.FINER:
                tracer.trace(TraceLevel.FINER, t.getMessage(), t);
                break;
            case LogLevel.FINEST:
                tracer.trace(TraceLevel.FINEST, t.getMessage(), t);
                break;
            case LogLevel.WARNING:
                tracer.trace(TraceLevel.WARNING, t.getMessage(), t);
                break;
            case LogLevel.ERROR:
            case LogLevel.CRITICAL:
            case LogLevel.SEVERE:
                tracer.trace(TraceLevel.SEVERE, t.getMessage(), t);
                break;
            }
        }
    }

    //
    // Private
    //

    /**
     * Registers the given activity in the activity map, and returns a handle to the activity.
     */
    private TCKActivityHandleImpl registerActivity(Object activity) {
        synchronized (activityMap) {
            return activityMap.allocateActivityHandle(activity);
        }
    }

    /**
     * Removes the given activity from the activity map.
     */
    private void deregisterActivity(TCKActivityHandleImpl ah) {
        synchronized (activityMap) {
            activityMap.deallocateActivityHandle(ah);
        }
    }

    /**
     * Returns the event type for the given event name, as defined by the SLEE container.
     */
    private FireableEventType lookupFireableEventType(String eventName) throws TCKTestErrorException {
        try {
            FireableEventType eventType = null;
            synchronized (eventTypes) {
                // check the cache
                eventType = (FireableEventType) eventTypes.get(eventName);
            }
            if (eventType == null) {
                // lookup the event type
                EventTypeID eventTypeID = new EventTypeID(eventName, EVENT_TYPE_VENDOR, EVENT_TYPE_VERSION);
                eventType = eventLookupFacility.getFireableEventType(eventTypeID);
                synchronized (eventTypes) {
                    eventTypes.put(eventName, eventType); // add the to cache
                }
            }
            return eventType;
        } catch (UnrecognizedEventException e) {
            throw new TCKTestErrorException("Unrecognized event type name: " + eventName);
        }
    }

    private String[] extractResourceOptionArrFromEvents(ReceivableEvent[] events) {
        String[] res = new String[events.length];
        for (int i = 0; i < events.length; i++)
            res[i] = events[i].getResourceOption();

        return res;
    }

    // SLEE objects and facility references
    private transient SleeEndpoint sleeEndpoint;
    private transient SleeTransactionManager sleeTransactionManager;
    private transient EventLookupFacility eventLookupFacility;
    private transient Tracer tracer;
    private ResourceAdaptorContext context;

    // Resource adaptor state
    private transient Registry rmiRegistry;
    private transient Map eventTypes;
    private transient TCKActivityMap activityMap;
    private transient TCKMarshalerImpl marshaler;
    private transient TCKResourceAdaptorInterface resource;
    private transient TCKResourceSbbInterface resourceSbbInterface;
    private transient TransactionIDAccess transactionIDAccess;
    private transient int rmiPort = -1;

}
