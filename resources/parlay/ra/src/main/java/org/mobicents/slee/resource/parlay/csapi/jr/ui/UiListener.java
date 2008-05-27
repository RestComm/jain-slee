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

/**
 * Ui Listener (where Ui means the whole User Interaction service)
 */
public interface UiListener {

    // AppManager originated events
    void onUserInteractionAbortedEvent(UserInteractionAbortedEvent event);

    void onUserInteractionNotificationInterruptedEvent(
            UserInteractionNotificationInterruptedEvent event);

    void onUserInteractionNotificationContinuedEvent(
            UserInteractionNotificationContinuedEvent event);

    void onReportNotificationEvent(ReportNotificationEvent event);

    void onReportEventNotificationEvent(ReportEventNotificationEvent event);

    // UI originated events
    void onSendInfoResEvent(SendInfoResEvent event);

    void onSendInfoErrEvent(SendInfoErrEvent event);

    void onSendInfoAndCollectResEvent(SendInfoAndCollectResEvent event);

    void onSendInfoAndCollectErrEvent(SendInfoAndCollectErrEvent event);

    void onUserInteractionFaultDetectedEvent(UserInteractionFaultDetectedEvent event);

    // Additional events for UI Call originated events
    void onRecordMessageResEvent(RecordMessageResEvent event);

    void onRecordMessageErrEvent(RecordMessageErrEvent event);

    void onDeleteMessageResEvent(DeleteMessageResEvent event);

    void onDeleteMessageErrEvent(DeleteMessageErrEvent event);

    void onAbortActionResEvent(AbortActionResEvent event);

    void onAbortActionErrEvent(AbortActionErrEvent event);

}
