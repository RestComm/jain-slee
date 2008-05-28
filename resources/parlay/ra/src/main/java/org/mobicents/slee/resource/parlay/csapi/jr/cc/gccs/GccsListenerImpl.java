
package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs;

import org.mobicents.csapi.jr.slee.cc.gccs.CallAbortedEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.CallEndedEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.CallEventNotifyEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.CallFaultDetectedEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.CallNotificationContinuedEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.CallNotificationInterruptedEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.CallOverloadCeasedEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.CallOverloadEncounteredEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.GetCallInfoErrEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.GetCallInfoResEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.GetMoreDialledDigitsErrEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.GetMoreDialledDigitsResEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.RouteErrEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.RouteResEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.SuperviseCallErrEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.SuperviseCallResEvent;
import org.mobicents.slee.resource.parlay.csapi.jr.ParlayServiceActivityHandle;
import org.mobicents.slee.resource.parlay.util.event.EventSender;

/**
 *
 **/
public class GccsListenerImpl implements GccsListener {
    
    private final transient EventSender eventSender;
    
    public GccsListenerImpl(EventSender eventSender) {
        super();
        this.eventSender = eventSender;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener#onCallAbortedEvent(org.mobicents.csapi.jr.slee.cc.gccs.CallAbortedEvent)
     */
    public void onCallAbortedEvent(final CallAbortedEvent event) {
        eventSender.sendEvent(event, new ParlayServiceActivityHandle(event
                .getService()));
        
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener#onCallEndedEvent(org.mobicents.csapi.jr.slee.cc.gccs.CallEndedEvent)
     */
    public void onCallEndedEvent(final CallEndedEvent event) {
        eventSender.sendEvent(event, new TpCallActivityHandle(event
                .getTpCallIdentifier()));
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener#onCallEventNotifiyEvent(org.mobicents.csapi.jr.slee.cc.gccs.CallEventNotifyEvent)
     */
    public void onCallEventNotifiyEvent(final CallEventNotifyEvent event) {
        eventSender.sendEvent(event, new ParlayServiceActivityHandle(event
                .getService()));
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener#onCallFaultDetectedEvent(org.mobicents.csapi.jr.slee.cc.gccs.CallFaultDetectedEvent)
     */
    public void onCallFaultDetectedEvent(final CallFaultDetectedEvent event) {
        eventSender.sendEvent(event, new TpCallActivityHandle(event
                .getTpCallIdentifier()));
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener#onCallNotificationContinuedEvent(org.mobicents.csapi.jr.slee.cc.gccs.CallNotificationContinuedEvent)
     */
    public void onCallNotificationContinuedEvent(final CallNotificationContinuedEvent event) {
        eventSender.sendEvent(event, new ParlayServiceActivityHandle(event
                .getService()));
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener#onCallNotificationInterruptedEvent(org.mobicents.csapi.jr.slee.cc.gccs.CallNotificationInterruptedEvent)
     */
    public void onCallNotificationInterruptedEvent(final CallNotificationInterruptedEvent event) {
        eventSender.sendEvent(event, new ParlayServiceActivityHandle(event
                .getService()));
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener#onCallOverloadCeasedEvent(org.mobicents.csapi.jr.slee.cc.gccs.CallOverloadCeasedEvent)
     */
    public void onCallOverloadCeasedEvent(final CallOverloadCeasedEvent event) {
        eventSender.sendEvent(event, new ParlayServiceActivityHandle(event
                .getService()));
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener#onGetCallInfoErrEvent(org.mobicents.csapi.jr.slee.cc.gccs.GetCallInfoErrEvent)
     */
    public void onGetCallInfoErrEvent(final GetCallInfoErrEvent event) {
        eventSender.sendEvent(event, new TpCallActivityHandle(event
                .getTpCallIdentifier()));
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener#onGetCallInfoResEvent(org.mobicents.csapi.jr.slee.cc.gccs.GetCallInfoResEvent)
     */
    public void onGetCallInfoResEvent(final GetCallInfoResEvent event) {
        eventSender.sendEvent(event, new TpCallActivityHandle(event
                .getTpCallIdentifier()));
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener#onGetMoreDialledDigitsErrEvent(org.mobicents.csapi.jr.slee.cc.gccs.GetMoreDialledDigitsErrEvent)
     */
    public void onGetMoreDialledDigitsErrEvent(final GetMoreDialledDigitsErrEvent event) {
        eventSender.sendEvent(event, new TpCallActivityHandle(event
                .getTpCallIdentifier()));
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener#onGetMoreDialledDigitsResEvent(org.mobicents.csapi.jr.slee.cc.gccs.GetMoreDialledDigitsResEvent)
     */
    public void onGetMoreDialledDigitsResEvent(final GetMoreDialledDigitsResEvent event) {
        eventSender.sendEvent(event, new TpCallActivityHandle(event
                .getTpCallIdentifier()));
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener#onRouteErrEvent(org.mobicents.csapi.jr.slee.cc.gccs.RouteErrEvent)
     */
    public void onRouteErrEvent(final RouteErrEvent event) {
        eventSender.sendEvent(event, new TpCallActivityHandle(event
                .getTpCallIdentifier()));
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener#onRouteResEvent(org.mobicents.csapi.jr.slee.cc.gccs.RouteResEvent)
     */
    public void onRouteResEvent(final RouteResEvent event) {
        eventSender.sendEvent(event, new TpCallActivityHandle(event
                .getTpCallIdentifier()));
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener#onSuperviseCallErrEvent(org.mobicents.csapi.jr.slee.cc.gccs.SuperviseCallErrEvent)
     */
    public void onSuperviseCallErrEvent(final SuperviseCallErrEvent event) {
        eventSender.sendEvent(event, new TpCallActivityHandle(event
                .getTpCallIdentifier()));
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener#onSuperviseCallResEvent(org.mobicents.csapi.jr.slee.cc.gccs.SuperviseCallResEvent)
     */
    public void onSuperviseCallResEvent(final SuperviseCallResEvent event) {
        eventSender.sendEvent(event, new TpCallActivityHandle(event
                .getTpCallIdentifier()));
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener#onCallOverloadEncounteredEvent(org.mobicents.csapi.jr.slee.cc.gccs.CallOverloadEncounteredEvent)
     */
    public void onCallOverloadEncounteredEvent(final CallOverloadEncounteredEvent event) {
        eventSender.sendEvent(event, new ParlayServiceActivityHandle(event
                .getService()));
    }

}
