package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs;

import org.mobicents.csapi.jr.slee.cc.mpccs.AttachMediaErrEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.AttachMediaResEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.CallAbortedEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.CallEndedEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.CallLegEndedEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.CallOverloadCeasedEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.CallOverloadEncounteredEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.CreateAndRouteCallLegErrEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.DetachMediaErrEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.DetachMediaResEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.EventReportErrEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.EventReportResEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.GetInfoErrEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.GetInfoResEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.ManagerInterruptedEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.ManagerResumedEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.ReportNotificationEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.RouteErrEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.SuperviseErrEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.SuperviseResEvent;
import org.mobicents.slee.resource.parlay.csapi.jr.ParlayServiceActivityHandle;
import org.mobicents.slee.resource.parlay.util.event.EventSender;


public class MpccsListenerImpl implements MpccsListener {

    private final transient EventSender eventSender;

    public MpccsListenerImpl(EventSender eventSender) {
        super();
        this.eventSender = eventSender;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsEventListener#onAttachMediaErrEvent(org.mobicents.csapi.jr.slee.cc.mpccs.AttachMediaErrEvent)
     */
    public void onAttachMediaErrEvent(final AttachMediaErrEvent event) {
        eventSender.sendEvent(event, new TpCallLegActivityHandle(event
                .getTpCallLegIdentifier()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsEventListener#onAttachMediaResEvent(org.mobicents.csapi.jr.slee.cc.mpccs.AttachMediaResEvent)
     */
    public void onAttachMediaResEvent(final AttachMediaResEvent event) {
        eventSender.sendEvent(event, new TpCallLegActivityHandle(event
                .getTpCallLegIdentifier()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsEventListener#onCallAbortedEvent(org.mobicents.csapi.jr.slee.cc.mpccs.CallAbortedEvent)
     */
    public void onCallAbortedEvent(final CallAbortedEvent event) {
        eventSender.sendEvent(event, new ParlayServiceActivityHandle(event
                .getService()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsEventListener#onCallEndedEvent(org.mobicents.csapi.jr.slee.cc.mpccs.CallEndedEvent)
     */
    public void onCallEndedEvent(final CallEndedEvent event) {
        eventSender.sendEvent(event, new TpMultiPartyCallActivityHandle(event
                .getTpMultiPartyCallIdentifier()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsEventListener#onCallLegEndedEvent(org.mobicents.csapi.jr.slee.cc.mpccs.CallLegEndedEvent)
     */
    public void onCallLegEndedEvent(final CallLegEndedEvent event) {
        eventSender.sendEvent(event, new TpCallLegActivityHandle(event
                .getTpCallLegIdentifier()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsEventListener#onCallOverloadCeasedEvent(org.mobicents.csapi.jr.slee.cc.mpccs.CallOverloadCeasedEvent)
     */
    public void onCallOverloadCeasedEvent(final CallOverloadCeasedEvent event) {
        eventSender.sendEvent(event, new ParlayServiceActivityHandle(event
                .getService()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsEventListener#onCallOverloadEncounteredEvent(org.mobicents.csapi.jr.slee.cc.mpccs.CallOverloadEncounteredEvent)
     */
    public void onCallOverloadEncounteredEvent(
            final CallOverloadEncounteredEvent event) {
        eventSender.sendEvent(event, new ParlayServiceActivityHandle(event
                .getService()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsEventListener#onCreateAndRouteCallLegErrEvent(org.mobicents.csapi.jr.slee.cc.mpccs.CreateAndRouteCallLegErrEvent)
     */
    public void onCreateAndRouteCallLegErrEvent(
            final CreateAndRouteCallLegErrEvent event) {
        eventSender.sendEvent(event, new TpMultiPartyCallActivityHandle(event
                .getTpMultiPartyCallIdentifier()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsEventListener#onDetachMediaErrEvent(org.mobicents.csapi.jr.slee.cc.mpccs.DetachMediaErrEvent)
     */
    public void onDetachMediaErrEvent(final DetachMediaErrEvent event) {
        eventSender.sendEvent(event, new TpCallLegActivityHandle(event
                .getTpCallLegIdentifier()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsEventListener#onDetachMediaResEvent(org.mobicents.csapi.jr.slee.cc.mpccs.DetachMediaResEvent)
     */
    public void onDetachMediaResEvent(final DetachMediaResEvent event) {
        eventSender.sendEvent(event, new TpCallLegActivityHandle(event
                .getTpCallLegIdentifier()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsEventListener#onEventReportErrEvent(org.mobicents.csapi.jr.slee.cc.mpccs.EventReportErrEvent)
     */
    public void onEventReportErrEvent(final EventReportErrEvent event) {
        eventSender.sendEvent(event, new TpCallLegActivityHandle(event
                .getTpCallLegIdentifier()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsEventListener#onEventReportResEvent(org.mobicents.csapi.jr.slee.cc.mpccs.EventReportResEvent)
     */
    public void onEventReportResEvent(final EventReportResEvent event) {
        eventSender.sendEvent(event, new TpCallLegActivityHandle(event
                .getTpCallLegIdentifier()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsEventListener#onGetInfoErrEvent(org.mobicents.csapi.jr.slee.cc.mpccs.GetInfoErrEvent)
     */
    public void onGetInfoErrEvent(final GetInfoErrEvent event) {
        if(event.getTpCallLegIdentifier() == null) {
            eventSender.sendEvent(event, new TpMultiPartyCallActivityHandle(event
                    .getTpMultiPartyCallIdentifier()));           
        }
        else {
            eventSender.sendEvent(event, new TpCallLegActivityHandle(event
                    .getTpCallLegIdentifier()));
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsEventListener#onGetInfoResEvent(org.mobicents.csapi.jr.slee.cc.mpccs.GetInfoResEvent)
     */
    public void onGetInfoResEvent(final GetInfoResEvent event) {
        if(event.getTpCallLegIdentifier() == null) {
            eventSender.sendEvent(event, new TpMultiPartyCallActivityHandle(event
                    .getTpMultiPartyCallIdentifier()));
            
        }
        else {
            eventSender.sendEvent(event, new TpCallLegActivityHandle(event
                    .getTpCallLegIdentifier()));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsEventListener#onManagerInterruptedEvent(org.mobicents.csapi.jr.slee.cc.mpccs.ManagerInterruptedEvent)
     */
    public void onManagerInterruptedEvent(final ManagerInterruptedEvent event) {
        eventSender.sendEvent(event, new ParlayServiceActivityHandle(event
                .getService()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsEventListener#onManagerResumedEvent(org.mobicents.csapi.jr.slee.cc.mpccs.ManagerResumedEvent)
     */
    public void onManagerResumedEvent(final ManagerResumedEvent event) {
        eventSender.sendEvent(event, new ParlayServiceActivityHandle(event
                .getService()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsEventListener#onReportNotificationEvent(org.mobicents.csapi.jr.slee.cc.mpccs.ReportNotificationEvent)
     */
    public void onReportNotificationEvent(final ReportNotificationEvent event) {
        eventSender.sendEvent(event, new ParlayServiceActivityHandle(event
                .getService()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsEventListener#onRouteErrEvent(org.mobicents.csapi.jr.slee.cc.mpccs.RouteErrEvent)
     */
    public void onRouteErrEvent(final RouteErrEvent event) {
        eventSender.sendEvent(event, new TpMultiPartyCallActivityHandle(event
                .getTpMultiPartyCallIdentifier()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsEventListener#onSuperviseErrEvent(org.mobicents.csapi.jr.slee.cc.mpccs.SuperviseErrEvent)
     */
    public void onSuperviseErrEvent(final SuperviseErrEvent event) {
        if(event.getTpCallLegIdentifier() == null) {
            eventSender.sendEvent(event, new TpMultiPartyCallActivityHandle(event
                    .getTpMultiPartyCallIdentifier()));           
        }
        else {
            eventSender.sendEvent(event, new TpCallLegActivityHandle(event
                    .getTpCallLegIdentifier()));
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsEventListener#onSuperviseResEvent(org.mobicents.csapi.jr.slee.cc.mpccs.SuperviseResEvent)
     */
    public void onSuperviseResEvent(final SuperviseResEvent event) {
        if(event.getTpCallLegIdentifier() != null) {
            eventSender.sendEvent(event, new TpCallLegActivityHandle(event
                    .getTpCallLegIdentifier()));
        }
        else {
            eventSender.sendEvent(event, new TpMultiPartyCallActivityHandle(event
                    .getTpMultiPartyCallIdentifier()));
        }
    }

}