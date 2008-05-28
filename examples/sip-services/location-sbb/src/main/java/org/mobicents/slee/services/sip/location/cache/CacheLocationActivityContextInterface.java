package org.mobicents.slee.services.sip.location.cache;

import javax.slee.ActivityContextInterface;
import javax.slee.facilities.TimerID;

/**
 * 
 * RegistrarActivityContextInterfaceProvided by the Sbb Developer
 * 
 * @author F.Moggia
 */
public interface CacheLocationActivityContextInterface extends ActivityContextInterface{
    /** sipAddress - User's public, well-known SIP address */
    public String getSipAddress();
    public void setSipAddress(String sipAddress);

    /** sipContactAddress - Physical network address registered for above sipAddress */
    public String getSipContactAddress();
    public void setSipContactAddress(String sipContactAddress);

    /** callId - SIP callId that was used in the REGISTER request */
    public String getCallId();
    public void setCallId(String callId);

    /** cSeq - SIP sequence number that was used in the REGISTER request */
    public long getCSeq();
    public void setCSeq(long cSeq);
    
    
    //For when entry is update we want to cancel old timer and creat new one.
    public void setTimerID(TimerID tid);
    public TimerID getTimerID();
    
}
