/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.slee.demo.ivr.sip;

import javax.sip.RequestEvent;
import javax.slee.ActivityContextInterface;

/**
 *
 * @author kulikov
 */
public interface Connection extends ActivityContextInterface {
    public final static int STATE_NULL = 0;
    
    public String getCallID();
    public void setCallID(String callID);
    
    public String getEndpoint();
    public void setEndpoint(String endpoint);
    
    public String getConnectionID();
    public void setConnectionID(String connectionID);
    
    public void setRequest(RequestEvent request);
    public RequestEvent getRequest();
    
    public ConnectionState getState();
    public void setState(ConnectionState state);
    
}
