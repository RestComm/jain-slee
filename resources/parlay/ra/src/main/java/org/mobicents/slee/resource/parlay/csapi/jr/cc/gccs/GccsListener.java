
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

/**
 *
 **/
public interface GccsListener {
    
    void onCallAbortedEvent(CallAbortedEvent event);
    
    void onCallEndedEvent(CallEndedEvent event);
    
    void onCallEventNotifiyEvent(CallEventNotifyEvent event);
    
    void onCallFaultDetectedEvent(CallFaultDetectedEvent event);
    
    void onCallNotificationContinuedEvent(CallNotificationContinuedEvent event);
    
    void onCallNotificationInterruptedEvent(CallNotificationInterruptedEvent event);
    
    void onCallOverloadEncounteredEvent(CallOverloadEncounteredEvent event);
    
    void onCallOverloadCeasedEvent(CallOverloadCeasedEvent event);
    
    void onGetCallInfoErrEvent(GetCallInfoErrEvent event);
    
    void onGetCallInfoResEvent(GetCallInfoResEvent event);
    
    void onGetMoreDialledDigitsErrEvent(GetMoreDialledDigitsErrEvent event);
    
    void onGetMoreDialledDigitsResEvent(GetMoreDialledDigitsResEvent event);
    
    void onRouteErrEvent(RouteErrEvent event);
    
    void onRouteResEvent(RouteResEvent event);
    
    void onSuperviseCallErrEvent(SuperviseCallErrEvent event);
    
    void onSuperviseCallResEvent(SuperviseCallResEvent event);
    
}
