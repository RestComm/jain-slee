/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.slee.demo.ivr.sip;

import jain.protocol.ip.mgcp.JainMgcpEvent;
import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.DeleteConnection;
import jain.protocol.ip.mgcp.message.DeleteConnectionResponse;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;
import javax.sip.RequestEvent;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import net.java.slee.resource.sip.CancelRequestEvent;
import org.mobicents.slee.demo.ivr.media.ConnectionState;


/**
 *
 * @author kulikov
 */
public abstract class ConnectionDeletedSbb extends BaseSbb {

    private RequestEvent event;
    
    public void onBye(RequestEvent event, ActivityContextInterface aci) {
        tracer.info(String.format("CallID = %s, State=%s, Deleting connection ", getCallID(), getState()));
        this.event = event;
        String endpointName = this.getEndpointID();
        
        EndpointIdentifier endpointID = new EndpointIdentifier(endpointName, JBOSS_BIND_ADDRESS + ":2427");
        DeleteConnection deleteConnection = new DeleteConnection(this, endpointID);

        deleteConnection.setTransactionHandle(mgcpProvider.getUniqueTransactionHandler());
        mgcpProvider.sendMgcpEvents(new JainMgcpEvent[]{deleteConnection});
    }  
    
    public void onCreateConnectionResponse(CreateConnectionResponse response, ActivityContextInterface aci) {
        ReturnCode returnCode = response.getReturnCode();
        switch (returnCode.getValue()) {
            case ReturnCode.TRANSACTION_BEING_EXECUTED :
                //provisional response received, awaitng for final response
                break;
            case ReturnCode.TRANSACTION_EXECUTED_NORMALLY :
                String endpoint = response.getSpecificEndpointIdentifier().getLocalEndpointName();
                EndpointIdentifier endpointID = new EndpointIdentifier(endpoint, JBOSS_BIND_ADDRESS + ":2427");
                DeleteConnection deleteConnection = new DeleteConnection(this, endpointID);

                deleteConnection.setTransactionHandle(mgcpProvider.getUniqueTransactionHandler());
                mgcpProvider.sendMgcpEvents(new JainMgcpEvent[]{deleteConnection});
                
                break;
            default :
                //ignore response
        }
    }
    
    public void onConnectionDeleted(DeleteConnectionResponse response, ActivityContextInterface aci) {
        tracer.info(String.format("CallID = %s, State=%s, Receive Delete connection response ", getCallID(), getState()));
        if (event == null) {
            return;
        }
        Request request = event.getRequest();

        ReturnCode returnCode = response.getReturnCode();
        switch (returnCode.getValue()) {
            case ReturnCode.TRANSACTION_BEING_EXECUTED :
                //provisional response
                break;
            case ReturnCode.TRANSACTION_EXECUTED_NORMALLY :
            case ReturnCode.CONNECTION_WAS_DELETED :
                setState(ConnectionState.CONNECTION_DELETED);
                tracer.info(String.format("CallID = %s, State=%s ", getCallID(), getState()));
                try {
                    Response ok = messageFactory.createResponse(Response.OK, request);
                    sipProvider.sendResponse(ok);
                } catch (Exception e) {
                    tracer.severe("Error while sending OK ", e);
                }
                break;
            case ReturnCode.ENDPOINT_UNKNOWN :
            case ReturnCode.UNKNOWN_CALL_ID :
            case ReturnCode.INCORRECT_CONNECTION_ID :
                break;
            default :
                try {
                    Response error = messageFactory.createResponse(Response.SERVER_INTERNAL_ERROR, request);
                    sipProvider.sendResponse(error);
                } catch (Exception e) {
                    tracer.severe("Error while sending OK ", e);
                }
        }
    }
    
    
}
