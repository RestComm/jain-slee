package org.mobicents.slee.resource.parlay.util.event;

import javax.slee.Address;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.SleeEndpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.csapi.jr.slee.ResourceEvent;
import org.mobicents.slee.resource.parlay.ParlayResourceAdaptor;

/**
 * 
 * Class Description for EventSenderImpl
 */
public class EventSenderImpl implements EventSender {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(EventSenderImpl.class);

    /**
     * @param eventLookupFacility
     * @param sleeEndpoint
     * @param defaultAddress
     */
    public EventSenderImpl(EventLookupFacility eventLookupFacility,
            SleeEndpoint sleeEndpoint, Address defaultAddress) {
        super();
        this.eventLookupFacility = eventLookupFacility;
        this.sleeEndpoint = sleeEndpoint;
        this.defaultAddress = defaultAddress;
    }

    private final transient EventLookupFacility eventLookupFacility;

    private final transient SleeEndpoint sleeEndpoint;

    private final transient Address defaultAddress;
    
    private final transient int ERROR_EVENT_ID = -1;

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.util.event.EventSender#send(org.mobicents.csapi.jr.slee.ResourceEvent)
     */
    public void sendEvent(final ResourceEvent event, final ActivityHandle activityHandle) {
        
        try {
            final int eventID = eventLookupFacility.getEventID(event.getClass().getName(),
                    ParlayResourceAdaptor.VENDOR, ParlayResourceAdaptor.VERSION);
            
            if(eventID != ERROR_EVENT_ID) {
                if(logger.isDebugEnabled()) {
                    logger.debug("Firing event " + event);
                }
                sleeEndpoint.fireEvent(activityHandle, event, eventID, defaultAddress);
            }
            else {
                logger.error("Lookup failed for event name " + event.getClass().getName());
            }
        } catch (UnrecognizedEventException e) {
            logger.error("Failed to send event due to unrecognized event " + event, e);
        } catch (UnrecognizedActivityException e) {
            logger.error("Failed to send event due to unrecognized activity " + event, e);
        } catch (RuntimeException e) {
            logger.error("Failed to send event " + event, e);
        } 
    }

    /**
     * @return Returns the eventLookupFacility.
     */
    public EventLookupFacility getEventLookupFacility() {
        return eventLookupFacility;
    }

    /**
     * @return Returns the sleeEndpoint.
     */
    public SleeEndpoint getSleeEndpoint() {
        return sleeEndpoint;
    }
}