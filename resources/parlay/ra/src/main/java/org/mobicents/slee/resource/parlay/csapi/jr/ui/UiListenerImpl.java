package org.mobicents.slee.resource.parlay.csapi.jr.ui;

import org.mobicents.csapi.jr.slee.ui.AbortActionErrEvent;
import org.mobicents.csapi.jr.slee.ui.AbortActionResEvent;
import org.mobicents.csapi.jr.slee.ui.DeleteMessageErrEvent;
import org.mobicents.csapi.jr.slee.ui.DeleteMessageResEvent;
import org.mobicents.csapi.jr.slee.ui.RecordMessageErrEvent;
import org.mobicents.csapi.jr.slee.ui.RecordMessageResEvent;
import org.mobicents.csapi.jr.slee.ui.ReportEventNotificationEvent;
import org.mobicents.csapi.jr.slee.ui.ReportNotificationEvent;
import org.mobicents.csapi.jr.slee.ui.SendInfoAndCollectErrEvent;
import org.mobicents.csapi.jr.slee.ui.SendInfoAndCollectResEvent;
import org.mobicents.csapi.jr.slee.ui.SendInfoErrEvent;
import org.mobicents.csapi.jr.slee.ui.SendInfoResEvent;
import org.mobicents.csapi.jr.slee.ui.UserInteractionAbortedEvent;
import org.mobicents.csapi.jr.slee.ui.UserInteractionFaultDetectedEvent;
import org.mobicents.csapi.jr.slee.ui.UserInteractionNotificationContinuedEvent;
import org.mobicents.csapi.jr.slee.ui.UserInteractionNotificationInterruptedEvent;
import org.mobicents.slee.resource.parlay.csapi.jr.ParlayServiceActivityHandle;
import org.mobicents.slee.resource.parlay.util.event.EventSender;

public class UiListenerImpl implements UiListener {

    private final transient EventSender eventSender;

    public UiListenerImpl(EventSender eventSender) {
        super();
        this.eventSender = eventSender;
    }

    // AppManager originated events ...

    public void onUserInteractionAbortedEvent(final UserInteractionAbortedEvent event) {
        eventSender.sendEvent(event, new ParlayServiceActivityHandle(event
                .getService()));
    }

 
    public void onUserInteractionNotificationInterruptedEvent(
            final UserInteractionNotificationInterruptedEvent event) {
        eventSender.sendEvent(event, new ParlayServiceActivityHandle(event
                .getService()));

    }

    public void onUserInteractionNotificationContinuedEvent(
            final UserInteractionNotificationContinuedEvent event) {
        eventSender.sendEvent(event, new ParlayServiceActivityHandle(event
                .getService()));

    }

    public void onReportNotificationEvent(final ReportNotificationEvent event) {
        eventSender.sendEvent(event, new ParlayServiceActivityHandle(event
                .getService()));

    }

    public void onReportEventNotificationEvent(final ReportEventNotificationEvent event) {
        eventSender.sendEvent(event, new ParlayServiceActivityHandle(event
                .getService()));

    }

    // UIGeneric or UICall originated events ...

    public void onSendInfoResEvent(final SendInfoResEvent event) {       
        if (event.getTpUIIdentifier() == null ){
            eventSender.sendEvent(event, new TpUICallActivityHandle(event.getTpUICallIdentifier()));
        } else {
            eventSender.sendEvent(event, new TpUIActivityHandle(event.getTpUIIdentifier()));
        }
    }

    public void onSendInfoErrEvent(final SendInfoErrEvent event) {
        if (event.getTpUIIdentifier() == null ){
            eventSender.sendEvent(event, new TpUICallActivityHandle(event.getTpUICallIdentifier()));
        } else {
            eventSender.sendEvent(event, new TpUIActivityHandle(event.getTpUIIdentifier()));
        }
    }

    public void onSendInfoAndCollectResEvent(final SendInfoAndCollectResEvent event) {
        if (event.getTpUIIdentifier() == null ){
            eventSender.sendEvent(event, new TpUICallActivityHandle(event.getTpUICallIdentifier()));
        } else {
            eventSender.sendEvent(event, new TpUIActivityHandle(event.getTpUIIdentifier()));
        }

    }

    public void onSendInfoAndCollectErrEvent(final SendInfoAndCollectErrEvent event) {
        if (event.getTpUIIdentifier() == null ){
            eventSender.sendEvent(event, new TpUICallActivityHandle(event.getTpUICallIdentifier()));
        } else {
            eventSender.sendEvent(event, new TpUIActivityHandle(event.getTpUIIdentifier()));
        }
    }

    public void onUserInteractionFaultDetectedEvent(
            final UserInteractionFaultDetectedEvent event) {
        if (event.getTpUIIdentifier() == null ){
            eventSender.sendEvent(event, new TpUICallActivityHandle(event.getTpUICallIdentifier()));
        } else {
            eventSender.sendEvent(event, new TpUIActivityHandle(event.getTpUIIdentifier()));
        }

    }

    // Additional events for UI Call originated events

    public void onRecordMessageResEvent(final RecordMessageResEvent event) {
        eventSender.sendEvent(event, new TpUICallActivityHandle(event
                .getTpUICallIdentifier()));

    }

    public void onRecordMessageErrEvent(final RecordMessageErrEvent event) {
        eventSender.sendEvent(event, new TpUICallActivityHandle(event
                .getTpUICallIdentifier()));

    }

    public void onDeleteMessageResEvent(final DeleteMessageResEvent event) {
        eventSender.sendEvent(event, new TpUICallActivityHandle(event
                .getTpUICallIdentifier()));

    }

    public void onDeleteMessageErrEvent(final DeleteMessageErrEvent event) {
        eventSender.sendEvent(event, new TpUICallActivityHandle(event
                .getTpUICallIdentifier()));

    }

    public void onAbortActionResEvent(final AbortActionResEvent event) {
        eventSender.sendEvent(event, new TpUICallActivityHandle(event
                .getTpUICallIdentifier()));

    }

    public void onAbortActionErrEvent(final AbortActionErrEvent event) {
        eventSender.sendEvent(event, new TpUICallActivityHandle(event
                .getTpUICallIdentifier()));

    }


}
