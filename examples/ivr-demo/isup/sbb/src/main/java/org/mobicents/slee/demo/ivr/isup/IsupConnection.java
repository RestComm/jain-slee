package org.mobicents.slee.demo.ivr.isup;

import javax.slee.ActivityContextInterface;

import org.mobicents.protocols.ss7.isup.message.InitialAddressMessage;

/**
 *
 * @author amit bhayani
 */
public interface IsupConnection extends ActivityContextInterface {
    public final static int STATE_NULL = 0;
    
    public String getCallID();
    public void setCallID(String callID);
    
    public String getEndpoint();
    public void setEndpoint(String endpoint);
    
    public String getConnectionID();
    public void setConnectionID(String connectionID);
    
    public void setRequest(InitialAddressMessage request);
    public InitialAddressMessage getRequest();
    
    public IsupConnectionState getState();
    public void setState(IsupConnectionState state);
    
}
