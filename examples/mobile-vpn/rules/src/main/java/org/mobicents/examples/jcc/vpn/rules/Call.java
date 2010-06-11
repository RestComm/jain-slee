/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.examples.jcc.vpn.rules;

import java.io.Serializable;

/**
 *
 * @author kulikov
 */
public class Call implements Serializable {
    private String callingParty;
    private String calledParty;
    private String calledPartyFormat;
    private String direction;
    private String groupName = "";
    private String networkID = "";
    private String destination;
    private String destinationFormat;
    private String callerID;

    public Call(String callingParty, String calledParty, String direction) {
        
        String tokens[] = calledParty.split(",");        
        this.calledParty = tokens[0];
        //parse paramas
        for (int i = 1; i < tokens.length; i++) {
            String s = tokens[i];
            String parts[] = s.split("=");
            if (parts[0].equalsIgnoreCase("noa")) {
                calledPartyFormat = parts[1];
            }
        }

        tokens = callingParty.split(",");        
        this.callingParty = tokens[0];
        
        this.direction = direction;
    }

    
    public String getCalledParty() {
        return calledParty;
    }

    public String getCalledPartyFormat() {
        return calledPartyFormat;
    }
    
    public String getCallerID() {
        return callerID;
    }

    public String getCallingParty() {
        return callingParty;
    }

    public String getDestination() {
        return destination;
    }

    public String getDirection() {
        return direction;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getNetworkID() {
        return networkID;
    }

    public String getDestinationFormat() {
        return destinationFormat;
    }
    
    public void setCalledParty(String calledParty) {
        this.calledParty = calledParty;
    }

    public void setCallerID(String callerID) {
        this.callerID = callerID;
    }

    public void setCallingParty(String callingParty) {
        this.callingParty = callingParty;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setNetworkID(String networkID) {
        this.networkID = networkID;
    }
    
    public void setDestinationFormat(String fmt) {
        this.destinationFormat = fmt;
    }
    
    public String getRouteAddress() {
        return destinationFormat != null ? 
            destination + ",NoA=" + destinationFormat :
            destination;
    }
}
