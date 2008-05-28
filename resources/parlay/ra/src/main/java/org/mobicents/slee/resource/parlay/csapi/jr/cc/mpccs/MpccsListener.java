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

/**
 *
 * Class Description for MpccsEventListener
 */
public interface MpccsListener {
    
    void onAttachMediaErrEvent(AttachMediaErrEvent event);

    void onAttachMediaResEvent(AttachMediaResEvent event);

    void onCallAbortedEvent(CallAbortedEvent event);

    void onCallEndedEvent(CallEndedEvent event);

    void onCallLegEndedEvent(CallLegEndedEvent event);

    void onCallOverloadCeasedEvent(CallOverloadCeasedEvent event);

    void onCallOverloadEncounteredEvent(CallOverloadEncounteredEvent event);

    void onCreateAndRouteCallLegErrEvent(CreateAndRouteCallLegErrEvent event);

    void onDetachMediaErrEvent(DetachMediaErrEvent event);

    void onDetachMediaResEvent(DetachMediaResEvent event);

    void onEventReportErrEvent(EventReportErrEvent event);

    void onEventReportResEvent(EventReportResEvent event);

    void onGetInfoErrEvent(GetInfoErrEvent event);

    void onGetInfoResEvent(GetInfoResEvent event);

    void onManagerInterruptedEvent(ManagerInterruptedEvent event);

    void onManagerResumedEvent(ManagerResumedEvent event);

    void onReportNotificationEvent(ReportNotificationEvent event);

    void onRouteErrEvent(RouteErrEvent event);

    void onSuperviseErrEvent(SuperviseErrEvent event);

    void onSuperviseResEvent(SuperviseResEvent event);

}
