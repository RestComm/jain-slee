package org.mobicents.slee.demo.ivr.isup;

import jain.protocol.ip.mgcp.JainMgcpEvent;
import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.DeleteConnection;
import jain.protocol.ip.mgcp.message.DeleteConnectionResponse;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;

import javax.slee.ActivityContextInterface;

import org.mobicents.protocols.ss7.isup.message.ReleaseMessage;


/**
 *
 * @author amit bhayani
 */
public abstract class IsupConnectionDeletedSbb extends BaseSbb {


	public void onRelease(ReleaseMessage event, ActivityContextInterface aci) {
        tracer.info(String.format("CallID = %s, State=%s, Deleting connection ", getCallID(), getState()));
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


        ReturnCode returnCode = response.getReturnCode();
        switch (returnCode.getValue()) {
            case ReturnCode.TRANSACTION_BEING_EXECUTED :
                //provisional response
                break;
            case ReturnCode.TRANSACTION_EXECUTED_NORMALLY :
            case ReturnCode.CONNECTION_WAS_DELETED :
                setState(IsupConnectionState.CONNECTION_DELETED);
                tracer.info(String.format("CallID = %s, State=%s ", getCallID(), getState()));
               
                break;
            case ReturnCode.ENDPOINT_UNKNOWN :
            case ReturnCode.UNKNOWN_CALL_ID :
            case ReturnCode.INCORRECT_CONNECTION_ID :
                break;
            default :
              //TODO Send error
        }
    }
    
    
}
