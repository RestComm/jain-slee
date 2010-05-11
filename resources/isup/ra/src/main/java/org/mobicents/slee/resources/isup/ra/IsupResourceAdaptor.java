/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.slee.resources.isup.ra;

import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.facilities.EventLookupFacility;
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
import javax.slee.resource.SleeEndpoint;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;

/**
 *
 * @author kulikov
 */
public class IsupResourceAdaptor implements ResourceAdaptor {

    private Tracer tracer;
    private transient SleeEndpoint sleeEndpoint = null;
    /** the EventLookupFacility is used to look up the event id of incoming events */
    private transient EventLookupFacility eventLookup = null;
    
    private FireableEventTypeCache eventTypeCache;
    private FireableEventTypeFilter eventTypeFilter;
    
    private ResourceAdaptorContext context;
    
    private static final int EVENT_FLAGS = getEventFlags();
    private static int getEventFlags() {
        int eventFlags = EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK;
        EventFlags.setRequestProcessingFailedCallback(eventFlags);
        return eventFlags;
    }
    
    public void setResourceAdaptorContext(ResourceAdaptorContext context) {
        this.context = context;
        this.tracer = context.getTracer(getClass().getSimpleName());
        this.sleeEndpoint = context.getSleeEndpoint();
        this.eventLookup = context.getEventLookupFacility();
        this.eventTypeCache = new FireableEventTypeCache(tracer);
        this.eventTypeFilter = new FireableEventTypeFilter();
    }

    public void unsetResourceAdaptorContext() {
        this.tracer = null;
        this.context = null;
        this.sleeEndpoint = null;
        this.eventLookup = null;
        this.eventTypeCache = null;
        this.eventTypeFilter = null;
    }

    public void raConfigure(ConfigProperties arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void raUnconfigure() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void raActive() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void raStopping() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void raInactive() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void raVerifyConfiguration(ConfigProperties arg0) throws InvalidConfigurationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void raConfigurationUpdate(ConfigProperties arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getResourceAdaptorInterface(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Marshaler getMarshaler() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void serviceActive(ReceivableService arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void serviceStopping(ReceivableService arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void serviceInactive(ReceivableService arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void queryLiveness(ActivityHandle arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getActivity(ActivityHandle arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ActivityHandle getActivityHandle(Object arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void administrativeRemove(ActivityHandle arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void eventProcessingSuccessful(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4, int arg5) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void eventProcessingFailed(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4, int arg5, FailureReason arg6) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void eventUnreferenced(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4, int arg5) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void activityEnded(ActivityHandle arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void activityUnreferenced(ActivityHandle arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected void fireEvent(String eventName, Object activity, ISUPMessage event) {
        final ActivityHandle handle = this.getActivityHandle(activity);        
        final FireableEventType eventType = eventTypeCache.getEventType(eventLookup, eventName);
        if (eventTypeFilter.filterEvent(eventType)) {
            if (tracer.isFineEnabled()) {
                tracer.fine("event " + eventName + " filtered");
            }
            return;
        }

        //TODO insert global title.
        final Address address = new Address(AddressPlan.GT, "");
        try {
            sleeEndpoint.fireEvent(handle, eventType, event, address, null, EVENT_FLAGS);
            tracer.info("Firde event: " + eventName);
        } catch (Throwable e) {
            tracer.severe("Failed to fire event", e);
        }
    }
    
}
