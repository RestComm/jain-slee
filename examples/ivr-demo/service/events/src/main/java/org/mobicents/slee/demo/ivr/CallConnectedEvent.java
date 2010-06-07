/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.slee.demo.ivr;

import java.io.Serializable;

/**
 *
 * @author kulikov
 */
public class CallConnectedEvent implements Serializable {
    private String connectionID;
    private String endpointID;
    private String caller;
    
    public CallConnectedEvent(String caller, String endpointID, String connectionID) {
        this.caller = caller;
        this.endpointID = endpointID;
        this.connectionID = connectionID;
    }
    
    public String getCaller() {
        return caller;
    }
    
    public String getEndpoint() {
        return endpointID;
    }
    
    public String getConnection() {
        return connectionID;
    }
}
