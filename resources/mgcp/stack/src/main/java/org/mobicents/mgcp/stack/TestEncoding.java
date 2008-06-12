/*
 * TestEncoding.java
 *
 * Created on 12 Март 2007 г., 12:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mobicents.mgcp.stack;

import jain.protocol.ip.mgcp.message.CreateConnection;
import jain.protocol.ip.mgcp.message.parms.CallIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConnectionMode;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.EventName;
import jain.protocol.ip.mgcp.message.parms.NotificationRequestParms;
import jain.protocol.ip.mgcp.message.parms.RequestIdentifier;
import jain.protocol.ip.mgcp.message.parms.RequestedAction;
import jain.protocol.ip.mgcp.message.parms.RequestedEvent;
import jain.protocol.ip.mgcp.pkg.MgcpEvent;
import jain.protocol.ip.mgcp.pkg.PackageName;

/**
 *
 * @author 1
 */
public class TestEncoding {
    
    /** Creates a new instance of TestEncoding */
    public TestEncoding() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        JainMgcpStackImpl stack = new JainMgcpStackImpl(2728);
        CallIdentifier callID = new CallIdentifier("1");
        
        EndpointIdentifier endpointID = new EndpointIdentifier("ann/00","localhost:2727");
        CreateConnection createConnection = new CreateConnection("",
                callID, endpointID, ConnectionMode.SendOnly);
        
        RequestIdentifier identifier = new RequestIdentifier(Integer.toString(1));
        NotificationRequestParms parms = new NotificationRequestParms(identifier);
        
        MgcpEvent music = MgcpEvent.ann.withParm("file:c:/sounds/dadada.wav");
        EventName eventName = new EventName(PackageName.Announcement, music);
        
        RequestedAction[] actions = new RequestedAction[] {RequestedAction.KeepSignalsActive};
        RequestedEvent requestedEvent = new RequestedEvent(eventName, actions);
        parms.setRequestedEvents(new RequestedEvent[]{requestedEvent});
        createConnection.setNotificationRequestParms(parms);
        
        CreateConnectionHandler h = new CreateConnectionHandler(stack, null, 0);
        String s = h.encode(createConnection);
        
        System.out.println(s);
    }
    
}
