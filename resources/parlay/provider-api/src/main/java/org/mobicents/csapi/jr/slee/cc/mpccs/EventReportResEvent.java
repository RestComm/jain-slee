package org.mobicents.csapi.jr.slee.cc.mpccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event reports that an event has occurred that was requested to be reported (for example, a mid-call event, the party has requested to disconnect, etc.).
Depending on the type of event received, outstanding requests for events are discarded. The exact details of these so-called disarming rules are captured in the data definition of the event type.
If this method is invoked for a report with a monitor mode of P_CALL_MONITOR_MODE_INTERRUPT, then the application has control of the call leg. If the application does nothing with the call leg within a specified time period (the duration which forms a part of the service level agreement), then the connection in the network shall be released and callLegEnded() shall be invoked, giving a release cause of P_TIMER_EXPIRY.
 * 
 * 
 */
public class EventReportResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for EventReportResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public EventReportResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier , TpCallLegIdentifier tpCallLegIdentifier , org.csapi.cc.TpCallEventInfo eventInfo ){
        super(tpServiceIdentifier);
        this.tpMultiPartyCallIdentifier = tpMultiPartyCallIdentifier;
        this.tpCallLegIdentifier = tpCallLegIdentifier;
        this.eventInfo = eventInfo;
    }

    /**
     * Returns the tpMultiPartyCallIdentifier
     * 
     */
    public TpMultiPartyCallIdentifier getTpMultiPartyCallIdentifier() {
        return this.tpMultiPartyCallIdentifier;
    }
    /**
     * Returns the tpCallLegIdentifier
     * 
     */
    public TpCallLegIdentifier getTpCallLegIdentifier() {
        return this.tpCallLegIdentifier;
    }
    /**
     * Returns the eventInfo
     * 
     */
    public org.csapi.cc.TpCallEventInfo getEventInfo() {
        return this.eventInfo;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof EventReportResEvent)) {
            return false;
        } 
        EventReportResEvent eventReportResEvent = (EventReportResEvent) o;
        if(!(this.getService() == eventReportResEvent.getService())) {
            return false;
        }
        if ((this.tpMultiPartyCallIdentifier != null) && (eventReportResEvent.tpMultiPartyCallIdentifier != null)) {
            if(!(this.tpMultiPartyCallIdentifier.equals(eventReportResEvent.tpMultiPartyCallIdentifier)))  {
                return false;
            }
        }
        if ((this.tpCallLegIdentifier != null) && (eventReportResEvent.tpCallLegIdentifier != null)) {
            if(!(this.tpCallLegIdentifier.equals(eventReportResEvent.tpCallLegIdentifier)))  {
                return false;
            }
        }
        if ((this.eventInfo != null) && (eventReportResEvent.eventInfo != null)) {
            if(!(this.eventInfo.equals(eventReportResEvent.eventInfo)))  {
                return false;
            }
        }
        if (this.hashCode() != o.hashCode()) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hashcode value for the object.
     */
    public int hashCode() {
        return 1;
    }

    // VARIABLES
    // .......................................................

    private TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier = null;
    private TpCallLegIdentifier tpCallLegIdentifier = null;
    private org.csapi.cc.TpCallEventInfo eventInfo = null;

} // EventReportResEvent

